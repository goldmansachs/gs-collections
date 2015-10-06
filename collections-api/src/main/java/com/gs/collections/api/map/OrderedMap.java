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
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.primitive.BooleanList;
import com.gs.collections.api.list.primitive.ByteList;
import com.gs.collections.api.list.primitive.CharList;
import com.gs.collections.api.list.primitive.DoubleList;
import com.gs.collections.api.list.primitive.FloatList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.list.primitive.LongList;
import com.gs.collections.api.list.primitive.ShortList;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.ordered.ReversibleIterable;
import com.gs.collections.api.partition.list.PartitionList;
import com.gs.collections.api.tuple.Pair;

/**
 * A map whose keys are ordered but not necessarily sorted, for example a linked hash map.
 */
public interface OrderedMap<K, V>
        extends MapIterable<K, V>, ReversibleIterable<V>
{
    OrderedMap<K, V> tap(Procedure<? super V> procedure);

    OrderedMap<V, K> flipUniqueValues();

    ListMultimap<V, K> flip();

    OrderedMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    OrderedMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    <K2, V2> OrderedMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> OrderedMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    ImmutableOrderedMap<K, V> toImmutable();

    OrderedMap<K, V> toReversed();

    OrderedMap<K, V> take(int count);

    OrderedMap<K, V> takeWhile(Predicate<? super V> predicate);

    OrderedMap<K, V> drop(int count);

    OrderedMap<K, V> dropWhile(Predicate<? super V> predicate);

    PartitionList<V> partitionWhile(Predicate<? super V> predicate);

    ListIterable<V> distinct();

    ListIterable<V> select(Predicate<? super V> predicate);

    <P> ListIterable<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    ListIterable<V> reject(Predicate<? super V> predicate);

    <P> ListIterable<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionList<V> partition(Predicate<? super V> predicate);

    <P> PartitionList<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    BooleanList collectBoolean(BooleanFunction<? super V> booleanFunction);

    ByteList collectByte(ByteFunction<? super V> byteFunction);

    CharList collectChar(CharFunction<? super V> charFunction);

    DoubleList collectDouble(DoubleFunction<? super V> doubleFunction);

    FloatList collectFloat(FloatFunction<? super V> floatFunction);

    IntList collectInt(IntFunction<? super V> intFunction);

    LongList collectLong(LongFunction<? super V> longFunction);

    ShortList collectShort(ShortFunction<? super V> shortFunction);

    <S> ListIterable<Pair<V, S>> zip(Iterable<S> that);

    ListIterable<Pair<V, Integer>> zipWithIndex();

    <P, V1> ListIterable<V1> collectWith(Function2<? super V, ? super P, ? extends V1> function, P parameter);

    <V1> ListIterable<V1> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends V1> function);

    <S> ListIterable<S> selectInstancesOf(Class<S> clazz);

    <V1> ListIterable<V1> flatCollect(Function<? super V, ? extends Iterable<V1>> function);

    <V1> ListMultimap<V1, V> groupBy(Function<? super V, ? extends V1> function);

    <V1> ListMultimap<V1, V> groupByEach(Function<? super V, ? extends Iterable<V1>> function);

    <V1> OrderedMap<V1, V> groupByUniqueKey(Function<? super V, ? extends V1> function);

    <KK, VV> OrderedMap<KK, VV> aggregateInPlaceBy(Function<? super V, ? extends KK> groupBy, Function0<? extends VV> zeroValueFactory, Procedure2<? super VV, ? super V> mutatingAggregator);

    <KK, VV> OrderedMap<KK, VV> aggregateBy(Function<? super V, ? extends KK> groupBy, Function0<? extends VV> zeroValueFactory, Function2<? super VV, ? super V, ? extends VV> nonMutatingAggregator);
}
