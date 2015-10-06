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

import java.util.Map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.tuple.Pair;

public interface ImmutableMapIterable<K, V> extends MapIterable<K, V>
{
    Map<K, V> castToMap();

    ImmutableMapIterable<K, V> newWithKeyValue(K key, V value);

    ImmutableMapIterable<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    ImmutableMapIterable<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    ImmutableMapIterable<K, V> newWithoutKey(K key);

    ImmutableMapIterable<K, V> newWithoutAllKeys(Iterable<? extends K> keys);

    // TODO
    // ImmutableSetIterable<K> keySet();

    ImmutableMapIterable<K, V> tap(Procedure<? super V> procedure);

    ImmutableMapIterable<V, K> flipUniqueValues();

    ImmutableMultimap<V, K> flip();

    ImmutableMapIterable<K, V> select(Predicate2<? super K, ? super V> predicate);

    ImmutableMapIterable<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <K2, V2> ImmutableMapIterable<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> ImmutableMapIterable<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    ImmutableCollection<V> select(Predicate<? super V> predicate);

    <P> ImmutableCollection<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    ImmutableCollection<V> reject(Predicate<? super V> predicate);

    <P> ImmutableCollection<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionImmutableCollection<V> partition(Predicate<? super V> predicate);

    <S> ImmutableCollection<S> selectInstancesOf(Class<S> clazz);

    <V1> ImmutableMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function);

    <V1> ImmutableMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function);

    <V1> ImmutableMapIterable<V1, V> groupByUniqueKey(Function<? super V, ? extends V1> function);

    <S> ImmutableCollection<Pair<V, S>> zip(Iterable<S> that);

    ImmutableCollection<Pair<V, Integer>> zipWithIndex();

    <KK, VV> ImmutableMapIterable<KK, VV> aggregateInPlaceBy(Function<? super V, ? extends KK> groupBy, Function0<? extends VV> zeroValueFactory, Procedure2<? super VV, ? super V> mutatingAggregator);

    <KK, VV> ImmutableMapIterable<KK, VV> aggregateBy(Function<? super V, ? extends KK> groupBy, Function0<? extends VV> zeroValueFactory, Function2<? super VV, ? super V, ? extends VV> nonMutatingAggregator);
}
