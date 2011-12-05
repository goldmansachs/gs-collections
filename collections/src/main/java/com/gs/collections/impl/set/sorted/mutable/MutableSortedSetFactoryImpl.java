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

package com.gs.collections.impl.set.sorted.mutable;

import java.util.Comparator;

import com.gs.collections.api.factory.set.sorted.MutableSortedSetFactory;
import com.gs.collections.api.set.sorted.MutableSortedSet;

public final class MutableSortedSetFactoryImpl implements MutableSortedSetFactory
{
    public <T> MutableSortedSet<T> of()
    {
        return TreeSortedSet.newSet();
    }

    public <T> MutableSortedSet<T> of(T... items)
    {
        return TreeSortedSet.newSetWith(items);
    }

    public <T> MutableSortedSet<T> ofAll(Iterable<? extends T> items)
    {
        return TreeSortedSet.newSet(items);
    }

    public <T> MutableSortedSet<T> of(Comparator<? super T> comparator)
    {
        return TreeSortedSet.newSet(comparator);
    }

    public <T> MutableSortedSet<T> of(Comparator<? super T> comparator, T... items)
    {
        return TreeSortedSet.newSetWith(comparator, items);
    }

    public <T> MutableSortedSet<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> items)
    {
        return TreeSortedSet.newSet(comparator, items);
    }
}
