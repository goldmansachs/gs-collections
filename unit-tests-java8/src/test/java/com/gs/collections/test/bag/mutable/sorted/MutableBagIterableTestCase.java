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

package com.gs.collections.test.bag.mutable.sorted;

import com.gs.collections.api.bag.MutableBagIterable;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.test.collection.mutable.MutableCollectionTestCase;
import org.junit.Test;

public interface MutableBagIterableTestCase extends MutableCollectionTestCase
{
    @Override
    <T> MutableBagIterable<T> newWith(T... elements);

    @Test
    default void MutableBag_addOccurrences_throws()
    {
        Verify.assertThrows(
                IllegalArgumentException.class,
                () -> this.newWith(1, 2, 2, 3, 3, 3).addOccurrences(4, -1));
    }

    @Test
    default void MutableBag_removeOccurrences_throws()
    {
        Verify.assertThrows(
                IllegalArgumentException.class,
                () -> this.newWith(1, 2, 2, 3, 3, 3).removeOccurrences(4, -1));
    }
}
