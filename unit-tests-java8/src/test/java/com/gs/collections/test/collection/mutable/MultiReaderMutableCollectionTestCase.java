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

import com.gs.collections.impl.collection.mutable.AbstractMultiReaderMutableCollection;
import org.junit.Test;

public interface MultiReaderMutableCollectionTestCase extends MutableCollectionTestCase
{
    @Override
    <T> AbstractMultiReaderMutableCollection<T> newWith(T... elements);

    @Test(expected = UnsupportedOperationException.class)
    default void Iterable_iterator_throws()
    {
        this.newWith(3, 2, 1).iterator();
    }

    @Test
    @Override
    default void Iterable_remove()
    {
        // Multi-reader collections don't support iterator()
    }

    @Test
    @Override
    default void Iterable_next()
    {
        // Multi-reader collections don't support iterator()
    }

    @Test
    @Override
    default void Iterable_hasNext()
    {
        // Multi-reader collections don't support iterator()
    }

    @Test
    @Override
    default void Iterable_next_throws_at_end()
    {
        // Multi-reader collections don't support iterator()
    }

    @Test
    @Override
    default void Iterable_next_throws_on_empty()
    {
        // Multi-reader collections don't support iterator()
    }

    @Test
    @Override
    default void RichIterable_getFirst()
    {
        // Does not support iterator outside withReadLockAndDelegate
    }

    @Test
    @Override
    default void RichIterable_getLast()
    {
        // Does not support iterator outside withReadLockAndDelegate
    }
}
