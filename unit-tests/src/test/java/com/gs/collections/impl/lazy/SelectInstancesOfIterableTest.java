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

package com.gs.collections.impl.lazy;

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.math.SumProcedure;
import com.gs.collections.impl.utility.LazyIterate;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectInstancesOfIterableTest extends AbstractLazyIterableTestCase
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectInstancesOfIterableTest.class);

    @Override
    protected <T> LazyIterable<T> newWith(T... elements)
    {
        return (LazyIterable<T>) LazyIterate.selectInstancesOf(FastList.newListWith(elements), Object.class);
    }

    @Test
    public void forEach()
    {
        InternalIterable<Integer> select = new SelectInstancesOfIterable<Integer>(FastList.newListWith(1, 2.0, 3, 4.0, 5), Integer.class);
        Sum sum = new IntegerSum(0);
        select.forEach(new SumProcedure<Integer>(sum));
        Assert.assertEquals(9, sum.getValue().intValue());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Integer> select = new SelectInstancesOfIterable<Integer>(FastList.newListWith(1, 2.0, 3, 4.0, 5), Integer.class);
        final Sum sum = new IntegerSum(0);
        select.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                sum.add(object);
                sum.add(index);

                LOGGER.info("value=" + object + " index=" + index);
            }
        });
        Assert.assertEquals(12, sum.getValue().intValue());
    }

    @Override
    @Test
    public void iterator()
    {
        InternalIterable<Integer> select = new SelectInstancesOfIterable<Integer>(FastList.newListWith(1, 2.0, 3, 4.0, 5), Integer.class);
        Sum sum = new IntegerSum(0);
        for (Integer each : select)
        {
            sum.add(each);
        }
        Assert.assertEquals(9, sum.getValue().intValue());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Integer> select = new SelectInstancesOfIterable<Integer>(FastList.newListWith(1, 2.0, 3, 4.0, 5), Integer.class);
        Sum sum = new IntegerSum(0);
        select.forEachWith(new Procedure2<Integer, Sum>()
        {
            public void value(Integer each, Sum aSum)
            {
                aSum.add(each);
            }
        }, sum);
        Assert.assertEquals(9, sum.getValue().intValue());
    }

    @Override
    @Test
    public void min_null_throws()
    {
        // Impossible for SelectInstancesOfIterable to contain null
        super.min_null_throws();
    }

    @Override
    @Test
    public void max_null_throws()
    {
        // Impossible for SelectInstancesOfIterable to contain null
        super.max_null_throws();
    }

    @Override
    @Test
    public void min_null_throws_without_comparator()
    {
        // Impossible for SelectInstancesOfIterable to contain null
        super.min_null_throws_without_comparator();
    }

    @Override
    @Test
    public void max_null_throws_without_comparator()
    {
        // Impossible for SelectInstancesOfIterable to contain null
        super.max_null_throws_without_comparator();
    }
}
