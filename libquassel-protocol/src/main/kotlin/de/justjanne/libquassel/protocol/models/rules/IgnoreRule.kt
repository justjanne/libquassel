/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.rules

import de.justjanne.libquassel.protocol.util.expression.ExpressionMatch

data class IgnoreRule(
  val type: IgnoreType,
  val ignoreRule: String,
  val isRegEx: Boolean = false,
  val strictness: StrictnessType,
  val isEnabled: Boolean = true,
  val scope: ScopeType,
  val scopeRule: String
) {
  val ignoreMatch = ExpressionMatch(
    ignoreRule,
    if (isRegEx) ExpressionMatch.MatchMode.MatchRegEx
    else ExpressionMatch.MatchMode.MatchWildcard,
    false
  )
  val scopeMatch = ExpressionMatch(
    scopeRule,
    ExpressionMatch.MatchMode.MatchMultiWildcard,
    false
  )
}
