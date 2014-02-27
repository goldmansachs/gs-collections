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

package com.gs.collections.impl.stack.mutable.primitive;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.stack.primitive.MutableBooleanStack;
import com.gs.collections.impl.collection.mutable.primitive.AbstractMutableBooleanStackTestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedBooleanStack}.
 */
public class SynchronizedBooleanStackTest extends AbstractMutableBooleanStackTestCase
{
    @Override
    protected SynchronizedBooleanStack classUnderTest()
    {
        return new SynchronizedBooleanStack(BooleanArrayStack.newStackWith(true, false, true, false));
    }

    @Override
    protected SynchronizedBooleanStack newWith(boolean... elements)
    {
        return new SynchronizedBooleanStack(BooleanArrayStack.newStackWith(elements));
    }

    @Override
    protected SynchronizedBooleanStack newMutableCollectionWith(boolean... elements)
    {
        return new SynchronizedBooleanStack(BooleanArrayStack.newStackWith(elements));
    }

    @Override
    protected SynchronizedBooleanStack newWithTopToBottom(boolean... elements)
    {
        return new SynchronizedBooleanStack(BooleanArrayStack.newStackFromTopToBottom(elements));
    }

    @Override
    protected SynchronizedBooleanStack newWithIterableTopToBottom(BooleanIterable iterable)
    {
        return new SynchronizedBooleanStack(BooleanArrayStack.newStackFromTopToBottom(iterable));
    }

    @Override
    protected SynchronizedBooleanStack newWithIterable(BooleanIterable iterable)
    {
        return new SynchronizedBooleanStack(BooleanArrayStack.newStack(iterable));
    }

    @Override
    @Test
    public void asSynchronized()
    {
        MutableBooleanStack stack1 = new SynchronizedBooleanStack(BooleanArrayStack.newStackWith(true, false, true), new Object());
        Assert.assertSame(stack1, stack1.asSynchronized());
        Assert.assertEquals(stack1, stack1.asSynchronized());
    }
}
