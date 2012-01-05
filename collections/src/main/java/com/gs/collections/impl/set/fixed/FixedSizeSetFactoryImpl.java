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

package com.gs.collections.impl.set.fixed;

import com.gs.collections.api.factory.set.FixedSizeSetFactory;
import com.gs.collections.api.set.FixedSizeSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class FixedSizeSetFactoryImpl implements FixedSizeSetFactory
{
    private static final FixedSizeSet<?> EMPTY_SET = new EmptySet();

    public <T> FixedSizeSet<T> of()
    {
        return (FixedSizeSet<T>) FixedSizeSetFactoryImpl.EMPTY_SET;
    }

    public <T> FixedSizeSet<T> of(T one)
    {
        return new SingletonSet<T>(one);
    }

    public <T> FixedSizeSet<T> of(T one, T two)
    {
        if (Comparators.nullSafeEquals(one, two))
        {
            return this.of(one);
        }
        return new DoubletonSet<T>(one, two);
    }

    public <T> FixedSizeSet<T> of(T one, T two, T three)
    {
        if (Comparators.nullSafeEquals(one, two))
        {
            return this.of(one, three);
        }
        if (Comparators.nullSafeEquals(one, three))
        {
            return this.of(one, two);
        }
        if (Comparators.nullSafeEquals(two, three))
        {
            return this.of(one, two);
        }
        return new TripletonSet<T>(one, two, three);
    }

    public <T> FixedSizeSet<T> of(T one, T two, T three, T four)
    {
        if (Comparators.nullSafeEquals(one, two))
        {
            return this.of(one, three, four);
        }
        if (Comparators.nullSafeEquals(one, three))
        {
            return this.of(one, two, four);
        }
        if (Comparators.nullSafeEquals(one, four))
        {
            return this.of(one, two, three);
        }
        if (Comparators.nullSafeEquals(two, three))
        {
            return this.of(one, two, four);
        }
        if (Comparators.nullSafeEquals(two, four))
        {
            return this.of(one, two, three);
        }
        if (Comparators.nullSafeEquals(three, four))
        {
            return this.of(one, two, three);
        }
        return new QuadrupletonSet<T>(one, two, three, four);
    }

    public <T> MutableSet<T> ofAll(Iterable<? extends T> items)
    {
        UnifiedSet<T> set = UnifiedSet.newSet(items);
        T[] itemArray;
        switch (set.size())
        {
            case 0:
                return new EmptySet<T>();
            case 1:
                itemArray = (T[]) set.toArray();
                return new SingletonSet<T>(itemArray[0]);
            case 2:
                itemArray = (T[]) set.toArray();
                return new DoubletonSet<T>(itemArray[0], itemArray[1]);
            case 3:
                itemArray = (T[]) set.toArray();
                return new TripletonSet<T>(itemArray[0], itemArray[1], itemArray[2]);
            case 4:
                itemArray = (T[]) set.toArray();
                return new QuadrupletonSet<T>(itemArray[0], itemArray[1], itemArray[2], itemArray[3]);
            default:
                return set;
        }
    }
}
