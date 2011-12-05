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

package com.gs.collections.impl.test;

import org.junit.Test;

public class AssertNotEqualsTest
{
    private static final double DOUBLE_DELTA = 0.5d;
    private static final float FLOAT_DELTA = 0.5f;

    @Test
    public void objectObject()
    {
        Verify.assertNotEquals(new Object(), new Object());
    }

    @Test
    public void stringObjectObject()
    {
        Verify.assertNotEquals("", new Object(), new Object());
    }

    @Test
    public void stringString()
    {
        Verify.assertNotEquals("1", "2");
        Verify.assertNotEquals(null, "2");
        Verify.assertNotEquals("1", null);
    }

    @Test
    public void stringStringString()
    {
        Verify.assertNotEquals("", "1", "2");
    }

    @Test
    public void doubleDoubleDouble()
    {
        Verify.assertNotEquals(1.0d, 2.0d, DOUBLE_DELTA);
        Verify.assertNotEquals(1.0d, Double.POSITIVE_INFINITY, DOUBLE_DELTA);
        Verify.assertNotEquals(Double.POSITIVE_INFINITY, 2.0d, DOUBLE_DELTA);
        Verify.assertNotEquals(1.0d, Double.NaN, DOUBLE_DELTA);
        Verify.assertNotEquals(Double.NaN, Double.NEGATIVE_INFINITY, DOUBLE_DELTA);
        Verify.assertNotEquals(Double.NaN, Double.NaN, DOUBLE_DELTA);
        Verify.assertNotEquals(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, DOUBLE_DELTA);
    }

    @Test
    public void stringDoubleDoubleDouble()
    {
        Verify.assertNotEquals("", 1.0d, 2.0d, DOUBLE_DELTA);
    }

    @Test
    public void floatFloatFloat()
    {
        Verify.assertNotEquals(1.0f, 2.0f, FLOAT_DELTA);
        Verify.assertNotEquals(1.0f, Float.POSITIVE_INFINITY, FLOAT_DELTA);
        Verify.assertNotEquals(Float.POSITIVE_INFINITY, 2.0f, FLOAT_DELTA);
        Verify.assertNotEquals(1.0f, Float.NaN, FLOAT_DELTA);
        Verify.assertNotEquals(Float.NaN, Float.NEGATIVE_INFINITY, FLOAT_DELTA);
        Verify.assertNotEquals(Float.NaN, Float.NaN, FLOAT_DELTA);
        Verify.assertNotEquals(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, FLOAT_DELTA);
    }

    @Test
    public void stringFloatFloatFloat()
    {
        Verify.assertNotEquals("", 1.0f, 2.0f, FLOAT_DELTA);
    }

    @Test
    public void longLong()
    {
        Verify.assertNotEquals(1L, 2L);
    }

    @Test
    public void stringLongLong()
    {
        Verify.assertNotEquals("", 1L, 2L);
    }

    @Test
    public void booleanBoolean()
    {
        Verify.assertNotEquals(true, false);
    }

    @Test
    public void stringBooleanBoolean()
    {
        Verify.assertNotEquals("", true, false);
    }

    @Test
    public void byteByte()
    {
        Verify.assertNotEquals((byte) 1, (byte) 2);
    }

    @Test
    public void stringByteByte()
    {
        Verify.assertNotEquals("", (byte) 1, (byte) 2);
    }

    @Test
    public void charChar()
    {
        Verify.assertNotEquals('1', '2');
    }

    @Test
    public void stringCharChar()
    {
        Verify.assertNotEquals("", '1', '2');
    }

    @Test
    public void shortShort()
    {
        Verify.assertNotEquals((short) 1, (short) 2);
    }

    @Test
    public void stringShortShort()
    {
        Verify.assertNotEquals("", (short) 1, (short) 2);
    }

    @Test
    public void intInt()
    {
        Verify.assertNotEquals(1, 2);
    }

    @Test
    public void stringIntInt()
    {
        Verify.assertNotEquals("", 1, 2);
    }
}
