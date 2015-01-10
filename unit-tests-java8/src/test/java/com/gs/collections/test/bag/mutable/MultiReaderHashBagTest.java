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
import java.util.NoSuchElementException;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.bag.mutable.MultiReaderHashBag;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.test.collection.mutable.MultiReaderMutableCollectionTestCase;
import com.gs.junit.runners.Java8Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Java8Runner.class)
public class MultiReaderHashBagTest implements MutableBagTestCase, MultiReaderMutableCollectionTestCase
{
    @SafeVarargs
    @Override
    public final <T> MultiReaderHashBag<T> newWith(T... elements)
    {
        return MultiReaderHashBag.newBagWith(elements);
    }

    @Test
    @Override
    public void Iterable_remove()
    {
        MultiReaderMutableCollectionTestCase.super.Iterable_remove();
    }

    @Test
    @Override
    public void Iterable_next()
    {
        MultiReaderMutableCollectionTestCase.super.Iterable_next();
    }

    @Override
    public void RichIterable_getFirst()
    {
        MultiReaderMutableCollectionTestCase.super.RichIterable_getFirst();
    }

    @Override
    public void RichIterable_getLast()
    {
        MultiReaderMutableCollectionTestCase.super.RichIterable_getLast();
    }

    @Override
    @Test
    public void RichIterable_iterator_iterationOrder()
    {
        MutableCollection<Integer> iterationOrder = this.newMutableForFilter();
        MultiReaderHashBag<Integer> instanceUnderTest = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);

        MutableCollection<Integer> expectedIterationOrder = this.newMutableForFilter();
        instanceUnderTest.forEach(Procedures.cast(expectedIterationOrder::add));

        instanceUnderTest.withReadLockAndDelegate(delegate -> {
            Iterator<Integer> iterator = delegate.iterator();
            while (iterator.hasNext())
            {
                iterationOrder.add(iterator.next());
            }
        });

        // TODO Report to JetBrains
        // assertEquals(MultiReaderMutableCollectionTestCase.super.expectedIterationOrder(), iterationOrder);
        assertEquals(expectedIterationOrder, iterationOrder);
    }

    @Test
    public void MultiReaderHashBag_next()
    {
        MultiReaderHashBag<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);

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
    public void MultiReaderHashBag_hasNext()
    {
        MultiReaderHashBag<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);
        iterable.withReadLockAndDelegate(delegate -> assertTrue(delegate.iterator().hasNext()));
        MultiReaderHashBag<?> emptyIterable = this.newWith();
        emptyIterable.withReadLockAndDelegate(delegate -> assertFalse(delegate.iterator().hasNext()));
    }

    @Test
    public void MultiReaderHashBag_next_throws_at_end()
    {
        MultiReaderHashBag<Integer> iterable = this.newWith(3, 2, 1);
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
    public void MultiReaderHashBag_next_throws_on_empty()
    {
        MultiReaderHashBag<Object> iterable = this.newWith();
        assertThrows(
                NoSuchElementException.class,
                () -> iterable.withReadLockAndDelegate(delegate -> delegate.iterator().next()));
    }
}
