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

package com.gs.collections.api.bimap;

import com.gs.collections.api.bag.ImmutableBagIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.map.ImmutableMapIterable;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.partition.set.PartitionImmutableSet;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.tuple.Pair;

/**
 * A {@link BiMap} whose contents cannot be altered after initialization.
 *
 * @since 4.2
 */
public interface ImmutableBiMap<K, V> extends BiMap<K, V>, ImmutableMapIterable<K, V>
{
    ImmutableBiMap<K, V> newWithKeyValue(K key, V value);

    ImmutableBiMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    ImmutableBiMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    ImmutableBiMap<K, V> newWithoutKey(K key);

    ImmutableBiMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys);

    ImmutableBiMap<V, K> inverse();

    ImmutableSetMultimap<V, K> flip();

    ImmutableBiMap<V, K> flipUniqueValues();

    ImmutableBiMap<K, V> tap(Procedure<? super V> procedure);

    ImmutableBiMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    ImmutableBiMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <K2, V2> ImmutableBiMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> ImmutableBiMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    ImmutableSet<V> select(Predicate<? super V> predicate);

    <P> ImmutableSet<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    ImmutableSet<V> reject(Predicate<? super V> predicate);

    <P> ImmutableSet<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionImmutableSet<V> partition(Predicate<? super V> predicate);

    <P> PartitionImmutableSet<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <S> ImmutableSet<S> selectInstancesOf(Class<S> clazz);

    <V1> ImmutableBagIterable<V1> collect(Function<? super V, ? extends V1> function);

    <P, V1> ImmutableBagIterable<V1> collectWith(Function2<? super V, ? super P, ? extends V1> function, P parameter);

    <V1> ImmutableBagIterable<V1> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends V1> function);

    <V1> ImmutableBagIterable<V1> flatCollect(Function<? super V, ? extends Iterable<V1>> function);

    <V1> ImmutableSetMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function);

    <V1> ImmutableSetMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function);

    <VV> ImmutableBiMap<VV, V> groupByUniqueKey(Function<? super V, ? extends VV> function);

    <S> ImmutableSet<Pair<V, S>> zip(Iterable<S> that);

    ImmutableSet<Pair<V, Integer>> zipWithIndex();
}
