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

package com.gs.collections.test.collection.mutable;

import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.test.IterableTestCase;
import com.gs.collections.test.RichIterableUniqueTestCase;
import org.junit.Assert;
import org.junit.Test;

public interface MutableCollectionUniqueTestCase extends MutableCollectionTestCase, RichIterableUniqueTestCase
{
    @Override
    <T> MutableCollection<T> newWith(T... elements);

    @Override
    @Test
    default void MutableCollection_removeIf()
    {
        MutableCollection<Integer> collection1 = this.newWith(5, 4, 3, 2, 1);
        Assert.assertTrue(collection1.removeIf(Predicates.cast(each -> each % 2 == 0)));
        IterableTestCase.assertEquals(this.getExpectedFiltered(5, 3, 1), collection1);

        MutableCollection<Integer> collection2 = this.newWith(1, 2, 3, 4);
        Assert.assertFalse(collection2.removeIf(Predicates.equal(5)));
        Assert.assertTrue(collection2.removeIf(Predicates.greaterThan(0)));
        Assert.assertFalse(collection2.removeIf(Predicates.greaterThan(2)));

        MutableCollection<Integer> collection3 = this.newWith();
        Assert.assertFalse(collection3.removeIf(Predicates.equal(5)));

        Predicate<Object> predicate = null;
        Verify.assertThrows(NullPointerException.class, () -> this.newWith(7, 4, 5, 1).removeIf(predicate));
    }

    @Override
    @Test
    default void MutableCollection_removeIfWith()
    {
        MutableCollection<Integer> collection = this.newWith(5, 4, 3, 2, 1);
        collection.removeIfWith(Predicates2.<Integer>in(), Lists.immutable.with(5, 3, 1));
        IterableTestCase.assertEquals(this.getExpectedFiltered(4, 2), collection);

        MutableCollection<Integer> collection2 = this.newWith(1, 2, 3, 4);
        Assert.assertFalse(collection2.removeIf(Predicates.equal(5)));
        Assert.assertTrue(collection2.removeIf(Predicates.greaterThan(0)));
        Assert.assertFalse(collection2.removeIf(Predicates.greaterThan(2)));

        MutableCollection<Integer> collection3 = this.newWith();
        Assert.assertFalse(collection3.removeIf(Predicates.equal(5)));

        Verify.assertThrows(NullPointerException.class, () -> this.newWith(7, 4, 5, 1).removeIf(Predicates.cast(null)));
    }

    @Override
    @Test
    default void MutableCollection_injectIntoWith()
    {
        MutableCollection<Integer> collection = this.newWith(4, 3, 2, 1);
        IterableTestCase.assertEquals(Integer.valueOf(51), collection.injectIntoWith(1, new Function3<Integer, Integer, Integer, Integer>()
        {
            @Override
            public Integer value(Integer argument1, Integer argument2, Integer argument3)
            {
                return argument1 + argument2 + argument3;
            }
        }, 10));
    }
}
