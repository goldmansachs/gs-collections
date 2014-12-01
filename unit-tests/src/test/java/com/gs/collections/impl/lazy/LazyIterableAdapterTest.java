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

import com.gs.collections.api.LazyIterable;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.math.SumProcedure;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazyIterableAdapterTest extends AbstractLazyIterableTestCase
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LazyIterableAdapterTest.class);

    @Override
    protected <T> LazyIterable<T> newWith(T... elements)
    {
        return new LazyIterableAdapter<>(FastList.newListWith(elements));
    }

    @Test
    public void forEach()
    {
        LazyIterable<Integer> select = new LazyIterableAdapter<>(Interval.oneTo(5));
        Sum sum = new IntegerSum(0);
        select.forEach(new SumProcedure<>(sum));
        Assert.assertEquals(15, sum.getValue().intValue());
    }

    @Test
    public void into()
    {
        int sum = new LazyIterableAdapter<>(Interval.oneTo(5)).into(FastList.<Integer>newList()).injectInto(0, AddFunction.INTEGER_TO_INT);
        Assert.assertEquals(15, sum);
    }

    @Test
    public void forEachWithIndex()
    {
        LazyIterable<Integer> select = new LazyIterableAdapter<>(Interval.oneTo(5));
        Sum sum = new IntegerSum(0);
        select.forEachWithIndex((object, index) -> {
            sum.add(object);
            sum.add(index);

            LOGGER.info("value={} index={}", object, index);
        });
        Assert.assertEquals(25, sum.getValue().intValue());
    }

    @Override
    @Test
    public void iterator()
    {
        LazyIterable<Integer> select = new LazyIterableAdapter<>(Interval.oneTo(5));
        Sum sum = new IntegerSum(0);
        for (Integer each : select)
        {
            sum.add(each);
        }
        Assert.assertEquals(15, sum.getValue().intValue());
    }

    @Test
    public void forEachWith()
    {
        LazyIterable<Integer> select = new LazyIterableAdapter<>(Interval.oneTo(5));
        Sum sum = new IntegerSum(0);
        select.forEachWith((each, aSum) -> aSum.add(each), sum);
        Assert.assertEquals(15, sum.getValue().intValue());
    }

    @Override
    @Test
    public void distinct()
    {
        super.distinct();
        LazyIterable<Integer> iterable = new LazyIterableAdapter<>(FastList.newListWith(3, 2, 2, 4, 1, 3, 1, 5));
        Assert.assertEquals(
                FastList.newListWith(3, 2, 4, 1, 5),
                iterable.distinct().toList());
    }
}
