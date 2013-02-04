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

import com.gs.collections.api.FloatIterable;
import com.gs.collections.api.block.function.primitive.FloatToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.FloatProcedure;
import com.gs.collections.api.iterator.FloatIterator;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.primitive.FloatPredicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.set.mutable.primitive.FloatHashSet;
import org.junit.Assert;
import org.junit.Test;

public class CollectFloatIterableTest
{
    private final FloatIterable floatIterable = Interval.oneTo(3).collectFloat(PrimitiveFunctions.unboxIntegerToFloat());

    @Test
    public void floatIterator()
    {
        float sum = 0.0f;
        FloatIterator iterator = this.floatIterable.floatIterator();
        while (iterator.hasNext())
        {
            sum += iterator.next();
        }
        Assert.assertEquals(6.0f, sum, 0.0f);
    }

    @Test
    public void size()
    {
        Assert.assertEquals(3L, this.floatIterable.size());
    }

    @Test
    public void empty()
    {
        Assert.assertTrue(this.floatIterable.notEmpty());
        Assert.assertFalse(this.floatIterable.isEmpty());
    }

    @Test
    public void forEach()
    {
        final float[] value = new float[1];
        this.floatIterable.forEach(new FloatProcedure()
        {
            public void value(float each)
            {
                value[0] += each;
            }
        });
        Assert.assertEquals(6.0f, value[0], 0.0);
    }

    @Test
    public void count()
    {
        Assert.assertEquals(1, this.floatIterable.count(FloatPredicates.equal(1.0f)));
        Assert.assertEquals(3, this.floatIterable.count(FloatPredicates.lessThan(4.0f)));
        Assert.assertEquals(2, this.floatIterable.count(FloatPredicates.greaterThan(1.0f)));
        Assert.assertEquals(3, FastList.<Float>newListWith(1.0001f, 1.0002f, 1.0003f, 1.01f, 1.02f)
                .asLazy()
                .collectFloat(PrimitiveFunctions.unboxFloatToFloat())
                .count(FloatPredicates.equal(1.0f, 0.001f)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.floatIterable.anySatisfy(FloatPredicates.greaterThan(1.0f)));
        Assert.assertTrue(this.floatIterable.anySatisfy(FloatPredicates.equal(1.0f)));
        Assert.assertFalse(this.floatIterable.anySatisfy(FloatPredicates.greaterThan(4.0f)));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.floatIterable.allSatisfy(FloatPredicates.lessThan(4.0f)));
        Assert.assertFalse(this.floatIterable.allSatisfy(FloatPredicates.lessThan(3.0f)));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(3L, this.floatIterable.select(FloatPredicates.lessThan(4.0f)).size());
        Assert.assertEquals(2L, this.floatIterable.select(FloatPredicates.lessThan(3.0f)).size());
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(0L, this.floatIterable.reject(FloatPredicates.lessThan(4.0f)).size());
        Assert.assertEquals(1L, this.floatIterable.reject(FloatPredicates.lessThan(3.0f)).size());
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertEquals(1.0, this.floatIterable.detectIfNone(FloatPredicates.lessThan(4.0f), 0.0f), 0.0);
        Assert.assertEquals(0.0, this.floatIterable.detectIfNone(FloatPredicates.greaterThan(3.0f), 0.0f), 0.0);
    }

    @Test
    public void sum()
    {
        Assert.assertEquals(6.0f, this.floatIterable.sum(), 0.0f);
    }

    @Test
    public void max()
    {
        Assert.assertEquals(3.0f, Interval.fromTo(-3, 3).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).max(), 0.0f);
    }

    @Test
    public void min()
    {
        Assert.assertEquals(-3.0f, Interval.fromTo(-3, 3).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).min(), 0.0f);
    }

    @Test
    public void minIfEmpty()
    {
        Assert.assertEquals(-3.0f, Interval.fromTo(-3, 3).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).minIfEmpty(0.0f), 0.0f);
        Assert.assertEquals(0.0f, FastList.<Integer>newList().asLazy().collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).minIfEmpty(0.0f), 0.0f);
    }

    @Test
    public void maxIfEmpty()
    {
        Assert.assertEquals(3.0f, Interval.fromTo(-3, 3).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).maxIfEmpty(0.0f), 0.0f);
        Assert.assertEquals(0.0f, FastList.<Integer>newList().asLazy().collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).maxIfEmpty(0.0f), 0.0f);
    }

    @Test(expected = NoSuchElementException.class)
    public void maxThrowsOnEmpty()
    {
        Lists.mutable.<Integer>of().asLazy().collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).max();
    }

    @Test(expected = NoSuchElementException.class)
    public void minThrowsOnEmpty()
    {
        Lists.mutable.<Integer>of().asLazy().collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).min();
    }

    @Test
    public void average()
    {
        Assert.assertEquals(2.5f, Interval.oneTo(4).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).average(), 0.001);
    }

    @Test
    public void median()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).median(), 0.001);
        Assert.assertEquals(4.0d, Interval.oneTo(7).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).median(), 0.001);
    }

    @Test
    public void toArray()
    {
        Assert.assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, Interval.oneTo(4).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).toArray(), 0.0f);
    }

    @Test
    public void toSortedArray()
    {
        Assert.assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).toSortedArray(), 0.0f);
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).contains(1.0f));
        Assert.assertTrue(Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).contains(3.0f));
        Assert.assertTrue(Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).contains(4.0f));
        Assert.assertFalse(Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).contains(5.0f));
    }

    @Test
    public void containsAll()
    {
        Assert.assertTrue(Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).containsAll(1.0f));
        Assert.assertTrue(Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).containsAll(1.0f, 2.0f, 3.0f, 4.0f));
        Assert.assertFalse(Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).containsAll(1.0f, 2.0f, 3.0f, 4.0f, 5.0f));
        Assert.assertFalse(Interval.fromTo(4, 1).collectFloat(PrimitiveFunctions.unboxIntegerToFloat()).containsAll(7.0f, 6.0f, 5.0f));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(FastList.newListWith("1.0", "2.0", "3.0"), this.floatIterable.collect(new FloatToObjectFunction<String>()
        {
            public String valueOf(float each)
            {
                return String.valueOf(each);
            }
        }).toList());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[1.0, 2.0, 3.0]", this.floatIterable.toString());
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("1.0, 2.0, 3.0", this.floatIterable.makeString());
        Assert.assertEquals("1.0/2.0/3.0", this.floatIterable.makeString("/"));
        Assert.assertEquals("[1.0, 2.0, 3.0]", this.floatIterable.makeString("[", ", ", "]"));
    }

    @Test
    public void appendString()
    {
        StringBuilder appendable = new StringBuilder();
        this.floatIterable.appendString(appendable);
        Assert.assertEquals("1.0, 2.0, 3.0", appendable.toString());
        StringBuilder appendable2 = new StringBuilder();
        this.floatIterable.appendString(appendable2, "/");
        Assert.assertEquals("1.0/2.0/3.0", appendable2.toString());
        StringBuilder appendable3 = new StringBuilder();
        this.floatIterable.appendString(appendable3, "[", ", ", "]");
        Assert.assertEquals(this.floatIterable.toString(), appendable3.toString());
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(FloatArrayList.newListWith(1.0f, 2.0f, 3.0f), this.floatIterable.toList());
    }

    @Test
    public void toSortedList()
    {
        Assert.assertEquals(FloatArrayList.newListWith(1.0f, 2.0f, 3.0f), this.floatIterable.toSortedList());
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(FloatHashSet.newSetWith(1.0f, 2.0f, 3.0f), this.floatIterable.toSet());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void toBag()
    {
        this.floatIterable.toBag();
    }
}
