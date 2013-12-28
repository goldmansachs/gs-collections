/*
 * Copyright 2013 Goldman Sachs.
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
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.tuple.Pair;

public interface MutablePrimitiveObjectMap<V>
{
    void clear();

    <K, V1> MutableMap<K, V1> aggregateInPlaceBy(Function<? super V, ? extends K> groupBy, Function0<? extends V1> zeroValueFactory, Procedure2<? super V1, ? super V> mutatingAggregator);

    <K, V1> MutableMap<K, V1> aggregateBy(Function<? super V, ? extends K> groupBy, Function0<? extends V1> zeroValueFactory, Function2<? super V1, ? super V, ? extends V1> nonMutatingAggregator);

    <V1> MutableMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function);

    <V1> MutableMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function);

    <V1> MutableCollection<V1> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends V1> function);

    <V1> MutableCollection<V1> collect(Function<? super V, ? extends V1> function);

    MutableBooleanCollection collectBoolean(BooleanFunction<? super V> booleanFunction);

    MutableByteCollection collectByte(ByteFunction<? super V> byteFunction);

    MutableCharCollection collectChar(CharFunction<? super V> charFunction);

    MutableDoubleCollection collectDouble(DoubleFunction<? super V> doubleFunction);

    MutableFloatCollection collectFloat(FloatFunction<? super V> floatFunction);

    MutableIntCollection collectInt(IntFunction<? super V> intFunction);

    MutableLongCollection collectLong(LongFunction<? super V> longFunction);

    MutableShortCollection collectShort(ShortFunction<? super V> shortFunction);

    <S> MutableCollection<S> selectInstancesOf(Class<S> clazz);

    MutableCollection<V> select(Predicate<? super V> predicate);

    <P> MutableCollection<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    MutableCollection<V> reject(Predicate<? super V> predicate);

    <P> MutableCollection<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionMutableCollection<V> partition(Predicate<? super V> predicate);

    <S> MutableCollection<Pair<V, S>> zip(Iterable<S> that);

    MutableCollection<Pair<V, Integer>> zipWithIndex();
}
