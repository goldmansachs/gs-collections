/*
 * Copyright 2011 Goldman Sachs.
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
 * A mutable sum which uses an int as the storage mechanism.
 *
 * @deprecated
 */
@Deprecated
public final class IntegerSum
    implements Sum
{
    private static final long serialVersionUID = 1L;

    private int sum = 0;

    public IntegerSum(int newSum)
    {
        this.sum = newSum;
    }

    public Sum speciesNew()
    {
        return new IntegerSum(0);
    }

    public Sum add(Object number)
    {
        return this.add((Number) number);
    }

    public Sum add(Number number)
    {
        return this.add(number.intValue());
    }

    public Sum add(Sum otherSum)
    {
        return this.add(otherSum.getValue());
    }

    public Sum add(int value)
    {
        this.sum += value;
        return this;
    }

    public int getIntSum()
    {
        return this.sum;
    }

    public Number getValue()
    {
        return this.sum;
    }

    @Override
    public boolean equals(Object o)
    {
        IntegerSum integerSum = (IntegerSum) o;
        return this.sum == integerSum.sum;
    }

    @Override
    public int hashCode()
    {
        return this.sum;
    }
}
