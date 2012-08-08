/*
 * Copyright 2011 Goldman Sachs.
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

package com.webguys.ponzu.impl.map.immutable;

import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.map.ImmutableMap;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.math.IntegerSum;
import com.webguys.ponzu.impl.math.Sum;
import com.webguys.ponzu.impl.parallel.BatchIterable;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableUnifiedMapTest extends ImmutableMapTestCase
{
    @Override
    protected ImmutableMap<Integer, String> classUnderTest()
    {
        return new ImmutableUnifiedMap<Integer, String>(UnifiedMap.newWithKeysValues(1, "1", 2, "2", 3, "3", 4, "4"));
    }

    @Override
    protected int size()
    {
        return 4;
    }

    @Test
    @Override
    public void testToString()
    {
        Assert.assertEquals("{1=1, 2=2, 3=3, 4=4}", this.classUnderTest().toString());
    }

    @Test
    public void getBatchCount()
    {
        BatchIterable<Integer> integerBatchIterable = (BatchIterable<Integer>) this.classUnderTest();
        Assert.assertEquals(5, integerBatchIterable.getBatchCount(3));
    }

    @Test
    public void batchForEach()
    {
        final Sum sum = new IntegerSum(0);
        BatchIterable<String> integerBatchIterable = (BatchIterable<String>) this.classUnderTest();
        integerBatchIterable.batchForEach(new Procedure<String>()
        {
            public void value(String each)
            {
                sum.add(Integer.valueOf(each));
            }
        }, 0, 1);
        Assert.assertEquals(10, sum.getValue());
    }
}
