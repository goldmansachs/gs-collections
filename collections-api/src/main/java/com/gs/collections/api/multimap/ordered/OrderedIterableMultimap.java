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

package com.gs.collections.api.multimap.ordered;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.bag.BagMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 6.0
 */
public interface OrderedIterableMultimap<K, V>
        extends Multimap<K, V>
{
    OrderedIterableMultimap<K, V> newEmpty();

    OrderedIterable<V> get(K key);

    OrderedIterableMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    OrderedIterableMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    OrderedIterableMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    OrderedIterableMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <K2, V2> BagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <V2> OrderedIterableMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
