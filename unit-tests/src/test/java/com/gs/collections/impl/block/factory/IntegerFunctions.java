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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;

public final class IntegerFunctions
{
    public static final ByteFunction<Integer> TO_BYTE = new ByteFunction<Integer>()
    {
        public byte byteValueOf(Integer anObject)
        {
            return anObject.byteValue();
        }
    };
    public static final CharFunction<Integer> TO_CHAR = new CharFunction<Integer>()
    {
        public char charValueOf(Integer anObject)
        {
            return (char) anObject.intValue();
        }
    };
    public static final DoubleFunction<Integer> TO_DOUBLE = new DoubleFunction<Integer>()
    {
        public double doubleValueOf(Integer anObject)
        {
            return anObject.doubleValue();
        }
    };
    public static final FloatFunction<Integer> TO_FLOAT = new FloatFunction<Integer>()
    {
        public float floatValueOf(Integer anObject)
        {
            return anObject.floatValue();
        }
    };
    public static final IntFunction<Integer> TO_INT = new IntFunction<Integer>()
    {
        public int intValueOf(Integer anObject)
        {
            return anObject.intValue();
        }
    };
    public static final LongFunction<Integer> TO_LONG = new LongFunction<Integer>()
    {
        public long longValueOf(Integer anObject)
        {
            return anObject.longValue();
        }
    };
    public static final ShortFunction<Integer> TO_SHORT = new ShortFunction<Integer>()
    {
        public short shortValueOf(Integer anObject)
        {
            return anObject.shortValue();
        }
    };
    public static final BooleanFunction<Integer> TO_IS_EVEN = new BooleanFunction<Integer>()
    {
        public boolean booleanValueOf(Integer anObject)
        {
            return anObject.intValue() % 2 == 0;
        }
    };

    private IntegerFunctions()
    {
    }
}
