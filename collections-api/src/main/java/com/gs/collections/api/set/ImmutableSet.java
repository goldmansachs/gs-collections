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

package com.gs.collections.api.set;

import java.util.Set;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.partition.set.PartitionImmutableSet;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableSet is the non-modifiable equivalent interface to {@link MutableSet}. {@link MutableSet#toImmutable()} will
 * give you an appropriately trimmed implementation of ImmutableSet.  All ImmutableSet implementations must implement
 * the java.util.Set interface so they can satisfy the equals() contract and be compared against other set structures
 * like UnifiedSet or HashSet.
 */
@Immutable
public interface ImmutableSet<T>
        extends ImmutableCollection<T>, UnsortedSetIterable<T>
{
    ImmutableSet<T> newWith(T element);

    ImmutableSet<T> newWithout(T element);

    ImmutableSet<T> newWithAll(Iterable<? extends T> elements);

    ImmutableSet<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableSet<T> filter(Predicate<? super T> predicate);

    ImmutableSet<T> filterNot(Predicate<? super T> predicate);

    PartitionImmutableSet<T> partition(Predicate<? super T> predicate);

    <V> ImmutableSet<V> transform(Function<? super T, ? extends V> function);

    <V> ImmutableSet<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableSet<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ImmutableSet<Pair<T, S>> zip(Iterable<S> that);

    ImmutableSet<Pair<T, Integer>> zipWithIndex();

    Set<T> castToSet();

    ImmutableSet<T> union(SetIterable<? extends T> set);

    ImmutableSet<T> intersect(SetIterable<? extends T> set);

    ImmutableSet<T> difference(SetIterable<? extends T> subtrahendSet);

    ImmutableSet<T> symmetricDifference(SetIterable<? extends T> setB);

    ImmutableSet<UnsortedSetIterable<T>> powerSet();
}
