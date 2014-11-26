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

public final class MutableFloat extends Number implements Comparable<MutableFloat>
{
    private static final long serialVersionUID = 1L;
    private float value = 0.0f;

    public MutableFloat(float value)
    {
        this.value = value;
    }

    public MutableFloat()
    {
        this(0.0f);
    }

    @Override
    public int compareTo(MutableFloat other)
    {
        return Float.compare(this.value, other.value);
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
        return Float.compare(((MutableFloat) other).value, this.value) == 0;
    }

    @SuppressWarnings("UnaryPlus")
    @Override
    public int hashCode()
    {
        return this.value == +0.0f ? 0 : Float.floatToIntBits(this.value);
    }

    public void setValue(float value)
    {
        this.value = value;
    }

    public MutableFloat add(float number)
    {
        this.value += number;
        return this;
    }

    public MutableFloat subtract(float number)
    {
        this.value -= number;
        return this;
    }

    public MutableFloat multiply(float number)
    {
        this.value *= number;
        return this;
    }

    public MutableFloat divide(float number)
    {
        this.value /= number;
        return this;
    }

    public MutableFloat min(float number)
    {
        this.value = Math.min(this.value, number);
        return this;
    }

    public MutableFloat max(float number)
    {
        this.value = Math.max(this.value, number);
        return this;
    }

    public MutableFloat abs()
    {
        this.value = Math.abs(this.value);
        return this;
    }

    public Float toFloat()
    {
        return Float.valueOf(this.value);
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
        return this.value;
    }

    @Override
    public double doubleValue()
    {
        return (double) this.value;
    }

    @Override
    public String toString()
    {
        return "MutableFloat{value=" + this.value + '}';
    }
}
