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

package com.webguys.ponzu.impl.list.immutable;

import com.webguys.ponzu.api.factory.list.ImmutableListFactory;
import com.webguys.ponzu.api.list.ImmutableList;
import com.webguys.ponzu.impl.utility.Iterate;

public final class ImmutableListFactoryImpl implements ImmutableListFactory
{
    public <T> ImmutableList<T> of()
    {
        return (ImmutableList<T>) ImmutableEmptyList.INSTANCE;
    }

    public <T> ImmutableList<T> of(T one)
    {
        return new ImmutableSingletonList<T>(one);
    }

    public <T> ImmutableList<T> of(T one, T two)
    {
        return new ImmutableDoubletonList<T>(one, two);
    }

    public <T> ImmutableList<T> of(T one, T two, T three)
    {
        return new ImmutableTripletonList<T>(one, two, three);
    }

    public <T> ImmutableList<T> of(T one, T two, T three, T four)
    {
        return new ImmutableQuadrupletonList<T>(one, two, three, four);
    }

    public <T> ImmutableList<T> of(T one, T two, T three, T four, T five)
    {
        return new ImmutableQuintupletonList<T>(one, two, three, four, five);
    }

    public <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six)
    {
        return new ImmutableSextupletonList<T>(one, two, three, four, five, six);
    }

    public <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six, T seven)
    {
        return new ImmutableSeptupletonList<T>(one, two, three, four, five, six, seven);
    }

    public <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six, T seven, T eight)
    {
        return new ImmutableOctupletonList<T>(one, two, three, four, five, six, seven, eight);
    }

    public <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six, T seven, T eight, T nine)
    {
        return new ImmutableNonupletonList<T>(one, two, three, four, five, six, seven, eight, nine);
    }

    public <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six, T seven, T eight, T nine, T ten)
    {
        return new ImmutableDecapletonList<T>(one, two, three, four, five, six, seven, eight, nine, ten);
    }

    public <T> ImmutableList<T> of(T... items)
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
            case 5:
                return this.of(items[0], items[1], items[2], items[3], items[4]);
            case 6:
                return this.of(items[0], items[1], items[2], items[3], items[4], items[5]);
            case 7:
                return this.of(items[0], items[1], items[2], items[3], items[4], items[5], items[6]);
            case 8:
                return this.of(items[0], items[1], items[2], items[3], items[4], items[5], items[6], items[7]);
            case 9:
                return this.of(items[0], items[1], items[2], items[3], items[4], items[5], items[6], items[7], items[8]);
            case 10:
                return this.of(items[0], items[1], items[2], items[3], items[4], items[5], items[6], items[7], items[8], items[9]);

            default:
                return ImmutableArrayList.newListWith(items);
        }
    }

    public <T> ImmutableList<T> ofAll(Iterable<? extends T> items)
    {
        if (items instanceof ImmutableList<?>)
        {
            return (ImmutableList<T>) items;
        }

        return this.of((T[]) Iterate.toArray(items));
    }
}
