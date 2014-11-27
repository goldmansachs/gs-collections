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
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.stack.primitive.MutableBooleanStack;
import com.gs.collections.impl.collection.mutable.primitive.AbstractMutableBooleanStackTestCase;
import com.gs.collections.impl.factory.primitive.BooleanStacks;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link BooleanArrayStack}.
 */
public class BooleanArrayStackTest extends AbstractMutableBooleanStackTestCase
{
    @Override
    protected MutableBooleanStack classUnderTest()
    {
        return BooleanStacks.mutable.with(true, false, true, false);
    }

    @Override
    protected MutableBooleanStack newWith(boolean... elements)
    {
        return BooleanStacks.mutable.of(elements);
    }

    @Override
    protected MutableBooleanStack newMutableCollectionWith(boolean... elements)
    {
        return BooleanArrayStack.newStackWith(elements);
    }

    @Override
    protected RichIterable<Object> newObjectCollectionWith(Object... elements)
    {
        return ArrayStack.newStackWith(elements);
    }

    @Override
    protected MutableBooleanStack newWithTopToBottom(boolean... elements)
    {
        return BooleanArrayStack.newStackFromTopToBottom(elements);
    }

    @Override
    protected MutableBooleanStack newWithIterableTopToBottom(BooleanIterable iterable)
    {
        return BooleanStacks.mutable.ofAllReversed(iterable);
    }

    @Override
    protected MutableBooleanStack newWithIterable(BooleanIterable iterable)
    {
        return BooleanStacks.mutable.ofAll(iterable);
    }

    @Test
    public void testPushPopAndPeek()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom();
        stack.push(true);
        Assert.assertTrue(stack.peek());
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(true), stack);

        stack.push(false);
        Assert.assertFalse(stack.peek());
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(false, true), stack);

        stack.push(true);
        Assert.assertTrue(stack.peek());
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(true, false, true), stack);

        Assert.assertFalse(stack.peekAt(1));
        Assert.assertTrue(stack.pop());
        Assert.assertFalse(stack.peek());
        Assert.assertFalse(stack.pop());
        Assert.assertTrue(stack.peek());
        Assert.assertTrue(stack.pop());

        BooleanArrayStack stack2 = BooleanArrayStack.newStackFromTopToBottom(true, false, true, false, true);
        stack2.pop(2);
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(true, false, true), stack2);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), stack2.peek(2));

        BooleanArrayStack stack8 = BooleanArrayStack.newStackFromTopToBottom(false, true, false, true);
        Verify.assertEmpty(stack8.pop(0));
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(false, true, false, true), stack8);
        Assert.assertEquals(new BooleanArrayList(), stack8.peek(0));

        BooleanArrayStack stack9 = BooleanArrayStack.newStackFromTopToBottom();
        Assert.assertEquals(new BooleanArrayList(), stack9.pop(0));
        Assert.assertEquals(new BooleanArrayList(), stack9.peek(0));
    }
}
