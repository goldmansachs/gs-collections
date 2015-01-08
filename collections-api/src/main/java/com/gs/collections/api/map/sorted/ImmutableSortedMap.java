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

package com.gs.collections.api.map.sorted;

import java.util.SortedMap;

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
import com.gs.collections.api.map.ImmutableMapIterable;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.partition.list.PartitionImmutableList;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * An ImmutableSortedMap is different than a JCF SortedMap in that it has no mutating methods, but it shares
 * the read-only protocol of a SortedMap.
 */
@Immutable
public interface ImmutableSortedMap<K, V>
        extends SortedMapIterable<K, V>, ImmutableMapIterable<K, V>
{
    SortedMap<K, V> castToMap();

    SortedMap<K, V> castToSortedMap();

    ImmutableSortedSetMultimap<V, K> flip();

    ImmutableSortedMap<K, V> newWithKeyValue(K key, V value);

    ImmutableSortedMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    ImmutableSortedMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    ImmutableSortedMap<K, V> newWithoutKey(K key);

    ImmutableSortedMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys);

    MutableSortedMap<K, V> toSortedMap();

    ImmutableMap<V, K> flipUniqueValues();

    ImmutableSortedMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    ImmutableSortedMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    ImmutableSortedMap<K, V> tap(Procedure<? super V> procedure);

    ImmutableList<V> select(Predicate<? super V> predicate);

    <P> ImmutableList<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    ImmutableList<V> reject(Predicate<? super V> predicate);

    <P> ImmutableList<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <S> ImmutableList<S> selectInstancesOf(Class<S> clazz);

    PartitionImmutableList<V> partition(Predicate<? super V> predicate);

    <P> PartitionImmutableList<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <R> ImmutableList<R> collect(Function<? super V, ? extends R> function);

    <P, VV> ImmutableList<VV> collectWith(Function2<? super V, ? super P, ? extends VV> function, P parameter);

    <K2, V2> ImmutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> ImmutableSortedMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    <R> ImmutableList<R> collectIf(
            Predicate<? super V> predicate,
            Function<? super V, ? extends R> function);

    <R> ImmutableList<R> flatCollect(Function<? super V, ? extends Iterable<R>> function);

    ImmutableBooleanList collectBoolean(BooleanFunction<? super V> booleanFunction);

    ImmutableByteList collectByte(ByteFunction<? super V> byteFunction);

    ImmutableCharList collectChar(CharFunction<? super V> charFunction);

    ImmutableDoubleList collectDouble(DoubleFunction<? super V> doubleFunction);

    ImmutableFloatList collectFloat(FloatFunction<? super V> floatFunction);

    ImmutableIntList collectInt(IntFunction<? super V> intFunction);

    ImmutableLongList collectLong(LongFunction<? super V> longFunction);

    ImmutableShortList collectShort(ShortFunction<? super V> shortFunction);

    <S> ImmutableList<Pair<V, S>> zip(Iterable<S> that);

    ImmutableList<Pair<V, Integer>> zipWithIndex();

    <VV> ImmutableListMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> ImmutableListMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);

    <VV> ImmutableMap<VV, V> groupByUniqueKey(Function<? super V, ? extends VV> function);

    <K2, V2> ImmutableMap<K2, V2> aggregateInPlaceBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Procedure2<? super V2, ? super V> mutatingAggregator);

    <K2, V2> ImmutableMap<K2, V2> aggregateBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator);
}
