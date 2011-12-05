/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.bag.immutable;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableArrayBagTest extends ImmutableBagTestCase
{
    @Override
    protected ImmutableBag<String> newBag()
    {
        return ImmutableArrayBag.newBagWith("1", "2", "2", "3", "3", "3", "4", "4", "4", "4");
    }

    @Override
    protected int numKeys()
    {
        return 4;
    }

    @Override
    @Test
    public void testSize()
    {
        Verify.assertIterableSize(10, this.newBag());
    }

    @Override
    @Test
    public void testNewWith()
    {
        super.testNewWith();
        Verify.assertInstanceOf(ImmutableArrayBag.class, Bags.immutable.ofAll(Interval.oneTo(9)).newWith(10));
        Verify.assertInstanceOf(ImmutableHashBag.class, Bags.immutable.ofAll(Interval.oneTo(10)).newWith(11));
    }

    @Override
    @Test
    public void testNewWithout()
    {
        super.testNewWithout();
        ImmutableBag<String> bag = this.newBag();
        ImmutableBag<String> newBag2 = bag.newWithout("2").newWithout("2");
        Verify.assertNotEquals(bag, newBag2);
        Assert.assertEquals(newBag2.size(), bag.size() - 2);
        Assert.assertEquals(3, newBag2.sizeDistinct());
        ImmutableBag<String> newBag3 = bag.newWithout("3").newWithout("3").newWithout("3");
        Verify.assertNotEquals(bag, newBag3);
        Assert.assertEquals(newBag3.size(), bag.size() - 3);
        Assert.assertEquals(3, newBag3.sizeDistinct());
        ImmutableBag<String> newBag4 = bag.newWithout("4").newWithout("4").newWithout("4").newWithout("4");
        Verify.assertNotEquals(bag, newBag4);
        Assert.assertEquals(newBag4.size(), bag.size() - 4);
        Assert.assertEquals(3, newBag4.sizeDistinct());
        ImmutableBag<String> newBag5 = bag.newWithout("5");
        Assert.assertEquals(bag, newBag5);
    }

    @Override
    @Test
    public void toMap()
    {
        super.toMap();
        ImmutableBag<String> integers = this.newBag();
        MutableMap<String, String> map =
                integers.toMap(Functions.getToString(), Functions.getToString());
        Assert.assertEquals(UnifiedMap.<String, String>newWithKeysValues("1", "1", "2", "2", "3", "3", "4", "4"), map);
    }

    @Test
    public void testNewBag()
    {
        for (int i = 1; i <= ImmutableArrayBag.MAXIMUM_USEFUL_ARRAY_BAG_SIZE + 1; i++)
        {
            Interval interval = Interval.oneTo(i);
            Verify.assertEqualsAndHashCode(HashBag.newBag(interval), Bags.immutable.ofAll(interval));
        }
    }
}
