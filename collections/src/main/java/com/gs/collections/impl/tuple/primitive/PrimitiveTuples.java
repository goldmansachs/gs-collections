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

package com.gs.collections.impl.tuple.primitive;

import com.gs.collections.api.tuple.primitive.BooleanObjectPair;
import com.gs.collections.api.tuple.primitive.ByteObjectPair;
import com.gs.collections.api.tuple.primitive.CharObjectPair;
import com.gs.collections.api.tuple.primitive.DoubleObjectPair;
import com.gs.collections.api.tuple.primitive.FloatObjectPair;
import com.gs.collections.api.tuple.primitive.IntObjectPair;
import com.gs.collections.api.tuple.primitive.LongObjectPair;
import com.gs.collections.api.tuple.primitive.ObjectBooleanPair;
import com.gs.collections.api.tuple.primitive.ObjectBytePair;
import com.gs.collections.api.tuple.primitive.ObjectCharPair;
import com.gs.collections.api.tuple.primitive.ObjectDoublePair;
import com.gs.collections.api.tuple.primitive.ObjectFloatPair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.api.tuple.primitive.ObjectLongPair;
import com.gs.collections.api.tuple.primitive.ObjectShortPair;
import com.gs.collections.api.tuple.primitive.ShortObjectPair;

public final class PrimitiveTuples
{
    private PrimitiveTuples()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> ObjectBooleanPair<T> pair(T one, boolean two)
    {
        return new ObjectBooleanPairImpl<T>(one, two);
    }

    public static <T> ObjectBytePair<T> pair(T one, byte two)
    {
        return new ObjectBytePairImpl<T>(one, two);
    }

    public static <T> ObjectCharPair<T> pair(T one, char two)
    {
        return new ObjectCharPairImpl<T>(one, two);
    }

    public static <T> ObjectShortPair<T> pair(T one, short two)
    {
        return new ObjectShortPairImpl<T>(one, two);
    }

    public static <T> ObjectIntPair<T> pair(T one, int two)
    {
        return new ObjectIntPairImpl<T>(one, two);
    }

    public static <T> ObjectFloatPair<T> pair(T one, float two)
    {
        return new ObjectFloatPairImpl<T>(one, two);
    }

    public static <T> ObjectLongPair<T> pair(T one, long two)
    {
        return new ObjectLongPairImpl<T>(one, two);
    }

    public static <T> ObjectDoublePair<T> pair(T one, double two)
    {
        return new ObjectDoublePairImpl<T>(one, two);
    }

    public static <T> BooleanObjectPair<T> pair(boolean one, T two)
    {
        return new BooleanObjectPairImpl<T>(one, two);
    }

    public static <T> ByteObjectPair<T> pair(byte one, T two)
    {
        return new ByteObjectPairImpl<T>(one, two);
    }

    public static <T> CharObjectPair<T> pair(char one, T two)
    {
        return new CharObjectPairImpl<T>(one, two);
    }

    public static <T> ShortObjectPair<T> pair(short one, T two)
    {
        return new ShortObjectPairImpl<T>(one, two);
    }

    public static <T> IntObjectPair<T> pair(int one, T two)
    {
        return new IntObjectPairImpl<T>(one, two);
    }

    public static <T> FloatObjectPair<T> pair(float one, T two)
    {
        return new FloatObjectPairImpl<T>(one, two);
    }

    public static <T> LongObjectPair<T> pair(long one, T two)
    {
        return new LongObjectPairImpl<T>(one, two);
    }

    public static <T> DoubleObjectPair<T> pair(double one, T two)
    {
        return new DoubleObjectPairImpl<T>(one, two);
    }
}
