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

package com.gs.collections.api.bag;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMapIterable;
import com.gs.collections.api.multimap.bag.MutableBagIterableMultimap;
import com.gs.collections.api.partition.bag.PartitionMutableBagIterable;
import com.gs.collections.api.set.MutableSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;

public interface MutableBagIterable<T> extends Bag<T>, MutableCollection<T>
{
    void addOccurrences(T item, int occurrences);

    boolean removeOccurrences(Object item, int occurrences);

    boolean setOccurrences(T item, int occurrences);

    MutableBagIterable<T> tap(Procedure<? super T> procedure);

    MutableBagIterable<T> select(Predicate<? super T> predicate);

    <P> MutableBagIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableBagIterable<T> reject(Predicate<? super T> predicate);

    <P> MutableBagIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableBagIterable<T> partition(Predicate<? super T> predicate);

    <P> PartitionMutableBagIterable<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> MutableBagIterable<S> selectInstancesOf(Class<S> clazz);

    <V> MutableBagIterableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableBagIterableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    MutableSetIterable<Pair<T, Integer>> zipWithIndex();

    MutableBagIterable<T> selectByOccurrences(IntPredicate predicate);

    MutableMapIterable<T, Integer> toMapOfItemToCount();

    /**
     * @since 6.0
     */
    MutableList<ObjectIntPair<T>> topOccurrences(int count);

    /**
     * @since 6.0
     */
    MutableList<ObjectIntPair<T>> bottomOccurrences(int count);

    MutableBagIterable<T> with(T element);

    MutableBagIterable<T> without(T element);

    MutableBagIterable<T> withAll(Iterable<? extends T> elements);

    MutableBagIterable<T> withoutAll(Iterable<? extends T> elements);
}
