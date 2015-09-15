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

package com.gs.collections.impl.bag.sorted.immutable;

import java.util.Comparator;

import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.SortedBag;
import com.gs.collections.api.factory.bag.sorted.ImmutableSortedBagFactory;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.utility.Iterate;

public class ImmutableSortedBagFactoryImpl implements ImmutableSortedBagFactory
{
    public <T> ImmutableSortedBag<T> empty()
    {
        return (ImmutableSortedBag<T>) ImmutableEmptySortedBag.INSTANCE;
    }

    public <T> ImmutableSortedBag<T> empty(Comparator<? super T> comparator)
    {
        return new ImmutableEmptySortedBag<T>(comparator);
    }

    public <T> ImmutableSortedBag<T> of()
    {
        return this.with();
    }

    public <T> ImmutableSortedBag<T> with()
    {
        return this.empty();
    }

    public <T> ImmutableSortedBag<T> of(T... items)
    {
        return this.with(items);
    }

    public <T> ImmutableSortedBag<T> with(T... items)
    {
        if (items == null || items.length == 0)
        {
            return this.of();
        }
        return new ImmutableSortedBagImpl<T>(TreeBag.newBagWith(items));
    }

    public <T> ImmutableSortedBag<T> ofAll(Iterable<? extends T> items)
    {
        return this.withAll(items);
    }

    public <T> ImmutableSortedBag<T> withAll(Iterable<? extends T> items)
    {
        if (items instanceof ImmutableSortedBag<?>)
        {
            return (ImmutableSortedBag<T>) items;
        }

        return this.of((T[]) Iterate.toArray(items));
    }

    public <T> ImmutableSortedBag<T> of(Comparator<? super T> comparator)
    {
        return this.with(comparator);
    }

    public <T> ImmutableSortedBag<T> with(Comparator<? super T> comparator)
    {
        if (comparator == null)
        {
            return this.of();
        }
        return new ImmutableEmptySortedBag<T>(comparator);
    }

    public <T> ImmutableSortedBag<T> of(Comparator<? super T> comparator, T... items)
    {
        return this.with(comparator, items);
    }

    public <T> ImmutableSortedBag<T> with(Comparator<? super T> comparator, T... items)
    {
        if (items == null || items.length == 0)
        {
            return this.of(comparator);
        }
        return new ImmutableSortedBagImpl<T>(TreeBag.newBagWith(comparator, items));
    }

    public <T> ImmutableSortedBag<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> items)
    {
        return this.withAll(comparator, items);
    }

    public <T> ImmutableSortedBag<T> withAll(Comparator<? super T> comparator, Iterable<? extends T> items)
    {
        return this.of(comparator, (T[]) Iterate.toArray(items));
    }

    public <T> ImmutableSortedBag<T> ofSortedBag(SortedBag<T> bag)
    {
        return this.withSortedBag(bag);
    }

    public <T> ImmutableSortedBag<T> withSortedBag(SortedBag<T> bag)
    {
        if (bag instanceof ImmutableSortedBag)
        {
            return (ImmutableSortedBag<T>) bag;
        }
        if (bag.isEmpty())
        {
            return this.of(bag.comparator());
        }
        return new ImmutableSortedBagImpl<T>(bag);
    }
}
