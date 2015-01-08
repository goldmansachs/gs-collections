/*
 * Copyright 2015 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.api.multimap.set;

import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.set.SetIterable;

public interface SetMultimap<K, V>
        extends Multimap<K, V>
{
    SetMultimap<K, V> newEmpty();

    SetIterable<V> get(K key);

    SetMultimap<V, K> flip();

    SetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    SetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    SetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    SetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);
}
