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

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.impl.factory.Sets;
import org.junit.Test;

import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;

public interface UnsortedSetIterableTestCase extends SetIterableTestCase, TransformsToUnsortedSetTrait, UnsortedSetLikeTestTrait
{
    @Override
    <T> UnsortedSetIterable<T> newWith(T... elements);

    @Override
    default <T> UnsortedSetIterable<T> getExpectedTransformed(T... elements)
    {
        return Sets.immutable.with(elements);
    }

    @Override
    default <T> MutableSet<T> newMutableForTransform(T... elements)
    {
        return Sets.mutable.with(elements);
    }

    @Override
    @Test
    default void RichIterable_toString()
    {
        RichIterable<Integer> iterable = this.newWith(3, 2, 1);
        assertThat(iterable.toString(), isOneOf(
                "[3, 2, 1]",
                "[3, 1, 2]",
                "[2, 3, 1]",
                "[2, 1, 3]",
                "[1, 3, 2]",
                "[1, 2, 3]"));
    }

    @Override
    @Test
    default void RichIterable_toArray()
    {
        UnsortedSetLikeTestTrait.super.RichIterable_toArray();
    }
}
