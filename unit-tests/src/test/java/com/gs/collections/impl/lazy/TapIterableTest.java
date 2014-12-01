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

public class TapIterableTest extends AbstractLazyIterableTestCase
{
    @Override
    protected <T> LazyIterable<T> newWith(T... elements)
    {
        Appendable builder = new StringBuilder();
        Procedure<T> appendProcedure = Procedures.append(builder);
        return LazyIterate.tap(FastList.newListWith(elements), appendProcedure);
    }

    @Test
    public void forEach()
    {
        StringBuilder builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);

        InternalIterable<Integer> tap = new TapIterable<>(Interval.oneTo(5), appendProcedure);
        Procedure<Integer> appendDouble = each -> builder.append(each * 2);
        tap.forEach(appendDouble);
        Assert.assertEquals("12243648510", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        StringBuilder builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);
        InternalIterable<Integer> tap = new TapIterable<>(Interval.oneTo(5), appendProcedure);
        tap.forEachWithIndex((each, index) -> {
            builder.append(each * 2);
            builder.append(index);
        });
        Assert.assertEquals("1202413624835104", builder.toString());
    }

    @Override
    @Test
    public void iterator()
    {
        StringBuilder builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);
        InternalIterable<Integer> tap = new TapIterable<>(Interval.oneTo(5), appendProcedure);
        for (Integer each : tap)
        {
            builder.append(each + 1);
        }
        Assert.assertEquals("1223344556", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        StringBuilder builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);
        InternalIterable<Integer> tap = new TapIterable<>(Interval.oneTo(5), appendProcedure);
        tap.forEachWith((each, aBuilder) -> aBuilder.append(each - 1), builder);
        Assert.assertEquals("1021324354", builder.toString());
    }
}
