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
import com.gs.collections.api.stack.StackIterable;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.StringPredicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.stack.StackIterableTestCase;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnmodifiableStackTest extends StackIterableTestCase
{
    private MutableStack<Integer> mutableStack;
    private MutableStack<String> mutableStackString;
    private MutableStack<Integer> unmodifiableStack;
    private MutableStack<String> unmodifiableStackString;

    @Before
    public void setUp()
    {
        this.mutableStack = ArrayStack.<Integer>newStackFromTopToBottom(1, 2, 3);
        this.unmodifiableStack = new UnmodifiableStack<Integer>(this.mutableStack);
        this.mutableStackString = ArrayStack.<String>newStackFromTopToBottom("1", "2", "3");
        this.unmodifiableStackString = new UnmodifiableStack<String>(this.mutableStackString);
    }

    @Override
    protected <T> MutableStack<T> newStackWith(T... elements)
    {
        return ArrayStack.newStackWith(elements).asUnmodifiable();
    }

    @Override
    protected <T> MutableStack<T> newStackFromTopToBottom(T... elements)
    {
        return ArrayStack.newStackFromTopToBottom(elements).asUnmodifiable();
    }

    @Override
    protected <T> StackIterable<T> newStackFromTopToBottom(Iterable<T> elements)
    {
        return ArrayStack.newStackFromTopToBottom(elements).asUnmodifiable();
    }

    @Override
    protected <T> StackIterable<T> newStack(Iterable<T> elements)
    {
        return ArrayStack.newStack(elements).asUnmodifiable();
    }

    @Override
    @Test
    public void iterator()
    {
        super.iterator();
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                newStackWith(1, 2, 3).iterator().remove();
            }
        });
    }

    @Test
    public void testNullStack()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableStack.of(null);
            }
        });
    }

    @Test
    public void testPop()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableStackTest.this.newStackFromTopToBottom(1, 2, 3).pop();
            }
        });

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableStackTest.this.newStackFromTopToBottom(1, 2).pop(3);
            }
        });

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableStackTest.this.newStackFromTopToBottom(1, 2, 3).pop(3);
            }
        });

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableStackTest.this.newStackFromTopToBottom(1, 2, 3).pop(3, FastList.<Integer>newList());
            }
        });
    }

    @Test
    public void testPush()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableStackTest.this.newStackFromTopToBottom(1, 2, 3).push(4);
            }
        });
    }

    @Test
    public void testSelect()
    {
        Assert.assertEquals(ArrayStack.newStackFromTopToBottom(2, 3), this.unmodifiableStack.select(Predicates.greaterThan(1)));
        Verify.assertSize(3, this.unmodifiableStackString.select(Predicates.alwaysTrue(), FastList.<String>newList()));
    }

    @Test
    public void testSelectWith()
    {
        Verify.assertSize(1,
                this.unmodifiableStackString.selectWith(Predicates2.equal(),
                        "2",
                        FastList.<String>newList()));
    }

    @Test
    public void testReject()
    {
        Assert.assertEquals(ArrayStack.newStackFromTopToBottom("2", "3"), this.unmodifiableStackString.reject(StringPredicates.contains("1")));
        Assert.assertEquals(FastList.newListWith("2", "3"),
                this.unmodifiableStackString.reject(StringPredicates.contains("1"), FastList.<String>newList()));
    }

    @Test
    public void testRejectWith()
    {
        Verify.assertSize(3,
                this.unmodifiableStackString.rejectWith(Predicates2.equal(),
                        3,
                        FastList.<String>newList()));
    }

    @Test
    public void testCollect()
    {
        Assert.assertEquals(this.mutableStack, this.unmodifiableStackString.collect(Functions.getStringToInteger()));
    }

    @Test
    public void testSize()
    {
        Assert.assertEquals(this.mutableStack.size(), this.unmodifiableStack.size());
    }

    @Test
    public void testIsEmpty()
    {
        Assert.assertEquals(this.mutableStack.isEmpty(), this.unmodifiableStack.isEmpty());
    }

    @Test
    public void testGetFirst()
    {
        Assert.assertEquals(this.mutableStack.getFirst(), this.unmodifiableStack.getFirst());
    }

    @Test
    public void testCount()
    {
        Assert.assertEquals(this.mutableStack.count(Predicates.alwaysTrue()),
                this.unmodifiableStack.count(Predicates.alwaysTrue()));
    }

    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedStack.class, this.unmodifiableStack.asSynchronized());
    }

    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableStack.class, this.unmodifiableStack.asUnmodifiable());
    }
}
