/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.collections;

import kotlin.jvm.functions.Function2;

import java.util.List;

public class PairsProxy {
  private PairsProxy() {
  }

  public static <T, R> List<R> call(
    Iterable<T> iterable,
    Function2<T, T, R> transformer
  ) {
    return PairsKt.pairs(iterable, transformer);
  }
}
