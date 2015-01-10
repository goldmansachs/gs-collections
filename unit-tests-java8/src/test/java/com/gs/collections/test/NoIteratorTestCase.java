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

import org.junit.Test;

public interface NoIteratorTestCase extends RichIterableTestCase
{
    @Override
    @Test
    default void Iterable_remove()
    {
        // Not applicable
    }

    @Override
    @Test
    default void RichIterable_getFirst()
    {
        // Not applicable
    }

    @Override
    @Test
    default void RichIterable_getLast()
    {
        // Not applicable
    }

    @Override
    @Test
    default void RichIterable_iterator_iterationOrder()
    {
        // Not applicable
    }

    @Override
    @Test
    default void Iterable_hasNext()
    {
        // Not applicable
    }

    @Override
    @Test
    default void Iterable_next()
    {
        // Not applicable
    }

    @Override
    @Test
    default void Iterable_next_throws_on_empty()
    {
        // Not applicable
    }

    @Override
    @Test
    default void Iterable_next_throws_at_end()
    {
        // Not applicable
    }
}
