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

import com.gs.collections.api.FloatIterable;
import com.gs.collections.api.block.procedure.primitive.FloatProcedure;
import com.gs.collections.api.iterator.FloatIterator;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.list.Interval;
import org.junit.Assert;
import org.junit.Test;

public class CollectFloatIterableTest
{

    private final FloatIterable floatIterable = Interval.oneTo(3).collectFloat(PrimitiveFunctions.unboxIntegerToFloat());

    @Test
    public void iterator()
    {
        float sum = 0.0f;
        FloatIterator iterator = this.floatIterable.iterator();
        while (iterator.hasNext())
        {
            sum += iterator.next();
        }
        Assert.assertEquals(6.0f, sum, 0.001f);
    }

    @Test
    public void testForEach()
    {
        final float[] value = new float[1];
        this.floatIterable.forEach(new FloatProcedure()
        {
            public void value(float each)
            {
                value[0] += each;
            }
        });
        Assert.assertEquals(6.0f, value[0], 0.001);
    }

    @Test
    public void testSum()
    {
        Assert.assertEquals(6.0f, this.floatIterable.sum(), 0.001f);
    }

    @Test
    public void testMax()
    {
        Assert.assertEquals(3.0f, Interval.fromTo(-3, 3).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).max(), 0.001f);
    }

    @Test
    public void testMin()
    {
        Assert.assertEquals(-3.0f, Interval.fromTo(-3, 3).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).min(), 0.001f);
    }

    @Test
    public void testAverage()
    {
        Assert.assertEquals(2.5f, Interval.oneTo(4).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).average(), 0.001);
    }

    @Test
    public void testMedian()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).median(), 0.001);
        Assert.assertEquals(4.0d, Interval.oneTo(7).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).median(), 0.001);
    }

    @Test
    public void testToArray()
    {
        Assert.assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, Interval.oneTo(4).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).toArray(), 0.001f);
    }

    @Test
    public void testToSortedArray()
    {
        Assert.assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).toSortedArray(), 0.001f);
    }
}
