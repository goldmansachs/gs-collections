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

import java.util.Map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.map.MutableMapIterable;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;

/**
 * A {@link BiMap} whose contents can be altered after initialization.
 *
 * @since 4.2
 */
public interface MutableBiMap<K, V> extends BiMap<K, V>, MutableMapIterable<K, V>, Cloneable
{
    MutableBiMap<K, V> newEmpty();

    MutableBiMap<V, K> inverse();

    MutableBiMap<V, K> flipUniqueValues();

    MutableSetMultimap<V, K> flip();

    /**
     * Similar to {@link Map#put(Object, Object)}, except that it throws on the addition of a duplicate value.
     *
     * @throws IllegalArgumentException if the value already exists in the bimap.
     */
    V put(K key, V value);

    /**
     * Similar to {@link #put(Object, Object)}, except that it quietly removes any existing entry with the same
     * value before putting the key-value pair.
     */
    V forcePut(K key, V value);

    MutableBiMap<K, V> asSynchronized();

    MutableBiMap<K, V> asUnmodifiable();

    MutableBiMap<K, V> clone();

    MutableBiMap<K, V> tap(Procedure<? super V> procedure);

    MutableBiMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    MutableBiMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <K2, V2> MutableBiMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> MutableBiMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    MutableSet<V> select(Predicate<? super V> predicate);

    <P> MutableSet<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    MutableSet<V> reject(Predicate<? super V> predicate);

    <P> MutableSet<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionMutableSet<V> partition(Predicate<? super V> predicate);

    <P> PartitionMutableSet<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <S> MutableSet<S> selectInstancesOf(Class<S> clazz);

    <S> MutableSet<Pair<V, S>> zip(Iterable<S> that);

    MutableSet<Pair<V, Integer>> zipWithIndex();

    <V1> MutableSetMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function);

    <V1> MutableSetMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function);

    <VV> MutableBiMap<VV, V> groupByUniqueKey(Function<? super V, ? extends VV> function);

    MutableBiMap<K, V> withKeyValue(K key, V value);

    MutableBiMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    MutableBiMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    MutableBiMap<K, V> withoutKey(K key);

    MutableBiMap<K, V> withoutAllKeys(Iterable<? extends K> keys);
}
