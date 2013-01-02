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
import com.gs.collections.api.block.function.primitive.DoubleToObjectFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CollectDoubleToObjectIterableTest
{
    public static final DoubleToObjectFunction<Double> BOX_DOUBLE = new DoubleToObjectFunction<Double>()
    {
        public Double valueOf(double each)
        {
            return Double.valueOf(each);
        }
    };

    private LazyIterable<Double> newPrimitiveWith(double... elements)
    {
        return new CollectDoubleToObjectIterable<Double>(DoubleArrayList.newListWith(elements), BOX_DOUBLE);
    }

    @Test
    public void forEach()
    {
        InternalIterable<Double> select = this.newPrimitiveWith(1.0, 2.0, 3.0, 4.0, 5.0);
        Appendable builder = new StringBuilder();
        Procedure<Double> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("1.02.03.04.05.0", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Double> select = this.newPrimitiveWith(1.0, 2.0, 3.0, 4.0, 5.0);
        final StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex(new ObjectIntProcedure<Double>()
        {
            public void value(Double object, int index)
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
        InternalIterable<Double> select = this.newPrimitiveWith(1.0, 2.0, 3.0, 4.0, 5.0);
        StringBuilder builder = new StringBuilder("");
        for (Double each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("1.02.03.04.05.0", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Double> select = this.newPrimitiveWith(1.0, 2.0, 3.0, 4.0, 5.0);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith(new Procedure2<Double, StringBuilder>()
        {
            public void value(Double each, StringBuilder aBuilder)
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
                FastList.<Double>newListWith(1.0, 2.0, 3.0, 4.0, 5.0),
                this.newPrimitiveWith(1.0, 2.0, 3.0, 4.0, 5.0).selectInstancesOf(Double.class).toList());
    }

    @Test
    public void sizeEmptyNotEmpty()
    {
        Verify.assertIterableSize(2, this.newPrimitiveWith(1.0, 2.0));
        Verify.assertIterableEmpty(this.newPrimitiveWith());
        Assert.assertTrue(this.newPrimitiveWith(1.0, 2.0).notEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrows()
    {
        this.newPrimitiveWith().iterator().remove();
    }
}
