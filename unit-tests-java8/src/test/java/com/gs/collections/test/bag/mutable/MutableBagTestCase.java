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

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.test.MutableUnorderedIterableTestCase;
import com.gs.collections.test.bag.UnsortedBagTestCase;
import com.gs.collections.test.bag.mutable.sorted.MutableBagIterableTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public interface MutableBagTestCase extends UnsortedBagTestCase, MutableUnorderedIterableTestCase, MutableBagIterableTestCase
{
    @Override
    <T> MutableBag<T> newWith(T... elements);

    @Test
    default void MutableBag_addOccurrences()
    {
        MutableBag<Integer> mutableBag = this.newWith(1, 2, 2, 3, 3, 3);
        mutableBag.addOccurrences(4, 4);
        assertEquals(Bags.immutable.with(1, 2, 2, 3, 3, 3, 4, 4, 4, 4), mutableBag);
        mutableBag.addOccurrences(1, 2);
        assertEquals(Bags.immutable.with(1, 1, 1, 2, 2, 3, 3, 3, 4, 4, 4, 4), mutableBag);
    }

    @Test
    default void MutableBag_removeOccurrences()
    {
        MutableBag<Integer> mutableBag = this.newWith(1, 2, 2, 3, 3, 3);
        assertFalse(mutableBag.removeOccurrences(4, 4));
        assertEquals(Bags.immutable.with(1, 2, 2, 3, 3, 3), mutableBag);
        assertTrue(mutableBag.removeOccurrences(1, 2));
        assertEquals(Bags.immutable.with(2, 2, 3, 3, 3), mutableBag);
        assertTrue(mutableBag.removeOccurrences(3, 2));
        assertEquals(Bags.immutable.with(2, 2, 3), mutableBag);
        assertTrue(mutableBag.removeOccurrences(2, 1));
        assertEquals(Bags.immutable.with(2, 3), mutableBag);
    }
}
