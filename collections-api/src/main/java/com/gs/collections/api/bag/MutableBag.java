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

package com.gs.collections.api.bag;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.tuple.Pair;

/**
 * A MutableBag is a Collection whose elements are unordered and may contain duplicate entries.  It varies from
 * MutableCollection in that it adds a protocol for determining, adding, and removing the number of occurrences for an
 * item.
 *
 * @since 1.0
 */
public interface MutableBag<T>
        extends Bag<T>, MutableCollection<T>
{
    MutableBag<T> with(T element);

    MutableBag<T> without(T element);

    MutableBag<T> withAll(Iterable<? extends T> elements);

    MutableBag<T> withoutAll(Iterable<? extends T> elements);

    MutableBag<T> newEmpty();

    void addOccurrences(T item, int occurrences);

    boolean removeOccurrences(Object item, int occurrences);

    MutableBag<T> filter(Predicate<? super T> predicate);

    <P> MutableBag<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableBag<T> filterNot(Predicate<? super T> predicate);

    <P> MutableBag<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableBag<T> partition(Predicate<? super T> predicate);

    <V> MutableBag<V> transform(Function<? super T, ? extends V> function);

    <P, V> MutableBag<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> MutableBag<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> MutableBag<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    <V> MutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> MutableBag<Pair<T, S>> zip(Iterable<S> that);

    MutableBag<Pair<T, Integer>> zipWithIndex();

    ImmutableBag<T> toImmutable();

    MutableBag<T> asUnmodifiable();

    MutableBag<T> asSynchronized();
}
