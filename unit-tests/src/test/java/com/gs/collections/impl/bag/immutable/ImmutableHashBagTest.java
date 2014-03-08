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

package com.gs.collections.impl.bag.immutable;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public class ImmutableHashBagTest extends ImmutableBagTestCase
{
    @Override
    protected ImmutableBag<String> newBag()
    {
        return ImmutableHashBag.newBagWith("1", "2", "2", "3", "3", "3", "4", "4", "4", "4");
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
    public void toStringOfItemToCount()
    {
        Assert.assertEquals("{}", ImmutableHashBag.newBag().toStringOfItemToCount());
        Assert.assertEquals("{1=3}", ImmutableHashBag.newBagWith("1", "1", "1").toStringOfItemToCount());
        String actual = ImmutableHashBag.newBagWith("1", "2", "2").toStringOfItemToCount();
        Assert.assertTrue("{1=1, 2=2}".equals(actual) || "{2=2, 1=1}".equals(actual));
    }

    @Test
    public void selectInstancesOf()
    {
        ImmutableBag<Number> numbers = ImmutableHashBag.<Number>newBagWith(1, 2.0, 2.0, 3, 3, 3, 4.0, 4.0, 4.0, 4.0);
        Assert.assertEquals(iBag(1, 3, 3, 3), numbers.selectInstancesOf(Integer.class));
        Assert.assertEquals(iBag(2.0, 2.0, 4.0, 4.0, 4.0, 4.0), numbers.selectInstancesOf(Double.class));
    }

    @Override
    @Test
    public void collectBoolean()
    {
        ImmutableBooleanBag result = this.newBag().collectBoolean("4"::equals);
        Assert.assertEquals(2, result.sizeDistinct());
        Assert.assertEquals(4, result.occurrencesOf(true));
        Assert.assertEquals(6, result.occurrencesOf(false));
    }

    @Test
    public void testNewBag()
    {
        ImmutableHashBag<Object> immutableHashBag = ImmutableHashBag.newBagWith(HashBag.newBag().with(1, 2, 3, 4));
        Verify.assertSize(4, immutableHashBag);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), immutableHashBag.toSortedList());
    }

    @Override
    @Test
    public void groupByEach()
    {
        ImmutableBag<Integer> immutableBag = ImmutableHashBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);

        final MutableMultimap<Integer, Integer> expected = HashBagMultimap.newMultimap();
        final int keys = this.numKeys();
        immutableBag.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int parameter)
            {
                final HashBag<Integer> bag = HashBag.newBag();
                Interval.fromTo(each, keys).forEach(new IntProcedure()
                {
                    public void value(int each)
                    {
                        bag.addOccurrences(each, each);
                    }
                });
                expected.putAll(-each, bag);
            }
        });
        Multimap<Integer, Integer> actual =
                immutableBag.groupByEach(new NegativeIntervalFunction());
        Assert.assertEquals(expected, actual);

        Multimap<Integer, Integer> actualWithTarget =
                immutableBag.groupByEach(new NegativeIntervalFunction(), HashBagMultimap.<Integer, Integer>newMultimap());
        Assert.assertEquals(expected, actualWithTarget);
    }
}
