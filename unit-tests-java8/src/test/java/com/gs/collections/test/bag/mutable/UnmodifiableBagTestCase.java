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
import com.gs.collections.test.UnmodifiableMutableCollectionTestCase;
import org.junit.Test;

public interface UnmodifiableBagTestCase extends UnmodifiableMutableCollectionTestCase, MutableBagTestCase
{
    @Override
    @Test
    default void Iterable_remove()
    {
        UnmodifiableMutableCollectionTestCase.super.Iterable_remove();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void MutableBag_addOccurrences()
    {
        MutableBag<Integer> mutableBag = this.newWith(1, 2, 2, 3, 3, 3);
        mutableBag.addOccurrences(4, 4);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void MutableBag_removeOccurrences()
    {
        MutableBag<Integer> mutableBag = this.newWith(1, 2, 2, 3, 3, 3);
        mutableBag.removeOccurrences(4, 4);
    }
}
