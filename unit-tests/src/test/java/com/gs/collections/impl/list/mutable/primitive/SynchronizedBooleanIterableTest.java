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

package com.gs.collections.impl.list.mutable.primitive;

import java.util.NoSuchElementException;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.collection.mutable.primitive.AbstractBooleanIterableTestCase;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.primitive.SynchronizedBooleanIterable;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedBooleanIterable}s
 */
public class SynchronizedBooleanIterableTest extends AbstractBooleanIterableTestCase
{
    @Override
    protected BooleanIterable classUnderTest()
    {
        return SynchronizedBooleanIterable.of(BooleanArrayList.newListWith(true, false, true));
    }

    @Override
    protected BooleanIterable newWith(boolean... elements)
    {
        return SynchronizedBooleanIterable.of(BooleanArrayList.newListWith(elements));
    }

    @Override
    protected BooleanIterable newMutableCollectionWith(boolean... elements)
    {
        return BooleanArrayList.newListWith(elements);
    }

    @Override
    protected RichIterable<Object> newObjectCollectionWith(Object... elements)
    {
        return FastList.newListWith(elements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void null_iterable_throws()
    {
        SynchronizedBooleanIterable iterable = SynchronizedBooleanIterable.of(null);
    }

    @Override
    @Test
    public void booleanIterator()
    {
        BooleanIterable iterable = this.newWith(true, true, false, false);
        BooleanArrayList list = BooleanArrayList.newListWith(true, true, false, false);
        BooleanIterator iterator = iterable.booleanIterator();
        for (int i = 0; i < 4; i++)
        {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertTrue(list.remove(iterator.next()));
        }
        Verify.assertEmpty(list);
        Assert.assertFalse(iterator.hasNext());

        Verify.assertThrows(NoSuchElementException.class, (Runnable) iterator::next);
    }

    @Override
    @Test
    public void testEquals()
    {
        //Testing equals() is not applicable.
    }

    @Override
    public void testHashCode()
    {
        //Testing hashCode() is not applicable.
    }

    @Override
    public void newCollection()
    {
        //Testing newCollection() is not applicable.
    }
}
