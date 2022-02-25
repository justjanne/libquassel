/*
 * libquassel
 * Copyright (c) 2022 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flow<StateHolder<T>?>.flatMap(): Flow<T?> =
  flatMapLatest { it?.flow() ?: flowOf(null) }

@ExperimentalCoroutinesApi
inline fun <reified T> Flow<Iterable<StateHolder<T>>?>.combineLatest(): Flow<List<T>> =
  flatMapLatest { combine(it?.map(StateHolder<T>::flow).orEmpty(), ::listOf) }
