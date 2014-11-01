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

package com.gs.collections.impl.stack.immutable.primitive;

import com.gs.collections.api.stack.primitive.ImmutableBooleanStack;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.stack.mutable.primitive.BooleanArrayStack;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableBooleanSingletonStack}.
 */
public class ImmutableBooleanSingletonStackTest extends AbstractImmutableBooleanStackTestCase
{
    @Override
    protected ImmutableBooleanStack classUnderTest()
    {
        return new ImmutableBooleanSingletonStack(true);
    }

    @Override
    @Test
    public void pop()
    {
        ImmutableBooleanStack stack = this.classUnderTest();
        ImmutableBooleanStack modified = stack.pop();
        Verify.assertEmpty(modified);
        Verify.assertSize(1, stack);
        Assert.assertNotSame(modified, stack);
        Assert.assertEquals(this.classUnderTest(), stack);
    }

    @Override
    @Test
    public void popWithCount()
    {
        ImmutableBooleanStack stack = this.classUnderTest();
        ImmutableBooleanStack stack1 = stack.pop(0);
        Assert.assertSame(stack1, stack);
        Assert.assertEquals(this.classUnderTest(), stack);
        ImmutableBooleanStack modified = stack.pop(1);
        Verify.assertEmpty(modified);
        Verify.assertSize(1, stack);
        Assert.assertNotSame(modified, stack);
        Assert.assertEquals(this.classUnderTest(), stack);
    }

    @Override
    @Test
    public void peek()
    {
        Assert.assertTrue(this.classUnderTest().peek());
        Assert.assertEquals(BooleanArrayList.newListWith(), this.classUnderTest().peek(0));
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.classUnderTest().peek(1));
        Verify.assertThrows(IllegalArgumentException.class, () -> this.classUnderTest().peek(2));
    }

    @Override
    @Test
    public void testEquals()
    {
        ImmutableBooleanStack stack = this.classUnderTest();
        Assert.assertEquals(stack, stack);
        Verify.assertPostSerializedEqualsAndHashCode(stack);
        Assert.assertEquals(stack, BooleanArrayStack.newStackWith(true));
        Assert.assertNotEquals(stack, this.newWith(true, false));
        Assert.assertNotEquals(stack, BooleanArrayList.newListWith(true));
        Assert.assertEquals(stack, this.newWith(true));
        Assert.assertNotEquals(stack, this.newWith());
    }
}
