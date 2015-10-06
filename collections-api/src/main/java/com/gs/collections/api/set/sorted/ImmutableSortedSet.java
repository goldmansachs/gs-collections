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

package com.gs.collections.api.set.sorted;

import java.util.Set;
import java.util.SortedSet;

import com.gs.collections.api.block.function.Function;
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.primitive.ImmutableBooleanList;
import com.gs.collections.api.list.primitive.ImmutableByteList;
import com.gs.collections.api.list.primitive.ImmutableCharList;
import com.gs.collections.api.list.primitive.ImmutableDoubleList;
import com.gs.collections.api.list.primitive.ImmutableFloatList;
import com.gs.collections.api.list.primitive.ImmutableIntList;
import com.gs.collections.api.list.primitive.ImmutableLongList;
import com.gs.collections.api.list.primitive.ImmutableShortList;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.partition.set.sorted.PartitionImmutableSortedSet;
import com.gs.collections.api.set.ImmutableSetIterable;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableSortedSet is the non-modifiable equivalent interface to {@link MutableSortedSet}. {@link
 * MutableSortedSet#toImmutable()} will give you an appropriately trimmed implementation of ImmutableSortedSet. All
 * ImmutableSortedSet implementations must implement the {@link SortedSet} interface so they can satisfy the {@link
 * Set#equals(Object)} contract and be compared against other Sets.
 */
@Immutable
public interface ImmutableSortedSet<T>
        extends SortedSetIterable<T>, ImmutableSetIterable<T>
{
    ImmutableSortedSet<T> newWith(T element);

    ImmutableSortedSet<T> newWithout(T element);

    ImmutableSortedSet<T> newWithAll(Iterable<? extends T> elements);

    ImmutableSortedSet<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableSortedSet<T> tap(Procedure<? super T> procedure);

    ImmutableSortedSet<T> select(Predicate<? super T> predicate);

    <P> ImmutableSortedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableSortedSet<T> reject(Predicate<? super T> predicate);

    <P> ImmutableSortedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableSortedSet<T> partition(Predicate<? super T> predicate);

    <P> PartitionImmutableSortedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableSortedSet<T> partitionWhile(Predicate<? super T> predicate);

    <S> ImmutableSortedSet<S> selectInstancesOf(Class<S> clazz);

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

    ImmutableSortedSet<T> takeWhile(Predicate<? super T> predicate);

    ImmutableSortedSet<T> dropWhile(Predicate<? super T> predicate);

    <V> ImmutableSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that);

    ImmutableSortedSet<Pair<T, Integer>> zipWithIndex();

    ImmutableSortedSet<T> toReversed();

    ImmutableSortedSet<T> take(int count);

    ImmutableSortedSet<T> drop(int count);

    SortedSet<T> castToSortedSet();

    ImmutableSortedSet<T> union(SetIterable<? extends T> set);

    ImmutableSortedSet<T> intersect(SetIterable<? extends T> set);

    ImmutableSortedSet<T> difference(SetIterable<? extends T> subtrahendSet);

    ImmutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB);

    ImmutableSortedSet<SortedSetIterable<T>> powerSet();
}
