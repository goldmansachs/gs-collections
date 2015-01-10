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

public class B implements A
{
    private final int i;

    public B(int i)
    {
        this.i = i;
    }

    @Override
    public double getDoubleValue()
    {
        return Integer.valueOf(this.i).doubleValue();
    }

    @Override
    public String toString()
    {
        return "B{i=" + this.i + '}';
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

        B b = (B) o;

        if (this.i != b.i)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return this.i;
    }
}
