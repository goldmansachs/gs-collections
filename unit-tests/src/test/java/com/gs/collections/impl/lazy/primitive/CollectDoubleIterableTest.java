/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.lazy.primitive;

import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.block.procedure.primitive.DoubleProcedure;
import com.gs.collections.api.iterator.DoubleIterator;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.list.Interval;
import org.junit.Assert;
import org.junit.Test;

public class CollectDoubleIterableTest
{
    private final DoubleIterable doubleIterable = Interval.oneTo(3).collectDouble(PrimitiveFunctions.unboxIntegerToDouble());

    @Test
    public void iterator()
    {
        double sum = 0.0d;
        DoubleIterator iterator = this.doubleIterable.iterator();
        while (iterator.hasNext())
        {
            sum += iterator.next();
        }
        Assert.assertEquals(6.0d, sum, 0.001d);
    }

    @Test
    public void testForEach()
    {
        final double[] value = new double[1];
        this.doubleIterable.forEach(new DoubleProcedure()
        {
            public void value(double each)
            {
                value[0] += each;
            }
        });
        Assert.assertEquals(6.0d, value[0], 0.001d);
    }

    @Test
    public void testSum()
    {
        Assert.assertEquals(6.0d, this.doubleIterable.sum(), 0.001d);
    }

    @Test
    public void testMax()
    {
        Assert.assertEquals(3.0d, Interval.fromTo(-3, 3).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).max(), 0.001d);
    }

    @Test
    public void testMin()
    {
        Assert.assertEquals(-3.0d, Interval.fromTo(-3, 3).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).min(), 0.001d);
    }

    @Test
    public void testAverage()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).average(), 0.001d);
    }

    @Test
    public void testMedian()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).median(), 0.001d);
        Assert.assertEquals(4.0d, Interval.oneTo(7).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).median(), 0.001d);
    }

    @Test
    public void testToArray()
    {
        Assert.assertArrayEquals(new double[]{1.0d, 2.0d, 3.0d, 4.0d}, Interval.oneTo(4).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).toArray(), 0.001d);
    }

    @Test
    public void testToSortedArray()
    {
        Assert.assertArrayEquals(new double[]{1.0d, 2.0d, 3.0d, 4.0d}, Interval.fromTo(4, 1).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).toSortedArray(), 0.001d);
    }
}
