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

package ponzu.api.map.sorted;

import java.util.SortedMap;

import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.list.ImmutableList;
import ponzu.api.map.ImmutableMap;
import ponzu.api.multimap.list.ImmutableListMultimap;
import ponzu.api.partition.list.PartitionImmutableList;
import ponzu.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * A MutableSortedMap is similar to a JCF SortedMap but adds additional useful internal iterator methods.
 * The MutableSortedMap interface additionally implements some of the methods in the Smalltalk Dictionary protocol.
 */
@Immutable
public interface ImmutableSortedMap<K, V>
        extends SortedMapIterable<K, V>
{
    SortedMap<K, V> castToSortedMap();

    ImmutableSortedMap<K, V> newWithKeyValue(K key, V value);

    ImmutableSortedMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    ImmutableSortedMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    ImmutableSortedMap<K, V> newWithoutKey(K key);

    ImmutableSortedMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys);

    MutableSortedMap<K, V> toSortedMap();

    ImmutableSortedMap<K, V> filter(Predicate2<? super K, ? super V> predicate);

    <K2, V2> ImmutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function);

    ImmutableSortedMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate);

    PartitionImmutableList<V> partition(Predicate<? super V> predicate);

    <R> ImmutableSortedMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function);

    ImmutableList<V> filter(Predicate<? super V> predicate);

    ImmutableList<V> filterNot(Predicate<? super V> predicate);

    <R> ImmutableList<R> transform(Function<? super V, ? extends R> function);

    <R> ImmutableList<R> transformIf(
            Predicate<? super V> predicate,
            Function<? super V, ? extends R> function);

    <R> ImmutableList<R> flatTransform(Function<? super V, ? extends Iterable<R>> function);

    <S> ImmutableList<Pair<V, S>> zip(Iterable<S> that);

    ImmutableList<Pair<V, Integer>> zipWithIndex();

    <VV> ImmutableListMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> ImmutableListMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);
}
