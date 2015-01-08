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

package com.gs.collections.api.multimap.sortedbag;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.bag.MutableBagIterableMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 4.2
 */
public interface MutableSortedBagMultimap<K, V>
        extends MutableBagIterableMultimap<K, V>, SortedBagMultimap<K, V>
{
    MutableSortedBag<V> replaceValues(K key, Iterable<? extends V> values);

    MutableSortedBag<V> removeAll(Object key);

    MutableSortedBagMultimap<K, V> newEmpty();

    MutableSortedBag<V> get(K key);

    MutableBagMultimap<V, K> flip();

    MutableSortedBagMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    MutableSortedBagMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    MutableSortedBagMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    MutableSortedBagMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <K2, V2> MutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <V2> MutableListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
