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

import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.bag.primitive.ImmutableByteBag;
import com.gs.collections.api.bag.primitive.ImmutableCharBag;
import com.gs.collections.api.bag.primitive.ImmutableDoubleBag;
import com.gs.collections.api.bag.primitive.ImmutableFloatBag;
import com.gs.collections.api.bag.primitive.ImmutableIntBag;
import com.gs.collections.api.bag.primitive.ImmutableLongBag;
import com.gs.collections.api.bag.primitive.ImmutableShortBag;
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
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;

/**
 * @since 1.0
 */
public interface ImmutableBag<T> extends UnsortedBag<T>, ImmutableBagIterable<T>
{
    ImmutableBag<T> newWith(T element);

    ImmutableBag<T> newWithout(T element);

    ImmutableBag<T> newWithAll(Iterable<? extends T> elements);

    ImmutableBag<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableBag<T> selectByOccurrences(IntPredicate predicate);

    ImmutableBag<T> tap(Procedure<? super T> procedure);

    ImmutableBag<T> select(Predicate<? super T> predicate);

    <P> ImmutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableBag<T> reject(Predicate<? super T> predicate);

    <P> ImmutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableBag<T> partition(Predicate<? super T> predicate);

    <P> PartitionImmutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ImmutableBag<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableBag<V> collect(Function<? super T, ? extends V> function);

    ImmutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction);

    ImmutableByteBag collectByte(ByteFunction<? super T> byteFunction);

    ImmutableCharBag collectChar(CharFunction<? super T> charFunction);

    ImmutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction);

    ImmutableFloatBag collectFloat(FloatFunction<? super T> floatFunction);

    ImmutableIntBag collectInt(IntFunction<? super T> intFunction);

    ImmutableLongBag collectLong(LongFunction<? super T> longFunction);

    ImmutableShortBag collectShort(ShortFunction<? super T> shortFunction);

    <P, V> ImmutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> ImmutableBag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    ImmutableSet<Pair<T, Integer>> zipWithIndex();

    /**
     * @since 6.0
     */
    ImmutableList<ObjectIntPair<T>> topOccurrences(int count);

    /**
     * @since 6.0
     */
    ImmutableList<ObjectIntPair<T>> bottomOccurrences(int count);
}
