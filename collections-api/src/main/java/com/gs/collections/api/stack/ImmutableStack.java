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

package com.gs.collections.api.stack;

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
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.partition.stack.PartitionImmutableStack;
import com.gs.collections.api.stack.primitive.ImmutableBooleanStack;
import com.gs.collections.api.stack.primitive.ImmutableByteStack;
import com.gs.collections.api.stack.primitive.ImmutableCharStack;
import com.gs.collections.api.stack.primitive.ImmutableDoubleStack;
import com.gs.collections.api.stack.primitive.ImmutableFloatStack;
import com.gs.collections.api.stack.primitive.ImmutableIntStack;
import com.gs.collections.api.stack.primitive.ImmutableLongStack;
import com.gs.collections.api.stack.primitive.ImmutableShortStack;
import com.gs.collections.api.tuple.Pair;

public interface ImmutableStack<T> extends StackIterable<T>
{
    ImmutableStack<T> push(T item);

    ImmutableStack<T> pop();

    ImmutableStack<T> pop(int count);

    ImmutableStack<T> tap(Procedure<? super T> procedure);

    ImmutableStack<T> select(Predicate<? super T> predicate);

    <P> ImmutableStack<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableStack<T> reject(Predicate<? super T> predicate);

    <P> ImmutableStack<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableStack<T> partition(Predicate<? super T> predicate);

    <P> PartitionImmutableStack<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <V> ImmutableStack<V> collect(Function<? super T, ? extends V> function);

    ImmutableBooleanStack collectBoolean(BooleanFunction<? super T> booleanFunction);

    ImmutableByteStack collectByte(ByteFunction<? super T> byteFunction);

    ImmutableCharStack collectChar(CharFunction<? super T> charFunction);

    ImmutableDoubleStack collectDouble(DoubleFunction<? super T> doubleFunction);

    ImmutableFloatStack collectFloat(FloatFunction<? super T> floatFunction);

    ImmutableIntStack collectInt(IntFunction<? super T> intFunction);

    ImmutableLongStack collectLong(LongFunction<? super T> longFunction);

    ImmutableShortStack collectShort(ShortFunction<? super T> shortFunction);

    <P, V> ImmutableStack<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> ImmutableStack<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableStack<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function);

    <S> ImmutableStack<Pair<T, S>> zip(Iterable<S> that);

    ImmutableStack<Pair<T, Integer>> zipWithIndex();

    /**
     * Size takes linear time on ImmutableStacks.
     */
    int size();
}
