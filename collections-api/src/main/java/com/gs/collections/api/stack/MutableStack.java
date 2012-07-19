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

import java.util.Collection;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.partition.stack.PartitionStack;
import com.gs.collections.api.tuple.Pair;

public interface MutableStack<T> extends StackIterable<T>
{
    MutableStack<T> select(Predicate<? super T> predicate);

    MutableStack<T> reject(Predicate<? super T> predicate);

    PartitionStack<T> partition(Predicate<? super T> predicate);

    <V> MutableStack<V> collect(Function<? super T, ? extends V> function);

    <V> MutableStack<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> MutableStack<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> MutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> MutableStack<Pair<T, S>> zip(Iterable<S> that);

    MutableStack<Pair<T, Integer>> zipWithIndex();

    void push(T item);

    T pop();

    ListIterable<T> pop(int count);

    <V, R extends Collection<V>> R pop(int count, R targetCollection);

    MutableStack<T> asUnmodifiable();

    MutableStack<T> asSynchronized();

    MutableStack<T> toStack();

    ImmutableStack<T> toImmutable();
}
