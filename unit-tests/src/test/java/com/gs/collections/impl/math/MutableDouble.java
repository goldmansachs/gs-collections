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

public final class MutableDouble extends Number implements Comparable<MutableDouble>
{
    private static final long serialVersionUID = 1L;
    private double value = 0.0;

    public MutableDouble(double value)
    {
        this.value = value;
    }

    public MutableDouble()
    {
        this(0.0);
    }

    @Override
    public int compareTo(MutableDouble other)
    {
        return Double.compare(this.value, other.value);
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
        return Double.compare(((MutableDouble) other).value, this.value) == 0;
    }

    @Override
    public int hashCode()
    {
        long temp = Double.doubleToLongBits(this.value);
        return (int) (temp ^ (temp >>> 32));
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public MutableDouble add(double number)
    {
        this.value += number;
        return this;
    }

    public MutableDouble subtract(double number)
    {
        this.value -= number;
        return this;
    }

    public MutableDouble multiply(double number)
    {
        this.value *= number;
        return this;
    }

    public MutableDouble divide(double number)
    {
        this.value /= number;
        return this;
    }

    public MutableDouble min(double number)
    {
        this.value = Math.min(this.value, number);
        return this;
    }

    public MutableDouble max(double number)
    {
        this.value = Math.max(this.value, number);
        return this;
    }

    public MutableDouble abs()
    {
        this.value = Math.abs(this.value);
        return this;
    }

    public Double toDouble()
    {
        return Double.valueOf(this.value);
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
        return this.value;
    }

    @Override
    public String toString()
    {
        return "MutableDouble{value=" + this.value + '}';
    }
}
