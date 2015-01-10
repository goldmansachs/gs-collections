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

package com.gs.collections.test.collection.immutable;

import java.util.Iterator;

import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.test.RichIterableTestCase;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public interface ImmutableCollectionTestCase extends RichIterableTestCase
{
    @Override
    <T> ImmutableCollection<T> newWith(T... elements);

    @Test
    default void ImmutableCollection_sanity_check()
    {
        String s = "";
        if (this.allowsDuplicates())
        {
            assertEquals(2, this.newWith(s, s).size());
        }
        else
        {
            assertThrows(IllegalStateException.class, () -> this.newWith(s, s));
        }

        ImmutableCollection<String> collection = this.newWith(s);
        ImmutableCollection<String> newCollection = collection.newWith(s);
        if (this.allowsDuplicates())
        {
            assertEquals(2, newCollection.size());
            assertEquals(this.newWith(s, s), newCollection);
        }
        else
        {
            assertEquals(1, newCollection.size());
            assertSame(collection, newCollection);
        }
    }

    @Override
    @Test
    default void Iterable_remove()
    {
        ImmutableCollection<Integer> collection = this.newWith(3, 2, 1);
        Iterator<Integer> iterator = collection.iterator();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    default void ImmutableCollection_newWith()
    {
        ImmutableCollection<Integer> immutableCollection = this.newWith(3, 3, 3, 2, 2, 1);
        ImmutableCollection<Integer> newWith = immutableCollection.newWith(4);

        assertEquals(this.newWith(3, 3, 3, 2, 2, 1, 4), newWith);
        assertNotSame(immutableCollection, newWith);
        assertThat(newWith, instanceOf(ImmutableCollection.class));

        ImmutableCollection<Integer> newWith2 = newWith.newWith(4);

        assertEquals(this.newWith(3, 3, 3, 2, 2, 1, 4, 4), newWith2);
    }
}
