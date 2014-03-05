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

package com.gs.collections.impl.stack.primitive;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.stack.primitive.BooleanStack;
import com.gs.collections.api.stack.primitive.ImmutableBooleanStack;
import com.gs.collections.impl.collection.mutable.primitive.AbstractBooleanIterableTestCase;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link BooleanStack}.
 */
public abstract class AbstractBooleanStackTestCase extends AbstractBooleanIterableTestCase
{
    @Override
    protected abstract BooleanStack classUnderTest();

    @Override
    protected abstract BooleanStack newWith(boolean... elements);

    @Override
    protected abstract BooleanStack newMutableCollectionWith(boolean... elements);

    @Override
    protected RichIterable<Object> newObjectCollectionWith(Object... elements)
    {
        return ArrayStack.newStackWith(elements);
    }

    protected abstract BooleanStack newWithTopToBottom(boolean... elements);

    @Override
    @Test
    public void booleanIterator()
    {
        BooleanIterator iterator = this.classUnderTest().booleanIterator();
        int size = this.classUnderTest().size();
        for (int i = 0; i < size; i++)
        {
            Assert.assertTrue(iterator.hasNext());
            boolean sizeEven = (size & 1) == 0;
            boolean iEven = (i & 1) == 0;
            Assert.assertEquals(sizeEven != iEven, iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals((this.classUnderTest().size() & 1) != 0, this.classUnderTest().booleanIterator().next());
    }

    @Test
    public void peek()
    {
        Assert.assertEquals((this.classUnderTest().size() & 1) != 0, this.classUnderTest().peek());
        Assert.assertEquals(BooleanArrayList.newListWith(), this.classUnderTest().peek(0));
        Assert.assertEquals(BooleanArrayList.newListWith((this.classUnderTest().size() & 1) != 0, (this.classUnderTest().size() & 1) == 0),
                this.classUnderTest().peek(2));
    }

    @Test
    public void peekAtIndex()
    {
        int size = this.classUnderTest().size();
        for (int i = 0; i < size; i++)
        {
            boolean sizeEven = (this.classUnderTest().size() & 1) == 0;
            boolean iEven = (i & 1) == 0;
            Assert.assertEquals(sizeEven != iEven, this.classUnderTest().peekAt(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void peek_at_index_less_than_zero_throws_exception()
    {
        this.classUnderTest().peekAt(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void peek_at_index_greater_than_size_throws_exception()
    {
        this.classUnderTest().peekAt(this.classUnderTest().size() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void peek_at_index_equal_to_size_throws_exception()
    {
        this.classUnderTest().peekAt(this.classUnderTest().size());
    }

    @Override
    @Test
    public void testToString()
    {
        super.testToString();
        Assert.assertEquals(this.createExpectedString("[", ", ", "]"), this.classUnderTest().toString());
    }

    @Override
    @Test
    public void toList()
    {
        super.toList();
        BooleanArrayList list = new BooleanArrayList();
        int size = this.classUnderTest().size();
        for (int i = 0; i < size; i++)
        {
            list.add((i & 1) != 0);
        }
        Assert.assertEquals(list, this.classUnderTest().toList());
    }

    @Override
    @Test
    public void makeString()
    {
        super.makeString();
        Assert.assertEquals(this.createExpectedString("", ", ", ""), this.classUnderTest().makeString());
        Assert.assertEquals(this.createExpectedString("", "|", ""), this.classUnderTest().makeString("|"));
        Assert.assertEquals(this.createExpectedString("{", "|", "}"), this.classUnderTest().makeString("{", "|", "}"));
    }

    protected String createExpectedString(String start, String sep, String end)
    {
        StringBuilder expectedString = new StringBuilder(start);
        int size = this.classUnderTest().size();
        for (int i = 0; i < size; i++)
        {
            boolean sizeEven = (this.classUnderTest().size() & 1) == 0;
            boolean iEven = (i & 1) == 0;
            expectedString.append(sizeEven != iEven);
            expectedString.append(i == size - 1 ? "" : sep);
        }
        expectedString.append(end);
        return expectedString.toString();
    }

    @Override
    @Test
    public void appendString()
    {
        super.appendString();
        StringBuilder appendable1 = new StringBuilder();
        this.classUnderTest().appendString(appendable1);
        Assert.assertEquals(this.createExpectedString("", ", ", ""), appendable1.toString());

        StringBuilder appendable2 = new StringBuilder();
        this.classUnderTest().appendString(appendable2, "|");
        Assert.assertEquals(this.createExpectedString("", "|", ""), appendable2.toString());

        StringBuilder appendable3 = new StringBuilder();
        this.classUnderTest().appendString(appendable3, "{", "|", "}");
        Assert.assertEquals(this.createExpectedString("{", "|", "}"), appendable3.toString());
    }

    @Test
    public void toImmutable()
    {
        Assert.assertEquals(this.classUnderTest(), this.classUnderTest().toImmutable());
        Verify.assertInstanceOf(ImmutableBooleanStack.class, this.classUnderTest().toImmutable());
    }
}
