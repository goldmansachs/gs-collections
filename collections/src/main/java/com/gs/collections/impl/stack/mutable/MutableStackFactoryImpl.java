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

package com.gs.collections.impl.stack.mutable;

import com.gs.collections.api.factory.stack.MutableStackFactory;
import com.gs.collections.api.stack.MutableStack;
import net.jcip.annotations.Immutable;

@Immutable
public final class MutableStackFactoryImpl implements MutableStackFactory
{
    public <T> MutableStack<T> empty()
    {
        return ArrayStack.newStack();
    }

    public <T> MutableStack<T> of()
    {
        return this.empty();
    }

    public <T> MutableStack<T> with()
    {
        return this.empty();
    }

    public <T> MutableStack<T> of(T... elements)
    {
        return this.with(elements);
    }

    public <T> MutableStack<T> with(T... elements)
    {
        return ArrayStack.newStackWith(elements);
    }

    public <T> MutableStack<T> ofAll(Iterable<? extends T> elements)
    {
        return this.withAll(elements);
    }

    public <T> MutableStack<T> withAll(Iterable<? extends T> elements)
    {
        return ArrayStack.newStack(elements);
    }

    public <T> MutableStack<T> ofReversed(T... elements)
    {
        return this.withReversed(elements);
    }

    public <T> MutableStack<T> withReversed(T... elements)
    {
        return ArrayStack.newStackFromTopToBottom(elements);
    }

    public <T> MutableStack<T> ofAllReversed(Iterable<? extends T> items)
    {
        return this.withAllReversed(items);
    }

    public <T> MutableStack<T> withAllReversed(Iterable<? extends T> items)
    {
        return ArrayStack.newStackFromTopToBottom(items);
    }
}
