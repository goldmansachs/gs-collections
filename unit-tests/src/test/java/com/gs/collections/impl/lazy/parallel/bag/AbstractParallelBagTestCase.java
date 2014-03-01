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

package com.gs.collections.impl.lazy.parallel.bag;

import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.ParallelBag;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterableTestCase;
import org.junit.Test;

public abstract class AbstractParallelBagTestCase extends AbstractParallelIterableTestCase
{
    @Override
    protected abstract ParallelBag<Integer> classUnderTest();

    @Override
    protected MutableBag<Integer> getExpected()
    {
        return HashBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    }

    @Override
    protected <T> RichIterable<T> getActual(ParallelIterable<T> actual)
    {
        return actual.toBag();
    }

    @Override
    protected boolean isOrdered()
    {
        return false;
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toArray()
    {
        super.toArray();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void contains()
    {
        super.contains();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void containsAllIterable()
    {
        super.containsAllIterable();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void containsAllArray()
    {
        super.containsAllArray();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void forEach()
    {
        super.forEach();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void select()
    {
        super.select();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void selectWith()
    {
        super.selectWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void reject()
    {
        super.reject();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void rejectWith()
    {
        super.rejectWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void partition()
    {
        super.partition();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void partitionWith()
    {
        super.partitionWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void collect()
    {
        super.collect();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void collectWith()
    {
        super.collectWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void collectIf()
    {
        super.collectIf();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void anySatisfy()
    {
        super.anySatisfy();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void anySatisfyWith()
    {
        super.anySatisfyWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void allSatisfy()
    {
        super.allSatisfy();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void allSatisfyWith()
    {
        super.allSatisfyWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void noneSatisfy()
    {
        super.noneSatisfy();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void noneSatisfyWith()
    {
        super.noneSatisfyWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toList()
    {
        super.toList();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSortedList()
    {
        super.toSortedList();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSortedList_comparator()
    {
        super.toSortedList_comparator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSortedListBy()
    {
        super.toSortedListBy();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSet()
    {
        super.toSet();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSortedSet()
    {
        super.toSortedSet();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSortedSet_comparator()
    {
        super.toSortedSet_comparator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSortedSetBy()
    {
        super.toSortedSetBy();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSortedMap()
    {
        super.toSortedMap();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void toSortedMap_comparator()
    {
        super.toSortedMap_comparator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void detect()
    {
        super.detect();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void detectIfNone()
    {
        super.detectIfNone();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void detectWith()
    {
        super.detectWith();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void detectWithIfNone()
    {
        super.detectWithIfNone();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void min_empty_throws()
    {
        super.min_empty_throws();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void max_empty_throws()
    {
        super.max_empty_throws();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void min()
    {
        super.min();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void max()
    {
        super.max();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void min_empty_throws_without_comparator()
    {
        super.min_empty_throws_without_comparator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void max_empty_throws_without_comparator()
    {
        super.max_empty_throws_without_comparator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void min_without_comparator()
    {
        super.min_without_comparator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void max_without_comparator()
    {
        super.max_without_comparator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void testToString()
    {
        super.testToString();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void makeString()
    {
        super.makeString();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void makeString_separator()
    {
        super.makeString_separator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void makeString_start_separator_end()
    {
        super.makeString_start_separator_end();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void appendString()
    {
        super.appendString();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void appendString_separator()
    {
        super.appendString_separator();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void appendString_start_separator_end()
    {
        super.appendString_start_separator_end();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void groupBy()
    {
        super.groupBy();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void groupByEach()
    {
        super.groupByEach();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void asUnique()
    {
        super.asUnique();
    }

    @Test(expected = NullPointerException.class)
    @Override
    public void executionException()
    {
        super.executionException();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void interruptedException()
    {
        Thread.currentThread().interrupt();
        MutableCollection<Integer> actual = HashBag.<Integer>newBag().asSynchronized();
        this.classUnderTest().forEach(CollectionAddProcedure.on(actual));
    }
}
