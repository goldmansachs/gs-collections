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

import com.gs.collections.api.set.SetIterable;
import com.gs.collections.test.RichIterableUniqueTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public interface SetIterableTestCase extends RichIterableUniqueTestCase
{
    @Override
    <T> SetIterable<T> newWith(T... elements);

    @Override
    @Test
    default void RichIterable_toArray()
    {
        Object[] array = this.newWith(3, 2, 1).toArray();
        assertArrayEquals(new Object[]{3, 2, 1}, array);
    }

    @Test
    default void SetIterable_union()
    {
        SetIterable<Integer> union = this.newWith(3, 2, 1).union(this.newWith(5, 4, 3));
        assertEquals(this.newWith(5, 4, 3, 2, 1), union);
    }
}
