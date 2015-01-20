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

package com.gs.collections.test.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.test.CollectionTestCase;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertPostSerializedEqualsAndHashCode;
import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public interface SetTestCase extends CollectionTestCase
{
    @Override
    <T> Set<T> newWith(T... elements);

    @Override
    @Test
    default void Object_PostSerializedEqualsAndHashCode()
    {
        Iterable<Integer> iterable = this.newWith(3, 2, 1);
        Object deserialized = SerializeTestHelper.serializeDeserialize(iterable);
        Assert.assertNotSame(iterable, deserialized);
    }

    @Override
    @Test
    default void Object_equalsAndHashCode()
    {
        assertPostSerializedEqualsAndHashCode(this.newWith(3, 2, 1));

        assertNotEquals(this.newWith(4, 3, 2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(4, 3, 2, 1));

        assertNotEquals(this.newWith(2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(2, 1));

        assertNotEquals(this.newWith(4, 2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(4, 2, 1));
    }

    @Override
    @Test
    default void Iterable_next()
    {
        Set<Integer> iterable = this.newWith(3, 2, 1);

        MutableSet<Integer> mutableSet = Sets.mutable.with();

        Iterator<Integer> iterator = iterable.iterator();
        while (iterator.hasNext())
        {
            Integer integer = iterator.next();
            assertTrue(mutableSet.add(integer));
        }

        assertEquals(iterable, mutableSet);
        assertFalse(iterator.hasNext());
    }

    @Override
    @Test
    default void Iterable_remove()
    {
        Set<Integer> set = this.newWith(3, 2, 1);
        Iterator<Integer> iterator = set.iterator();
        iterator.next();
        iterator.remove();
        assertThat(set, isOneOf(
                this.newWith(1, 2),
                this.newWith(1, 3),
                this.newWith(2, 3)));
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
