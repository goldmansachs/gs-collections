/*
 * Copyright 2011 Goldman Sachs.
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

package com.webguys.ponzu.impl.utility;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.list.ImmutableList;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.block.factory.Procedures;
import com.webguys.ponzu.impl.block.function.AddFunction;
import com.webguys.ponzu.impl.list.Interval;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.math.IntegerSum;
import com.webguys.ponzu.impl.math.Sum;
import org.junit.Assert;
import org.junit.Test;

public class LazyIterateTest
{
    @Test
    public void selectForEach()
    {
        LazyIterable<Integer> select = LazyIterate.filter(Interval.oneTo(5), Predicates.lessThan(5));
        int sum = select.foldLeft(0, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(10, sum);
    }

    @Test
    public void selectForEachWithIndex()
    {
        LazyIterable<Integer> select = LazyIterate.filter(Interval.oneTo(5), Predicates.lessThan(5));
        final Sum sum = new IntegerSum(0);
        select.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                sum.add(object);
                sum.add(index);
            }
        });
        Assert.assertEquals(16, sum.getValue().intValue());
    }

    @Test
    public void selectIterator()
    {
        LazyIterable<Integer> select = LazyIterate.filter(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        for (Integer each : select)
        {
            sum.add(each);
        }
        Assert.assertEquals(10, sum.getValue().intValue());
    }

    @Test
    public void selectForEachWith()
    {
        LazyIterable<Integer> select = LazyIterate.filter(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        select.forEachWith(new Procedure2<Integer, Sum>()
        {
            public void value(Integer each, Sum aSum)
            {
                aSum.add(each);
            }
        }, sum);
        Assert.assertEquals(10, sum.getValue().intValue());
    }

    @Test
    public void rejectForEach()
    {
        LazyIterable<Integer> select = LazyIterate.filterNot(Interval.oneTo(5), Predicates.lessThan(5));
        int sum = select.foldLeft(0, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(5, sum);
    }

    @Test
    public void rejectForEachWithIndex()
    {
        LazyIterable<Integer> select = LazyIterate.filterNot(Interval.oneTo(5), Predicates.lessThan(5));
        final Sum sum = new IntegerSum(0);
        select.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                sum.add(object);
                sum.add(index);
            }
        });
        Assert.assertEquals(5, sum.getValue().intValue());
    }

    @Test
    public void rejectIterator()
    {
        LazyIterable<Integer> select = LazyIterate.filterNot(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        for (Integer each : select)
        {
            sum.add(each);
        }
        Assert.assertEquals(5, sum.getValue().intValue());
    }

    @Test
    public void rejectForEachWith()
    {
        LazyIterable<Integer> select = LazyIterate.filterNot(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        select.forEachWith(new Procedure2<Integer, Sum>()
        {
            public void value(Integer each, Sum aSum)
            {
                aSum.add(each);
            }
        }, sum);
        Assert.assertEquals(5, sum.getValue().intValue());
    }

    @Test
    public void collectForEach()
    {
        LazyIterable<String> select = LazyIterate.transform(Interval.oneTo(5), Functions.getToString());
        Appendable builder = new StringBuilder();
        Procedure<String> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void collectForEachWithIndex()
    {
        LazyIterable<String> select = LazyIterate.transform(Interval.oneTo(5), Functions.getToString());
        final StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String object, int index)
            {
                builder.append(object);
                builder.append(index);
            }
        });
        Assert.assertEquals("1021324354", builder.toString());
    }

    @Test
    public void collectIterator()
    {
        LazyIterable<String> select = LazyIterate.transform(Interval.oneTo(5), Functions.getToString());
        StringBuilder builder = new StringBuilder("");
        for (String each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void collectForEachWith()
    {
        LazyIterable<String> select = LazyIterate.transform(Interval.oneTo(5), Functions.getToString());
        StringBuilder builder = new StringBuilder("");
        select.forEachWith(new Procedure2<String, StringBuilder>()
        {
            public void value(String each, StringBuilder aBuilder)
            {
                aBuilder.append(each);
            }
        }, builder);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void asDeferred()
    {
        MutableList<Integer> expected = FastList.newList(Interval.oneTo(5));
        MutableList<Integer> actual0 = LazyIterate.adapt(Interval.oneTo(5)).toList();
        MutableList<Integer> actual1 = Interval.oneTo(5).asLazy().toList();
        MutableList<Integer> actual2 = FastList.newList(Interval.oneTo(5)).asLazy().toList();
        MutableList<Integer> actual3 = actual2.asUnmodifiable().asLazy().toList();
        MutableList<Integer> actual4 = actual2.asSynchronized().asLazy().toList();
        MutableList<Integer> actual5 = actual2.asLazy().filter(Predicates.alwaysTrue()).toList();
        MutableList<Integer> actual6 = actual2.toImmutable().asLazy().toList();
        ImmutableList<Integer> actual7 = actual2.asLazy().toList().toImmutable();
        Assert.assertEquals(expected, actual0);
        Assert.assertEquals(expected, actual1);
        Assert.assertEquals(expected, actual2);
        Assert.assertEquals(expected, actual3);
        Assert.assertEquals(expected, actual4);
        Assert.assertEquals(expected, actual5);
        Assert.assertEquals(expected, actual6);
        Assert.assertEquals(expected, actual7);
    }
}
