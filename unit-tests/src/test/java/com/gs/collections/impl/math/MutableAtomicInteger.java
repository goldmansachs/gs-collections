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
        for (; ; )
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
        for (; ; )
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
        for (; ; )
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
        for (; ; )
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
        for (; ; )
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
        for (; ; )
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
