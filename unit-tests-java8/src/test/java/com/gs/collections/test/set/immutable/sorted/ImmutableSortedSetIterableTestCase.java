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

package com.gs.collections.test.set.immutable.sorted;

import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.test.collection.immutable.ImmutableCollectionTestCase;
import com.gs.collections.test.set.sorted.SortedSetIterableTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public interface ImmutableSortedSetIterableTestCase extends SortedSetIterableTestCase, ImmutableCollectionTestCase
{
    @Override
    <T> ImmutableSortedSet<T> newWith(T... elements);

    @Override
    @Test
    default void ImmutableCollection_newWith()
    {
        ImmutableSortedSet<Integer> immutableCollection = this.newWith(3, 2, 1);
        ImmutableSortedSet<Integer> newWith = immutableCollection.newWith(4);

        assertEquals(this.newWith(4, 3, 2, 1).castToSortedSet(), newWith.castToSortedSet());
        assertNotSame(immutableCollection, newWith);
        assertThat(newWith, instanceOf(ImmutableSortedSet.class));

        ImmutableSortedSet<Integer> newWith2 = newWith.newWith(4);
        assertSame(newWith, newWith2);
        assertEquals(this.newWith(4, 3, 2, 1).castToSortedSet(), newWith2.castToSortedSet());
    }
}
