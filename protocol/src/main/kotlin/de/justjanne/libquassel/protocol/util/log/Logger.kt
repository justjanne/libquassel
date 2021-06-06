/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.log

import org.slf4j.Logger

fun Logger.trace(f: () -> String) {
  if (isTraceEnabled) {
    trace(f())
  }
}

fun Logger.debug(f: () -> String) {
  if (isDebugEnabled) {
    debug(f())
  }
}

fun Logger.info(f: () -> String) {
  if (isInfoEnabled) {
    info(f())
  }
}

fun Logger.warn(f: () -> String) {
  if (isWarnEnabled) {
    info(f())
  }
}
