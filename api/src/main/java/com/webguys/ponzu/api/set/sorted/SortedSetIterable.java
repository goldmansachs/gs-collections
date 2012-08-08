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

package com.webguys.ponzu.api.set.sorted;

import java.util.Comparator;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.list.ListIterable;
import com.webguys.ponzu.api.multimap.sortedset.SortedSetMultimap;
import com.webguys.ponzu.api.partition.set.sorted.PartitionSortedSet;
import com.webguys.ponzu.api.set.SetIterable;
import com.webguys.ponzu.api.tuple.Pair;

/**
 * An iterable whose items are unique and sorted by some comparator or their natural ordering.
 */
public interface SortedSetIterable<T>
        extends SetIterable<T>, Comparable<SortedSetIterable<T>>
{
    /**
     * Returns the comparator used to order the elements in this set, or null if this set uses the natural ordering of
     * its elements.
     */
    Comparator<? super T> comparator();

    /**
     * Returns the set of all objects that are a member of {@code this} or {@code set} or both. The union of [1, 2, 3]
     * and [2, 3, 4] is the set [1, 2, 3, 4]. If equal elements appear in both sets, then the output will contain the
     * copy from {@code this}.
     */
    SortedSetIterable<T> union(SetIterable<? extends T> set);

    /**
     * Returns the set of all objects that are members of both {@code this} and {@code set}. The intersection of
     * [1, 2, 3] and [2, 3, 4] is the set [2, 3]. The output will contain instances from {@code this}, not {@code set}.
     */
    SortedSetIterable<T> intersect(SetIterable<? extends T> set);

    /**
     * Returns the set of all members of {@code this} that are not members of {@code subtrahendSet}. The difference of
     * [1,2,3] and [2,3,4] is [1].
     */
    SortedSetIterable<T> difference(SetIterable<? extends T> subtrahendSet);

    /**
     * Returns the set of all objects that are a member of exactly one of {@code this} and {@code setB} (elements which
     * are in one of the sets, but not in both). For instance, for the sets [1,2,3] and [2,3,4] , the symmetric
     * difference set is [1,4] . It is the set difference of the union and the intersection.
     */
    SortedSetIterable<T> symmetricDifference(SetIterable<? extends T> setB);

    /**
     * Returns the set whose members are all possible subsets of {@code this}. For example, the powerset of [1, 2] is
     * [[], [1], [2], [1,2]].
     */
    SortedSetIterable<SortedSetIterable<T>> powerSet();

    /**
     * Returns the set whose members are all possible ordered pairs (a,b) where a is a member of {@code this} and b is a
     * member of {@code set}.
     */
    <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set);

    SortedSetIterable<T> filter(Predicate<? super T> predicate);

    SortedSetIterable<T> filterNot(Predicate<? super T> predicate);

    PartitionSortedSet<T> partition(Predicate<? super T> predicate);

    <V> ListIterable<V> transform(Function<? super T, ? extends V> function);

    <V> ListIterable<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ListIterable<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    <V> SortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> SortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> SortedSetIterable<Pair<T, S>> zip(Iterable<S> that);

    SortedSetIterable<Pair<T, Integer>> zipWithIndex();
}
