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

package com.gs.collections.api.bag.sorted;

import java.util.Comparator;
import java.util.NoSuchElementException;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.multimap.sortedbag.SortedBagMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionSortedBag;

/**
 * An Iterable whose elements are sorted by some comparator or their natural ordering and may contain duplicate entries.
 *
 * @since 4.2
 */
public interface SortedBag<T>
        extends Bag<T>, Comparable<SortedBag<T>>
{
    SortedBag<T> selectByOccurrences(IntPredicate predicate);

    /**
     * Convert the SortedBag to an ImmutableSortedBag.  If the bag is immutable, it returns itself.
     * Not yet supported.
     */
    ImmutableSortedBag<T> toImmutable();

    /**
     * Returns the minimum element out of this container based on the natural order, not the order of this bag.
     * If you want the minimum element based on the order of this bag, use {@link #getFirst()}.
     *
     * @throws ClassCastException     if the elements are not {@link Comparable}
     * @throws NoSuchElementException if the SortedBag is empty
     * @since 1.0
     */
    T min();

    /**
     * Returns the maximum element out of this container based on the natural order, not the order of this bag.
     * If you want the maximum element based on the order of this bag, use {@link #getLast()}.
     *
     * @throws ClassCastException     if the elements are not {@link Comparable}
     * @throws NoSuchElementException if the SortedBag is empty
     * @since 1.0
     */
    T max();

    SortedBag<T> select(Predicate<? super T> predicate);

    SortedBag<T> reject(Predicate<? super T> predicate);

    PartitionSortedBag<T> partition(Predicate<? super T> predicate);

    <S> SortedBag<S> selectInstancesOf(Class<S> clazz);

    <V> SortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> SortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Returns the comparator used to order the elements in this bag, or null if this bag uses the natural ordering of
     * its elements.
     */
    Comparator<? super T> comparator();

//    Not yet supported
//    <S> MutableBag<Pair<T, S>> zip(Iterable<S> that);
//
//    MutableBag<Pair<T, Integer>> zipWithIndex();
//    <K, V> SortedMapIterable<K, V> aggregateInPlaceBy(
//            Function<? super T, ? extends K> groupBy,
//            Function0<? extends V> zeroValueFactory,
//            Procedure2<? super V, ? super T> mutatingAggregator);
//
//    <K, V> SortedMapIterable<K, V> aggregateBy(
//            Function<? super T, ? extends K> groupBy,
//            Function0<? extends V> zeroValueFactory,
//            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);
}
