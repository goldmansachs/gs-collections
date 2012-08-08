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

package com.gs.collections.impl.lazy.primitive;

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.primitive.FloatToObjectFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public class CollectFloatToObjectIterableTest
{

    private <T> LazyIterable<T> newWith(T... elements)
    {
        return new CollectFloatToObjectIterable(
                new CollectFloatIterable(
                        ArrayAdapter.<T>adapt(elements).asLazy(),
                        PrimitiveFunctions.unboxNumberToFloat()),
                new FloatToObjectFunction()
                {
                    public T valueOf(float each)
                    {
                        return (T) Float.valueOf(each);
                    }
                });
    }

    @Test
    public void forEach()
    {
        InternalIterable<Float> select = this.newWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        Appendable builder = new StringBuilder();
        Procedure<Float> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("1.02.03.04.05.0", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Float> select = this.newWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        final StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex(new ObjectIntProcedure<Float>()
        {
            public void value(Float object, int index)
            {
                builder.append(object);
                builder.append(index);
            }
        });
        Assert.assertEquals("1.002.013.024.035.04", builder.toString());
    }

    @Test
    public void iterator()
    {
        InternalIterable<Float> select = this.newWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        StringBuilder builder = new StringBuilder("");
        for (Float each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("1.02.03.04.05.0", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Float> select = this.newWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith(new Procedure2<Float, StringBuilder>()
        {
            public void value(Float each, StringBuilder aBuilder)
            {
                aBuilder.append(each);
            }
        }, builder);
        Assert.assertEquals("1.02.03.04.05.0", builder.toString());
    }

    @Test
    public void selectInstancesOf()
    {
        Assert.assertEquals(
                FastList.<Float>newListWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f),
                this.newWith(1, 2.0f, 3, 4.0f, 5).selectInstancesOf(Float.class).toList());
    }
}
