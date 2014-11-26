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

public final class MutableLong extends Number implements Comparable<MutableLong>
{
    private static final long serialVersionUID = 1L;
    private long value = 0L;

    public MutableLong(long value)
    {
        this.value = value;
    }

    public MutableLong()
    {
        this(0L);
    }

    @Override
    public int compareTo(MutableLong other)
    {
        return Long.compare(this.value, other.value);
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
        return this.value == ((MutableLong) other).value;
    }

    @Override
    public int hashCode()
    {
        return (int) (this.value ^ (this.value >>> 32));
    }

    public void setValue(long value)
    {
        this.value = value;
    }

    public MutableLong add(double number)
    {
        this.value += number;
        return this;
    }

    public MutableLong subtract(double number)
    {
        this.value -= number;
        return this;
    }

    public MutableLong multiply(double number)
    {
        this.value *= number;
        return this;
    }

    public MutableLong divide(double number)
    {
        this.value /= number;
        return this;
    }

    public MutableLong min(long number)
    {
        this.value = Math.min(this.value, number);
        return this;
    }

    public MutableLong max(long number)
    {
        this.value = Math.max(this.value, number);
        return this;
    }

    public MutableLong abs()
    {
        this.value = Math.abs(this.value);
        return this;
    }

    public Long toLong()
    {
        return Long.valueOf(this.value);
    }

    @Override
    public int intValue()
    {
        return (int) this.value;
    }

    @Override
    public long longValue()
    {
        return this.value;
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
    public String toString()
    {
        return "MutableLong{value=" + this.value + '}';
    }
}
