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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.bag.ImmutableBagIterableMultimap;
import com.gs.collections.api.set.ImmutableSetIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 6.0
 */
public interface ImmutableSetIterableMultimap<K, V>
        extends SetMultimap<K, V>, ImmutableMultimap<K, V>
{
    ImmutableSetIterable<V> get(K key);

    ImmutableSetIterableMultimap<K, V> newEmpty();

    ImmutableSetIterableMultimap<K, V> newWith(K key, V value);

    ImmutableSetIterableMultimap<K, V> newWithout(Object key, Object value);

    ImmutableSetIterableMultimap<K, V> newWithAll(K key, Iterable<? extends V> values);

    ImmutableSetIterableMultimap<K, V> newWithoutAll(Object key);

    ImmutableSetIterableMultimap<V, K> flip();

    ImmutableSetIterableMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    ImmutableSetIterableMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    ImmutableSetIterableMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    ImmutableSetIterableMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate);

    <K2, V2> ImmutableBagIterableMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <V2> ImmutableMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);
}
