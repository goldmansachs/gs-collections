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
import com.gs.collections.api.block.function.primitive.CharToObjectFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import org.junit.Assert;
import org.junit.Test;

public class CollectCharToObjectIterableTest
{
    public static final CharToObjectFunction<Character> BOX_CHAR = new CharToObjectFunction<Character>()
    {
        public Character valueOf(char each)
        {
            return Character.valueOf(each);
        }
    };

    private LazyIterable<Character> newPrimitiveWith(char... elements)
    {
        return new CollectCharToObjectIterable<Character>(CharArrayList.newListWith(elements), BOX_CHAR);
    }

    @Test
    public void forEach()
    {
        InternalIterable<Character> select = this.newPrimitiveWith('1', '2', '3', '4', '5');
        Appendable builder = new StringBuilder();
        Procedure<Character> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Character> select = this.newPrimitiveWith('1', '2', '3', '4', '5');
        final StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex(new ObjectIntProcedure<Character>()
        {
            public void value(Character object, int index)
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
        InternalIterable<Character> select = this.newPrimitiveWith('1', '2', '3', '4', '5');
        StringBuilder builder = new StringBuilder("");
        for (Character each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Character> select = this.newPrimitiveWith('1', '2', '3', '4', '5');
        StringBuilder builder = new StringBuilder("");
        select.forEachWith(new Procedure2<Character, StringBuilder>()
        {
            public void value(Character each, StringBuilder aBuilder)
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
                FastList.<Character>newListWith('1', '2', '3', '4', '5'),
                this.newPrimitiveWith('1', '2', '3', '4', '5').selectInstancesOf(Character.class).toList());
    }
}
