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

package com.gs.collections.api.bag.sorted;

import com.gs.collections.api.bag.MutableBagIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.list.primitive.MutableDoubleList;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.sortedbag.MutableSortedBagMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionMutableSortedBag;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;

/**
 * @since 4.2
 */
public interface MutableSortedBag<T>
        extends SortedBag<T>, MutableBagIterable<T>, Cloneable
{
    MutableSortedBag<T> selectByOccurrences(IntPredicate predicate);

    MutableSortedMap<T, Integer> toMapOfItemToCount();

    MutableSortedBag<T> with(T element);

    MutableSortedBag<T> without(T element);

    MutableSortedBag<T> withAll(Iterable<? extends T> elements);

    MutableSortedBag<T> withoutAll(Iterable<? extends T> elements);

    MutableSortedBag<T> newEmpty();

    MutableSortedBag<T> clone();

    /**
     * Returns an unmodifiable view of the set. The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this set
     */
    MutableSortedBag<T> asUnmodifiable();

    MutableSortedBag<T> asSynchronized();

    MutableSortedBag<T> tap(Procedure<? super T> procedure);

    MutableSortedBag<T> select(Predicate<? super T> predicate);

    <P> MutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableSortedBag<T> reject(Predicate<? super T> predicate);

    <P> MutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSortedBag<T> partition(Predicate<? super T> predicate);

    <P> PartitionMutableSortedBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSortedBag<T> partitionWhile(Predicate<? super T> predicate);

    <S> MutableSortedBag<S> selectInstancesOf(Class<S> clazz);

    <V> MutableList<V> collect(Function<? super T, ? extends V> function);

    MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction);

    MutableByteList collectByte(ByteFunction<? super T> byteFunction);

    MutableCharList collectChar(CharFunction<? super T> charFunction);

    MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction);

    MutableFloatList collectFloat(FloatFunction<? super T> floatFunction);

    MutableIntList collectInt(IntFunction<? super T> intFunction);

    MutableLongList collectLong(LongFunction<? super T> longFunction);

    MutableShortList collectShort(ShortFunction<? super T> shortFunction);

    <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> MutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    MutableSortedSet<T> distinct();

    MutableSortedBag<T> takeWhile(Predicate<? super T> predicate);

    MutableSortedBag<T> dropWhile(Predicate<? super T> predicate);

    <V> MutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Can return an MutableMap that's backed by a LinkedHashMap.
     */
    <K, V> MutableMap<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);

    /**
     * Can return an MutableMap that's backed by a LinkedHashMap.
     */
    <K, V> MutableMap<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator);

    <S> MutableList<Pair<T, S>> zip(Iterable<S> that);

    MutableSortedSet<Pair<T, Integer>> zipWithIndex();

    MutableSortedBag<T> toReversed();

    MutableSortedBag<T> take(int count);

    MutableSortedBag<T> drop(int count);
}
