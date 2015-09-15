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
import java.util.Iterator;

import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.SortedBag;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.factory.SortedBags;
import org.junit.Test;

public class ImmutableSortedBagImplNoIteratorTest extends ImmutableSortedBagImplTest
{
    @Override
    protected ImmutableSortedBag<Integer> classUnderTest()
    {
        return new ImmutableSortedBagImplNoIterator<>(SortedBags.immutable.with(1, 1, 1, 2));
    }

    @Override
    protected <T> MutableCollection<T> newMutable()
    {
        return SortedBags.mutable.empty();
    }

    @Override
    protected ImmutableSortedBag<Integer> classUnderTest(Comparator<? super Integer> comparator)
    {
        return new ImmutableSortedBagImplNoIterator<>(SortedBags.immutable.with(comparator, 1, 1, 1, 2));
    }

    @Override
    protected <T> ImmutableSortedBag<T> newWith(T... elements)
    {
        ImmutableSortedBag<T> bag = SortedBags.immutable.with(elements);
        if (bag.isEmpty())
        {
            return new ImmutableEmptySortedBagImplNoIterator<>(bag.comparator());
        }
        return new ImmutableSortedBagImplNoIterator<>(bag);
    }

    @Override
    protected <T> ImmutableSortedBag<T> newWith(Comparator<? super T> comparator, T... elements)
    {
        return new ImmutableSortedBagImplNoIterator<>(SortedBags.immutable.with(comparator, elements));
    }

    @Override
    @Test
    public void forLoop()
    {
        //not applicable
    }

    @Override
    public void iteratorRemove()
    {
        //not applicable
    }

    private static final class ImmutableSortedBagImplNoIterator<T> extends ImmutableSortedBagImpl<T>
    {
        ImmutableSortedBagImplNoIterator(SortedBag<T> sortedBag)
        {
            super(sortedBag);
        }

        @Override
        public Iterator<T> iterator()
        {
            throw new AssertionError("No methods should delegate to iterator");
        }
    }

    private static final class ImmutableEmptySortedBagImplNoIterator<T> extends ImmutableEmptySortedBag<T>
    {
        ImmutableEmptySortedBagImplNoIterator(Comparator<? super T> comparator)
        {
            super(comparator);
        }

        @Override
        public Iterator<T> iterator()
        {
            throw new AssertionError("No methods should delegate to iterator");
        }
    }
}
