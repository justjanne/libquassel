/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.expression

/**
 * Construct an Expression match with the given parameters
 *
 * @param expression  A phrase, wildcard expression, or regular expression
 * @param mode     Expression matching mode @see ExpressionMatch.MatchMode
 * @param caseSensitive If true, match case-sensitively, otherwise ignore case when matching
 */
data class ExpressionMatch(
  val expression: String,
  val mode: MatchMode,
  val caseSensitive: Boolean
) {
  enum class MatchMode {
    MatchPhrase,
    MatchMultiPhrase,
    MatchWildcard,
    MatchMultiWildcard,
    MatchRegEx
  }

  internal val positiveRegex: Regex?
  internal val negativeRegex: Regex?

  init {
    val (positive, negative) = when (mode) {
      MatchMode.MatchPhrase -> parsePhrase(expression)
      MatchMode.MatchMultiPhrase -> parseMultiPhrase(expression)
      MatchMode.MatchWildcard -> parseWildcard(expression)
      MatchMode.MatchMultiWildcard -> parseMultiWildcard(expression)
      MatchMode.MatchRegEx -> parseRegEx(expression)
    }

    positiveRegex = regex(positive, caseSensitive = caseSensitive)
    negativeRegex = regex(negative, caseSensitive = caseSensitive)
  }

  fun isEmpty() = positiveRegex == null && negativeRegex == null

  fun match(content: String, matchEmpty: Boolean = false): Boolean {
    if (isEmpty()) {
      return matchEmpty
    }

    if (negativeRegex != null && negativeRegex.containsMatchIn(content)) {
      return false
    }

    if (positiveRegex != null) {
      return positiveRegex.containsMatchIn(content)
    }

    return true
  }

  companion object {
    private fun regex(expression: String, caseSensitive: Boolean): Regex? = try {
      when {
        expression.isBlank() -> null
        caseSensitive -> expression.toRegex()
        else -> expression.toRegex(RegexOption.IGNORE_CASE)
      }
    } catch (t: Throwable) {
      null
    }

    internal fun splitWithEscaping(expression: String): List<String> {
      val result = mutableListOf<String>()
      val current = StringBuilder()
      var escaped = false
      for (char in expression) {
        if (escaped) {
          when (char) {
            '\\' -> {
              current.append(char)
              current.append(char)
              escaped = false
            }
            '\n' -> {
              current.append("\\")
              result.add(current.toString())
              current.clear()
              escaped = false
            }
            else -> {
              current.append("\\")
              current.append(char)
              escaped = false
            }
          }
        } else {
          when (char) {
            ';', '\n' -> {
              result.add(current.toString())
              current.clear()
            }
            '\\' -> {
              escaped = true
            }
            else -> {
              current.append(char)
            }
          }
        }
      }
      result.add(current.toString())
      return result
    }

    private fun parseInverted(expression: String): Pair<Boolean, String> {
      if (expression.startsWith('!')) {
        return Pair(true, expression.substring(1))
      }

      if (expression.startsWith("\\!")) {
        return Pair(false, expression.substring(1))
      }

      return Pair(false, expression)
    }

    private fun escape(expression: String): String {
      val result = StringBuilder(expression.length)
      for (char in expression) {
        when (char) {
          '\\', '.', '[', ']', '{', '}', '(', ')', '<', '>', '*', '+', '-', '=', '?', '^', '$', '|' -> {
            result.append('\\')
            result.append(char)
          }
          else -> {
            result.append(char)
          }
        }
      }
      return result.toString()
    }

    private fun parsePhrase(expression: String): Pair<String, String> {
      if (expression.isBlank()) {
        return Pair("", "")
      }

      return Pair("(?:^|\\W)${escape(expression)}(?:\\W|$)", "")
    }

    private fun parseMultiPhrase(expression: String): Pair<String, String> {
      val components = expression.split('\n')
        .filter(String::isNotEmpty)
        .map(::escape)

      if (components.isEmpty()) {
        return Pair("", "")
      }

      return Pair(components.joinToString("|", prefix = "(?:^|\\W)(?:", postfix = ")(?:\\W|$)"), "")
    }

    private fun parseWildcardInternal(expression: String): String {
      val result = StringBuilder()
      var escaped = false
      for (char in expression) {
        when (char) {
          '\\' -> if (escaped) {
            result.append('\\')
            result.append(char)
            escaped = false
          } else {
            escaped = true
          }
          '?' -> if (escaped) {
            result.append('\\')
            result.append(char)
            escaped = false
          } else {
            result.append('.')
            escaped = false
          }
          '*' -> if (escaped) {
            result.append('\\')
            result.append(char)
            escaped = false
          } else {
            result.append(".*")
            escaped = false
          }
          '.', '[', ']', '{', '}', '(', ')', '<', '>', '+', '-', '=', '^', '$', '|' -> {
            result.append('\\')
            result.append(char)
            escaped = false
          }
          else -> {
            result.append(char)
            escaped = false
          }
        }
      }
      return result.toString()
    }

    private fun parseWildcard(expression: String): Pair<String, String> {
      val (inverted, phrase) = parseInverted(expression)
      val result = "^${parseWildcardInternal(phrase)}$"

      return if (inverted) Pair("", result)
      else Pair(result, "")
    }

    private fun parseMultiWildcard(expression: String): Pair<String, String> {
      val components = splitWithEscaping(expression)

      val positive = components
        .asSequence()
        .map(::parseInverted)
        .filterNot(Pair<Boolean, String>::first)
        .map(Pair<Boolean, String>::second)
        .map(String::trim)
        .map(::parseWildcardInternal)
        .filter(String::isNotEmpty)
        .toList()

      val negative = components
        .asSequence()
        .map(::parseInverted)
        .filter(Pair<Boolean, String>::first)
        .map(Pair<Boolean, String>::second)
        .map(String::trim)
        .map(::parseWildcardInternal)
        .filter(String::isNotEmpty)
        .toList()

      return Pair(
        if (positive.isEmpty()) ""
        else positive.joinToString("|", prefix = "^(?:", postfix = ")$"),
        if (negative.isEmpty()) ""
        else negative.joinToString("|", prefix = "^(?:", postfix = ")$")
      )
    }

    private fun parseRegEx(expression: String): Pair<String, String> {
      val (inverted, phrase) = parseInverted(expression)
      return when {
        phrase.isBlank() -> return Pair("", "")
        inverted -> Pair("", phrase)
        else -> Pair(phrase, "")
      }
    }
  }
}
