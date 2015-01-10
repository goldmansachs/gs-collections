/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.test.domain;

public class C implements A
{
    private final double d;

    public C(double d)
    {
        this.d = d;
    }

    @Override
    public double getDoubleValue()
    {
        return this.d;
    }

    @Override
    public String toString()
    {
        return "C{d=" + this.d + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        C c = (C) o;

        if (Double.compare(c.d, this.d) != 0)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        long temp = Double.doubleToLongBits(this.d);
        return (int) (temp ^ (temp >>> 32));
    }
}
