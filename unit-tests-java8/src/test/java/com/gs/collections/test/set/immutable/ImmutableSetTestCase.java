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

package com.gs.collections.test.set.immutable;

import java.util.Set;

import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.test.collection.immutable.ImmutableCollectionUniqueTestCase;
import com.gs.collections.test.set.UnsortedSetIterableTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public interface ImmutableSetTestCase extends ImmutableCollectionUniqueTestCase, UnsortedSetIterableTestCase
{
    @Override
    <T> ImmutableSet<T> newWith(T... elements);

    @Override
    @Test
    default void Iterable_remove()
    {
        ImmutableCollectionUniqueTestCase.super.Iterable_remove();
    }

    @Override
    default void ImmutableCollection_newWith()
    {
        ImmutableCollection<Integer> immutableCollection = this.newWith(3, 2, 1);
        ImmutableCollection<Integer> newWith = immutableCollection.newWith(4);

        assertEquals(this.newWith(3, 2, 1, 4), newWith);
        assertNotSame(immutableCollection, newWith);
        assertThat(newWith, instanceOf(ImmutableCollection.class));

        ImmutableCollection<Integer> newWith2 = newWith.newWith(4);

        assertEquals(this.newWith(3, 2, 1, 4), newWith2);
        assertSame(newWith, newWith2);
    }

    @Test
    default void ImmutableSet_castToSet()
    {
        ImmutableSet<Integer> immutableSet = this.newWith(3, 2, 1);
        Set<Integer> set = immutableSet.castToSet();
        assertSame(immutableSet, set);
    }
}
