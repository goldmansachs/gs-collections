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

public class MutableInteger extends Number implements Comparable<MutableInteger>
{
    private static final long serialVersionUID = 1L;
    private int value = 0;

    public MutableInteger(int value)
    {
        this.value = value;
    }

    public MutableInteger()
    {
        this(0);
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
        return this.value == ((MutableInteger) other).value;
    }

    @Override
    public int hashCode()
    {
        return this.value;
    }

    @Override
    public int compareTo(MutableInteger other)
    {
        return Integer.compare(this.value, other.value);
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public MutableInteger add(int number)
    {
        this.value += number;
        return this;
    }

    public MutableInteger subtract(int number)
    {
        this.value -= number;
        return this;
    }

    public MutableInteger multiply(int number)
    {
        this.value *= number;
        return this;
    }

    public MutableInteger divide(int number)
    {
        this.value /= number;
        return this;
    }

    public MutableInteger min(int number)
    {
        this.value = Math.min(this.value, number);
        return this;
    }

    public MutableInteger max(int number)
    {
        this.value = Math.max(this.value, number);
        return this;
    }

    public MutableInteger abs()
    {
        this.value = Math.abs(this.value);
        return this;
    }

    public Integer toInteger()
    {
        return Integer.valueOf(this.value);
    }

    @Override
    public int intValue()
    {
        return this.value;
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
    public String toString()
    {
        return "MutableInteger{value=" + this.value + '}';
    }
}
