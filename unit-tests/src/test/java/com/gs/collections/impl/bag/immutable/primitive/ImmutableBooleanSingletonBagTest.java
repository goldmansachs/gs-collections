/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.bag.immutable.primitive;

import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.BooleanIntProcedure;
import com.gs.collections.impl.factory.primitive.BooleanBags;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableBooleanSingletonBag}.
 */
public class ImmutableBooleanSingletonBagTest extends AbstractImmutableBooleanBagTestCase
{
    @Override
    protected final ImmutableBooleanBag classUnderTest()
    {
        return BooleanBags.immutable.of(true);
    }

    @Override
    @Test
    public void forEachWithOccurrences()
    {
        final StringBuilder stringBuilder = new StringBuilder();
        this.classUnderTest().forEachWithOccurrences(new BooleanIntProcedure()
        {
            public void value(boolean argument1, int argument2)
            {
                stringBuilder.append(argument1).append(argument2);
            }
        });
        String string = stringBuilder.toString();
        Assert.assertEquals("true1", string);
    }

    @Override
    @Test
    public void size()
    {
        Verify.assertSize(1, this.classUnderTest());
    }

    @Test
    public void injectInto()
    {
        ImmutableBooleanSingletonBag hashBag = new ImmutableBooleanSingletonBag(true);
        Integer total = hashBag.injectInto(Integer.valueOf(0), new ObjectBooleanToObjectFunction<Integer, Integer>()
        {
            public Integer valueOf(Integer result, boolean value)
            {
                if (value)
                {
                    return result += 2;
                }

                return result;
            }
        });
        Assert.assertEquals(Integer.valueOf(2), total);
    }
}
