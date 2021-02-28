/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.types;

import de.justjanne.libquassel.protocol.models.ids.SignedId;
import de.justjanne.libquassel.protocol.models.ids.SignedIdKt;

public class SignedIdProxy {
  private SignedIdProxy() {

  }

  public static boolean isValidId(SignedId<Integer> data) {
    return SignedIdKt.isValidId(data);
  }

  public static boolean isValidId64(SignedId<Long> data) {
    return SignedIdKt.isValidId64(data);
  }

  public static <T extends Number & Comparable<T>> String toString(SignedId<T> data) {
    return data.toString();
  }

  public static <T extends Number & Comparable<T>> int hashCode(SignedId<T> data) {
    return data.hashCode();
  }
}
