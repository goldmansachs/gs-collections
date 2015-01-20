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

package com.gs.collections.test.set.sorted;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import com.gs.collections.test.CollectionTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public interface SortedSetTestCase extends CollectionTestCase
{
    @Override
    <T> SortedSet<T> newWith(T... elements);

    @Override
    @Test
    default void Iterable_next()
    {
        Set<Integer> iterable = this.newWith(3, 2, 1);

        Iterator<Integer> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Override
    @Test
    default void Iterable_remove()
    {
        Iterable<Integer> iterable = this.newWith(3, 2, 1);
        Iterator<Integer> iterator = iterable.iterator();
        assertEquals(Integer.valueOf(3), iterator.next());
        iterator.remove();
        assertEquals(this.newWith(2, 1), iterable);
    }

    @Override
    @Test
    default void Collection_add()
    {
        Collection<Integer> collection = this.newWith(1, 2, 3);
        assertFalse(collection.add(3));
    }

    @Override
    @Test
    default void Collection_size()
    {
        assertThat(this.newWith(3, 2, 1), hasSize(3));
        assertThat(this.newWith(), hasSize(0));
    }
}
