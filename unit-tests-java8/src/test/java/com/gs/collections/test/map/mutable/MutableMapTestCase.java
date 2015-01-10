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

package com.gs.collections.test.map.mutable;

import java.util.Iterator;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.test.MutableUnorderedIterableTestCase;
import com.gs.collections.test.map.UnsortedMapIterableTestCase;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;

public interface MutableMapTestCase extends UnsortedMapIterableTestCase, MutableUnorderedIterableTestCase
{
    @Override
    <T> MutableMap<Object, T> newWith(T... elements);

    @Override
    default void Iterable_remove()
    {
        MutableMap<Object, Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);
        Iterator<Integer> iterator = iterable.iterator();
        iterator.next();
        iterator.remove();
        assertEquals(this.allowsDuplicates() ? 5 : 2, Iterate.sizeOf(iterable));
        assertThat(iterable.toBag(), isOneOf(
                this.getExpectedFiltered(3, 3, 3, 2, 2),
                this.getExpectedFiltered(3, 3, 3, 2, 1),
                this.getExpectedFiltered(3, 3, 2, 2, 1)));
    }
}
