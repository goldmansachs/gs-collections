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

package com.webguys.ponzu.impl.list.fixed;

import com.webguys.ponzu.api.factory.list.FixedSizeListFactory;
import com.webguys.ponzu.api.list.FixedSizeList;
import com.webguys.ponzu.impl.utility.Iterate;

public class FixedSizeListFactoryImpl implements FixedSizeListFactory
{
    private static final FixedSizeList<?> EMPTY_LIST = new EmptyList();

    public <T> FixedSizeList<T> of()
    {
        return (FixedSizeList<T>) FixedSizeListFactoryImpl.EMPTY_LIST;
    }

    public <T> FixedSizeList<T> of(T one)
    {
        return new SingletonList<T>(one);
    }

    public <T> FixedSizeList<T> of(T one, T two)
    {
        return new DoubletonList<T>(one, two);
    }

    public <T> FixedSizeList<T> of(T one, T two, T three)
    {
        return new TripletonList<T>(one, two, three);
    }

    public <T> FixedSizeList<T> of(T one, T two, T three, T four)
    {
        return new QuadrupletonList<T>(one, two, three, four);
    }

    public <T> FixedSizeList<T> of(T one, T two, T three, T four, T five)
    {
        return new QuintupletonList<T>(one, two, three, four, five);
    }

    public <T> FixedSizeList<T> of(T one, T two, T three, T four, T five, T six)
    {
        return new SextupletonList<T>(one, two, three, four, five, six);
    }

    public <T> FixedSizeList<T> of(T... items)
    {
        switch (items.length)
        {
            case 0:
                return this.of();
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
            default:
                return ArrayAdapter.newArrayWith(items);
        }
    }

    public <T> FixedSizeList<T> ofAll(Iterable<? extends T> items)
    {
        return this.of((T[]) Iterate.toArray(items));
    }
}
