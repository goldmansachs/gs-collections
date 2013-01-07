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

package com.gs.collections.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.block.function.primitive.IntFunction;

/**
 * A Counter can be used to increment and return an integer count.  A Counter can be used in Anonymous
 * inner classes if it is declared final, unlike an int, which once declared final cannot be modified.
 */
public final class Counter implements Externalizable
{
    public static final IntFunction<Counter> TO_COUNT = new IntFunction<Counter>()
    {
        public int intValueOf(Counter counter)
        {
            return counter.getCount();
        }
    };

    private static final long serialVersionUID = 1L;

    private int count;

    public Counter(int startCount)
    {
        this.count = startCount;
    }

    public Counter()
    {
        this(0);
    }

    public void increment()
    {
        this.count++;
    }

    public void decrement()
    {
        this.count--;
    }

    public void add(int value)
    {
        this.count += value;
    }

    public int getCount()
    {
        return this.count;
    }

    public void reset()
    {
        this.count = 0;
    }

    @Override
    public String toString()
    {
        return String.valueOf(this.count);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Counter))
        {
            return false;
        }

        Counter counter = (Counter) o;

        return this.count == counter.count;
    }

    @Override
    public int hashCode()
    {
        return this.count;
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.count);
    }

    public void readExternal(ObjectInput in) throws IOException
    {
        this.count = in.readInt();
    }
}
