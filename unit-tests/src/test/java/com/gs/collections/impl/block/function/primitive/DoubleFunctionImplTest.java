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

package com.gs.collections.impl.block.function.primitive;

import org.junit.Assert;
import org.junit.Test;

public final class DoubleFunctionImplTest
{
    private static final Object JUNK = new Object();

    @Test
    public void testValueOf()
    {
        Assert.assertSame(new TestDoubleFunctionImpl(0.0d).valueOf(JUNK), new TestDoubleFunctionImpl(0.0d).valueOf(JUNK));
        Assert.assertEquals(Double.valueOf(1.0d), new TestDoubleFunctionImpl(1.0d).valueOf(JUNK));
    }

    private static final class TestDoubleFunctionImpl extends DoubleFunctionImpl<Object>
    {
        private static final long serialVersionUID = 1L;
        private final double toReturn;

        private TestDoubleFunctionImpl(double toReturn)
        {
            this.toReturn = toReturn;
        }

        @Override
        public double doubleValueOf(Object anObject)
        {
            return this.toReturn;
        }
    }
}
