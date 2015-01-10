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

package com.gs.collections.test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.SortedSet;

import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.ordered.ReversibleIterable;
import com.gs.collections.api.ordered.SortedIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.MultiReaderFastList;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertIterablesEqual;
import static com.gs.collections.impl.test.Verify.assertPostSerializedEqualsAndHashCode;
import static com.gs.collections.impl.test.Verify.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public interface IterableTestCase
{
    static void assertEquals(Object o1, Object o2)
    {
        Assert.assertEquals(o1, o2);
        IterableTestCase.checkNotSame(o1, o2);

        if (o1 instanceof MultiReaderFastList<?> || o2 instanceof MultiReaderFastList<?>)
        {
            return;
        }
        if (o1 instanceof SortedIterable<?> || o2 instanceof SortedIterable<?>
                || o1 instanceof ReversibleIterable<?> || o2 instanceof ReversibleIterable<?>
                || o1 instanceof List<?> || o2 instanceof List<?>
                || o1 instanceof SortedSet<?> || o2 instanceof SortedSet<?>)
        {
            assertIterablesEqual((Iterable<?>) o1, (Iterable<?>) o2);
            if (o1 instanceof SortedIterable<?> || o2 instanceof SortedIterable<?>)
            {
                assertSame(((SortedIterable<?>) o1).comparator(), ((SortedIterable<?>) o2).comparator());
            }
        }
        else if (o1 instanceof SortedMap<?, ?> || o2 instanceof SortedMap<?, ?>)
        {
            IterableTestCase.assertEquals(((SortedMap<?, ?>) o1).keySet(), ((SortedMap<?, ?>) o2).keySet());
        }
    }

    static void checkNotSame(Object o1, Object o2)
    {
        if (o1 instanceof String && o2 instanceof String)
        {
            return;
        }
        if ((o1 instanceof Number && o2 instanceof Number)
                || (o1 instanceof Boolean && o2 instanceof Boolean)
                || o1 instanceof ImmutableCollection<?> && o2 instanceof ImmutableCollection<?>
                && ((ImmutableCollection<?>) o1).isEmpty() && ((ImmutableCollection<?>) o2).isEmpty())
        {
            assertSame(o1, o2);
            return;
        }
        assertNotSame(o1, o2);
    }

    static <T> void addAllTo(T[] elements, MutableCollection<T> result)
    {
        for (T element : elements)
        {
            if (!result.add(element))
            {
                throw new IllegalStateException();
            }
        }
    }

    <T> Iterable<T> newWith(T... elements);

    boolean allowsDuplicates();

    @Test
    default void Object_equalsAndHashCode()
    {
        assertPostSerializedEqualsAndHashCode(this.newWith(3, 3, 3, 2, 2, 1));

        assertNotEquals(this.newWith(4, 3, 2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(4, 3, 2, 1));

        assertNotEquals(this.newWith(2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(2, 1));

        assertNotEquals(this.newWith(3, 3, 2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(3, 3, 2, 1));

        assertNotEquals(this.newWith(3, 3, 2, 1), this.newWith(3, 2, 2, 1));
        assertNotEquals(this.newWith(3, 2, 2, 1), this.newWith(3, 3, 2, 1));
    }

    @Test
    default void Iterable_hasNext()
    {
        assertTrue(this.newWith(3, 2, 1).iterator().hasNext());
        assertFalse(this.newWith().iterator().hasNext());
    }

    @Test
    default void Iterable_next()
    {
        Iterator<Integer> iterator = this.newWith(3, 2, 1).iterator();
        MutableSet<Integer> set = Sets.mutable.with();
        assertTrue(set.add(iterator.next()));
        assertTrue(set.add(iterator.next()));
        assertTrue(set.add(iterator.next()));
        IterableTestCase.assertEquals(Sets.immutable.with(3, 2, 1), set);
    }

    @Test(expected = NoSuchElementException.class)
    default void Iterable_next_throws_on_empty()
    {
        this.newWith().iterator().next();
    }

    @Test
    default void Iterable_next_throws_at_end()
    {
        Iterable<Integer> iterable = this.newWith(3, 2, 1);
        Iterator<Integer> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, (Runnable) iterator::next);

        Iterator<Integer> iterator2 = iterable.iterator();
        iterator2.next();
        iterator2.next();
        iterator2.next();
        assertThrows(NoSuchElementException.class, (Runnable) iterator2::next);
        assertThrows(NoSuchElementException.class, (Runnable) iterator2::next);
    }

    void Iterable_remove();
}
