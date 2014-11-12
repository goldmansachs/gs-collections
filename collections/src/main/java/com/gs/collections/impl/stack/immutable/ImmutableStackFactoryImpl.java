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

package com.gs.collections.impl.stack.immutable;

import com.gs.collections.api.factory.stack.ImmutableStackFactory;
import com.gs.collections.api.stack.ImmutableStack;

public class ImmutableStackFactoryImpl implements ImmutableStackFactory
{
    public <T> ImmutableStack<T> empty()
    {
        return ImmutableArrayStack.newStack();
    }

    public <T> ImmutableStack<T> of()
    {
        return this.empty();
    }

    public <T> ImmutableStack<T> with()
    {
        return this.empty();
    }

    public <T> ImmutableStack<T> of(T element)
    {
        return this.with(element);
    }

    public <T> ImmutableStack<T> with(T element)
    {
        return ImmutableArrayStack.newStackWith(element);
    }

    public <T> ImmutableStack<T> of(T... elements)
    {
        return this.with(elements);
    }

    public <T> ImmutableStack<T> with(T... elements)
    {
        return ImmutableArrayStack.newStackWith(elements);
    }

    public <T> ImmutableStack<T> ofAll(Iterable<? extends T> items)
    {
        return this.withAll(items);
    }

    public <T> ImmutableStack<T> withAll(Iterable<? extends T> items)
    {
        return ImmutableArrayStack.newStack(items);
    }

    public <T> ImmutableStack<T> ofReversed(T... elements)
    {
        return this.withReversed(elements);
    }

    public <T> ImmutableStack<T> withReversed(T... elements)
    {
        return ImmutableArrayStack.newStackFromTopToBottom(elements);
    }

    public <T> ImmutableStack<T> ofAllReversed(Iterable<? extends T> items)
    {
        return this.withAllReversed(items);
    }

    public <T> ImmutableStack<T> withAllReversed(Iterable<? extends T> items)
    {
        return ImmutableArrayStack.newStackFromTopToBottom(items);
    }
}
