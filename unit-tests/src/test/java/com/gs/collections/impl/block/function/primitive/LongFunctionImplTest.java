/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.block.function.primitive;

import org.junit.Assert;
import org.junit.Test;

/**
 * Junit test for {@link LongFunctionImpl}.
 */
public class LongFunctionImplTest
{
    @Test
    public void valueOf()
    {
        LongFunctionImpl<Long> longFunction = new LongFunctionImpl<Long>()
        {
            public long longValueOf(Long each)
            {
                return each.longValue();
            }
        };

        Assert.assertEquals(1L, longFunction.longValueOf(1L));
        Assert.assertEquals(1L, longFunction.valueOf(1L).longValue());
    }
}
