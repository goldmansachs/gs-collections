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

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.test.collection.mutable.MutableCollectionTestCase;
import org.junit.Assert;
import org.junit.Test;

public interface MutableSortedNaturalOrderTestCase extends SortedNaturalOrderTestCase, MutableOrderedIterableTestCase, MutableCollectionTestCase
{
    @Test
    @Override
    default void Iterable_remove()
    {
        Iterable<Integer> iterable = this.newWith(1, 1, 1, 2, 2, 3);
        Iterator<Integer> iterator = iterable.iterator();
        iterator.next();
        iterator.remove();
        IterableTestCase.assertEquals(this.newWith(1, 1, 2, 2, 3), iterable);
    }

    @Override
    @Test
    default void MutableCollection_removeIf()
    {
        MutableCollection<Integer> collection1 = this.newWith(1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
        Assert.assertTrue(collection1.removeIf(Predicates.cast(each -> each % 2 == 0)));
        IterableTestCase.assertEquals(this.getExpectedFiltered(1, 1, 3, 3, 5, 5), collection1);

        MutableCollection<Integer> collection2 = this.newWith(1, 2, 3, 4);
        Assert.assertFalse(collection2.removeIf(Predicates.equal(5)));
        Assert.assertTrue(collection2.removeIf(Predicates.greaterThan(0)));
        Assert.assertEquals(this.newWith(), collection2);
        Assert.assertFalse(collection2.removeIf(Predicates.greaterThan(2)));

        Predicate<Object> predicate = null;
        Verify.assertThrows(NullPointerException.class, () -> this.newWith(1, 4, 5, 7).removeIf(predicate));
    }

    @Override
    @Test
    default void MutableCollection_removeIfWith()
    {
        MutableCollection<Integer> collection = this.newWith(1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
        Assert.assertTrue(collection.removeIfWith(Predicates2.<Integer>in(), Lists.immutable.with(5, 3, 1)));
        IterableTestCase.assertEquals(this.getExpectedFiltered(2, 2, 4, 4), collection);
        Verify.assertThrows(NullPointerException.class, () -> this.newWith(7, 4, 5, 1).removeIfWith(null, this));

        MutableCollection<Integer> collection2 = this.newWith(1, 2, 3, 4);
        Assert.assertFalse(collection2.removeIfWith(Predicates2.equal(), 5));
        Assert.assertTrue(collection2.removeIfWith(Predicates2.greaterThan(), 0));
        Assert.assertEquals(this.newWith(), collection2);
        Assert.assertFalse(collection2.removeIfWith(Predicates2.greaterThan(), 2));

        Verify.assertThrows(NullPointerException.class, () -> this.newWith(1, 4, 5, 7).removeIfWith(null, null));
    }
}
