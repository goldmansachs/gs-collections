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

package com.webguys.ponzu.api.map;

import java.util.Map;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.collection.ImmutableCollection;
import com.webguys.ponzu.api.multimap.ImmutableMultimap;
import com.webguys.ponzu.api.partition.PartitionImmutableCollection;
import com.webguys.ponzu.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * A MutableMap is similar to a JCF Map but adds additional useful internal iterator methods.
 * The MutableMap interface additionally implements some of the methods in the Smalltalk Dictionary protocol.
 */
@Immutable
public interface ImmutableMap<K, V>
        extends UnsortedMapIterable<K, V>
{
    ImmutableMap<K, V> filter(Predicate2<? super K, ? super V> predicate);

    <K2, V2> ImmutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> ImmutableMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function);

    ImmutableMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate);

    Map<K, V> castToMap();

    ImmutableMap<K, V> newWithKeyValue(K key, V value);

    ImmutableMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    ImmutableMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    ImmutableMap<K, V> newWithoutKey(K key);

    ImmutableMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys);

    MutableMap<K, V> toMap();

    <R> ImmutableCollection<R> transform(Function<? super V, ? extends R> function);

    <R> ImmutableCollection<R> transformIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function);

    <R> ImmutableCollection<R> flatTransform(Function<? super V, ? extends Iterable<R>> function);

    ImmutableCollection<V> filterNot(Predicate<? super V> predicate);

    ImmutableCollection<V> filter(Predicate<? super V> predicate);

    PartitionImmutableCollection<V> partition(Predicate<? super V> predicate);

    <S> ImmutableCollection<Pair<V, S>> zip(Iterable<S> that);

    ImmutableCollection<Pair<V, Integer>> zipWithIndex();

    <VV> ImmutableMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> ImmutableMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);
}
