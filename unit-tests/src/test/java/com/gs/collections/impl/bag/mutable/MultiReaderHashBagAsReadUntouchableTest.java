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

package com.gs.collections.impl.bag.mutable;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollectionTestCase;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import org.junit.Assert;
import org.junit.Test;

public class MultiReaderHashBagAsReadUntouchableTest extends UnmodifiableMutableCollectionTestCase<Integer>
{
    @Override
    protected MutableBag<Integer> getCollection()
    {
        return MultiReaderHashBag.newBagWith(1, 1).asReadUntouchable();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addOccurrences()
    {
        this.getCollection().addOccurrences(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeOccurrences()
    {
        this.getCollection().removeOccurrences(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setOccurrences()
    {
        this.getCollection().setOccurrences(1, 1);
    }

    @Test
    public void occurrencesOf()
    {
        Assert.assertEquals(2, this.getCollection().occurrencesOf(1));
        Assert.assertEquals(0, this.getCollection().occurrencesOf(0));
    }

    @Test
    public void sizeDistinct()
    {
        Assert.assertEquals(1, this.getCollection().sizeDistinct());
    }

    @Test
    public void toMapOfItemToCount()
    {
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, 2), this.getCollection().toMapOfItemToCount());
    }

    @Test
    public void toStringOfItemToCount()
    {
        Assert.assertEquals("{1=2}", this.getCollection().toStringOfItemToCount());
    }

    @Test
    public void forEachWithOccurrences()
    {
        int[] sum = new int[1];
        this.getCollection().forEachWithOccurrences((each, occurrences) -> {
            if (occurrences > 1)
            {
                sum[0] += each * occurrences;
            }
        });

        Assert.assertEquals(2, sum[0]);
    }
}
