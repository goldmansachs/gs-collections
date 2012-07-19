/*
 * Copyright 2012 Goldman Sachs.
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
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.partition.stack.PartitionImmutableStack;
import com.gs.collections.api.tuple.Pair;

public interface ImmutableStack<T> extends StackIterable<T>
{
    ImmutableStack<T> select(Predicate<? super T> predicate);

    ImmutableStack<T> reject(Predicate<? super T> predicate);

    PartitionImmutableStack<T> partition(Predicate<? super T> predicate);

    <V> ImmutableStack<V> collect(Function<? super T, ? extends V> function);

    <V> ImmutableStack<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableStack<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ImmutableStack<Pair<T, S>> zip(Iterable<S> that);

    ImmutableStack<Pair<T, Integer>> zipWithIndex();

    ImmutableStack<T> pop();

    ImmutableStack<T> pop(int count);

    ImmutableStack<T> push(T item);

    /**
     * Size takes linear time on ImmutableStacks.
     */
    int size();
}
