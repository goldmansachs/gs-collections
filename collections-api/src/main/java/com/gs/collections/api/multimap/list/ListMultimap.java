/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.api.multimap.list;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.multimap.bag.BagMultimap;
import com.gs.collections.api.multimap.bag.UnsortedBagMultimap;
import com.gs.collections.api.multimap.ordered.ReversibleIterableMultimap;
import com.gs.collections.api.tuple.Pair;

public interface ListMultimap<K, V>
        extends ReversibleIterableMultimap<K, V>
{
    ListMultimap<K, V> newEmpty();

    ListIterable<V> get(K key);

    MutableListMultimap<K, V> toMutable();

    ImmutableListMultimap<K, V> toImmutable();

    UnsortedBagMultimap<V, K> flip();

    ListMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    ListMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    ListMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    ListMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <K2, V2> BagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <V2> ListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
