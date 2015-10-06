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
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.list.primitive.MutableDoubleList;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.partition.set.sorted.PartitionMutableSortedSet;
import com.gs.collections.api.set.MutableSetIterable;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * A MutableSortedSet is an implementation of a JCF SortedSet which provides methods matching the Smalltalk Collection
 * protocol.
 *
 * @since 1.0
 */
public interface MutableSortedSet<T>
        extends MutableSetIterable<T>, SortedSetIterable<T>, SortedSet<T>, Cloneable
{
    MutableSortedSet<T> with(T element);

    MutableSortedSet<T> without(T element);

    MutableSortedSet<T> withAll(Iterable<? extends T> elements);

    MutableSortedSet<T> withoutAll(Iterable<? extends T> elements);

    MutableSortedSet<T> newEmpty();

    MutableSortedSet<T> clone();

    MutableSortedSet<T> tap(Procedure<? super T> procedure);

    MutableSortedSet<T> select(Predicate<? super T> predicate);

    <P> MutableSortedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableSortedSet<T> reject(Predicate<? super T> predicate);

    <P> MutableSortedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSortedSet<T> partition(Predicate<? super T> predicate);

    <P> PartitionMutableSortedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSortedSet<T> partitionWhile(Predicate<? super T> predicate);

    <S> MutableSortedSet<S> selectInstancesOf(Class<S> clazz);

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

    MutableSortedSet<T> takeWhile(Predicate<? super T> predicate);

    MutableSortedSet<T> dropWhile(Predicate<? super T> predicate);

    /**
     * Returns an unmodifable view of the set. The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this set
     */
    MutableSortedSet<T> asUnmodifiable();

    MutableSortedSet<T> asSynchronized();

    /**
     * Returns an immutable copy of this set. If the set is immutable, it returns itself.
     * <p>
     * The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     */
    ImmutableSortedSet<T> toImmutable();

    <V> MutableSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    // TODO Return linked set
    <S> MutableList<Pair<T, S>> zip(Iterable<S> that);

    MutableSortedSet<Pair<T, Integer>> zipWithIndex();

    MutableSortedSet<T> toReversed();

    MutableSortedSet<T> take(int count);

    MutableSortedSet<T> drop(int count);

    MutableSortedSet<T> union(SetIterable<? extends T> set);

    MutableSortedSet<T> intersect(SetIterable<? extends T> set);

    MutableSortedSet<T> difference(SetIterable<? extends T> subtrahendSet);

    MutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB);

    MutableSortedSet<SortedSetIterable<T>> powerSet();

    MutableSortedSet<T> subSet(T fromElement, T toElement);

    MutableSortedSet<T> headSet(T toElement);

    MutableSortedSet<T> tailSet(T fromElement);
}
