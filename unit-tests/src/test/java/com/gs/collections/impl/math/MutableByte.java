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

package com.gs.collections.impl.math;

public class MutableByte extends Number implements Comparable<MutableByte>
{
    private static final long serialVersionUID = 1L;
    private byte value = 0;

    public MutableByte(byte value)
    {
        this.value = value;
    }

    public MutableByte()
    {
        this((byte) 0);
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (other == null || this.getClass() != other.getClass())
        {
            return false;
        }
        return this.value == ((MutableByte) other).value;
    }

    @Override
    public int hashCode()
    {
        return this.intValue();
    }

    @Override
    public int compareTo(MutableByte other)
    {
        return Byte.compare(this.value, other.value);
    }

    public void setValue(byte value)
    {
        this.value = value;
    }

    public MutableByte add(byte number)
    {
        this.value += number;
        return this;
    }

    public MutableByte subtract(byte number)
    {
        this.value -= number;
        return this;
    }

    public MutableByte multiply(byte number)
    {
        this.value *= number;
        return this;
    }

    public MutableByte divide(byte number)
    {
        this.value /= number;
        return this;
    }

    public MutableByte min(byte number)
    {
        this.value = (byte) Math.min(this.intValue(), (int) number);
        return this;
    }

    public MutableByte max(byte number)
    {
        this.value = (byte) Math.max(this.intValue(), (int) number);
        return this;
    }

    public MutableByte abs()
    {
        this.value = (byte) Math.abs(this.intValue());
        return this;
    }

    public Byte toByte()
    {
        return Byte.valueOf(this.value);
    }

    @Override
    public int intValue()
    {
        return (int) this.value;
    }

    @Override
    public long longValue()
    {
        return (long) this.value;
    }

    @Override
    public float floatValue()
    {
        return (float) this.value;
    }

    @Override
    public double doubleValue()
    {
        return (double) this.value;
    }

    @Override
    public byte byteValue()
    {
        return this.value;
    }

    @Override
    public String toString()
    {
        return "MutableByte{value=" + this.value + '}';
    }
}
