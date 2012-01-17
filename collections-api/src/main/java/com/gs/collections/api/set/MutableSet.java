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
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.api.tuple.Pair;

/**
 * A MutableSet is an implementation of a JCF Set which provides methods matching the Smalltalk Collection protocol.
 */
public interface MutableSet<T>
        extends UnsortedSetIterable<T>, MutableCollection<T>, Set<T>, Cloneable
{
    MutableSet<T> with(T element);

    MutableSet<T> without(T element);

    MutableSet<T> withAll(Iterable<? extends T> elements);

    MutableSet<T> withoutAll(Iterable<? extends T> elements);

    MutableSet<T> newEmpty();

    MutableSet<T> clone();

    MutableSet<T> filter(Predicate<? super T> predicate);

    <P> MutableSet<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableSet<T> filterNot(Predicate<? super T> predicate);

    <P> MutableSet<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSet<T> partition(Predicate<? super T> predicate);

    <V> MutableSet<V> transform(Function<? super T, ? extends V> function);

    <P, V> MutableSet<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> MutableSet<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> MutableSet<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Returns an unmodifable view of the set.
     * The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this set
     */
    MutableSet<T> asUnmodifiable();

    MutableSet<T> asSynchronized();

    /**
     * Returns an immutable copy of this set. If the set is immutable, it returns itself.
     * <p/>
     * The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     */
    ImmutableSet<T> toImmutable();

    <V> MutableSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> MutableSet<Pair<T, S>> zip(Iterable<S> that);

    MutableSet<Pair<T, Integer>> zipWithIndex();

    MutableSet<T> union(SetIterable<? extends T> set);

    MutableSet<T> intersect(SetIterable<? extends T> set);

    MutableSet<T> difference(SetIterable<? extends T> subtrahendSet);

    MutableSet<T> symmetricDifference(SetIterable<? extends T> setB);

    MutableSet<UnsortedSetIterable<T>> powerSet();
}
