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

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.bag.primitive.ImmutableByteBag;
import com.gs.collections.api.bag.primitive.ImmutableCharBag;
import com.gs.collections.api.bag.primitive.ImmutableDoubleBag;
import com.gs.collections.api.bag.primitive.ImmutableFloatBag;
import com.gs.collections.api.bag.primitive.ImmutableIntBag;
import com.gs.collections.api.bag.primitive.ImmutableLongBag;
import com.gs.collections.api.bag.primitive.ImmutableShortBag;
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
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * An ImmutableMap is different than a JCF Map but in that it has no mutating methods.  It shares the read-only
 * protocol of a JDK Map.
 */
@Immutable
public interface ImmutableMap<K, V>
        extends UnsortedMapIterable<K, V>, ImmutableMapIterable<K, V>
{
    ImmutableMap<K, V> newWithKeyValue(K key, V value);

    ImmutableMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues);

    ImmutableMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs);

    ImmutableMap<K, V> newWithoutKey(K key);

    ImmutableMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys);

    MutableMap<K, V> toMap();

    ImmutableSetMultimap<V, K> flip();

    ImmutableMap<K, V> select(Predicate2<? super K, ? super V> predicate);

    ImmutableMap<K, V> reject(Predicate2<? super K, ? super V> predicate);

    ImmutableMap<K, V> tap(Procedure<? super V> procedure);

    ImmutableBag<V> select(Predicate<? super V> predicate);

    <P> ImmutableBag<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    ImmutableBag<V> reject(Predicate<? super V> predicate);

    <P> ImmutableBag<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter);

    PartitionImmutableBag<V> partition(Predicate<? super V> predicate);

    <P> PartitionImmutableBag<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter);

    <S> ImmutableBag<S> selectInstancesOf(Class<S> clazz);

    <K2, V2> ImmutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function);

    <R> ImmutableMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function);

    <VV> ImmutableBag<VV> collect(Function<? super V, ? extends VV> function);

    <P, VV> ImmutableBag<VV> collectWith(Function2<? super V, ? super P, ? extends VV> function, P parameter);

    ImmutableBooleanBag collectBoolean(BooleanFunction<? super V> booleanFunction);

    ImmutableByteBag collectByte(ByteFunction<? super V> byteFunction);

    ImmutableCharBag collectChar(CharFunction<? super V> charFunction);

    ImmutableDoubleBag collectDouble(DoubleFunction<? super V> doubleFunction);

    ImmutableFloatBag collectFloat(FloatFunction<? super V> floatFunction);

    ImmutableIntBag collectInt(IntFunction<? super V> intFunction);

    ImmutableLongBag collectLong(LongFunction<? super V> longFunction);

    ImmutableShortBag collectShort(ShortFunction<? super V> shortFunction);

    <R> ImmutableBag<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function);

    <R> ImmutableBag<R> flatCollect(Function<? super V, ? extends Iterable<R>> function);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    <S> ImmutableBag<Pair<V, S>> zip(Iterable<S> that);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    ImmutableSet<Pair<V, Integer>> zipWithIndex();

    <VV> ImmutableBagMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> ImmutableBagMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);

    <V1> ImmutableMap<V1, V> groupByUniqueKey(Function<? super V, ? extends V1> function);

    <K2, V2> ImmutableMap<K2, V2> aggregateInPlaceBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Procedure2<? super V2, ? super V> mutatingAggregator);

    <K2, V2> ImmutableMap<K2, V2> aggregateBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator);

    ImmutableMap<V, K> flipUniqueValues();
}
