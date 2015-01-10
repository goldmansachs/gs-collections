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

package com.gs.collections.test.map;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.UnsortedBag;
import com.gs.collections.api.map.UnsortedMapIterable;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.test.UnorderedIterableTestCase;
import com.gs.collections.test.bag.TransformsToBagTrait;

public interface UnsortedMapIterableTestCase extends MapIterableTestCase, UnorderedIterableTestCase, TransformsToBagTrait
{
    @Override
    <T> UnsortedMapIterable<Object, T> newWith(T... elements);

    @Override
    default <T> UnsortedBag<T> getExpectedFiltered(T... elements)
    {
        return Bags.immutable.with(elements);
    }

    @Override
    default <T> MutableBag<T> newMutableForFilter(T... elements)
    {
        return Bags.mutable.with(elements);
    }
}
