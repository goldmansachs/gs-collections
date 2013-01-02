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
import com.gs.collections.api.block.function.primitive.ShortToObjectFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CollectShortToObjectIterableTest
{
    public static final ShortToObjectFunction<Short> BOX_SHORT = new ShortToObjectFunction<Short>()
    {
        public Short valueOf(short each)
        {
            return Short.valueOf(each);
        }
    };

    private LazyIterable<Short> newPrimitiveWith(short... elements)
    {
        return new CollectShortToObjectIterable<Short>(ShortArrayList.newListWith(elements), BOX_SHORT);
    }

    @Test
    public void forEach()
    {
        InternalIterable<Short> select = this.newPrimitiveWith((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        Appendable builder = new StringBuilder();
        Procedure<Short> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Short> select = this.newPrimitiveWith((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        final StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex(new ObjectIntProcedure<Short>()
        {
            public void value(Short object, int index)
            {
                builder.append(object);
                builder.append(index);
            }
        });
        Assert.assertEquals("1021324354", builder.toString());
    }

    @Test
    public void iterator()
    {
        InternalIterable<Short> select = this.newPrimitiveWith((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        StringBuilder builder = new StringBuilder("");
        for (Short each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Short> select = this.newPrimitiveWith((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith(new Procedure2<Short, StringBuilder>()
        {
            public void value(Short each, StringBuilder aBuilder)
            {
                aBuilder.append(each);
            }
        }, builder);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void selectInstancesOf()
    {
        Assert.assertEquals(
                FastList.<Short>newListWith((short) 1, (short) 2, (short) 3, (short) 4, (short) 5),
                this.newPrimitiveWith((short) 1, (short) 2, (short) 3, (short) 4, (short) 5).selectInstancesOf(Short.class).toList());
    }

    @Test
    public void sizeEmptyNotEmpty()
    {
        Verify.assertIterableSize(2, this.newPrimitiveWith((short) 1, (short) 2));
        Verify.assertIterableEmpty(this.newPrimitiveWith());
        Assert.assertTrue(this.newPrimitiveWith((short) 1, (short) 2).notEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrows()
    {
        this.newPrimitiveWith().iterator().remove();
    }
}
