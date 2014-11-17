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

package com.gs.collections.impl.bag.sorted.mutable;

import java.util.Comparator;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import org.junit.Assert;

/**
 * JUnit test for {@link SynchronizedSortedBag}.
 */
public class SynchronizedSortedBagTest extends AbstractSortedBagTestCase
{
    @Override
    protected <T> MutableSortedBag<T> newWith(T... littleElements)
    {
        return new SynchronizedSortedBag<T>(TreeBag.newBagWith(littleElements));
    }

    @Override
    protected <T> MutableSortedBag<T> newWith(Comparator<? super T> comparator, T... elements)
    {
        return new SynchronizedSortedBag<T>(TreeBag.newBagWith(comparator, elements));
    }

    @Override
    public void asSynchronized()
    {
        MutableSortedBag<Object> synchronizedBag = this.newWith();
        Assert.assertSame(synchronizedBag, synchronizedBag.asSynchronized());
    }
}
