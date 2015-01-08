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

package com.gs.collections.api.multimap.sortedset;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.multimap.set.MutableSetIterableMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 1.0
 */
public interface MutableSortedSetMultimap<K, V>
        extends MutableSetIterableMultimap<K, V>, SortedSetMultimap<K, V>
{
    MutableSortedSet<V> replaceValues(K key, Iterable<? extends V> values);

    MutableSortedSet<V> removeAll(Object key);

    MutableSortedSetMultimap<K, V> newEmpty();

    MutableSortedSet<V> get(K key);

    MutableSetMultimap<V, K> flip();

    MutableSortedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    MutableSortedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    MutableSortedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    MutableSortedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <K2, V2> MutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <V2> MutableListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
