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

package ponzu.impl.lazy;

import org.junit.Assert;
import org.junit.Test;
import ponzu.api.InternalIterable;
import ponzu.api.LazyIterable;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.list.Interval;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.math.IntegerSum;
import ponzu.impl.math.Sum;
import ponzu.impl.math.SumProcedure;
import ponzu.impl.utility.LazyIterate;

public class FilterNotIterableTest extends AbstractLazyIterableTestCase
{
    @Override
    protected LazyIterable<Integer> newWith(Integer... integers)
    {
        return LazyIterate.filterNot(FastList.newListWith(integers), Predicates.alwaysFalse());
    }

    @Test
    public void forEach()
    {
        InternalIterable<Integer> select = new FilterNotIterable<Integer>(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        select.forEach(new SumProcedure<Integer>(sum));
        Assert.assertEquals(5, sum.getValue().intValue());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Integer> select = new FilterNotIterable<Integer>(Interval.oneTo(5), Predicates.lessThan(2).or(Predicates.greaterThan(3)));
        final Sum sum = new IntegerSum(0);
        select.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                sum.add(object);
                sum.add(index);
            }
        });
        Assert.assertEquals(6, sum.getValue().intValue());
    }

    @Override
    @Test
    public void iterator()
    {
        InternalIterable<Integer> select = new FilterNotIterable<Integer>(Interval.oneTo(5), Predicates.lessThan(5));
        Sum sum = new IntegerSum(0);
        for (Integer each : select)
        {
            sum.add(each);
        }
        Assert.assertEquals(5, sum.getValue().intValue());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Integer> select = new FilterNotIterable<Integer>(Interval.oneTo(5), Predicates.lessThan(5));
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
}
