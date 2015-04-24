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

package com.gs.collections.impl.block.function;

import org.junit.Assert;
import org.junit.Test;

/**
 * Junit test for {@link SubtractFunction}.
 */
public class SubtractFunctionTest
{
    @Test
    public void subtractIntegerFunction()
    {
        Assert.assertEquals(Integer.valueOf(1), SubtractFunction.INTEGER.value(2, 1));
        Assert.assertEquals(Integer.valueOf(0), SubtractFunction.INTEGER.value(1, 1));
        Assert.assertEquals(Integer.valueOf(-1), SubtractFunction.INTEGER.value(1, 2));
    }

    @Test
    public void subtractDoubleFunction()
    {
        Assert.assertEquals(Double.valueOf(0.5), SubtractFunction.DOUBLE.value(2.0, 1.5));
        Assert.assertEquals(Double.valueOf(0), SubtractFunction.DOUBLE.value(2.0, 2.0));
        Assert.assertEquals(Double.valueOf(-0.5), SubtractFunction.DOUBLE.value(1.5, 2.0));
    }

    @Test
    public void subtractLongFunction()
    {
        Assert.assertEquals(Long.valueOf(1L), SubtractFunction.LONG.value(2L, 1L));
        Assert.assertEquals(Long.valueOf(0L), SubtractFunction.LONG.value(1L, 1L));
        Assert.assertEquals(Long.valueOf(-1L), SubtractFunction.LONG.value(1L, 2L));
    }
}
