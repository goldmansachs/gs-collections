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

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.test.Verify;
import junit.framework.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedSortedSet}.
 */
public class SynchronizedSortedSet2Test extends AbstractSortedSetTestCase
{
    @Override
    protected <T> MutableSortedSet<T> classUnderTest()
    {
        return new SynchronizedSortedSet<T>(SortedSetAdapter.<T>adapt(new TreeSet<T>()));
    }

    @Override
    protected <T> MutableSortedSet<T> classUnderTest(T... elements)
    {
        return new SynchronizedSortedSet<T>(TreeSortedSet.newSetWith(elements));
    }

    @Override
    protected <T> MutableSortedSet<T> classUnderTest(Comparator<? super T> comparator, T... elements)
    {
        return new SynchronizedSortedSet<T>(TreeSortedSet.newSetWith(comparator, elements));
    }

    private MutableSortedSet<String> newWith(String... strings)
    {
        return new SynchronizedSortedSet<String>(SortedSetAdapter.adapt(new TreeSet<String>(Arrays.asList(strings))));
    }

    @Override
    public void asSynchronized()
    {
        MutableSortedSet<Object> synchronizedSet = this.classUnderTest();
        Assert.assertSame(synchronizedSet, synchronizedSet.asSynchronized());
    }

    @Override
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableSortedSet.class, this.<Object>classUnderTest().asUnmodifiable());
    }

    @Test(expected = NoSuchElementException.class)
    public void min_empty_throws_without_comparator()
    {
        this.newWith().min();
    }

    @Test(expected = NoSuchElementException.class)
    public void max_empty_throws_without_comparator()
    {
        this.newWith().max();
    }
}
