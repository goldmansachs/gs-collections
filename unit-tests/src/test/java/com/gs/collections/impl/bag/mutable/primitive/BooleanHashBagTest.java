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

package com.gs.collections.impl.bag.mutable.primitive;

import java.util.NoSuchElementException;

import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link BooleanHashBag}.
 */
public class BooleanHashBagTest extends AbstractMutableBooleanBagTestCase
{
    @Override
    protected BooleanHashBag classUnderTest()
    {
        return BooleanHashBag.newBagWith(true, false, true);
    }

    @Override
    protected BooleanHashBag newWith(boolean... elements)
    {
        return BooleanHashBag.newBagWith(elements);
    }

    @Override
    @Test
    public void newCollection()
    {
        super.newCollection();
        Assert.assertEquals(
                BooleanHashBag.newBagWith(true, false, true, false, true),
                BooleanHashBag.newBag(BooleanArrayList.newListWith(true, false, true, false, true)));
    }

    @Override
    @Test
    public void size()
    {
        super.size();
        Verify.assertSize(3, BooleanHashBag.newBagWith(true, false, true));
        Verify.assertSize(3, new BooleanHashBag(BooleanHashBag.newBagWith(true, false, true)));
        Verify.assertSize(3, new BooleanHashBag(BooleanArrayList.newListWith(true, false, true)));
    }

    @Override
    @Test
    public void with()
    {
        super.with();
        BooleanHashBag hashBag = new BooleanHashBag().with(true);
        BooleanHashBag emptyBag = new BooleanHashBag();
        BooleanHashBag hashBag0 = emptyBag.with(true, false);
        BooleanHashBag hashBag1 = new BooleanHashBag().with(true, false, true);
        BooleanHashBag hashBag2 = new BooleanHashBag().with(true).with(false).with(true).with(false);
        BooleanHashBag hashBag3 = new BooleanHashBag().with(true).with(false).with(true).with(false).with(true);
        Assert.assertSame(emptyBag, hashBag0);
        Assert.assertEquals(BooleanHashBag.newBagWith(true), hashBag);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), hashBag0);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), hashBag1);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false), hashBag2);
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false, true), hashBag3);
    }

    @Override
    @Test
    public void booleanIterator()
    {
        super.booleanIterator();
        BooleanHashBag bag = this.newWith(true, false, false, true, true, true);
        BooleanIterator iterator = bag.booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertFalse(iterator.hasNext());

        Verify.assertThrows(NoSuchElementException.class, (Runnable) iterator::next);
    }

    @Override
    @Test
    public void appendString()
    {
        super.appendString();
        StringBuilder appendable2 = new StringBuilder();
        BooleanHashBag bag1 = this.newWith(false, false, true);
        bag1.appendString(appendable2);
        Assert.assertEquals(appendable2.toString(), "false, false, true", appendable2.toString());
    }

    @Override
    @Test
    public void toList()
    {
        super.toList();
        MutableBooleanList list = this.newWith(true, true, true, false).toList();
        Assert.assertEquals(list, BooleanArrayList.newListWith(false, true, true, true));
    }
}
