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

package com.gs.collections.impl.lazy.primitive;

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CollectIntToObjectIterableTest
{
    private LazyIterable<Integer> newPrimitiveWith(int... elements)
    {
        return new CollectIntToObjectIterable<Integer>(IntArrayList.newListWith(elements), Integer::valueOf);
    }

    @Test
    public void forEach()
    {
        InternalIterable<Integer> select = this.newPrimitiveWith(1, 2, 3, 4, 5);
        Appendable builder = new StringBuilder();
        Procedure<Integer> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Integer> select = this.newPrimitiveWith(1, 2, 3, 4, 5);
        StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex((object, index) -> {
            builder.append(object);
            builder.append(index);
        });
        Assert.assertEquals("1021324354", builder.toString());
    }

    @Test
    public void iterator()
    {
        InternalIterable<Integer> select = this.newPrimitiveWith(1, 2, 3, 4, 5);
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
        InternalIterable<Integer> select = this.newPrimitiveWith(1, 2, 3, 4, 5);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith((each, aBuilder) -> { aBuilder.append(each); }, builder);
        Assert.assertEquals("12345", builder.toString());
    }

    public void selectInstancesOf()
    {
        Assert.assertEquals(
                FastList.newListWith(1, 2, 3, 4, 5),
                this.newPrimitiveWith(1, 2, 3, 4, 5).selectInstancesOf(Integer.class).toList());
    }

    @Test
    public void sizeEmptyNotEmpty()
    {
        Verify.assertIterableSize(2, this.newPrimitiveWith(1, 2));
        Verify.assertIterableEmpty(this.newPrimitiveWith());
        Assert.assertTrue(this.newPrimitiveWith(1, 2).notEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrows()
    {
        this.newPrimitiveWith().iterator().remove();
    }
}
