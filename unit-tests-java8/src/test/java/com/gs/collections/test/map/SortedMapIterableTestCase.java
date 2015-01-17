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

import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.sorted.SortedMapIterable;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.test.OrderedIterableTestCase;
import com.gs.collections.test.list.TransformsToListTrait;
import org.junit.Assert;
import org.junit.Test;

public interface SortedMapIterableTestCase extends MapIterableTestCase, OrderedIterableTestCase, TransformsToListTrait
{
    @Override
    <T> SortedMapIterable<Object, T> newWith(T... elements);

    @Override
    default <T> ListIterable<T> getExpectedFiltered(T... elements)
    {
        return Lists.immutable.with(elements);
    }

    @Override
    default <T> MutableList<T> newMutableForFilter(T... elements)
    {
        return Lists.mutable.with(elements);
    }

    @Override
    @Test
    default void RichIterable_toString()
    {
        Assert.assertEquals(
                "{10=4, 9=4, 8=4, 7=4, 6=3, 5=3, 4=3, 3=2, 2=2, 1=1}",
                this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1).toString());
    }
}
