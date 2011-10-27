/*
 * Copyright 2011 Goldman Sachs & Co.
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.LazyIterate;
import org.junit.Assert;
import org.junit.Test;

public class CollectIterableTest extends AbstractLazyIterableTestCase
{
    @Override
    protected LazyIterable<Integer> newWith(Integer... integers)
    {
        return LazyIterate.collect(FastList.newListWith(integers), Functions.getIntegerPassThru());
    }

    @Test
    public void forEach()
    {
        InternalIterable<String> select = new CollectIterable<Integer, String>(Interval.oneTo(5), Functions.getToString());
        Appendable builder = new StringBuilder();
        Procedure<String> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<String> select = new CollectIterable<Integer, String>(Interval.oneTo(5), Functions.getToString());
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

    @Override
    @Test
    public void iterator()
    {
        InternalIterable<String> select = new CollectIterable<Integer, String>(Interval.oneTo(5), Functions.getToString());
        StringBuilder builder = new StringBuilder("");
        for (String each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<String> select = new CollectIterable<Integer, String>(Interval.oneTo(5), Functions.getToString());
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
}
