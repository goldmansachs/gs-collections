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

package com.gs.collections.test.list.mutable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.list.mutable.MultiReaderFastList;
import com.gs.collections.test.collection.mutable.MultiReaderMutableCollectionTestCase;
import com.gs.junit.runners.Java8Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Java8Runner.class)
public class MultiReaderFastListTest implements MutableListTestCase, MultiReaderMutableCollectionTestCase
{
    @SafeVarargs
    @Override
    public final <T> MultiReaderFastList<T> newWith(T... elements)
    {
        return MultiReaderFastList.newListWith(elements);
    }

    @Override
    @Test
    public void Iterable_remove()
    {
        MultiReaderMutableCollectionTestCase.super.Iterable_remove();
    }

    @Override
    @Test
    public void RichIterable_iterator_iterationOrder()
    {
        MutableCollection<Integer> iterationOrder = this.newMutableForFilter();
        MultiReaderFastList<Integer> instanceUnderTest = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        instanceUnderTest.withReadLockAndDelegate(delegate -> {
            Iterator<Integer> iterator = delegate.iterator();
            while (iterator.hasNext())
            {
                iterationOrder.add(iterator.next());
            }
        });
        assertEquals(this.expectedIterationOrder(), iterationOrder);
    }

    @Override
    @Test
    public void OrderedIterable_next()
    {
        // Does not support iterator outside withReadLockAndDelegate

        MultiReaderFastList<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);

        MutableCollection<Integer> mutableCollection = this.newMutableForFilter();

        iterable.withReadLockAndDelegate(delegate -> {
            Iterator<Integer> iterator = delegate.iterator();
            while (iterator.hasNext())
            {
                Integer integer = iterator.next();
                mutableCollection.add(integer);
            }

            assertEquals(this.getExpectedFiltered(3, 3, 3, 2, 2, 1), mutableCollection);
            assertFalse(iterator.hasNext());
        });
    }

    @Test
    public void MultiReaderFastList_hasNext()
    {
        MultiReaderFastList<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);
        iterable.withReadLockAndDelegate(delegate -> assertTrue(delegate.iterator().hasNext()));
        MultiReaderFastList<?> emptyIterable = this.newWith();
        emptyIterable.withReadLockAndDelegate(delegate -> assertFalse(delegate.iterator().hasNext()));
    }

    @Test
    public void MultiReaderFastList_next_throws_at_end()
    {
        MultiReaderFastList<Integer> iterable = this.newWith(3, 2, 1);
        iterable.withReadLockAndDelegate(delegate -> {
            Iterator<Integer> iterator = delegate.iterator();
            assertTrue(iterator.hasNext());
            iterator.next();
            assertTrue(iterator.hasNext());
            iterator.next();
            assertTrue(iterator.hasNext());
            iterator.next();
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, (Runnable) iterator::next);
        });
    }

    @Test
    public void MultiReaderFastList_next_throws_on_empty()
    {
        MultiReaderFastList<Object> iterable = this.newWith();
        assertThrows(
                NoSuchElementException.class,
                () -> iterable.withReadLockAndDelegate(delegate -> delegate.iterator().next()));
    }
}
