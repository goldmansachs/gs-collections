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

package com.gs.collections.api.map;

import com.gs.collections.api.bag.Bag;
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.multimap.bag.BagMultimap;
import com.gs.collections.api.multimap.set.UnsortedSetMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.PartitionBag;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.tuple.Pair;

/**
 * An iterable Map whose elements are unsorted.
 */
public interface UnsortedMapIterable<K, V>
        extends MapIterable<K, V>
{
    UnsortedSetMultimap<V, K> flip();

    UnsortedMapIterable<V, K> flipUniqueValues();

    UnsortedMapIterable<K, V> tap(Procedure<? super V> procedure);

    UnsortedMapIterable<K, V> select(Predicate2<? super K, ? super V> predicate);

    UnsortedMapIterable<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <R> UnsortedMapIterable<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    <K2, V2> UnsortedMapIterable<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    Bag<V> select(Predicate<? super V> predicate);

    <P> Bag<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    Bag<V> reject(Predicate<? super V> predicate);

    <P> Bag<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionBag<V> partition(Predicate<? super V> predicate);

    <S> Bag<S> selectInstancesOf(Class<S> clazz);

    <V1> Bag<V1> collect(Function<? super V, ? extends V1> function);

    BooleanBag collectBoolean(BooleanFunction<? super V> booleanFunction);

    ByteBag collectByte(ByteFunction<? super V> byteFunction);

    CharBag collectChar(CharFunction<? super V> charFunction);

    DoubleBag collectDouble(DoubleFunction<? super V> doubleFunction);

    FloatBag collectFloat(FloatFunction<? super V> floatFunction);

    IntBag collectInt(IntFunction<? super V> intFunction);

    LongBag collectLong(LongFunction<? super V> longFunction);

    ShortBag collectShort(ShortFunction<? super V> shortFunction);

    <P, V1> Bag<V1> collectWith(Function2<? super V, ? super P, ? extends V1> function, P parameter);

    <V1> Bag<V1> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends V1> function);

    <V1> Bag<V1> flatCollect(Function<? super V, ? extends Iterable<V1>> function);

    <V1> BagMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function);

    <V1> BagMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function);

    <V1> UnsortedMapIterable<V1, V> groupByUniqueKey(Function<? super V, ? extends V1> function);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    <S> Bag<Pair<V, S>> zip(Iterable<S> that);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    UnsortedSetIterable<Pair<V, Integer>> zipWithIndex();

    /**
     * Converts the UnsortedMapIterable to an immutable implementation. Returns this for immutable maps.
     *
     * @since 5.0
     */
    ImmutableMap<K, V> toImmutable();
}
