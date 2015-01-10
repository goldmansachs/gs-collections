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

import java.util.Comparator;

import com.gs.collections.api.ordered.SortedIterable;
import com.gs.collections.impl.block.factory.Comparators;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static org.junit.Assert.assertSame;

public interface SortedIterableTestCase extends OrderedIterableTestCase
{
    @Override
    <T> SortedIterable<T> newWith(T... elements);

    @Test
    default void SortedIterable_comparator()
    {
        SortedIterable<Object> objects = this.newWith();
        Comparator<? super Object> comparator = objects.comparator();
        assertSame(Comparators.reverseNaturalOrder(), comparator);
    }

    @Override
    default void RichIterable_min_non_comparable()
    {
        assertThrows(ClassCastException.class, () -> this.newWith(new Object()));
    }

    @Override
    default void RichIterable_max_non_comparable()
    {
        assertThrows(ClassCastException.class, () -> this.newWith(new Object()));
    }

    @Override
    default void RichIterable_min_comparator_non_comparable()
    {
        assertThrows(ClassCastException.class, () -> this.newWith(new Object()));
    }

    @Override
    default void RichIterable_max_comparator_non_comparable()
    {
        assertThrows(ClassCastException.class, () -> this.newWith(new Object()));
    }
}
