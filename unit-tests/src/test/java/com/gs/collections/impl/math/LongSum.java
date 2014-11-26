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

/**
 * A mutable sum which uses a long as the storage mechanism
 *
 * @deprecated use MutableLong instead
 */
@Deprecated
public final class LongSum
    implements Sum
{
    private static final long serialVersionUID = 1L;

    private long sum = 0;

    public LongSum(long newSum)
    {
        this.sum = newSum;
    }

    @Override
    public Sum speciesNew()
    {
        return new LongSum(0);
    }

    @Override
    public Sum add(Object number)
    {
        this.add((Number) number);
        return this;
    }

    @Override
    public Sum add(int value)
    {
        this.add((long) value);
        return this;
    }

    @Override
    public Sum add(Sum otherSum)
    {
        return this.add(otherSum.getValue());
    }

    @Override
    public Sum add(Number number)
    {
        this.add(number.longValue());
        return this;
    }

    public Sum add(long value)
    {
        this.sum += value;
        return this;
    }

    @Override
    public Number getValue()
    {
        return this.sum;
    }

    @Override
    public boolean equals(Object o)
    {
        LongSum longSum = (LongSum) o;
        return this.sum == longSum.sum;
    }

    @Override
    public int hashCode()
    {
        return (int) (this.sum ^ this.sum >>> 32);
    }
}
