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

package com.gs.collections.api.map.primitive;

import com.gs.collections.api.bag.MutableBag;
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
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableByteCollection;
import com.gs.collections.api.collection.primitive.MutableCharCollection;
import com.gs.collections.api.collection.primitive.MutableDoubleCollection;
import com.gs.collections.api.collection.primitive.MutableFloatCollection;
import com.gs.collections.api.collection.primitive.MutableIntCollection;
import com.gs.collections.api.collection.primitive.MutableLongCollection;
import com.gs.collections.api.collection.primitive.MutableShortCollection;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;

public interface MutablePrimitiveObjectMap<V>
{
    void clear();

    <K, VV> MutableMap<K, VV> aggregateInPlaceBy(Function<? super V, ? extends K> groupBy, Function0<? extends VV> zeroValueFactory, Procedure2<? super VV, ? super V> mutatingAggregator);

    <K, VV> MutableMap<K, VV> aggregateBy(Function<? super V, ? extends K> groupBy, Function0<? extends VV> zeroValueFactory, Function2<? super VV, ? super V, ? extends VV> nonMutatingAggregator);

    <VV> MutableBagMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);

    <VV> MutableBagMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> MutableMap<VV, V> groupByUniqueKey(Function<? super V, ? extends VV> function);

    <VV> MutableBag<VV> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends VV> function);

    <VV> MutableCollection<VV> collect(Function<? super V, ? extends VV> function);

    MutableBooleanCollection collectBoolean(BooleanFunction<? super V> booleanFunction);

    MutableByteCollection collectByte(ByteFunction<? super V> byteFunction);

    MutableCharCollection collectChar(CharFunction<? super V> charFunction);

    MutableDoubleCollection collectDouble(DoubleFunction<? super V> doubleFunction);

    MutableFloatCollection collectFloat(FloatFunction<? super V> floatFunction);

    MutableIntCollection collectInt(IntFunction<? super V> intFunction);

    MutableLongCollection collectLong(LongFunction<? super V> longFunction);

    MutableShortCollection collectShort(ShortFunction<? super V> shortFunction);

    <P, VV> MutableCollection<VV> collectWith(Function2<? super V, ? super P, ? extends VV> function, P parameter);

    <S> MutableBag<S> selectInstancesOf(Class<S> clazz);

    MutableCollection<V> select(Predicate<? super V> predicate);

    <P> MutableCollection<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    MutableCollection<V> reject(Predicate<? super V> predicate);

    <P> MutableCollection<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionMutableBag<V> partition(Predicate<? super V> predicate);

    <P> PartitionMutableBag<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    /**
     * @deprecated in 7.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    <S> MutableBag<Pair<V, S>> zip(Iterable<S> that);

    /**
     * @deprecated in 7.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    MutableSet<Pair<V, Integer>> zipWithIndex();
}
