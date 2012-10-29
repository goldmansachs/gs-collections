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

import com.gs.collections.api.LongIterable;
import com.gs.collections.api.block.function.primitive.LongToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.LongProcedure;
import com.gs.collections.api.iterator.LongIterator;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.primitive.LongPredicates;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public class CollectLongIterableTest
{
    private final LongIterable longIterable = Interval.oneTo(3).collectLong(PrimitiveFunctions.unboxIntegerToLong());

    @Test
    public void iterator()
    {
        long sum = 0;
        LongIterator iterator = this.longIterable.longIterator();
        while (iterator.hasNext())
        {
            sum += iterator.next();
        }
        Assert.assertEquals(6L, sum);
    }

    @Test
    public void size()
    {
        Assert.assertEquals(3L, this.longIterable.size());
    }

    @Test
    public void empty()
    {
        Assert.assertTrue(this.longIterable.notEmpty());
        Assert.assertFalse(this.longIterable.isEmpty());
    }

    @Test
    public void forEach()
    {
        final long[] value = new long[1];
        this.longIterable.forEach(new LongProcedure()
        {
            public void value(long each)
            {
                value[0] += each;
            }
        });
        Assert.assertEquals(6, value[0]);
    }

    @Test
    public void count()
    {
        Assert.assertEquals(1, this.longIterable.count(LongPredicates.equal(1)));
        Assert.assertEquals(3, this.longIterable.count(LongPredicates.lessThan(4)));
        Assert.assertEquals(2, this.longIterable.count(LongPredicates.greaterThan(1)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.longIterable.anySatisfy(LongPredicates.greaterThan(1)));
        Assert.assertTrue(this.longIterable.anySatisfy(LongPredicates.equal(1)));
        Assert.assertFalse(this.longIterable.anySatisfy(LongPredicates.greaterThan(4)));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.longIterable.allSatisfy(LongPredicates.lessThan(4)));
        Assert.assertFalse(this.longIterable.allSatisfy(LongPredicates.lessThan(3)));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(3L, this.longIterable.select(LongPredicates.lessThan(4L)).size());
        Assert.assertEquals(2L, this.longIterable.select(LongPredicates.lessThan(3L)).size());
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(0L, this.longIterable.reject(LongPredicates.lessThan(4L)).size());
        Assert.assertEquals(1L, this.longIterable.reject(LongPredicates.lessThan(3L)).size());
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertEquals(1L, this.longIterable.detectIfNone(LongPredicates.lessThan(4L), 0L));
        Assert.assertEquals(0L, this.longIterable.detectIfNone(LongPredicates.greaterThan(3L), 0L));
    }

    @Test
    public void sum()
    {
        Assert.assertEquals(6, this.longIterable.sum());
    }

    @Test
    public void max()
    {
        Assert.assertEquals(3, Interval.fromTo(-3, 3).collectLong(PrimitiveFunctions.unboxIntegerToLong()).max());
    }

    @Test
    public void min()
    {
        Assert.assertEquals(-3, Interval.fromTo(-3, 3).collectLong(PrimitiveFunctions.unboxIntegerToLong()).min());
    }

    @Test
    public void average()
    {
        Assert.assertEquals(2.5, Interval.oneTo(4).collectLong(PrimitiveFunctions.unboxIntegerToLong()).average(), 0.001);
    }

    @Test
    public void median()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectLong(PrimitiveFunctions.unboxIntegerToLong()).median(), 0.001);
        Assert.assertEquals(4.0d, Interval.oneTo(7).collectLong(PrimitiveFunctions.unboxIntegerToLong()).median(), 0.001);
    }

    @Test
    public void toArray()
    {
        Assert.assertArrayEquals(new long[]{1, 2, 3, 4}, Interval.oneTo(4).collectLong(PrimitiveFunctions.unboxIntegerToLong()).toArray());
    }

    @Test
    public void toSortedArray()
    {
        Assert.assertArrayEquals(new long[]{1, 2, 3, 4}, Interval.fromTo(4, 1).collectLong(PrimitiveFunctions.unboxIntegerToLong()).toSortedArray());
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(FastList.newListWith("1", "2", "3"), this.longIterable.collect(new LongToObjectFunction<Object>()
        {
            public String valueOf(long each)
            {
                return String.valueOf(each);
            }
        }).toList());
    }
}
