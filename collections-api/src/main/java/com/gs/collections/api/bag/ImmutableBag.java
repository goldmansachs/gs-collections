/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.api.bag;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 1.0
 */
public interface ImmutableBag<T> extends Bag<T>, ImmutableCollection<T>
{
    ImmutableBag<T> newWith(T element);

    ImmutableBag<T> newWithout(T element);

    ImmutableBag<T> newWithAll(Iterable<? extends T> elements);

    ImmutableBag<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableBag<T> selectByOccurrences(IntPredicate predicate);

    ImmutableBag<T> select(Predicate<? super T> predicate);

    ImmutableBag<T> reject(Predicate<? super T> predicate);

    PartitionImmutableBag<T> partition(Predicate<? super T> predicate);

    <S> ImmutableBag<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableBag<V> collect(Function<? super T, ? extends V> function);

    <V> ImmutableBag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that);

    ImmutableBag<Pair<T, Integer>> zipWithIndex();
}
