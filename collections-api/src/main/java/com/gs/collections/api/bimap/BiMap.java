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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.set.SetMultimap;
import com.gs.collections.api.partition.set.PartitionUnsortedSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * A map that allows users to look up key-value pairs from either direction. Uniqueness is enforced on both the keys and values.
 *
 * @since 4.2
 */
public interface BiMap<K, V> extends MapIterable<K, V>
{
    /**
     * Returns an inversed view of this BiMap, where the associations are in the direction of this bimap's values to keys.
     */
    BiMap<V, K> inverse();

    SetMultimap<V, K> flip();

    BiMap<V, K> flipUniqueValues();

    /**
     * Converts the BiMap to an ImmutableBiMap.  If the bimap is immutable, it returns itself.
     */
    ImmutableBiMap<K, V> toImmutable();

    BiMap<K, V> tap(Procedure<? super V> procedure);

    BiMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    BiMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    /**
     * {@inheritDoc}
     *
     * Implementations are expected to delegate to {@link MutableBiMap#put(Object, Object)},
     * {@link ImmutableBiMap#newWithKeyValue(Object, Object)}, or equivalent, not {@link MutableBiMap#forcePut(Object, Object)}.
     *
     * @throws RuntimeException when {@code function} returns colliding keys or values.
     */
    <K2, V2> BiMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    /**
     * {@inheritDoc}
     *
     * Implementations are expected to delegate to {@link MutableBiMap#put(Object, Object)},
     * {@link ImmutableBiMap#newWithKeyValue(Object, Object)}, or equivalent, not {@link MutableBiMap#forcePut(Object, Object)}.
     *
     * @throws RuntimeException when {@code function} returns colliding values.
     */
    <R> BiMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    SetIterable<V> select(Predicate<? super V> predicate);

    <P> SetIterable<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    SetIterable<V> reject(Predicate<? super V> predicate);

    <P> SetIterable<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionUnsortedSet<V> partition(Predicate<? super V> predicate);

    <P> PartitionUnsortedSet<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <S> SetIterable<S> selectInstancesOf(Class<S> clazz);

    <S> SetIterable<Pair<V, S>> zip(Iterable<S> that);

    SetIterable<Pair<V, Integer>> zipWithIndex();

    <V1> SetMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function);

    <V1> SetMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function);

    <VV> BiMap<VV, V> groupByUniqueKey(Function<? super V, ? extends VV> function);
}
