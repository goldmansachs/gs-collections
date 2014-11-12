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

package com.gs.collections.api.factory.stack;

import com.gs.collections.api.stack.MutableStack;

public interface MutableStackFactory
{
    /**
     * @since 6.0
     */
    <T> MutableStack<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableStack<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableStack<T> with();

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> MutableStack<T> of(T... elements);

    <T> MutableStack<T> with(T... elements);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> MutableStack<T> ofAll(Iterable<? extends T> elements);

    <T> MutableStack<T> withAll(Iterable<? extends T> elements);

    /**
     * Same as {@link #withReversed(Object[])}.
     */
    <T> MutableStack<T> ofReversed(T... elements);

    <T> MutableStack<T> withReversed(T... elements);

    /**
     * Same as {@link #withAllReversed(Iterable)}.
     */
    <T> MutableStack<T> ofAllReversed(Iterable<? extends T> items);

    <T> MutableStack<T> withAllReversed(Iterable<? extends T> items);
}
