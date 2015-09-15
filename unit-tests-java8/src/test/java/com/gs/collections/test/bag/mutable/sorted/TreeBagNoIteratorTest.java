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

package com.gs.collections.test.bag.mutable.sorted;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.junit.runners.Java8Runner;
import org.junit.runner.RunWith;

@RunWith(Java8Runner.class)
public class TreeBagNoIteratorTest implements MutableSortedBagTestCase, OrderedIterableNoIteratorTest
{
    @SafeVarargs
    @Override
    public final <T> MutableSortedBag<T> newWith(T... elements)
    {
        return new TreeBagNoIterator<>(Comparators.reverseNaturalOrder(), Arrays.asList(elements));
    }

    @Override
    public void Iterable_remove()
    {
        OrderedIterableNoIteratorTest.super.Iterable_remove();
    }

    @Override
    public void RichIterable_iterator_iterationOrder()
    {
        OrderedIterableNoIteratorTest.super.RichIterable_iterator_iterationOrder();
    }

    public static class TreeBagNoIterator<T> extends TreeBag<T>
    {
        public TreeBagNoIterator()
        {
            // For serialization
        }

        public TreeBagNoIterator(Comparator<? super T> comparator, Iterable<? extends T> iterable)
        {
            super(comparator, iterable);
        }

        @Override
        public Iterator<T> iterator()
        {
            throw new AssertionError("No iteration patterns should delegate to iterator()");
        }
    }
}
