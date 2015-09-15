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

package com.gs.collections.impl.bag.sorted.mutable;

import java.util.Comparator;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.factory.bag.sorted.MutableSortedBagFactory;

public final class MutableSortedBagFactoryImpl implements MutableSortedBagFactory
{
    public <T> MutableSortedBag<T> empty()
    {
        return TreeBag.newBag();
    }

    public <T> MutableSortedBag<T> empty(Comparator<? super T> comparator)
    {
        return TreeBag.newBag(comparator);
    }

    public <T> MutableSortedBag<T> of()
    {
        return this.with();
    }

    public <T> MutableSortedBag<T> with()
    {
        return TreeBag.newBag();
    }

    public <T> MutableSortedBag<T> of(Comparator<? super T> comparator)
    {
        return this.with(comparator);
    }

    public <T> MutableSortedBag<T> with(Comparator<? super T> comparator)
    {
        return TreeBag.newBag(comparator);
    }

    public <T> MutableSortedBag<T> of(T... elements)
    {
        return this.with(elements);
    }

    public <T> MutableSortedBag<T> with(T... elements)
    {
        return TreeBag.newBagWith(elements);
    }

    public <T> MutableSortedBag<T> of(Comparator<? super T> comparator, T... elements)
    {
        return this.with(comparator, elements);
    }

    public <T> MutableSortedBag<T> with(Comparator<? super T> comparator, T... elements)
    {
        return TreeBag.newBagWith(comparator, elements);
    }

    public <T> MutableSortedBag<T> ofAll(Iterable<? extends T> items)
    {
        return this.withAll(items);
    }

    public <T> MutableSortedBag<T> withAll(Iterable<? extends T> items)
    {
        return TreeBag.newBag(items);
    }

    public <T> MutableSortedBag<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> items)
    {
        return this.withAll(comparator, items);
    }

    public <T> MutableSortedBag<T> withAll(Comparator<? super T> comparator, Iterable<? extends T> items)
    {
        return TreeBag.newBag(comparator, items);
    }
}
