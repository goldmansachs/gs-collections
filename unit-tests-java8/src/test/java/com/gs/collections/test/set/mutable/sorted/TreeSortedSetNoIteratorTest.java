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

package com.gs.collections.test.set.mutable.sorted;

import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.test.IterableTestCase;
import com.gs.collections.test.NoIteratorTestCase;
import com.gs.junit.runners.Java8Runner;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore("Requires scapegoat tree implementation")
@RunWith(Java8Runner.class)
public class TreeSortedSetNoIteratorTest implements MutableSortedSetTestCase, NoIteratorTestCase
{
    @SafeVarargs
    @Override
    public final <T> MutableSortedSet<T> newWith(T... elements)
    {
        MutableSortedSet<T> result = new TreeSortedSetNoIterator<T>(Comparators.reverseNaturalOrder());
        IterableTestCase.addAllTo(elements, result);
        return result;
    }

    @Override
    public void Iterable_next()
    {
        NoIteratorTestCase.super.Iterable_next();
    }

    @Override
    public void Iterable_remove()
    {
        NoIteratorTestCase.super.Iterable_remove();
    }

    @Override
    public void RichIterable_getFirst()
    {
        NoIteratorTestCase.super.RichIterable_getFirst();
    }

    @Override
    public void RichIterable_getLast()
    {
        NoIteratorTestCase.super.RichIterable_getLast();
    }

    public static class TreeSortedSetNoIterator<T> extends TreeSortedSet<T>
    {
        public TreeSortedSetNoIterator()
        {
            // For serialization
        }

        public TreeSortedSetNoIterator(Comparator<? super T> comparator)
        {
            super(comparator);
        }

        @Override
        public Iterator<T> iterator()
        {
            throw new AssertionError("No iteration patterns should delegate to iterator()");
        }
    }
}
