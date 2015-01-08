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

package com.gs.collections.api.set;

import java.util.Set;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.set.MutableSetIterableMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.set.PartitionMutableSetIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 6.0
 */
public interface MutableSetIterable<T> extends SetIterable<T>, MutableCollection<T>, Set<T>
{
    MutableSetIterable<T> tap(Procedure<? super T> procedure);

    MutableSetIterable<T> select(Predicate<? super T> predicate);

    <P> MutableSetIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableSetIterable<T> reject(Predicate<? super T> predicate);

    <P> MutableSetIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSetIterable<T> partition(Predicate<? super T> predicate);

    <P> PartitionMutableSetIterable<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> MutableSetIterable<S> selectInstancesOf(Class<S> clazz);

    <V> MutableSetIterableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableSetIterableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    <S> MutableCollection<Pair<T, S>> zip(Iterable<S> that);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    MutableSetIterable<Pair<T, Integer>> zipWithIndex();
}
