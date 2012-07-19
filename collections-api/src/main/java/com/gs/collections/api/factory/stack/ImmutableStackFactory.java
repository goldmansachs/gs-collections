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

package com.gs.collections.api.factory.stack;

import com.gs.collections.api.stack.ImmutableStack;

public interface ImmutableStackFactory
{
    <T> ImmutableStack<T> of();

    <T> ImmutableStack<T> of(T element);

    <T> ImmutableStack<T> of(T... elements);

    <T> ImmutableStack<T> ofAll(Iterable<? extends T> items);

    <T> ImmutableStack<T> ofReversed(T... elements);

    <T> ImmutableStack<T> ofAllReversed(Iterable<? extends T> items);
}
