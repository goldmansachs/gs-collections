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

import com.gs.collections.api.tuple.primitive.BooleanBooleanPair;
import com.gs.collections.api.tuple.primitive.BooleanBytePair;
import com.gs.collections.api.tuple.primitive.BooleanCharPair;
import com.gs.collections.api.tuple.primitive.BooleanDoublePair;
import com.gs.collections.api.tuple.primitive.BooleanFloatPair;
import com.gs.collections.api.tuple.primitive.BooleanIntPair;
import com.gs.collections.api.tuple.primitive.BooleanLongPair;
import com.gs.collections.api.tuple.primitive.BooleanObjectPair;
import com.gs.collections.api.tuple.primitive.BooleanShortPair;
import com.gs.collections.api.tuple.primitive.ByteBooleanPair;
import com.gs.collections.api.tuple.primitive.ByteBytePair;
import com.gs.collections.api.tuple.primitive.ByteCharPair;
import com.gs.collections.api.tuple.primitive.ByteDoublePair;
import com.gs.collections.api.tuple.primitive.ByteFloatPair;
import com.gs.collections.api.tuple.primitive.ByteIntPair;
import com.gs.collections.api.tuple.primitive.ByteLongPair;
import com.gs.collections.api.tuple.primitive.ByteObjectPair;
import com.gs.collections.api.tuple.primitive.ByteShortPair;
import com.gs.collections.api.tuple.primitive.CharBooleanPair;
import com.gs.collections.api.tuple.primitive.CharBytePair;
import com.gs.collections.api.tuple.primitive.CharCharPair;
import com.gs.collections.api.tuple.primitive.CharDoublePair;
import com.gs.collections.api.tuple.primitive.CharFloatPair;
import com.gs.collections.api.tuple.primitive.CharIntPair;
import com.gs.collections.api.tuple.primitive.CharLongPair;
import com.gs.collections.api.tuple.primitive.CharObjectPair;
import com.gs.collections.api.tuple.primitive.CharShortPair;
import com.gs.collections.api.tuple.primitive.DoubleBooleanPair;
import com.gs.collections.api.tuple.primitive.DoubleBytePair;
import com.gs.collections.api.tuple.primitive.DoubleCharPair;
import com.gs.collections.api.tuple.primitive.DoubleDoublePair;
import com.gs.collections.api.tuple.primitive.DoubleFloatPair;
import com.gs.collections.api.tuple.primitive.DoubleIntPair;
import com.gs.collections.api.tuple.primitive.DoubleLongPair;
import com.gs.collections.api.tuple.primitive.DoubleObjectPair;
import com.gs.collections.api.tuple.primitive.DoubleShortPair;
import com.gs.collections.api.tuple.primitive.FloatBooleanPair;
import com.gs.collections.api.tuple.primitive.FloatBytePair;
import com.gs.collections.api.tuple.primitive.FloatCharPair;
import com.gs.collections.api.tuple.primitive.FloatDoublePair;
import com.gs.collections.api.tuple.primitive.FloatFloatPair;
import com.gs.collections.api.tuple.primitive.FloatIntPair;
import com.gs.collections.api.tuple.primitive.FloatLongPair;
import com.gs.collections.api.tuple.primitive.FloatObjectPair;
import com.gs.collections.api.tuple.primitive.FloatShortPair;
import com.gs.collections.api.tuple.primitive.IntBooleanPair;
import com.gs.collections.api.tuple.primitive.IntBytePair;
import com.gs.collections.api.tuple.primitive.IntCharPair;
import com.gs.collections.api.tuple.primitive.IntDoublePair;
import com.gs.collections.api.tuple.primitive.IntFloatPair;
import com.gs.collections.api.tuple.primitive.IntIntPair;
import com.gs.collections.api.tuple.primitive.IntLongPair;
import com.gs.collections.api.tuple.primitive.IntObjectPair;
import com.gs.collections.api.tuple.primitive.IntShortPair;
import com.gs.collections.api.tuple.primitive.LongBooleanPair;
import com.gs.collections.api.tuple.primitive.LongBytePair;
import com.gs.collections.api.tuple.primitive.LongCharPair;
import com.gs.collections.api.tuple.primitive.LongDoublePair;
import com.gs.collections.api.tuple.primitive.LongFloatPair;
import com.gs.collections.api.tuple.primitive.LongIntPair;
import com.gs.collections.api.tuple.primitive.LongLongPair;
import com.gs.collections.api.tuple.primitive.LongObjectPair;
import com.gs.collections.api.tuple.primitive.LongShortPair;
import com.gs.collections.api.tuple.primitive.ObjectBooleanPair;
import com.gs.collections.api.tuple.primitive.ObjectBytePair;
import com.gs.collections.api.tuple.primitive.ObjectCharPair;
import com.gs.collections.api.tuple.primitive.ObjectDoublePair;
import com.gs.collections.api.tuple.primitive.ObjectFloatPair;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.api.tuple.primitive.ObjectLongPair;
import com.gs.collections.api.tuple.primitive.ObjectShortPair;
import com.gs.collections.api.tuple.primitive.ShortBooleanPair;
import com.gs.collections.api.tuple.primitive.ShortBytePair;
import com.gs.collections.api.tuple.primitive.ShortCharPair;
import com.gs.collections.api.tuple.primitive.ShortDoublePair;
import com.gs.collections.api.tuple.primitive.ShortFloatPair;
import com.gs.collections.api.tuple.primitive.ShortIntPair;
import com.gs.collections.api.tuple.primitive.ShortLongPair;
import com.gs.collections.api.tuple.primitive.ShortObjectPair;
import com.gs.collections.api.tuple.primitive.ShortShortPair;

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

    public static IntIntPair pair(int one, int two)
    {
        return new IntIntPairImpl(one, two);
    }

    public static IntFloatPair pair(int one, float two)
    {
        return new IntFloatPairImpl(one, two);
    }

    public static IntDoublePair pair(int one, double two)
    {
        return new IntDoublePairImpl(one, two);
    }

    public static IntLongPair pair(int one, long two)
    {
        return new IntLongPairImpl(one, two);
    }

    public static IntShortPair pair(int one, short two)
    {
        return new IntShortPairImpl(one, two);
    }

    public static IntBytePair pair(int one, byte two)
    {
        return new IntBytePairImpl(one, two);
    }

    public static IntCharPair pair(int one, char two)
    {
        return new IntCharPairImpl(one, two);
    }

    public static IntBooleanPair pair(int one, boolean two)
    {
        return new IntBooleanPairImpl(one, two);
    }

    public static FloatIntPair pair(float one, int two)
    {
        return new FloatIntPairImpl(one, two);
    }

    public static FloatFloatPair pair(float one, float two)
    {
        return new FloatFloatPairImpl(one, two);
    }

    public static FloatDoublePair pair(float one, double two)
    {
        return new FloatDoublePairImpl(one, two);
    }

    public static FloatLongPair pair(float one, long two)
    {
        return new FloatLongPairImpl(one, two);
    }

    public static FloatShortPair pair(float one, short two)
    {
        return new FloatShortPairImpl(one, two);
    }

    public static FloatBytePair pair(float one, byte two)
    {
        return new FloatBytePairImpl(one, two);
    }

    public static FloatCharPair pair(float one, char two)
    {
        return new FloatCharPairImpl(one, two);
    }

    public static FloatBooleanPair pair(float one, boolean two)
    {
        return new FloatBooleanPairImpl(one, two);
    }

    public static DoubleIntPair pair(double one, int two)
    {
        return new DoubleIntPairImpl(one, two);
    }

    public static DoubleFloatPair pair(double one, float two)
    {
        return new DoubleFloatPairImpl(one, two);
    }

    public static DoubleDoublePair pair(double one, double two)
    {
        return new DoubleDoublePairImpl(one, two);
    }

    public static DoubleLongPair pair(double one, long two)
    {
        return new DoubleLongPairImpl(one, two);
    }

    public static DoubleShortPair pair(double one, short two)
    {
        return new DoubleShortPairImpl(one, two);
    }

    public static DoubleBytePair pair(double one, byte two)
    {
        return new DoubleBytePairImpl(one, two);
    }

    public static DoubleCharPair pair(double one, char two)
    {
        return new DoubleCharPairImpl(one, two);
    }

    public static DoubleBooleanPair pair(double one, boolean two)
    {
        return new DoubleBooleanPairImpl(one, two);
    }

    public static LongIntPair pair(long one, int two)
    {
        return new LongIntPairImpl(one, two);
    }

    public static LongFloatPair pair(long one, float two)
    {
        return new LongFloatPairImpl(one, two);
    }

    public static LongDoublePair pair(long one, double two)
    {
        return new LongDoublePairImpl(one, two);
    }

    public static LongLongPair pair(long one, long two)
    {
        return new LongLongPairImpl(one, two);
    }

    public static LongShortPair pair(long one, short two)
    {
        return new LongShortPairImpl(one, two);
    }

    public static LongBytePair pair(long one, byte two)
    {
        return new LongBytePairImpl(one, two);
    }

    public static LongCharPair pair(long one, char two)
    {
        return new LongCharPairImpl(one, two);
    }

    public static LongBooleanPair pair(long one, boolean two)
    {
        return new LongBooleanPairImpl(one, two);
    }

    public static ShortIntPair pair(short one, int two)
    {
        return new ShortIntPairImpl(one, two);
    }

    public static ShortFloatPair pair(short one, float two)
    {
        return new ShortFloatPairImpl(one, two);
    }

    public static ShortDoublePair pair(short one, double two)
    {
        return new ShortDoublePairImpl(one, two);
    }

    public static ShortLongPair pair(short one, long two)
    {
        return new ShortLongPairImpl(one, two);
    }

    public static ShortShortPair pair(short one, short two)
    {
        return new ShortShortPairImpl(one, two);
    }

    public static ShortBytePair pair(short one, byte two)
    {
        return new ShortBytePairImpl(one, two);
    }

    public static ShortCharPair pair(short one, char two)
    {
        return new ShortCharPairImpl(one, two);
    }

    public static ShortBooleanPair pair(short one, boolean two)
    {
        return new ShortBooleanPairImpl(one, two);
    }

    public static ByteIntPair pair(byte one, int two)
    {
        return new ByteIntPairImpl(one, two);
    }

    public static ByteFloatPair pair(byte one, float two)
    {
        return new ByteFloatPairImpl(one, two);
    }

    public static ByteDoublePair pair(byte one, double two)
    {
        return new ByteDoublePairImpl(one, two);
    }

    public static ByteLongPair pair(byte one, long two)
    {
        return new ByteLongPairImpl(one, two);
    }

    public static ByteShortPair pair(byte one, short two)
    {
        return new ByteShortPairImpl(one, two);
    }

    public static ByteBytePair pair(byte one, byte two)
    {
        return new ByteBytePairImpl(one, two);
    }

    public static ByteCharPair pair(byte one, char two)
    {
        return new ByteCharPairImpl(one, two);
    }

    public static ByteBooleanPair pair(byte one, boolean two)
    {
        return new ByteBooleanPairImpl(one, two);
    }

    public static CharIntPair pair(char one, int two)
    {
        return new CharIntPairImpl(one, two);
    }

    public static CharFloatPair pair(char one, float two)
    {
        return new CharFloatPairImpl(one, two);
    }

    public static CharDoublePair pair(char one, double two)
    {
        return new CharDoublePairImpl(one, two);
    }

    public static CharLongPair pair(char one, long two)
    {
        return new CharLongPairImpl(one, two);
    }

    public static CharShortPair pair(char one, short two)
    {
        return new CharShortPairImpl(one, two);
    }

    public static CharBytePair pair(char one, byte two)
    {
        return new CharBytePairImpl(one, two);
    }

    public static CharCharPair pair(char one, char two)
    {
        return new CharCharPairImpl(one, two);
    }

    public static CharBooleanPair pair(char one, boolean two)
    {
        return new CharBooleanPairImpl(one, two);
    }

    public static BooleanIntPair pair(boolean one, int two)
    {
        return new BooleanIntPairImpl(one, two);
    }

    public static BooleanFloatPair pair(boolean one, float two)
    {
        return new BooleanFloatPairImpl(one, two);
    }

    public static BooleanDoublePair pair(boolean one, double two)
    {
        return new BooleanDoublePairImpl(one, two);
    }

    public static BooleanLongPair pair(boolean one, long two)
    {
        return new BooleanLongPairImpl(one, two);
    }

    public static BooleanShortPair pair(boolean one, short two)
    {
        return new BooleanShortPairImpl(one, two);
    }

    public static BooleanBytePair pair(boolean one, byte two)
    {
        return new BooleanBytePairImpl(one, two);
    }

    public static BooleanCharPair pair(boolean one, char two)
    {
        return new BooleanCharPairImpl(one, two);
    }

    public static BooleanBooleanPair pair(boolean one, boolean two)
    {
        return new BooleanBooleanPairImpl(one, two);
    }
}
