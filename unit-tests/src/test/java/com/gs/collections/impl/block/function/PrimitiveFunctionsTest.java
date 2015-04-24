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

import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.mutable.primitive.DoubleHashSet;
import com.gs.collections.impl.set.mutable.primitive.FloatHashSet;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import com.gs.collections.impl.set.mutable.primitive.LongHashSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * Junit test for {@link PrimitiveFunctions}.
 */
public class PrimitiveFunctionsTest
{
    @Test
    public void unboxNumberToInt()
    {
        Assert.assertEquals(
                IntHashSet.newSetWith(1, 2, 3),
                UnifiedSet.newSetWith(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)).collectInt(PrimitiveFunctions.unboxNumberToInt()));

        Assert.assertEquals(
                IntHashSet.newSetWith(1, 2, 3),
                UnifiedSet.newSetWith(1.1, 2.2, 3.3).collectInt(PrimitiveFunctions.unboxNumberToInt()));
    }

    @Test
    public void unboxNumberToFloat()
    {
        Assert.assertEquals(
                FloatHashSet.newSetWith(1.0f, 2.0f, 3.0f),
                UnifiedSet.newSetWith(1, 2, 3).collectFloat(PrimitiveFunctions.unboxNumberToFloat()));
    }

    @Test
    public void unboxNumberToLong()
    {
        Assert.assertEquals(
                LongHashSet.newSetWith(1L, 2L, 3L),
                UnifiedSet.newSetWith(1, 2, 3).collectLong(PrimitiveFunctions.unboxNumberToLong()));
    }

    @Test
    public void unboxNumberToDouble()
    {
        Assert.assertEquals(
                DoubleHashSet.newSetWith(1.0, 2.0, 3.0),
                UnifiedSet.newSetWith(1, 2, 3).collectDouble(PrimitiveFunctions.unboxNumberToDouble()));
    }

    @Test
    public void unboxDoubleToDouble()
    {
        Assert.assertEquals(
                DoubleHashSet.newSetWith(1.0, 2.0, 3.0),
                UnifiedSet.newSetWith(Double.valueOf(1.0), Double.valueOf(2.0), Double.valueOf(3.0)).collectDouble(PrimitiveFunctions.unboxDoubleToDouble()));
    }

    @Test
    public void unboxFloatToFloat()
    {
        Assert.assertEquals(
                FloatHashSet.newSetWith(1.0f, 2.0f, 3.0f),
                UnifiedSet.newSetWith(Float.valueOf(1.0f), Float.valueOf(2.0f), Float.valueOf(3.0f)).collectFloat(PrimitiveFunctions.unboxFloatToFloat()));
    }
}
