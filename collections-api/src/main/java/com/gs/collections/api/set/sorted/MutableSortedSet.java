/*
 * Copyright 2011 Goldman Sachs & Co.
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

import java.util.SortedSet;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.partition.set.sorted.PartitionMutableSortedSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * A MutableSortedSet is an implementation of a JCF SortedSet which provides methods matching the Smalltalk Collection
 * protocol.
 *
 * @since 1.0
 */
public interface MutableSortedSet<T>
        extends SortedSetIterable<T>, MutableCollection<T>, SortedSet<T>, Cloneable
{
    MutableSortedSet<T> with(T element);

    MutableSortedSet<T> without(T element);

    MutableSortedSet<T> withAll(Iterable<? extends T> elements);

    MutableSortedSet<T> withoutAll(Iterable<? extends T> elements);

    MutableSortedSet<T> newEmpty();

    MutableSortedSet<T> clone();

    MutableSortedSet<T> select(Predicate<? super T> predicate);

    <P> MutableSortedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableSortedSet<T> reject(Predicate<? super T> predicate);

    <P> MutableSortedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSortedSet<T> partition(Predicate<? super T> predicate);

    <V> MutableList<V> collect(Function<? super T, ? extends V> function);

    <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> MutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Returns an unmodifable view of the set. The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this set
     */
    MutableSortedSet<T> asUnmodifiable();

    MutableSortedSet<T> asSynchronized();

    /**
     * Returns an immutable copy of this set. If the set is immutable, it returns itself.
     * <p/>
     * The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     */
    ImmutableSortedSet<T> toImmutable();

    <V> MutableSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> MutableSortedSet<Pair<T, S>> zip(Iterable<S> that);

    MutableSortedSet<Pair<T, Integer>> zipWithIndex();

    MutableSortedSet<T> union(SetIterable<? extends T> set);

    MutableSortedSet<T> intersect(SetIterable<? extends T> set);

    MutableSortedSet<T> difference(SetIterable<? extends T> subtrahendSet);

    MutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB);

    MutableSortedSet<SortedSetIterable<T>> powerSet();

    MutableSortedSet<T> subSet(T fromElement, T toElement);

    MutableSortedSet<T> headSet(T toElement);

    MutableSortedSet<T> tailSet(T fromElement);
}
