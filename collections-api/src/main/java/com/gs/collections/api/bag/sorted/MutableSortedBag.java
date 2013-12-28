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

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.multimap.sortedbag.MutableSortedBagMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;

/**
 * @since 4.2
 */
public interface MutableSortedBag<T>
        extends SortedBag<T>, MutableBag<T>, Cloneable
{
    MutableSortedBag<T> selectByOccurrences(IntPredicate predicate);

    MutableSortedBag<T> with(T element);

    MutableSortedBag<T> without(T element);

    MutableSortedBag<T> withAll(Iterable<? extends T> elements);

    MutableSortedBag<T> withoutAll(Iterable<? extends T> elements);

    MutableSortedBag<T> newEmpty();

    /**
     * Returns an unmodifable view of the set. The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this set
     */
    MutableSortedBag<T> asUnmodifiable();

    MutableSortedBag<T> asSynchronized();

    <V> MutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    MutableSortedBag<T> select(Predicate<? super T> predicate);

    <P> MutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableSortedBag<T> reject(Predicate<? super T> predicate);

    <P> MutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSortedBag<T> partition(Predicate<? super T> predicate);

    <S> MutableSortedBag<S> selectInstancesOf(Class<S> clazz);

    <V> MutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    // Not yet supported
    // <S> MutableBag<Pair<T, S>> zip(Iterable<S> that);
    //
    // MutableBag<Pair<T, Integer>> zipWithIndex();
    //
    // <K, V> SortedMapIterable<K, V> aggregateInPlaceBy(
    //            Function<? super T, ? extends K> groupBy,
    //            Function0<? extends V> zeroValueFactory,
    //            Procedure2<? super V, ? super T> mutatingAggregator);
    //
    // <K, V> SortedMapIterable<K, V> aggregateBy(
    //            Function<? super T, ? extends K> groupBy,
    //            Function0<? extends V> zeroValueFactory,
    //            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);
    // MutableSortedMap<T, Integer> toMapOfItemToCount();
}
