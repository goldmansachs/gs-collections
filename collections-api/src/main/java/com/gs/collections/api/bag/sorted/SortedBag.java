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

import java.util.Comparator;
import java.util.NoSuchElementException;

import com.gs.collections.api.bag.Bag;
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
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.primitive.BooleanList;
import com.gs.collections.api.list.primitive.ByteList;
import com.gs.collections.api.list.primitive.CharList;
import com.gs.collections.api.list.primitive.DoubleList;
import com.gs.collections.api.list.primitive.FloatList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.list.primitive.LongList;
import com.gs.collections.api.list.primitive.ShortList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.sorted.SortedMapIterable;
import com.gs.collections.api.multimap.sortedbag.SortedBagMultimap;
import com.gs.collections.api.ordered.ReversibleIterable;
import com.gs.collections.api.ordered.SortedIterable;
import com.gs.collections.api.partition.bag.sorted.PartitionSortedBag;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * An Iterable whose elements are sorted by some comparator or their natural ordering and may contain duplicate entries.
 *
 * @since 4.2
 */
public interface SortedBag<T>
        extends Bag<T>, Comparable<SortedBag<T>>, SortedIterable<T>, ReversibleIterable<T>
{
    SortedBag<T> selectByOccurrences(IntPredicate predicate);

    SortedMapIterable<T, Integer> toMapOfItemToCount();

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

    SortedBag<T> tap(Procedure<? super T> procedure);

    SortedBag<T> select(Predicate<? super T> predicate);

    <P> SortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    SortedBag<T> reject(Predicate<? super T> predicate);

    <P> SortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionSortedBag<T> partition(Predicate<? super T> predicate);

    <P> PartitionSortedBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionSortedBag<T> partitionWhile(Predicate<? super T> predicate);

    <S> SortedBag<S> selectInstancesOf(Class<S> clazz);

    <V> ListIterable<V> collect(Function<? super T, ? extends V> function);

    BooleanList collectBoolean(BooleanFunction<? super T> booleanFunction);

    ByteList collectByte(ByteFunction<? super T> byteFunction);

    CharList collectChar(CharFunction<? super T> charFunction);

    DoubleList collectDouble(DoubleFunction<? super T> doubleFunction);

    FloatList collectFloat(FloatFunction<? super T> floatFunction);

    IntList collectInt(IntFunction<? super T> intFunction);

    LongList collectLong(LongFunction<? super T> longFunction);

    ShortList collectShort(ShortFunction<? super T> shortFunction);

    <P, V> ListIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> ListIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ListIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    SortedSetIterable<T> distinct();

    SortedBag<T> takeWhile(Predicate<? super T> predicate);

    SortedBag<T> dropWhile(Predicate<? super T> predicate);

    <V> SortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> SortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Can return an MapIterable that's backed by a LinkedHashMap.
     */
    <K, V> MapIterable<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);

    /**
     * Can return an MapIterable that's backed by a LinkedHashMap.
     */
    <K, V> MapIterable<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator);

    /**
     * Returns the comparator used to order the elements in this bag, or null if this bag uses the natural ordering of
     * its elements.
     */
    Comparator<? super T> comparator();

    SortedSetIterable<Pair<T, Integer>> zipWithIndex();

    SortedBag<T> toReversed();

    SortedBag<T> take(int count);

    SortedBag<T> drop(int count);
}
