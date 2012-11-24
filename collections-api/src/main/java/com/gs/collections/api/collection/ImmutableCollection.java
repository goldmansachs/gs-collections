/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.api.collection;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableCollection is the common interface between ImmutableList and ImmutableSet.
 */
@Immutable
public interface ImmutableCollection<T>
        extends RichIterable<T>
{
    ImmutableCollection<T> newWith(T element);

    ImmutableCollection<T> newWithout(T element);

    ImmutableCollection<T> newWithAll(Iterable<? extends T> elements);

    ImmutableCollection<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableCollection<T> select(Predicate<? super T> predicate);

    ImmutableCollection<T> reject(Predicate<? super T> predicate);

    PartitionImmutableCollection<T> partition(Predicate<? super T> predicate);

    <S> ImmutableCollection<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableCollection<V> collect(Function<? super T, ? extends V> function);

    <V> ImmutableCollection<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableCollection<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ImmutableCollection<Pair<T, S>> zip(Iterable<S> that);

    ImmutableCollection<Pair<T, Integer>> zipWithIndex();

    <K, V> ImmutableMap<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator);

    <K, V> ImmutableMap<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);
}
