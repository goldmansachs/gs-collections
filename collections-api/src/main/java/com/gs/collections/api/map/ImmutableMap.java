/*
 * Copyright 2011 Goldman Sachs.
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

import java.util.Map;

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
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.collection.primitive.ImmutableByteCollection;
import com.gs.collections.api.collection.primitive.ImmutableCharCollection;
import com.gs.collections.api.collection.primitive.ImmutableDoubleCollection;
import com.gs.collections.api.collection.primitive.ImmutableFloatCollection;
import com.gs.collections.api.collection.primitive.ImmutableIntCollection;
import com.gs.collections.api.collection.primitive.ImmutableLongCollection;
import com.gs.collections.api.collection.primitive.ImmutableShortCollection;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * An ImmutableMap is different than a JCF Map but in that it has no mutating methods.  It shares the read-only
 * protocol of a JDK Map.
 */
@Immutable
public interface ImmutableMap<K, V>
        extends UnsortedMapIterable<K, V>
{
    ImmutableMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    <K2, V2> ImmutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> ImmutableMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    ImmutableMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    Map<K, V> castToMap();

    ImmutableMap<K, V> newWithKeyValue(K key, V value);

    ImmutableMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    ImmutableMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    ImmutableMap<K, V> newWithoutKey(K key);

    ImmutableMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys);

    MutableMap<K, V> toMap();

    <R> ImmutableCollection<R> collect(Function<? super V, ? extends R> function);

    ImmutableBooleanCollection collectBoolean(BooleanFunction<? super V> booleanFunction);

    ImmutableByteCollection collectByte(ByteFunction<? super V> byteFunction);

    ImmutableCharCollection collectChar(CharFunction<? super V> charFunction);

    ImmutableDoubleCollection collectDouble(DoubleFunction<? super V> doubleFunction);

    ImmutableFloatCollection collectFloat(FloatFunction<? super V> floatFunction);

    ImmutableIntCollection collectInt(IntFunction<? super V> intFunction);

    ImmutableLongCollection collectLong(LongFunction<? super V> longFunction);

    ImmutableShortCollection collectShort(ShortFunction<? super V> shortFunction);

    <R> ImmutableCollection<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function);

    <R> ImmutableCollection<R> flatCollect(Function<? super V, ? extends Iterable<R>> function);

    ImmutableCollection<V> select(Predicate<? super V> predicate);

    ImmutableCollection<V> reject(Predicate<? super V> predicate);

    PartitionImmutableCollection<V> partition(Predicate<? super V> predicate);

    <S> ImmutableCollection<S> selectInstancesOf(Class<S> clazz);

    <S> ImmutableCollection<Pair<V, S>> zip(Iterable<S> that);

    ImmutableCollection<Pair<V, Integer>> zipWithIndex();

    <VV> ImmutableMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> ImmutableMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);

    <K2, V2> ImmutableMap<K2, V2> aggregateInPlaceBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Procedure2<? super V2, ? super V> mutatingAggregator);

    <K2, V2> ImmutableMap<K2, V2> aggregateBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator);
}
