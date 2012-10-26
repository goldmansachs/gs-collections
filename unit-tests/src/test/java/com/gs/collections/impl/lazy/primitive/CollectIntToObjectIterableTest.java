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
import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.lazy.AbstractLazyIterableTestCase;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public class CollectIntToObjectIterableTest
        extends AbstractLazyIterableTestCase
{
    @Override
    protected <T> LazyIterable<T> newWith(T... elements)
    {
        return new CollectIntToObjectIterable<T>(
                new CollectIntIterable(
                        ArrayAdapter.<T>adapt(elements).asLazy(),
                        PrimitiveFunctions.unboxNumberToInt()),
                new IntToObjectFunction<T>()
                {
                    public T valueOf(int each)
                    {
                        return (T) Integer.valueOf(each);
                    }
                });
    }

    @Test
    public void forEach()
    {
        InternalIterable<Integer> select = this.newWith(1, 2, 3, 4, 5);
        Appendable builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Integer> select = this.newWith(1, 2, 3, 4, 5);
        final StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
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
        InternalIterable<Integer> select = this.newWith(1, 2, 3, 4, 5);
        StringBuilder builder = new StringBuilder("");
        for (Integer each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Integer> select = this.newWith(1, 2, 3, 4, 5);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith(new Procedure2<Integer, StringBuilder>()
        {
            public void value(Integer each, StringBuilder aBuilder)
            {
                aBuilder.append(each);
            }
        }, builder);
        Assert.assertEquals("12345", builder.toString());
    }

    @Override
    public void selectInstancesOf()
    {
        Assert.assertEquals(
                FastList.<Integer>newListWith(1, 2, 3, 4, 5),
                this.newWith(1, 2.0, 3, 4.0, 5).selectInstancesOf(Integer.class).toList());
    }
}
