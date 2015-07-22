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

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.test.collection.mutable.MutableCollectionTestCase;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;

public interface UnmodifiableMutableCollectionTestCase extends UnmodifiableCollectionTestCase, MutableCollectionTestCase
{
    @Test
    @Override
    default void Iterable_remove()
    {
        UnmodifiableCollectionTestCase.super.Iterable_remove();
    }

    @Override
    @Test
    default void MutableCollection_sanity_check()
    {
        // Cannot call add()

        String s = "";
        if (this.allowsDuplicates())
        {
            assertEquals(2, this.newWith(s, s).size());
        }
        else
        {
            assertThrows(IllegalStateException.class, () -> this.newWith(s, s));
        }
    }

    @Override
    @Test
    default void MutableCollection_removeIf()
    {
        MutableCollection<Integer> collection = this.newWith(5, 4, 3, 2, 1);
        assertThrows(UnsupportedOperationException.class, () -> collection.removeIf(Predicates.cast(each -> each % 2 == 0)));
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(7, 4, 5, 1).removeIf(Predicates.cast(null)));
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(9, 5, 1).removeIf(Predicates.cast(each -> each % 2 == 0)));
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(6, 4, 2).removeIf(Predicates.cast(each -> each % 2 == 0)));
        assertThrows(UnsupportedOperationException.class, () -> this.<Integer>newWith().removeIf(Predicates.cast(each -> each % 2 == 0)));
    }

    @Override
    @Test
    default void MutableCollection_removeIfWith()
    {
        MutableCollection<Integer> collection = this.newWith(5, 4, 3, 2, 1);
        assertThrows(UnsupportedOperationException.class, () -> collection.removeIfWith(Predicates2.<Integer>in(), Lists.immutable.with(5, 3, 1)));
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(7, 4, 5, 1).removeIfWith(null, this));
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(9, 5, 1).removeIfWith(Predicates2.greaterThan(), 10));
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(6, 4, 2).removeIfWith(Predicates2.greaterThan(), 2));
        assertThrows(UnsupportedOperationException.class, () -> this.<Integer>newWith().removeIfWith(Predicates2.greaterThan(), 2));
    }
}
