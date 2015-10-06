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

import com.gs.collections.api.bag.sorted.SortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.bag.BagMultimap;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.multimap.ordered.ReversibleIterableMultimap;
import com.gs.collections.api.multimap.ordered.SortedIterableMultimap;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 4.2
 */
public interface SortedBagMultimap<K, V>
        extends BagMultimap<K, V>, SortedIterableMultimap<K, V>, ReversibleIterableMultimap<K, V>
{
    SortedBagMultimap<K, V> newEmpty();

    SortedBag<V> get(K key);

    MutableSortedBagMultimap<K, V> toMutable();

    ImmutableSortedBagMultimap<K, V> toImmutable();

    SortedBagMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    SortedBagMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    SortedBagMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    SortedBagMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <K2, V2> BagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <V2> ListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
