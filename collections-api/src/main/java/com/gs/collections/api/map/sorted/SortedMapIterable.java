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

import java.util.Comparator;

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
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.primitive.BooleanList;
import com.gs.collections.api.list.primitive.ByteList;
import com.gs.collections.api.list.primitive.CharList;
import com.gs.collections.api.list.primitive.DoubleList;
import com.gs.collections.api.list.primitive.FloatList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.list.primitive.LongList;
import com.gs.collections.api.list.primitive.ShortList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.multimap.sortedset.SortedSetMultimap;
import com.gs.collections.api.partition.list.PartitionList;
import com.gs.collections.api.tuple.Pair;

/**
 * An iterable Map whose elements are sorted.
 */
public interface SortedMapIterable<K, V>
        extends MapIterable<K, V> // TODO: OrderedIterable<V>
{
    Comparator<? super K> comparator();

    SortedSetMultimap<V, K> flip();

    // TODO
    // OrderedMapIterable<V, K> flipUniqueValues();

    SortedMapIterable<K, V> select(Predicate2<? super K, ? super V> predicate);

    SortedMapIterable<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <R> SortedMapIterable<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    SortedMapIterable<K, V> tap(Procedure<? super V> procedure);

    ListIterable<V> select(Predicate<? super V> predicate);

    <P> ListIterable<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    ListIterable<V> reject(Predicate<? super V> predicate);

    <P> ListIterable<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionList<V> partition(Predicate<? super V> predicate);

    <P> PartitionList<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <S> ListIterable<S> selectInstancesOf(Class<S> clazz);

    <V1> ListIterable<V1> collect(Function<? super V, ? extends V1> function);

    BooleanList collectBoolean(BooleanFunction<? super V> booleanFunction);

    ByteList collectByte(ByteFunction<? super V> byteFunction);

    CharList collectChar(CharFunction<? super V> charFunction);

    DoubleList collectDouble(DoubleFunction<? super V> doubleFunction);

    FloatList collectFloat(FloatFunction<? super V> floatFunction);

    IntList collectInt(IntFunction<? super V> intFunction);

    LongList collectLong(LongFunction<? super V> longFunction);

    ShortList collectShort(ShortFunction<? super V> shortFunction);

    <P, V1> ListIterable<V1> collectWith(Function2<? super V, ? super P, ? extends V1> function, P parameter);

    <V1> ListIterable<V1> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends V1> function);

    <V1> ListIterable<V1> flatCollect(Function<? super V, ? extends Iterable<V1>> function);

    <S> ListIterable<Pair<V, S>> zip(Iterable<S> that);

    ListIterable<Pair<V, Integer>> zipWithIndex();

    <VV> ListMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> ListMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);

    /**
     * Converts the SortedMapIterable to an immutable implementation. Returns this for immutable maps.
     *
     * @since 5.0
     */
    ImmutableSortedMap<K, V> toImmutable();
}
