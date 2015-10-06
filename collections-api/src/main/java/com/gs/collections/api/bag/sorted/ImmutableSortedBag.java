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

import com.gs.collections.api.bag.ImmutableBagIterable;
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
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.primitive.ImmutableBooleanList;
import com.gs.collections.api.list.primitive.ImmutableByteList;
import com.gs.collections.api.list.primitive.ImmutableCharList;
import com.gs.collections.api.list.primitive.ImmutableDoubleList;
import com.gs.collections.api.list.primitive.ImmutableFloatList;
import com.gs.collections.api.list.primitive.ImmutableIntList;
import com.gs.collections.api.list.primitive.ImmutableLongList;
import com.gs.collections.api.list.primitive.ImmutableShortList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionImmutableSortedBag;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableSortedBag is the non-modifiable equivalent interface to {@link MutableSortedBag}.
 *
 * @since 4.2
 */
@Immutable
public interface ImmutableSortedBag<T>
        extends ImmutableBagIterable<T>, SortedBag<T>
{
    ImmutableSortedBag<T> newWith(T element);

    ImmutableSortedBag<T> newWithout(T element);

    ImmutableSortedBag<T> newWithAll(Iterable<? extends T> elements);

    ImmutableSortedBag<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableSortedBag<T> selectByOccurrences(IntPredicate predicate);

    ImmutableSortedBag<T> tap(Procedure<? super T> procedure);

    ImmutableSortedBag<T> select(Predicate<? super T> predicate);

    <P> ImmutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableSortedBag<T> reject(Predicate<? super T> predicate);

    <P> ImmutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableSortedBag<T> partition(Predicate<? super T> predicate);

    <P> PartitionImmutableSortedBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ImmutableSortedBag<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableList<V> collect(Function<? super T, ? extends V> function);

    ImmutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction);

    ImmutableByteList collectByte(ByteFunction<? super T> byteFunction);

    ImmutableCharList collectChar(CharFunction<? super T> charFunction);

    ImmutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction);

    ImmutableFloatList collectFloat(FloatFunction<? super T> floatFunction);

    ImmutableIntList collectInt(IntFunction<? super T> intFunction);

    ImmutableLongList collectLong(LongFunction<? super T> longFunction);

    ImmutableShortList collectShort(ShortFunction<? super T> shortFunction);

    <P, V> ImmutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> ImmutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    ImmutableSortedSet<T> distinct();

    ImmutableSortedBag<T> takeWhile(Predicate<? super T> predicate);

    ImmutableSortedBag<T> dropWhile(Predicate<? super T> predicate);

    <V> ImmutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Can return an ImmutableMap that's backed by a LinkedHashMap.
     */
    <K, V> ImmutableMap<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);

    /**
     * Can return an ImmutableMap that's backed by a LinkedHashMap.
     */
    <K, V> ImmutableMap<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator);

    <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that);

    ImmutableSortedSet<Pair<T, Integer>> zipWithIndex();

    MutableSortedMap<T, Integer> toMapOfItemToCount();

    ImmutableSortedBag<T> toReversed();

    ImmutableSortedBag<T> take(int count);

    ImmutableSortedBag<T> drop(int count);
}
