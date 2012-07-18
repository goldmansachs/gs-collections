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

package com.gs.collections.api;

import java.util.Collection;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.tuple.Pair;

/**
 * A LazyIterable is RichIterable which will defer evaluation for certain methods like select, reject, collect, etc.
 * Any methods that do not return a LazyIterable when called will cause evaluation to be forced.
 *
 * @since 1.0
 */
public interface LazyIterable<T>
        extends RichIterable<T>
{
    /**
     * Creates a deferred iterable for selecting elements from the current iterable.
     */
    LazyIterable<T> select(Predicate<? super T> predicate);

    <S> LazyIterable<S> selectInstancesOf(Class<S> clazz);

    /**
     * Creates a deferred iterable for rejecting elements from the current iterable.
     */
    LazyIterable<T> reject(Predicate<? super T> predicate);

    /**
     * Creates a deferred iterable for collecting elements from the current iterable.
     */
    <V> LazyIterable<V> collect(Function<? super T, ? extends V> function);

    /**
     * Creates a deferred iterable for selecting and collecting elements from the current iterable.
     */
    <V> LazyIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    /**
     * Creates a deferred take iterable for the current iterable using the specified count as the limit.
     */
    LazyIterable<T> take(int count);

    /**
     * Creates a deferred drop iterable for the current iterable using the specified count as the limit.
     */
    LazyIterable<T> drop(int count);

    /**
     * Creates a deferred flattening iterable for the current iterable.
     */
    <V> LazyIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Creates a deferred iterable that will join this iterable with the specified iterable.
     */
    LazyIterable<T> concatenate(Iterable<T> iterable);

    /**
     * Creates a deferred zip iterable.
     */
    <S> LazyIterable<Pair<T, S>> zip(Iterable<S> that);

    /**
     * Creates a deferred zipWithIndex iterable.
     */
    LazyIterable<Pair<T, Integer>> zipWithIndex();

    /**
     * Creates a deferred chunking iterable.
     */
    LazyIterable<RichIterable<T>> chunk(int size);

    /**
     * Iterates over this iterable adding all elements into the target collection.
     */
    <R extends Collection<T>> R into(R target);
}
