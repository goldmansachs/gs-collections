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

import com.gs.collections.api.bag.primitive.BooleanBag;
import com.gs.collections.api.bag.primitive.ByteBag;
import com.gs.collections.api.bag.primitive.CharBag;
import com.gs.collections.api.bag.primitive.DoubleBag;
import com.gs.collections.api.bag.primitive.FloatBag;
import com.gs.collections.api.bag.primitive.IntBag;
import com.gs.collections.api.bag.primitive.LongBag;
import com.gs.collections.api.bag.primitive.ShortBag;
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
import com.gs.collections.api.multimap.bag.UnsortedBagMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.PartitionUnsortedBag;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.tuple.Pair;

public interface UnsortedBag<T> extends Bag<T>
{
    UnsortedBag<T> selectByOccurrences(IntPredicate predicate);

    UnsortedBag<T> select(Predicate<? super T> predicate);

    <P> UnsortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    UnsortedBag<T> reject(Predicate<? super T> predicate);

    <P> UnsortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> UnsortedBag<S> selectInstancesOf(Class<S> clazz);

    PartitionUnsortedBag<T> partition(Predicate<? super T> predicate);

    <V> UnsortedBag<V> collect(Function<? super T, ? extends V> function);

    BooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction);

    ByteBag collectByte(ByteFunction<? super T> byteFunction);

    CharBag collectChar(CharFunction<? super T> charFunction);

    DoubleBag collectDouble(DoubleFunction<? super T> doubleFunction);

    FloatBag collectFloat(FloatFunction<? super T> floatFunction);

    IntBag collectInt(IntFunction<? super T> intFunction);

    LongBag collectLong(LongFunction<? super T> longFunction);

    ShortBag collectShort(ShortFunction<? super T> shortFunction);

    <P, V> UnsortedBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> UnsortedBag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> UnsortedBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    <S> UnsortedBag<Pair<T, S>> zip(Iterable<S> that);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    UnsortedSetIterable<Pair<T, Integer>> zipWithIndex();

    <V> UnsortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> UnsortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Converts the UnsortedBag to an ImmutableBag. If the bag is immutable, it returns itself.
     */
    ImmutableBag<T> toImmutable();
}
