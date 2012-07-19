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

import java.util.EmptyStackException;

import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.stack.StackIterableTestCase;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public abstract class MutableStackTestCase extends StackIterableTestCase
{
    @Override
    protected abstract <T> MutableStack<T> newStackWith(T... elements);

    @Override
    protected abstract <T> MutableStack<T> newStackFromTopToBottom(T... elements);

    @Override
    protected abstract <T> MutableStack<T> newStackFromTopToBottom(Iterable<T> elements);

    @Override
    protected abstract <T> MutableStack<T> newStack(Iterable<T> elements);

    @Test
    public void testPushPopAndPeek()
    {
        MutableStack<String> stack = newStackWith();
        stack.push("1");
        Assert.assertEquals("1", stack.peek());
        Assert.assertEquals(newStackWith("1"), stack);

        stack.push("2");
        Assert.assertEquals("2", stack.peek());
        Assert.assertEquals(newStackWith("1", "2"), stack);

        stack.push("3");
        Assert.assertEquals("3", stack.peek());
        Assert.assertEquals(newStackWith("1", "2", "3"), stack);

        Assert.assertEquals("3", stack.pop());
        Assert.assertEquals("2", stack.peek());
        Assert.assertEquals("2", stack.pop());
        Assert.assertEquals("1", stack.peek());
        Assert.assertEquals("1", stack.pop());

        MutableStack<Integer> stack2 = newStackFromTopToBottom(5, 4, 3, 2, 1);
        stack2.pop(2);
        Assert.assertEquals(newStackFromTopToBottom(3, 2, 1), stack2);
        Assert.assertEquals(FastList.newListWith(3, 2), stack2.peek(2));

        MutableStack<Integer> stack3 = Stacks.mutable.ofReversed(1, 2, 3);
        Assert.assertEquals(newStackFromTopToBottom(1, 2, 3), stack3);

        MutableStack<Integer> stack4 = Stacks.mutable.ofAll(FastList.newListWith(1, 2, 3));
        MutableStack<Integer> stack5 = Stacks.mutable.ofAllReversed(FastList.newListWith(1, 2, 3));

        Assert.assertEquals(newStackFromTopToBottom(3, 2, 1), stack4);
        Assert.assertEquals(newStackFromTopToBottom(1, 2, 3), stack5);

        MutableStack<Integer> stack6 = newStackFromTopToBottom(1, 2, 3, 4);
        Assert.assertEquals(FastList.newListWith(1, 2), stack6.pop(2, FastList.newList()));
    }

    @Test
    public void testNewStackWithOrder()
    {
        MutableStack<String> stack = newStackWith("1", "2", "3");
        Assert.assertEquals("3", stack.pop());
        Assert.assertEquals("2", stack.pop());
        Assert.assertEquals("1", stack.pop());
    }

    @Test
    public void testNewStackIterableOrder()
    {
        MutableStack<String> stack = newStack(FastList.newListWith("1", "2", "3"));
        Assert.assertEquals("3", stack.pop());
        Assert.assertEquals("2", stack.pop());
        Assert.assertEquals("1", stack.pop());
    }

    @Test
    public void testNewStackFromTopToBottomOrder()
    {
        MutableStack<String> stack = newStackFromTopToBottom("3", "2", "1");
        Assert.assertEquals("3", stack.pop());
        Assert.assertEquals("2", stack.pop());
        Assert.assertEquals("1", stack.pop());
    }

    @Test
    public void testNewStackFromTopToBottomIterableOrder()
    {
        MutableStack<String> stack = newStackFromTopToBottom(FastList.newListWith("3", "2", "1"));
        Assert.assertEquals("3", stack.pop());
        Assert.assertEquals("2", stack.pop());
        Assert.assertEquals("1", stack.pop());
    }

    @Test(expected = EmptyStackException.class)
    public void pop_empty_throws()
    {
        newStackWith().pop();
    }

    @Test(expected = EmptyStackException.class)
    public void pop_int_empty_throws()
    {
        newStackWith().pop(1);
        newStackWith(1, 2, 3).pop(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pop_int_count_throws()
    {
        newStackWith(1, 2, 3).pop(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pop_int_zero_throws()
    {
        newStackWith(1, 2, 3).pop(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pop_int_neg_throws()
    {
        newStackWith(1, 2, 3).pop(-1);
    }

    @Test(expected = EmptyStackException.class)
    public void pop_target_empty_throws()
    {
        newStackWith().pop(5, FastList.newList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void pop_target_zero_throws()
    {
        newStackWith(1, 2, 3).pop(0, FastList.newList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void pop_target_count_throws()
    {
        newStackWith(1, 2, 3).pop(5, FastList.newList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void pop_target_neg_throws()
    {
        newStackWith(1, 2, 3).pop(-1, FastList.newList());
    }

    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableStack.class, newStackWith().asUnmodifiable());
    }

    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedStack.class, newStackWith().asSynchronized());
    }
}
