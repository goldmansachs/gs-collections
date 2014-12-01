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

package com.gs.collections.impl.lazy;

import java.util.NoSuchElementException;

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.lazy.iterator.DistinctIterator;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.LazyIterate;
import org.junit.Assert;
import org.junit.Test;

public class DistinctIterableTest extends AbstractLazyIterableTestCase
{
    @Override
    protected <T> LazyIterable<T> newWith(T... elements)
    {
        return LazyIterate.distinct(FastList.newListWith(elements));
    }

    @Test
    public void forEach()
    {
        InternalIterable<Integer> distinct = new DistinctIterable<>(FastList.newListWith(3, 1, 2, 2, 3, 4));
        Appendable builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);
        distinct.forEach(appendProcedure);
        Assert.assertEquals("3124", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Integer> distinct = new DistinctIterable<>(FastList.newListWith(1, 2, 1, 3, 2, 4, 3, 5, 4, 6, 5, 7, 6, 8, 7, 9));
        StringBuilder builder = new StringBuilder("");
        distinct.forEachWithIndex((object, index) -> {
            builder.append(object);
            builder.append(index);
        });
        Assert.assertEquals("102132435465768798", builder.toString());
    }

    @Override
    @Test
    public void iterator()
    {
        InternalIterable<Integer> distinct = new DistinctIterable<>(FastList.newListWith(3, 1, 2, 2, 3, 4, 2, 5));
        StringBuilder builder = new StringBuilder("");
        for (Integer each : distinct)
        {
            builder.append(each);
        }
        Assert.assertEquals("31245", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Integer> distinct = new DistinctIterable<>(FastList.newListWith(1, 3, 3, 2, 5, 4, 2, 5, 4));
        StringBuilder builder = new StringBuilder("");
        distinct.forEachWith((each, aBuilder) -> aBuilder.append(each), builder);
        Assert.assertEquals("13254", builder.toString());
    }

    @Test(expected = NoSuchElementException.class)
    public void noSuchElementException()
    {
        new DistinctIterator<>(Lists.mutable.<Integer>of()).next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove()
    {
        new DistinctIterator<>(Lists.mutable.<Integer>of()).remove();
    }

    @Override
    @Test
    public void distinct()
    {
        super.distinct();
        DistinctIterable<Integer> distinct = new DistinctIterable<>(FastList.newListWith(3, 2, 2, 4, 1, 3, 1, 5));
        LazyIterable<Integer> distinctDistinct = distinct.distinct();
        Assert.assertSame(distinctDistinct, distinct);
        Assert.assertEquals(
                FastList.newListWith(3, 2, 4, 1, 5),
                distinctDistinct.toList());
    }
}
