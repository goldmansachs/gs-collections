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
import org.junit.Assert;
import org.junit.Test;

public class ImmutableBooleanHashBagTest extends AbstractImmutableBooleanBagTestCase
{
    @Override
    protected ImmutableBooleanBag classUnderTest()
    {
        return ImmutableBooleanHashBag.newBagWith(true, false, true);
    }

    @Test
    public void injectInto()
    {
        ImmutableBooleanHashBag hashBag = ImmutableBooleanHashBag.newBagWith(true, true, true, false, false, true);
        Integer total = hashBag.injectInto(Integer.valueOf(2), new ObjectBooleanToObjectFunction<Integer, Integer>()
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
        Assert.assertEquals(Integer.valueOf(10), total);
    }
}
