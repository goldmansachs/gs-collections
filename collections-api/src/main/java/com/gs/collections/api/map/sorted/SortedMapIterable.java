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

package com.gs.collections.api.map.sorted;

import java.util.Comparator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.partition.list.PartitionList;
import com.gs.collections.api.tuple.Pair;

/**
 * An iterable whose elements are unique.
 */
public interface SortedMapIterable<K, V>
        extends MapIterable<K, V>
{
    Comparator<? super K> comparator();

    SortedMapIterable<K, V> select(Predicate2<? super K, ? super V> predicate);

    SortedMapIterable<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <R> SortedMapIterable<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    ListIterable<V> select(Predicate<? super V> predicate);

    ListIterable<V> reject(Predicate<? super V> predicate);

    PartitionList<V> partition(Predicate<? super V> predicate);

    <S> ListIterable<S> selectInstancesOf(Class<S> clazz);

    <S> ListIterable<Pair<V, S>> zip(Iterable<S> that);

    ListIterable<Pair<V, Integer>> zipWithIndex();

    <VV> ListMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> ListMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);
}
