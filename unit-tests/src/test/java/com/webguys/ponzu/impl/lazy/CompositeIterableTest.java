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

package com.webguys.ponzu.impl.lazy;

import java.util.Iterator;
import java.util.List;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.block.procedure.CollectionAddProcedure;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.list.Interval;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CompositeIterableTest extends AbstractLazyIterableTestCase
{
    @Override
    protected LazyIterable<Integer> newWith(Integer... integers)
    {
        return CompositeIterable.with(FastList.newListWith(integers));
    }

    @Override
    @Test
    public void iterator()
    {
        LazyIterable<Integer> select = Interval.oneTo(3).asLazy().concatenate(Interval.fromTo(4, 5));
        StringBuilder builder = new StringBuilder("");
        for (Integer each : select)
        {
            builder.append(each.toString());
        }
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void emptyIterator()
    {
        LazyIterable<String> list = new CompositeIterable<String>();
        Assert.assertFalse(list.iterator().hasNext());
    }

    @Test
    public void iteratorAll()
    {
        LazyIterable<Integer> iterables = CompositeIterable.with(Interval.oneTo(5), Interval.fromTo(6, 10));
        Verify.assertAllSatisfy(iterables, Predicates.greaterThan(0).and(Predicates.lessThan(11)));
    }

    @Test
    public void iteratorAny()
    {
        LazyIterable<Integer> iterables = CompositeIterable.with(Interval.oneTo(5), Interval.fromTo(6, 10));
        Verify.assertAnySatisfy(iterables, Predicates.greaterThan(0).and(Predicates.lessThan(11)));
    }

    @Test
    public void forEach()
    {
        MutableList<Integer> list = Lists.mutable.of();
        LazyIterable<Integer> iterables = CompositeIterable.with(Interval.oneTo(5), Interval.fromTo(6, 10));
        iterables.forEach(CollectionAddProcedure.<Integer>on(list));
        Verify.assertSize(10, list);
        Verify.assertAllSatisfy(list, Predicates.greaterThan(0).and(Predicates.lessThan(11)));
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableList<Integer> list = Lists.mutable.of();
        LazyIterable<Integer> iterables = CompositeIterable.with(Interval.fromTo(6, 10), Interval.oneTo(5));
        iterables.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                list.add(index, each);
            }
        });
        Verify.assertSize(10, list);
        Verify.assertAllSatisfy(list, Predicates.greaterThan(0).and(Predicates.lessThan(11)));
        Verify.assertStartsWith(list, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5);
    }

    @Test
    public void forEachWith()
    {
        final MutableList<Integer> list = Lists.mutable.of();
        LazyIterable<Integer> iterables = CompositeIterable.with(Interval.fromTo(6, 10), Interval.oneTo(5));
        iterables.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer each, Integer parameter)
            {
                list.add(parameter.intValue(), each);
            }
        }, 0);
        Verify.assertSize(10, list);
        Verify.assertAllSatisfy(list, Predicates.greaterThan(0).and(Predicates.lessThan(11)));
        Verify.assertStartsWith(list, 5, 4, 3, 2, 1, 10, 9, 8, 7, 6);
    }

    @Test
    public void ensureLazy()
    {
        CompositeIterable<Integer> iterables = new CompositeIterable<Integer>();
        List<Integer> expected = Interval.oneTo(5);
        iterables.add(expected);
        iterables.add(
                new Iterable<Integer>()
                {
                    public Iterator<Integer> iterator()
                    {
                        throw new RuntimeException("Iterator should not be invoked eagerly");
                    }
                });
        Assert.assertEquals(expected, iterables.take(expected.size()).toList());
    }
}
