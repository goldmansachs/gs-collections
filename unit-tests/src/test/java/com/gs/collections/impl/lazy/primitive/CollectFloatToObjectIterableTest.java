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
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CollectFloatToObjectIterableTest
{
    private LazyIterable<Float> newPrimitiveWith(float... elements)
    {
        return new CollectFloatToObjectIterable<Float>(FloatArrayList.newListWith(elements), Float::valueOf);
    }

    @Test
    public void forEach()
    {
        InternalIterable<Float> select = this.newPrimitiveWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        Appendable builder = new StringBuilder();
        Procedure<Float> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("1.02.03.04.05.0", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Float> select = this.newPrimitiveWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex((object, index) -> {
            builder.append(object);
            builder.append(index);
        });
        Assert.assertEquals("1.002.013.024.035.04", builder.toString());
    }

    @Test
    public void iterator()
    {
        InternalIterable<Float> select = this.newPrimitiveWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
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
        InternalIterable<Float> select = this.newPrimitiveWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith((each, aBuilder) -> { aBuilder.append(each); }, builder);
        Assert.assertEquals("1.02.03.04.05.0", builder.toString());
    }

    @Test
    public void selectInstancesOf()
    {
        Assert.assertEquals(
                FastList.newListWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f),
                this.newPrimitiveWith(1.0f, 2.0f, 3.0f, 4.0f, 5.0f).selectInstancesOf(Float.class).toList());
    }

    @Test
    public void sizeEmptyNotEmpty()
    {
        Verify.assertIterableSize(2, this.newPrimitiveWith(1.0f, 2.0f));
        Verify.assertIterableEmpty(this.newPrimitiveWith());
        Assert.assertTrue(this.newPrimitiveWith(1.0f, 2.0f).notEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrows()
    {
        this.newPrimitiveWith().iterator().remove();
    }
}
