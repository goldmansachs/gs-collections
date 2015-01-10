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

import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.test.UnmodifiableIterableTestCase;
import org.junit.Test;

public interface UnmodifiableMutableStackTestCase extends MutableStackTestCase, UnmodifiableIterableTestCase
{
    @Override
    @Test
    default void Iterable_remove()
    {
        UnmodifiableIterableTestCase.super.Iterable_remove();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void MutableStack_pop()
    {
        MutableStack<Integer> mutableStack = this.newWith(5, 1, 4, 2, 3);
        mutableStack.pop();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void MutableStack_pop_throws()
    {
        MutableStack<Integer> mutableStack = this.newWith();
        mutableStack.pop();
    }
}
