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

package com.gs.collections.test.list.mutable;

import java.util.Random;

import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.test.UnmodifiableMutableCollectionTestCase;
import com.gs.collections.test.list.UnmodifiableListTestCase;
import org.junit.Test;

public interface UnmodifiableMutableListTestCase extends UnmodifiableMutableCollectionTestCase, UnmodifiableListTestCase, MutableListTestCase
{
    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void MutableList_sortThis()
    {
        this.newWith(5, 1, 4, 2, 3).sortThis();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void MutableList_shuffleThis()
    {
        this.newWith(5, 1, 4, 2, 3).shuffleThis();
        this.newWith(5, 1, 4, 2, 3).shuffleThis(new Random(8));
    }

    @Override
    @Test
    default void Iterable_remove()
    {
        UnmodifiableMutableCollectionTestCase.super.Iterable_remove();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void MutableList_sortThis_comparator()
    {
        this.newWith(5, 1, 4, 2, 3).sortThis(Comparators.reverseNaturalOrder());
    }
}
