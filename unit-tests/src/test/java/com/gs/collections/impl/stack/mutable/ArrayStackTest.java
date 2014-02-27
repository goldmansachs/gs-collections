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

package com.gs.collections.impl.stack.mutable;

import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.impl.factory.Stacks;

/**
 * JUnit test for {@link ArrayStack}.
 */
public class ArrayStackTest extends MutableStackTestCase
{
    @Override
    protected <T> MutableStack<T> newStackWith(T... elements)
    {
        return Stacks.mutable.of(elements);
    }

    @Override
    protected <T> MutableStack<T> newStackFromTopToBottom(T... elements)
    {
        return Stacks.mutable.ofReversed(elements);
    }

    @Override
    protected <T> MutableStack<T> newStackFromTopToBottom(Iterable<T> elements)
    {
        return Stacks.mutable.ofAllReversed(elements);
    }

    @Override
    protected <T> MutableStack<T> newStack(Iterable<T> elements)
    {
        return Stacks.mutable.ofAll(elements);
    }
}
