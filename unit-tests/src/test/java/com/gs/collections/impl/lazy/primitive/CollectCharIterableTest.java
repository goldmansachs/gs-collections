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

package com.gs.collections.impl.lazy.primitive;

import java.util.NoSuchElementException;

import com.gs.collections.api.CharIterable;
import com.gs.collections.api.LazyCharIterable;
import com.gs.collections.api.block.function.primitive.CharToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.CharProcedure;
import com.gs.collections.api.iterator.CharIterator;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.primitive.CharPredicates;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.set.mutable.primitive.CharHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CollectCharIterableTest
{
    private final CharIterable charIterable = Interval.oneTo(3).collectChar(PrimitiveFunctions.unboxIntegerToChar());

    @Test
    public void iterator()
    {
        long sum = 0;
        CharIterator iterator = this.charIterable.charIterator();
        while (iterator.hasNext())
        {
            sum += iterator.next();
        }
        Assert.assertEquals(6L, sum);
    }

    @Test
    public void size()
    {
        Assert.assertEquals(3L, this.charIterable.size());
    }

    @Test
    public void empty()
    {
        Assert.assertTrue(this.charIterable.notEmpty());
        Assert.assertFalse(this.charIterable.isEmpty());
    }

    @Test
    public void forEach()
    {
        final char[] value = new char[1];
        this.charIterable.forEach(new CharProcedure()
        {
            public void value(char each)
            {
                value[0] += each;
            }
        });
        Assert.assertEquals(6, value[0]);
    }

    @Test
    public void count()
    {
        Assert.assertEquals(1, this.charIterable.count(CharPredicates.equal((char) 1)));
        Assert.assertEquals(3, this.charIterable.count(CharPredicates.lessThan((char) 4)));
        Assert.assertEquals(2, this.charIterable.count(CharPredicates.greaterThan((char) 1)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.charIterable.anySatisfy(CharPredicates.greaterThan((char) 1)));
        Assert.assertTrue(this.charIterable.anySatisfy(CharPredicates.equal((char) 1)));
        Assert.assertFalse(this.charIterable.anySatisfy(CharPredicates.greaterThan((char) 4)));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertFalse(this.charIterable.noneSatisfy(CharPredicates.greaterThan((char) 2)));
        Assert.assertTrue(this.charIterable.noneSatisfy(CharPredicates.greaterThan((char) 4)));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.charIterable.allSatisfy(CharPredicates.lessThan((char) 4)));
        Assert.assertFalse(this.charIterable.allSatisfy(CharPredicates.lessThan((char) 3)));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(3L, this.charIterable.select(CharPredicates.lessThan((char) 4)).size());
        Assert.assertEquals(2L, this.charIterable.select(CharPredicates.lessThan((char) 3)).size());
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(0L, this.charIterable.reject(CharPredicates.lessThan((char) 4)).size());
        Assert.assertEquals(1L, this.charIterable.reject(CharPredicates.lessThan((char) 3)).size());
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertEquals((char) 1, this.charIterable.detectIfNone(CharPredicates.lessThan((char) 4), (char) 0));
        Assert.assertEquals((char) 0, this.charIterable.detectIfNone(CharPredicates.greaterThan((char) 3), (char) 0));
    }

    @Test
    public void sum()
    {
        Assert.assertEquals(6L, this.charIterable.sum());
    }

    @Test
    public void max()
    {
        Assert.assertEquals((char) 3, Interval.oneTo(3).collectChar(PrimitiveFunctions.unboxIntegerToChar()).max());
    }

    @Test
    public void min()
    {
        Assert.assertEquals((char) 1, Interval.oneTo(3).collectChar(PrimitiveFunctions.unboxIntegerToChar()).min());
    }

    @Test
    public void minIfEmpty()
    {
        Assert.assertEquals((char) 1, Interval.oneTo(3).collectChar(PrimitiveFunctions.unboxIntegerToChar()).minIfEmpty((char) 0));
        Assert.assertEquals((char) 0, FastList.<Integer>newList().asLazy().collectChar(PrimitiveFunctions.unboxIntegerToChar()).minIfEmpty((char) 0));
    }

    @Test
    public void maxIfEmpty()
    {
        Assert.assertEquals((char) 3, Interval.oneTo(3).collectChar(PrimitiveFunctions.unboxIntegerToChar()).maxIfEmpty((char) 0));
        Assert.assertEquals((char) 0, FastList.<Integer>newList().asLazy().collectChar(PrimitiveFunctions.unboxIntegerToChar()).maxIfEmpty((char) 0));
    }

    @Test(expected = NoSuchElementException.class)
    public void maxThrowsOnEmpty()
    {
        Lists.mutable.<Integer>of().asLazy().collectChar(PrimitiveFunctions.unboxIntegerToChar()).max();
    }

    @Test(expected = NoSuchElementException.class)
    public void minThrowsOnEmpty()
    {
        Lists.mutable.<Integer>of().asLazy().collectChar(PrimitiveFunctions.unboxIntegerToChar()).min();
    }

    @Test
    public void average()
    {
        Assert.assertEquals(2.5, Interval.oneTo(4).collectChar(PrimitiveFunctions.unboxIntegerToChar()).average(), 0.001);
    }

    @Test(expected = ArithmeticException.class)
    public void averageThrowsOnEmpty()
    {
        Lists.mutable.<Integer>of().asLazy().collectChar(PrimitiveFunctions.unboxIntegerToChar()).average();
    }

    @Test
    public void median()
    {
        Assert.assertEquals(2.5d, Interval.oneTo(4).collectChar(PrimitiveFunctions.unboxIntegerToChar()).median(), 0.001);
        Assert.assertEquals(4.0d, Interval.oneTo(7).collectChar(PrimitiveFunctions.unboxIntegerToChar()).median(), 0.001);
    }

    @Test(expected = ArithmeticException.class)
    public void medianThrowsOnEmpty()
    {
        Lists.mutable.<Integer>of().asLazy().collectChar(PrimitiveFunctions.unboxIntegerToChar()).median();
    }

    @Test
    public void toArray()
    {
        Assert.assertArrayEquals(new char[]{(char) 1, (char) 2, (char) 3, (char) 4},
                Interval.oneTo(4).collectChar(PrimitiveFunctions.unboxIntegerToChar()).toArray());
    }

    @Test
    public void toSortedArray()
    {
        Assert.assertArrayEquals(new char[]{(char) 1, (char) 2, (char) 3, (char) 4},
                Interval.fromTo(4, 1).collectChar(PrimitiveFunctions.unboxIntegerToChar()).toSortedArray());
    }

    @Test
    public void contains()
    {
        CharIterable charIterable = Interval.fromTo(4, 1).collectChar(PrimitiveFunctions.unboxIntegerToChar());
        Assert.assertTrue(charIterable.contains((char) 1));
        Assert.assertTrue(charIterable.contains((char) 3));
        Assert.assertTrue(charIterable.contains((char) 4));
        Assert.assertFalse(charIterable.contains((char) 5));
    }

    @Test
    public void containsAllArray()
    {
        CharIterable charIterable = Interval.fromTo(4, 1).collectChar(PrimitiveFunctions.unboxIntegerToChar());
        Assert.assertTrue(charIterable.containsAll((char) 1));
        Assert.assertTrue(charIterable.containsAll((char) 1, (char) 2, (char) 3, (char) 4));
        Assert.assertFalse(charIterable.containsAll((char) 1, (char) 2, (char) 3, (char) 4, (char) 5));
        Assert.assertFalse(charIterable.containsAll((char) 7, (char) 6, (char) 5));
    }

    @Test
    public void containsAllIterable()
    {
        CharIterable charIterable = Interval.fromTo(4, 1).collectChar(PrimitiveFunctions.unboxIntegerToChar());
        Assert.assertTrue(charIterable.containsAll(CharArrayList.newListWith((char) 1)));
        Assert.assertTrue(charIterable.containsAll(CharArrayList.newListWith((char) 1, (char) 2, (char) 3, (char) 4)));
        Assert.assertFalse(charIterable.containsAll(CharArrayList.newListWith((char) 1, (char) 2, (char) 3, (char) 4, (char) 5)));
        Assert.assertFalse(charIterable.containsAll(CharArrayList.newListWith((char) 7, (char) 6, (char) 5)));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(FastList.newListWith("\u0001", "\u0002", "\u0003"), this.charIterable.collect(new CharToObjectFunction<Object>()
        {
            public String valueOf(char each)
            {
                return String.valueOf(each);
            }
        }).toList());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[\u0001, \u0002, \u0003]", this.charIterable.toString());
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("\u0001, \u0002, \u0003", this.charIterable.makeString());
        Assert.assertEquals("\u0001/\u0002/\u0003", this.charIterable.makeString("/"));
        Assert.assertEquals("[\u0001, \u0002, \u0003]", this.charIterable.makeString("[", ", ", "]"));
    }

    @Test
    public void appendString()
    {
        StringBuilder appendable = new StringBuilder();
        this.charIterable.appendString(appendable);
        Assert.assertEquals("\u0001, \u0002, \u0003", appendable.toString());
        StringBuilder appendable2 = new StringBuilder();
        this.charIterable.appendString(appendable2, "/");
        Assert.assertEquals("\u0001/\u0002/\u0003", appendable2.toString());
        StringBuilder appendable3 = new StringBuilder();
        this.charIterable.appendString(appendable3, "[", ", ", "]");
        Assert.assertEquals(this.charIterable.toString(), appendable3.toString());
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(CharArrayList.newListWith((char) 1, (char) 2, (char) 3), this.charIterable.toList());
    }

    @Test
    public void toSortedList()
    {
        Assert.assertEquals(CharArrayList.newListWith((char) 1, (char) 2, (char) 3), this.charIterable.toSortedList());
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(CharHashSet.newSetWith((char) 1, (char) 2, (char) 3), this.charIterable.toSet());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(CharHashBag.newBagWith((char) 1, (char) 2, (char) 3), this.charIterable.toBag());
    }

    @Test
    public void asLazy()
    {
        Assert.assertEquals(this.charIterable.toSet(), this.charIterable.asLazy().toSet());
        Verify.assertInstanceOf(LazyCharIterable.class, this.charIterable.asLazy());
    }
}
