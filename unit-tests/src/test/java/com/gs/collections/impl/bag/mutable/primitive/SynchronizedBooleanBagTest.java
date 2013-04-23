/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.bag.mutable.primitive;

import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.block.procedure.primitive.BooleanIntProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.collection.mutable.primitive.AbstractSynchronizedBooleanCollectionTestCase;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedBooleanBag}.
 */
public class SynchronizedBooleanBagTest extends AbstractSynchronizedBooleanCollectionTestCase
{
    @Override
    protected final MutableBooleanBag classUnderTest()
    {
        return new SynchronizedBooleanBag(BooleanHashBag.newBagWith(true, false, true));
    }

    @Override
    protected MutableBooleanCollection classUnderTestWithLock()
    {
        return new SynchronizedBooleanBag(BooleanHashBag.newBagWith(true, false, true), new Object());
    }

    @Override
    protected MutableBooleanBag getEmptyCollection()
    {
        return new SynchronizedBooleanBag(new BooleanHashBag());
    }

    @Override
    protected MutableBooleanBag getEmptyUnSynchronizedCollection()
    {
        return new BooleanHashBag();
    }

    @Override
    protected MutableCollection<Boolean> getEmptyObjectCollection()
    {
        return HashBag.newBag();
    }

    @Override
    protected MutableBooleanBag newWith(boolean... elements)
    {
        return new SynchronizedBooleanBag(BooleanHashBag.newBagWith(elements));
    }

    @Override
    protected BooleanHashBag newUnSynchronizedCollectionWith(boolean... elements)
    {
        return BooleanHashBag.newBagWith(elements);
    }

    @Override
    protected MutableCollection<Object> newObjectCollectionWith(Object... elements)
    {
        return HashBag.newBagWith(elements);
    }

    private final MutableBooleanBag bag = this.classUnderTest();

    @Test
    public void addOccurrences()
    {
        this.bag.addOccurrences(false, 3);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, false, false), this.bag);
        this.bag.addOccurrences(false, 2);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, false, false, false, false), this.bag);
        this.bag.addOccurrences(true, 0);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, false, false, false, false), this.bag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOccurrences_throws()
    {
        new BooleanHashBag().addOccurrences(true, -1);
    }

    @Test
    public void forEachWithOccurrences()
    {
        final StringBuilder stringBuilder = new StringBuilder();
        this.bag.forEachWithOccurrences(new BooleanIntProcedure()
        {
            public void value(boolean argument1, int argument2)
            {
                stringBuilder.append(argument1).append(argument2);
            }
        });
        String string = stringBuilder.toString();
        Assert.assertTrue("true2false1".equals(string)
                || "false1true2".equals(string));
    }

    @Test
    public void removeOccurrences()
    {
        Assert.assertTrue(this.bag.removeOccurrences(true, 1));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), this.bag);
        Assert.assertFalse(this.bag.removeOccurrences(true, 0));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), this.bag);
        Assert.assertFalse(this.bag.removeOccurrences(false, 0));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), this.bag);
        Assert.assertTrue(this.bag.removeOccurrences(false, 1));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), this.bag);
        Assert.assertFalse(this.bag.removeOccurrences(false, 5));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), this.bag);
        Assert.assertTrue(this.bag.removeOccurrences(true, 1));
        Assert.assertEquals(new BooleanHashBag(), this.bag);
        Assert.assertFalse(this.bag.removeOccurrences(true, 1));
        Assert.assertEquals(new BooleanHashBag(), this.bag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeOccurrences_throws()
    {
        new BooleanHashBag().removeOccurrences(true, -1);
    }

    @Override
    @Test
    public void iterator()
    {
        BooleanArrayList list = BooleanArrayList.newListWith(true, false, true);
        BooleanIterator iterator = this.bag.booleanIterator();
        for (int i = 0; i < 3; i++)
        {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertTrue(list.remove(iterator.next()));
        }
        Assert.assertTrue(list.isEmpty());
        Assert.assertFalse(iterator.hasNext());
    }

    @Override
    @Test
    public void testEquals()
    {
        super.testEquals();
        Assert.assertNotEquals(this.getEmptyCollection(), this.bag);
        Assert.assertNotEquals(this.newWith(true), this.bag);
        Assert.assertNotEquals(this.newWith(false), this.bag);
        Verify.assertEqualsAndHashCode(this.newUnSynchronizedCollectionWith(false, true, true), this.bag);
        Verify.assertEqualsAndHashCode(this.newUnSynchronizedCollectionWith(true, false, true), this.bag);
    }
}
