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
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CollectLongToObjectIterableTest
{
    private LazyIterable<Long> newPrimitiveWith(long... elements)
    {
        return new CollectLongToObjectIterable<Long>(LongArrayList.newListWith(elements), Long::valueOf);
    }

    @Test
    public void forEach()
    {
        InternalIterable<Long> select = this.newPrimitiveWith(1L, 2L, 3L, 4L, 5L);
        Appendable builder = new StringBuilder();
        Procedure<Long> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Long> select = this.newPrimitiveWith(1L, 2L, 3L, 4L, 5L);
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
        InternalIterable<Long> select = this.newPrimitiveWith(1L, 2L, 3L, 4L, 5L);
        StringBuilder builder = new StringBuilder("");
        for (Long each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Long> select = this.newPrimitiveWith(1L, 2L, 3L, 4L, 5L);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith((each, aBuilder) -> { aBuilder.append(each); }, builder);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void selectInstancesOf()
    {
        Assert.assertEquals(
                FastList.newListWith(1L, 2L, 3L, 4L, 5L),
                this.newPrimitiveWith(1L, 2L, 3L, 4L, 5L).selectInstancesOf(Long.class).toList());
    }

    @Test
    public void sizeEmptyNotEmpty()
    {
        Verify.assertIterableSize(2, this.newPrimitiveWith(1L, 2L));
        Verify.assertIterableEmpty(this.newPrimitiveWith());
        Assert.assertTrue(this.newPrimitiveWith(1L, 2L).notEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrows()
    {
        this.newPrimitiveWith().iterator().remove();
    }
}
