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

import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.IntIterable;
import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.iterator.IntIterator;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public class CollectIntIterableTest
{
    private final IntIterable intIterable = Interval.oneTo(3).collectInt(PrimitiveFunctions.unboxIntegerToInt());

    @Test
    public void iterator()
    {
        int sum = 0;
        IntIterator iterator = this.intIterable.intIterator();
        while (iterator.hasNext())
        {
            sum += iterator.next();
        }
        Assert.assertEquals(6, sum);
    }

    @Test
    public void size()
    {
        Assert.assertEquals(3L, this.intIterable.size());
    }

    @Test
    public void empty()
    {
        Assert.assertTrue(this.intIterable.notEmpty());
        Assert.assertFalse(this.intIterable.isEmpty());
    }

    @Test
    public void forEach()
    {
        final AtomicInteger value = new AtomicInteger(0);
        this.intIterable.forEach(new IntProcedure()
        {
            public void value(int each)
            {
                value.addAndGet(each);
            }
        });
        Assert.assertEquals(6, value.intValue());
    }

    @Test
    public void count()
    {
        Assert.assertEquals(1, this.intIterable.count(IntPredicates.equal(1)));
        Assert.assertEquals(3, this.intIterable.count(IntPredicates.lessThan(4)));
        Assert.assertEquals(2, this.intIterable.count(IntPredicates.greaterThan(1)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.intIterable.anySatisfy(IntPredicates.greaterThan(1)));
        Assert.assertTrue(this.intIterable.anySatisfy(IntPredicates.equal(1)));
        Assert.assertFalse(this.intIterable.anySatisfy(IntPredicates.greaterThan(4)));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.intIterable.allSatisfy(IntPredicates.lessThan(4)));
        Assert.assertFalse(this.intIterable.allSatisfy(IntPredicates.lessThan(3)));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(3L, this.intIterable.select(IntPredicates.lessThan(4)).size());
        Assert.assertEquals(2L, this.intIterable.select(IntPredicates.lessThan(3)).size());
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(0L, this.intIterable.reject(IntPredicates.lessThan(4)).size());
        Assert.assertEquals(1L, this.intIterable.reject(IntPredicates.lessThan(3)).size());
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertEquals(1L, this.intIterable.detectIfNone(IntPredicates.lessThan(4), 0));
        Assert.assertEquals(0L, this.intIterable.detectIfNone(IntPredicates.greaterThan(3), 0));
    }

    @Test
    public void sum()
    {
        Assert.assertEquals(6, this.intIterable.sum());
    }

    @Test
    public void max()
    {
        Assert.assertEquals(3, Interval.fromTo(-3, 3).collectInt(PrimitiveFunctions.unboxIntegerToInt()).max());
    }

    @Test
    public void min()
    {
        Assert.assertEquals(-3, Interval.fromTo(-3, 3).collectInt(PrimitiveFunctions.unboxIntegerToInt()).min());
    }

    @Test
    public void average()
    {
        Assert.assertEquals(2.5, Interval.oneTo(4).collectInt(PrimitiveFunctions.unboxIntegerToInt()).average(), 0.001);
    }

    @Test
    public void median()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectInt(PrimitiveFunctions.unboxIntegerToInt()).median(), 0.001);
        Assert.assertEquals(4.0d, Interval.oneTo(7).collectInt(PrimitiveFunctions.unboxIntegerToInt()).median(), 0.001);
    }

    @Test
    public void toArray()
    {
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, Interval.oneTo(4).collectInt(PrimitiveFunctions.unboxIntegerToInt()).toArray());
    }

    @Test
    public void toSortedArray()
    {
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4}, Interval.fromTo(4, 1).collectInt(PrimitiveFunctions.unboxIntegerToInt()).toSortedArray());
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(FastList.newListWith("1", "2", "3"), this.intIterable.collect(new IntToObjectFunction<String>()
        {
            public String valueOf(int each)
            {
                return String.valueOf(each);
            }
        }).toList());
    }
}
