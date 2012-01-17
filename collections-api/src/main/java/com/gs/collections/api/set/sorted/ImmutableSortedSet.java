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

package com.gs.collections.api.set.sorted;

import java.util.Set;
import java.util.SortedSet;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.partition.set.sorted.PartitionImmutableSortedSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableSortedSet is the non-modifiable equivalent interface to {@link MutableSortedSet}. {@link
 * MutableSortedSet#toImmutable()} will give you an appropriately trimmed implementation of ImmutableSortedSet. All
 * ImmutableSortedSet implementations must implement the {@link SortedSet} interface so they can satisfy the {@link
 * Set#equals(Object)} contract and be compared against other Sets.
 */
@Immutable
public interface ImmutableSortedSet<T>
        extends ImmutableCollection<T>, SortedSetIterable<T>
{
    ImmutableSortedSet<T> newWith(T element);

    ImmutableSortedSet<T> newWithout(T element);

    ImmutableSortedSet<T> newWithAll(Iterable<? extends T> elements);

    ImmutableSortedSet<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableSortedSet<T> filter(Predicate<? super T> predicate);

    ImmutableSortedSet<T> filterNot(Predicate<? super T> predicate);

    PartitionImmutableSortedSet<T> partition(Predicate<? super T> predicate);

    <V> ImmutableList<V> transform(Function<? super T, ? extends V> function);

    <V> ImmutableList<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ImmutableSortedSet<Pair<T, S>> zip(Iterable<S> that);

    ImmutableSortedSet<Pair<T, Integer>> zipWithIndex();

    SortedSet<T> castToSortedSet();

    ImmutableSortedSet<T> union(SetIterable<? extends T> set);

    ImmutableSortedSet<T> intersect(SetIterable<? extends T> set);

    ImmutableSortedSet<T> difference(SetIterable<? extends T> subtrahendSet);

    ImmutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB);

    ImmutableSortedSet<SortedSetIterable<T>> powerSet();
}
