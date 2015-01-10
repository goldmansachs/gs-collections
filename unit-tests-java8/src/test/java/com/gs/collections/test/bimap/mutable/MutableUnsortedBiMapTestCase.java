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

package com.gs.collections.test.bimap.mutable;

import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.test.MutableUnorderedIterableTestCase;
import com.gs.collections.test.bimap.UnsortedBiMapTestCase;

public interface MutableUnsortedBiMapTestCase extends UnsortedBiMapTestCase, MutableUnorderedIterableTestCase
{
    @Override
    <T> MutableBiMap<Object, T> newWith(T... elements);

    @Override
    default void Iterable_remove()
    {
        UnsortedBiMapTestCase.super.Iterable_remove();
    }

    @Override
    default void Iterable_next()
    {
        UnsortedBiMapTestCase.super.Iterable_next();
    }

    @Override
    default void RichIterable_toArray()
    {
        UnsortedBiMapTestCase.super.RichIterable_toArray();
    }
}
