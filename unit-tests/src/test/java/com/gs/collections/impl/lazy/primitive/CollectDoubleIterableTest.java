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

import java.util.NoSuchElementException;

import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.block.function.primitive.DoubleToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.DoubleProcedure;
import com.gs.collections.api.iterator.DoubleIterator;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.primitive.DoublePredicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.junit.Assert;
import org.junit.Test;

public class CollectDoubleIterableTest
{
    private final DoubleIterable doubleIterable = Interval.oneTo(3).collectDouble(PrimitiveFunctions.unboxIntegerToDouble());

    @Test
    public void iterator()
    {
        double sum = 0.0d;
        DoubleIterator iterator = this.doubleIterable.doubleIterator();
        while (iterator.hasNext())
        {
            sum += iterator.next();
        }
        Assert.assertEquals(6.0d, sum, 0.0d);
    }

    @Test
    public void size()
    {
        Assert.assertEquals(3L, this.doubleIterable.size());
    }

    @Test
    public void empty()
    {
        Assert.assertTrue(this.doubleIterable.notEmpty());
        Assert.assertFalse(this.doubleIterable.isEmpty());
    }

    @Test
    public void forEach()
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
    public void count()
    {
        Assert.assertEquals(1, this.doubleIterable.count(DoublePredicates.equal(1.0d)));
        Assert.assertEquals(3, this.doubleIterable.count(DoublePredicates.lessThan(4.0d)));
        Assert.assertEquals(2, this.doubleIterable.count(DoublePredicates.greaterThan(1.0d)));
        Assert.assertEquals(3, FastList.<Double>newListWith(1.0001d, 1.0002d, 1.0003d, 1.01d, 1.02d)
                .asLazy()
                .collectDouble(PrimitiveFunctions.unboxDoubleToDouble())
                .count(DoublePredicates.equal(1.0d, 0.001d)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.doubleIterable.anySatisfy(DoublePredicates.greaterThan(1.0d)));
        Assert.assertTrue(this.doubleIterable.anySatisfy(DoublePredicates.equal(1.0d)));
        Assert.assertFalse(this.doubleIterable.anySatisfy(DoublePredicates.greaterThan(4.0d)));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.doubleIterable.allSatisfy(DoublePredicates.lessThan(4.0d)));
        Assert.assertFalse(this.doubleIterable.allSatisfy(DoublePredicates.lessThan(3.0d)));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(3L, this.doubleIterable.select(DoublePredicates.lessThan(4.0d)).size());
        Assert.assertEquals(2L, this.doubleIterable.select(DoublePredicates.lessThan(3.0d)).size());
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(0L, this.doubleIterable.reject(DoublePredicates.lessThan(4.0d)).size());
        Assert.assertEquals(1L, this.doubleIterable.reject(DoublePredicates.lessThan(3.0d)).size());
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertEquals(1.0, this.doubleIterable.detectIfNone(DoublePredicates.lessThan(4.0d), 0.0d), 0.0);
        Assert.assertEquals(0.0, this.doubleIterable.detectIfNone(DoublePredicates.greaterThan(3.0d), 0.0d), 0.0);
    }

    @Test
    public void sum()
    {
        Assert.assertEquals(6.0d, this.doubleIterable.sum(), 0.0d);
    }

    @Test
    public void max()
    {
        Assert.assertEquals(3.0d, Interval.fromTo(-3, 3).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).max(), 0.0d);
    }

    @Test
    public void min()
    {
        Assert.assertEquals(-3.0d, Interval.fromTo(-3, 3).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).min(), 0.0d);
    }

    @Test(expected = NoSuchElementException.class)
    public void maxThrowsOnEmpty()
    {
        Lists.mutable.<Integer>of().asLazy().collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).max();
    }

    @Test(expected = NoSuchElementException.class)
    public void minThrowsOnEmpty()
    {
        Lists.mutable.<Integer>of().asLazy().collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).min();
    }

    @Test
    public void average()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).average(), 0.0d);
    }

    @Test
    public void median()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).median(), 0.0d);
        Assert.assertEquals(4.0d, Interval.oneTo(7).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).median(), 0.0d);
    }

    @Test
    public void toArray()
    {
        Assert.assertArrayEquals(new double[]{1.0d, 2.0d, 3.0d, 4.0d}, Interval.oneTo(4).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).toArray(), 0.0d);
    }

    @Test
    public void toSortedArray()
    {
        Assert.assertArrayEquals(new double[]{1.0d, 2.0d, 3.0d, 4.0d}, Interval.fromTo(4, 1).collectDouble(PrimitiveFunctions.unboxIntegerToDouble()).toSortedArray(), 0.0d);
    }

    @Test
    public void contains()
    {
        DoubleIterable doubleIterable = Interval.fromTo(4, 1).collectDouble(PrimitiveFunctions.unboxIntegerToDouble());
        Assert.assertTrue(doubleIterable.contains(1.0d));
        Assert.assertTrue(doubleIterable.contains(3.0d));
        Assert.assertTrue(doubleIterable.contains(4.0d));
        Assert.assertFalse(doubleIterable.contains(5.0d));
    }

    @Test
    public void containsAllArray()
    {
        DoubleIterable doubleIterable = Interval.fromTo(4, 1).collectDouble(PrimitiveFunctions.unboxIntegerToDouble());
        Assert.assertTrue(doubleIterable.containsAll(1.0d));
        Assert.assertTrue(doubleIterable.containsAll(1.0d, 2.0d, 3.0d, 4.0d));
        Assert.assertFalse(doubleIterable.containsAll(1.0d, 2.0d, 3.0d, 4.0d, 5.0d));
        Assert.assertFalse(doubleIterable.containsAll(7.0d, 6.0d, 5.0d));
    }

    @Test
    public void containsAllIterable()
    {
        DoubleIterable doubleIterable = Interval.fromTo(4, 1).collectDouble(PrimitiveFunctions.unboxIntegerToDouble());
        Assert.assertTrue(doubleIterable.containsAll(DoubleArrayList.newListWith(1.0d)));
        Assert.assertTrue(doubleIterable.containsAll(DoubleArrayList.newListWith(1.0d, 2.0d, 3.0d, 4.0d)));
        Assert.assertFalse(doubleIterable.containsAll(DoubleArrayList.newListWith(1.0d, 2.0d, 3.0d, 4.0d, 5.0d)));
        Assert.assertFalse(doubleIterable.containsAll(DoubleArrayList.newListWith(7.0d, 6.0d, 5.0d)));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(FastList.newListWith("1.0", "2.0", "3.0"), this.doubleIterable.collect(new DoubleToObjectFunction<String>()
        {
            public String valueOf(double each)
            {
                return String.valueOf(each);
            }
        }).toList());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[1.0, 2.0, 3.0]", this.doubleIterable.toString());
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("1.0, 2.0, 3.0", this.doubleIterable.makeString());
        Assert.assertEquals("1.0/2.0/3.0", this.doubleIterable.makeString("/"));
        Assert.assertEquals("[1.0, 2.0, 3.0]", this.doubleIterable.makeString("[", ", ", "]"));
    }

    @Test
    public void appendString()
    {
        StringBuilder appendable = new StringBuilder();
        this.doubleIterable.appendString(appendable);
        Assert.assertEquals("1.0, 2.0, 3.0", appendable.toString());
        StringBuilder appendable2 = new StringBuilder();
        this.doubleIterable.appendString(appendable2, "/");
        Assert.assertEquals("1.0/2.0/3.0", appendable2.toString());
        StringBuilder appendable3 = new StringBuilder();
        this.doubleIterable.appendString(appendable3, "[", ", ", "]");
        Assert.assertEquals(this.doubleIterable.toString(), appendable3.toString());
    }
}
