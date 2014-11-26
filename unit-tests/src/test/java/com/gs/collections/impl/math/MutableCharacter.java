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

import java.io.Serializable;

public class MutableCharacter implements Comparable<MutableCharacter>, Serializable
{
    private static final long serialVersionUID = 1L;
    private char value = 0;

    public MutableCharacter(char value)
    {
        this.value = value;
    }

    public MutableCharacter()
    {
        this((char) 0);
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
        return this.value == ((MutableCharacter) other).value;
    }

    @Override
    public int hashCode()
    {
        return this.intValue();
    }

    @Override
    public int compareTo(MutableCharacter other)
    {
        return Character.compare(this.value, other.value);
    }

    public void setValue(char value)
    {
        this.value = value;
    }

    @Override
    protected MutableCharacter clone()
    {
        try
        {
            MutableCharacter cloned = (MutableCharacter) super.clone();
            cloned.value = this.value;
            return cloned;
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }

    public MutableCharacter add(char number)
    {
        this.value += number;
        return this;
    }

    public MutableCharacter subtract(char number)
    {
        this.value -= number;
        return this;
    }

    public MutableCharacter multiply(char number)
    {
        this.value *= number;
        return this;
    }

    public MutableCharacter divide(char number)
    {
        this.value /= number;
        return this;
    }

    public MutableCharacter min(char number)
    {
        this.value = (char) Math.min(this.intValue(), (int) number);
        return this;
    }

    public MutableCharacter max(char number)
    {
        this.value = (char) Math.max(this.intValue(), (int) number);
        return this;
    }

    public MutableCharacter abs()
    {
        this.value = (char) Math.abs(this.intValue());
        return this;
    }

    public Character toCharacter()
    {
        return Character.valueOf(this.value);
    }

    public int intValue()
    {
        return (int) this.value;
    }

    public short shortValue()
    {
        return (short) this.value;
    }

    public byte byteValue()
    {
        return (byte) this.value;
    }

    public long longValue()
    {
        return (long) this.value;
    }

    public float floatValue()
    {
        return (float) this.value;
    }

    public double doubleValue()
    {
        return (double) this.value;
    }

    @Override
    public String toString()
    {
        return "MutableCharacter{value=" + this.value + '}';
    }
}
