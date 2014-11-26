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

import java.util.concurrent.atomic.AtomicInteger;

public final class MutableAtomicInteger extends AtomicInteger implements Comparable<MutableAtomicInteger>
{
    private static final long serialVersionUID = 1L;

    public MutableAtomicInteger(int value)
    {
        super(value);
    }

    public MutableAtomicInteger()
    {
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
        return this.get() == ((MutableAtomicInteger) other).get();
    }

    @Override
    public int hashCode()
    {
        return this.get();
    }

    @Override
    public int compareTo(MutableAtomicInteger other)
    {
        return Integer.compare(this.get(), other.get());
    }

    public MutableAtomicInteger add(int number)
    {
        this.getAndAdd(number);
        return this;
    }

    public MutableAtomicInteger subtract(int number)
    {
        while (true)
        {
            int current = this.get();
            int next = current - number;
            if (this.compareAndSet(current, next))
            {
                break;
            }
        }
        return this;
    }

    public MutableAtomicInteger multiply(int number)
    {
        while (true)
        {
            int current = this.get();
            int next = current * number;
            if (this.compareAndSet(current, next))
            {
                break;
            }
        }
        return this;
    }

    public MutableAtomicInteger divide(int number)
    {
        while (true)
        {
            int current = this.get();
            int next = current / number;
            if (this.compareAndSet(current, next))
            {
                break;
            }
        }
        return this;
    }

    public MutableAtomicInteger min(int number)
    {
        while (true)
        {
            int current = this.get();
            int next = Math.min(current, number);
            if (this.compareAndSet(current, next))
            {
                break;
            }
        }
        return this;
    }

    public MutableAtomicInteger max(int number)
    {
        while (true)
        {
            int current = this.get();
            int next = Math.max(current, number);
            if (this.compareAndSet(current, next))
            {
                break;
            }
        }
        return this;
    }

    public MutableAtomicInteger abs()
    {
        while (true)
        {
            int current = this.get();
            int next = Math.abs(current);
            if (this.compareAndSet(current, next))
            {
                break;
            }
        }
        return this;
    }

    public Integer toInteger()
    {
        return Integer.valueOf(this.get());
    }
}
