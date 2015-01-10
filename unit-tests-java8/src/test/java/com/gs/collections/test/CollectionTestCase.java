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

import java.util.Collection;

import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public interface CollectionTestCase extends IterableTestCase
{
    @Override
    <T> Collection<T> newWith(T... elements);

    @Test
    default void Collection_size()
    {
        if (this.allowsDuplicates())
        {
            Collection<Integer> collection = this.newWith(3, 3, 3, 2, 2, 1);
            assertThat(collection, hasSize(6));
        }
        else
        {
            Collection<Integer> collection = this.newWith(3, 2, 1);
            assertThat(collection, hasSize(3));
        }
        assertThat(this.newWith(), hasSize(0));
    }

    @Test
    default void Collection_contains()
    {
        Collection<Integer> collection = this.newWith(3, 2, 1);
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
        assertTrue(collection.contains(3));
        assertFalse(collection.contains(4));
    }

    @Test
    default void Collection_add()
    {
        Collection<Integer> collection = this.newWith(3, 2, 1);
        assertTrue(collection.add(4));
        assertEquals(this.allowsDuplicates(), collection.add(4));
    }

    @Test
    default void Collection_clear()
    {
        Collection<Integer> collection = this.newWith(1, 2, 3);
        assertThat(collection, is(not(empty())));
        collection.clear();
        assertThat(collection, is(empty()));
        assertThat(collection, hasSize(0));
        collection.clear();
        assertThat(collection, is(empty()));
    }
}
