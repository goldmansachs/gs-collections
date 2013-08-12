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

package com.gs.collections.impl;

import java.io.Serializable;

public class CollidingInt implements Serializable, Comparable<CollidingInt>
{
    private static final long serialVersionUID = 1L;
    private final int value;
    private final int shift;

    public CollidingInt(int value, int shift)
    {
        this.shift = shift;
        this.value = value;
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

        CollidingInt that = (CollidingInt) o;

        return this.value == that.value && this.shift == that.shift;
    }

    @Override
    public int hashCode()
    {
        return this.value >> this.shift;
    }

    public int getValue()
    {
        return this.value;
    }

    @Override
    public int compareTo(CollidingInt o)
    {
        int result = Integer.valueOf(this.value).compareTo(o.value);
        if (result != 0)
        {
            return result;
        }
        return Integer.valueOf(this.shift).compareTo(o.shift);
    }
}
