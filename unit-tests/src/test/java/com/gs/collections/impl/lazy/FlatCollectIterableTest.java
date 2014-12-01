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

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.LazyIterate;
import org.junit.Assert;
import org.junit.Test;

public class FlatCollectIterableTest extends AbstractLazyIterableTestCase
{
    @Override
    protected <T> LazyIterable<T> newWith(T... elements)
    {
        return LazyIterate.flatCollect(FastList.newListWith(elements), object -> FastList.newListWith(object));
    }

    @Test
    public void forEach()
    {
        InternalIterable<Integer> select = new FlatCollectIterable<>(Interval.oneTo(5), Interval::oneTo);
        Appendable builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("112123123412345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Integer> select = new FlatCollectIterable<>(Interval.oneTo(5), Interval::oneTo);
        StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex((object, index) -> {
            builder.append(object);
            builder.append(index);
        });
        Assert.assertEquals("10112213243516273849110211312413514", builder.toString());
    }

    @Override
    @Test
    public void iterator()
    {
        InternalIterable<Integer> select = new FlatCollectIterable<>(Interval.oneTo(5), Interval::oneTo);
        StringBuilder builder = new StringBuilder("");
        for (Integer each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("112123123412345", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Integer> select = new FlatCollectIterable<>(Interval.oneTo(5), Interval::oneTo);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith((each, aBuilder) -> aBuilder.append(each), builder);
        Assert.assertEquals("112123123412345", builder.toString());
    }

    @Override
    @Test
    public void distinct()
    {
        super.distinct();
        FlatCollectIterable<Integer, Integer> iterable = new FlatCollectIterable<>(FastList.newListWith(3, 2, 2, 4, 1, 3, 1, 5), Interval::oneTo);
        Assert.assertEquals(
                FastList.newListWith(1, 2, 3, 4, 5),
                iterable.distinct().toList());
    }
}
