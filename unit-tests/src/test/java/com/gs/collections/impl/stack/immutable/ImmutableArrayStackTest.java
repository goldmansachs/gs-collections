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

import java.util.EmptyStackException;

import com.gs.collections.api.stack.ImmutableStack;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableArrayStackTest extends ImmutableStackTestCase
{
    @Override
    protected <T> ImmutableStack<T> newStackWith(T... elements)
    {
        return Stacks.immutable.of(elements);
    }

    @Override
    protected <T> ImmutableStack<T> newStackFromTopToBottom(T... elements)
    {
        return Stacks.immutable.ofReversed(elements);
    }

    @Override
    protected <T> ImmutableStack<T> newStackFromTopToBottom(Iterable<T> elements)
    {
        return Stacks.immutable.ofAllReversed(elements);
    }

    @Override
    protected <T> ImmutableStack<T> newStack(Iterable<T> elements)
    {
        return Stacks.immutable.ofAll(elements);
    }

    @Override
    @Test
    public void testEquals()
    {
        super.testEquals();
        Assert.assertEquals(ImmutableArrayStack.newStack(), ArrayStack.newStackWith());
        Assert.assertNotEquals(this.newStackWith(4, 5, 6), ArrayStack.newStackWith(1, 2, 3));
    }

    @Test
    public void push()
    {
        ImmutableStack<Integer> stack = this.newStackWith(1, 2, 3);
        ImmutableStack<Integer> modifiedStack = stack.push(4);
        Assert.assertEquals(this.newStackWith(1, 2, 3, 4), modifiedStack);
        Assert.assertNotSame(modifiedStack, stack);
        Assert.assertEquals(this.newStackWith(1, 2, 3), stack);
        modifiedStack.push(5);
        Assert.assertEquals(this.newStackWith(1, 2, 3), stack);

        ImmutableStack<Integer> stack1 = this.newStackWith();
        ImmutableStack<Integer> modifiedStack1 = stack1.push(1);
        Assert.assertEquals(this.newStackWith(1), modifiedStack1);
        Assert.assertNotSame(modifiedStack1, stack1);
        Assert.assertEquals(this.newStackWith(), stack1);
        modifiedStack1.push(5);
        Assert.assertEquals(this.newStackWith(), stack1);
    }

    @Test
    public void pop()
    {
        Verify.assertThrows(EmptyStackException.class, () -> this.newStackWith().pop());

        ImmutableStack<Integer> stack = this.newStackWith(1, 2, 3);
        ImmutableStack<Integer> modifiedStack = stack.pop();
        Assert.assertEquals(this.newStackWith(1, 2), modifiedStack);
        Assert.assertNotSame(modifiedStack, stack);
        Assert.assertEquals(this.newStackWith(1, 2, 3), stack);

        ImmutableStack<Integer> stack1 = this.newStackWith(1);
        ImmutableStack<Integer> modifiedStack1 = stack1.pop();
        Assert.assertEquals(this.newStackWith(), modifiedStack1);
        Assert.assertNotSame(modifiedStack1, stack1);
        Assert.assertEquals(this.newStackWith(1), stack1);
    }

    @Test
    public void popCount()
    {
        Verify.assertThrows(EmptyStackException.class, () -> this.newStackWith().pop(1));

        Assert.assertEquals(this.newStackWith(), this.newStackWith().pop(0));

        ImmutableStack<Integer> stack = this.newStackWith(1, 2, 3);
        ImmutableStack<Integer> modifiedStack = stack.pop(1);
        Assert.assertEquals(this.newStackWith(1, 2), modifiedStack);
        Assert.assertNotSame(modifiedStack, stack);
        Assert.assertNotSame(this.newStackWith(1, 2, 3), stack);

        ImmutableStack<Integer> stack1 = this.newStackWith(1);
        Verify.assertThrows(IllegalArgumentException.class, () -> stack1.pop(2));
        ImmutableStack<Integer> modifiedStack1 = stack1.pop(1);
        Assert.assertEquals(this.newStackWith(), modifiedStack1);
        Assert.assertNotSame(modifiedStack1, stack1);
        Assert.assertEquals(this.newStackWith(1), stack1);
    }
}
