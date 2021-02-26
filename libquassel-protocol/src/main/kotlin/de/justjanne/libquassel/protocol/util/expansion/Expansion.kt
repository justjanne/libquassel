/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.expansion

/**
 * Model for a command expansion
 */
sealed class Expansion {
  abstract val source: String

  /**
   * Model for raw text
   */
  data class Text(
    /**
     * Text to insert
     */
    override val source: String
  ) : Expansion()

  /**
   * Model for a parameter range
   */
  data class ParameterRange(
    /**
     * Index of the first included parameter.
     */
    val from: Int,
    /**
     * Index of the last included parameter.
     * If null, this means all parameters after the start should be included
     */
    val to: Int?,
    /**
     * Original value that was parsed and replaced
     */
    override val source: String
  ) : Expansion()

  /**
   * Model for a single parameter
   */
  data class Parameter(
    /**
     * Index of the parameter
     */
    val index: Int,
    /**
     * Attribute of the parameter to access, if possible.
     *
     * If null, this just inserts the value of the nth parameter itself.
     * Otherwise this causes a lookup of the specified value.
     *
     * If e.g. the nth value is "justjanne", and this field is set to
     * [ParameterField.VERIFIED_IDENT], it’ll look up the ident of justjanne
     * in the current context, try to verify it, and set it to * otherwise.
     */
    val field: ParameterField?,
    /**
     * Original value that was parsed and replaced
     */
    override val source: String
  ) : Expansion()

  /**
   * Model for a single constant in the current context
   */
  data class Constant(
    /**
     * Type of constant to be inserted
     */
    val field: ConstantField,
    /**
     * Original value that was parsed and replaced
     */
    override val source: String
  ) : Expansion()

  /**
   * Potential fields that can be specified on a given parameter
   */
  enum class ParameterField {
    /**
     * Hostname of the ircuser with the specified nick on the network of the
     * given context, or "*" if the user is not found or the hostname could not
     * be determined.
     */
    HOSTNAME,

    /**
     * Ident of the ircuser with the specified nick on the network of the
     * given context.
     *
     * Will be set to "*" if the ident is not verified (prefixed with ~), the
     * user is not found or the ident could not be determined.
     */
    VERIFIED_IDENT,

    /**
     * Ident of the ircuser with the specified nick on the network of the
     * given context, or "*" if the user is not found or the hostname could not
     * be determined.
     */
    IDENT,

    /**
     * Account of the ircuser with the specified nick on the network of the
     * given context, or "*" if the user is not found or the hostname could not
     * be determined.
     */
    ACCOUNT
  }

  /**
   * Potential fields that can be specified on a given context
   */
  enum class ConstantField {
    /**
     * Name of the current IRC channel, otherwise the name of the current chat
     * if the alias is invoked outside of a channel
     */
    CHANNEL,

    /**
     * Nick of the user invoking this alias
     */
    NICK,

    /**
     * Name of the network this alias is invoked on
     */
    NETWORK
  }

  companion object {
    /**
     * Parse a list of expansions from a given expansion string
     */
    fun parse(text: String): List<Expansion> =
      ExpansionParsingContext(text).parse()
  }
}
