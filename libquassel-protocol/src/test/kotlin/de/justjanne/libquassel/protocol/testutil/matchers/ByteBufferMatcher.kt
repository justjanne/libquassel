/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.testutil.matchers

import de.justjanne.libquassel.protocol.io.contentToString
import de.justjanne.libquassel.protocol.io.isEmpty
import de.justjanne.libquassel.protocol.util.withRewind
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import java.nio.ByteBuffer

class ByteBufferMatcher(buffer: ByteBuffer?) : BaseMatcher<ByteBuffer>() {
  private val expected = buffer?.let { original ->
    val copy = ByteBuffer.allocateDirect(original.limit())
    original.rewind()
    copy.put(original)
    copy.rewind()
    original.rewind()
    copy
  }

  override fun describeTo(description: Description?) {
    description?.appendText(expected?.contentToString())
  }

  override fun describeMismatch(item: Any?, description: Description?) {
    description?.appendText("was ")
    description?.appendText((item as? ByteBuffer)?.withRewind()?.contentToString())
  }

  override fun matches(item: Any?): Boolean {
    val actual = item as? ByteBuffer

    if (actual.isEmpty() && expected.isEmpty()) {
      return true
    }

    return actual?.withRewind()?.contentToString() == expected?.withRewind()?.contentToString()
  }
}
