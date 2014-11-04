/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.api.collection;

import com.gs.collections.api.RichIterable;
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
import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.collection.primitive.ImmutableByteCollection;
import com.gs.collections.api.collection.primitive.ImmutableCharCollection;
import com.gs.collections.api.collection.primitive.ImmutableDoubleCollection;
import com.gs.collections.api.collection.primitive.ImmutableFloatCollection;
import com.gs.collections.api.collection.primitive.ImmutableIntCollection;
import com.gs.collections.api.collection.primitive.ImmutableLongCollection;
import com.gs.collections.api.collection.primitive.ImmutableShortCollection;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableCollection is the common interface between ImmutableList and ImmutableSet.
 */
@Immutable
public interface ImmutableCollection<T>
        extends RichIterable<T>
{
    ImmutableCollection<T> newWith(T element);

    ImmutableCollection<T> newWithout(T element);

    ImmutableCollection<T> newWithAll(Iterable<? extends T> elements);

    ImmutableCollection<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableCollection<T> tap(Procedure<? super T> procedure);

    ImmutableCollection<T> select(Predicate<? super T> predicate);

    <P> ImmutableCollection<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableCollection<T> reject(Predicate<? super T> predicate);

    <P> ImmutableCollection<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableCollection<T> partition(Predicate<? super T> predicate);

    <P> PartitionImmutableCollection<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ImmutableCollection<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableCollection<V> collect(Function<? super T, ? extends V> function);

    ImmutableBooleanCollection collectBoolean(BooleanFunction<? super T> booleanFunction);

    ImmutableByteCollection collectByte(ByteFunction<? super T> byteFunction);

    ImmutableCharCollection collectChar(CharFunction<? super T> charFunction);

    ImmutableDoubleCollection collectDouble(DoubleFunction<? super T> doubleFunction);

    ImmutableFloatCollection collectFloat(FloatFunction<? super T> floatFunction);

    ImmutableIntCollection collectInt(IntFunction<? super T> intFunction);

    ImmutableLongCollection collectLong(LongFunction<? super T> longFunction);

    ImmutableShortCollection collectShort(ShortFunction<? super T> shortFunction);

    <P, V> ImmutableCollection<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> ImmutableCollection<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableCollection<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function);

    <S> ImmutableCollection<Pair<T, S>> zip(Iterable<S> that);

    ImmutableCollection<Pair<T, Integer>> zipWithIndex();

    <K, V> ImmutableMap<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator);

    <K, V> ImmutableMap<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);
}
