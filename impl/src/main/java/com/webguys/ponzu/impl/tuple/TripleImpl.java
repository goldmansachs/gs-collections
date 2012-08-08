/*
 * Copyright 2012 Kevin Birch.
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

package com.webguys.ponzu.impl.tuple;

import com.webguys.ponzu.impl.block.factory.Comparators;

/**
 * A TripleImpl is a container that holds three related objects.
 */
public class TripleImpl<T1, T2, T3> implements Triple<T1, T2, T3>
{
    private static final long serialVersionUID = 1L;

    private final T1 one;
    private final T2 two;
    private final T3 three;

    public TripleImpl(T1 one, T2 two, T3 three)
    {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    @Override
    public T1 getOne()
    {
        return this.one;
    }

    @Override
    public T2 getTwo()
    {
        return this.two;
    }

    @Override
    public T3 getThree()
    {
        return this.three;
    }

    @Override
    public String toString()
    {
        return String.format("(%s, %s, %s)", this.one, this.two, this.three);
    }

    @Override
    public int compareTo(Triple<T1, T2, T3> o)
    {
        int i = ((Comparable<T1>) this.one).compareTo(o.getOne());
        if (0 != i)
        {
            return  i;
        }
        int j = ((Comparable<T2>) this.two).compareTo(o.getTwo());
        if (0 != j)
        {
            return j;
        }
        return ((Comparable<T3>) this.three).compareTo(o.getThree());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || !(obj instanceof Triple))
        {
            return false;
        }

        TripleImpl<?, ?, ?> that = (TripleImpl<?, ?, ?>) obj;

        return Comparators.nullSafeEquals(this.one, that.one)
                && Comparators.nullSafeEquals(this.two, that.two)
                && Comparators.nullSafeEquals(this.three, that.three);

    }

    @Override
    public int hashCode()
    {
        int result = this.one == null ? 0 : this.one.hashCode();
        result = 31 * result + (this.two == null ? 0 : this.two.hashCode());
        result = 31 * result + (this.three == null ? 0 : this.three.hashCode());
        return result;
    }
}
