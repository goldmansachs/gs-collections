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

package com.gs.collections.impl.set.immutable;

import com.gs.collections.api.factory.set.ImmutableSetFactory;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

@Immutable
public final class ImmutableSetFactoryImpl implements ImmutableSetFactory
{
    public <T> ImmutableSet<T> empty()
    {
        return (ImmutableSet<T>) ImmutableEmptySet.INSTANCE;
    }

    public <T> ImmutableSet<T> of()
    {
        return this.empty();
    }

    public <T> ImmutableSet<T> with()
    {
        return this.empty();
    }

    public <T> ImmutableSet<T> of(T one)
    {
        return this.with(one);
    }

    public <T> ImmutableSet<T> with(T one)
    {
        return new ImmutableSingletonSet<T>(one);
    }

    public <T> ImmutableSet<T> of(T one, T two)
    {
        return this.with(one, two);
    }

    public <T> ImmutableSet<T> with(T one, T two)
    {
        if (Comparators.nullSafeEquals(one, two))
        {
            return this.of(one);
        }
        return new ImmutableDoubletonSet<T>(one, two);
    }

    public <T> ImmutableSet<T> of(T one, T two, T three)
    {
        return this.with(one, two, three);
    }

    public <T> ImmutableSet<T> with(T one, T two, T three)
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
        return new ImmutableTripletonSet<T>(one, two, three);
    }

    public <T> ImmutableSet<T> of(T one, T two, T three, T four)
    {
        return this.with(one, two, three, four);
    }

    public <T> ImmutableSet<T> with(T one, T two, T three, T four)
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
        return new ImmutableQuadrupletonSet<T>(one, two, three, four);
    }

    public <T> ImmutableSet<T> of(T... items)
    {
        return this.with(items);
    }

    public <T> ImmutableSet<T> with(T... items)
    {
        if (items == null || items.length == 0)
        {
            return this.of();
        }

        switch (items.length)
        {
            case 1:
                return this.of(items[0]);
            case 2:
                return this.of(items[0], items[1]);
            case 3:
                return this.of(items[0], items[1], items[2]);
            case 4:
                return this.of(items[0], items[1], items[2], items[3]);
            default:
                return ImmutableUnifiedSet.newSetWith(items);
        }
    }

    public <T> ImmutableSet<T> ofAll(Iterable<? extends T> items)
    {
        return this.withAll(items);
    }

    public <T> ImmutableSet<T> withAll(Iterable<? extends T> items)
    {
        if (items instanceof ImmutableSet<?>)
        {
            return (ImmutableSet<T>) items;
        }

        if (Iterate.isEmpty(items))
        {
            return this.with();
        }
        return this.with((T[]) Iterate.toArray(items));
    }
}
