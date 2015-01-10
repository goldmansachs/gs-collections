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

package com.gs.collections.test.bag.mutable;

import java.util.Iterator;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.test.IterableTestCase;
import com.gs.collections.test.NoIteratorTestCase;
import com.gs.junit.runners.Java8Runner;
import org.junit.runner.RunWith;

@RunWith(Java8Runner.class)
public class HashBagNoIteratorTest implements MutableBagTestCase, NoIteratorTestCase
{
    @SafeVarargs
    @Override
    public final <T> MutableBag<T> newWith(T... elements)
    {
        MutableBag<T> result = new HashBagNoIterator<>();
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
    public void RichIterable_iterator_iterationOrder()
    {
        NoIteratorTestCase.super.RichIterable_iterator_iterationOrder();
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

    public static class HashBagNoIterator<T> extends HashBag<T>
    {
        @Override
        public Iterator<T> iterator()
        {
            throw new AssertionError("No iteration patterns should delegate to iterator()");
        }
    }
}
