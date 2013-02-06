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

import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link BooleanHashBag}.
 */
public class BooleanHashBagTest
{
    @Test
    public void add()
    {
        BooleanHashBag bag = new BooleanHashBag();
        Assert.assertTrue(bag.add(true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true), bag);
        Assert.assertTrue(bag.add(false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), bag);
        Assert.assertTrue(bag.add(true));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), bag);
        Assert.assertTrue(bag.add(false));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true, false), bag);
    }

    @Test
    public void addOccurrences()
    {
        BooleanHashBag bag = new BooleanHashBag();
        bag.addOccurrences(false, 3);
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false), bag);
        bag.addOccurrences(false, 2);
        Assert.assertEquals(BooleanHashBag.newBagWith(false, false, false, false, false), bag);
    }

    @Test
    public void remove()
    {
        BooleanHashBag bag = new BooleanHashBag();
        Assert.assertFalse(bag.remove(false));
        Assert.assertEquals(BooleanHashBag.newBag(), bag);
        Assert.assertTrue(bag.add(false));
        Assert.assertTrue(bag.add(false));
        Assert.assertTrue(bag.remove(false));
        Assert.assertEquals(BooleanHashBag.newBagWith(false), bag);
        Assert.assertTrue(bag.remove(false));
        Assert.assertEquals(BooleanHashBag.newBag(), bag);
    }

    @Test
    public void removeOccurrences()
    {
        BooleanHashBag bag = new BooleanHashBag();
        bag.addOccurrences(true, 5);
        Assert.assertTrue(bag.removeOccurrences(true, 2));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, true), bag);
        Assert.assertFalse(bag.removeOccurrences(true, 0));
        Assert.assertEquals(BooleanHashBag.newBagWith(true, true, true), bag);
        Assert.assertTrue(bag.removeOccurrences(true, 5));
        Assert.assertEquals(BooleanHashBag.newBag(), bag);
        Assert.assertFalse(bag.removeOccurrences(true, 5));
        Assert.assertEquals(BooleanHashBag.newBag(), bag);
    }
}
