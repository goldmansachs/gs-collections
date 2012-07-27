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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;

public final class PrimitiveFunctions
{
    private static final UnboxNumberToInt UNBOX_NUMBER_TO_INT = new UnboxNumberToInt();
    private static final UnboxIntegerToInt UNBOX_INTEGER_TO_INT = new UnboxIntegerToInt();
    private static final UnboxIntegerToFloat UNBOX_INTEGER_TO_FLOAT = new UnboxIntegerToFloat();
    private static final UnboxIntegerToLong UNBOX_INTEGER_TO_LONG = new UnboxIntegerToLong();
    private static final UnboxIntegerToDouble UNBOX_INTEGER_TO_DOUBLE = new UnboxIntegerToDouble();

    private PrimitiveFunctions()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static IntFunction<Number> unboxNumberToInt()
    {
        return UNBOX_NUMBER_TO_INT;
    }

    public static IntFunction<Integer> unboxIntegerToInt()
    {
        return UNBOX_INTEGER_TO_INT;
    }

    public static FloatFunction<Integer> unboxIntegerToFloat()
    {
        return UNBOX_INTEGER_TO_FLOAT;
    }

    public static LongFunction<Integer> unboxIntegerToLong()
    {
        return UNBOX_INTEGER_TO_LONG;
    }

    public static DoubleFunction<Integer> unboxIntegerToDouble()
    {
        return UNBOX_INTEGER_TO_DOUBLE;
    }

    private static class UnboxNumberToInt
            implements IntFunction<Number>
    {
        private static final long serialVersionUID = 6510193995011010233L;

        public int intValueOf(Number number)
        {
            return number.intValue();
        }
    }

    private static class UnboxIntegerToInt
            implements IntFunction<Integer>
    {
        private static final long serialVersionUID = -2237117769427976064L;

        public int intValueOf(Integer integer)
        {
            return integer;
        }
    }

    private static class UnboxIntegerToFloat
            implements FloatFunction<Integer>
    {
        private static final long serialVersionUID = 1677416777147190512L;

        public float floatValueOf(Integer integer)
        {
            return integer;
        }
    }

    private static class UnboxIntegerToLong
            implements LongFunction<Integer>
    {
        private static final long serialVersionUID = -2245531753693069446L;

        public long longValueOf(Integer integer)
        {
            return integer;
        }
    }

    private static class UnboxIntegerToDouble
            implements DoubleFunction<Integer>
    {
        private static final long serialVersionUID = -4676486269281421901L;

        public double doubleValueOf(Integer integer)
        {
            return integer;
        }
    }
}
