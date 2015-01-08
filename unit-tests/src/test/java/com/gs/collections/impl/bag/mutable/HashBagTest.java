/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.bag.mutable;

import java.util.Collections;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import org.junit.Assert;
import org.junit.Test;

public class HashBagTest extends MutableBagTestCase
{
    @Override
    protected <T> MutableBag<T> newWith(T... littleElements)
    {
        return HashBag.newBagWith(littleElements);
    }

    protected <T> MutableBag<T> newWithOccurrences(ObjectIntPair<T>... elementsWithOccurrences)
    {
        MutableBag<T> bag = this.newWith();
        for (int i = 0; i < elementsWithOccurrences.length; i++)
        {
            ObjectIntPair<T> itemToAdd = elementsWithOccurrences[i];
            bag.addOccurrences(itemToAdd.getOne(), itemToAdd.getTwo());
        }
        return bag;
    }

    @Test
    public void newBagWith()
    {
        HashBag<String> bag = new HashBag<String>().with("apple", "apple");
        assertBagsEqual(HashBag.newBagWith("apple", "apple"), bag);

        bag.with("hope", "hope", "hope");
        assertBagsEqual(HashBag.newBagWith("apple", "apple", "hope", "hope", "hope"), bag);

        bag.withAll(Collections.nCopies(5, "ubermench"));
        Assert.assertEquals(
                UnifiedMap.newWithKeysValues(
                        "apple", 2,
                        "hope", 3,
                        "ubermench", 5),
                bag.toMapOfItemToCount());
    }

    @Test
    public void newBagFromIterable()
    {
        assertBagsEqual(
                HashBag.newBagWith(1, 2, 2, 3, 3, 3),
                HashBag.newBag(FastList.newListWith(1, 2, 2, 3, 3, 3)));
    }

    @Test
    public void newBagFromBag()
    {
        Assert.assertEquals(
                HashBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4),
                HashBag.newBag(HashBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)));
    }
}
