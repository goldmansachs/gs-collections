/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.test.stack.mutable;

import java.util.EmptyStackException;
import java.util.Iterator;

import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.test.stack.StackIterableTestCase;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;

public interface MutableStackTestCase extends StackIterableTestCase
{
    @Override
    <T> MutableStack<T> newWith(T... elements);

    @Override
    @Test
    default void Iterable_remove()
    {
        MutableStack<Integer> stack = this.newWith(3, 3, 3, 2, 2, 1);
        Iterator<Integer> iterator = stack.iterator();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    default void MutableStack_pop()
    {
        MutableStack<Integer> mutableStack = this.newWith(5, 1, 4, 2, 3);
        assertEquals(Integer.valueOf(5), mutableStack.pop());
        assertEquals(Stacks.immutable.withReversed(1, 4, 2, 3), mutableStack);
    }

    @Test
    default void MutableStack_pop_throws()
    {
        MutableStack<Integer> mutableStack = this.newWith(5, 1, 4, 2, 3);
        assertEquals(Integer.valueOf(5), mutableStack.pop());
        assertEquals(Integer.valueOf(1), mutableStack.pop());
        assertEquals(Integer.valueOf(4), mutableStack.pop());
        assertEquals(Integer.valueOf(2), mutableStack.pop());
        assertEquals(Integer.valueOf(3), mutableStack.pop());
        assertThrows(EmptyStackException.class, (Runnable) mutableStack::pop);
    }
}
