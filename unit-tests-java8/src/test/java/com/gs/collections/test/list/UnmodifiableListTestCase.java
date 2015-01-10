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

package com.gs.collections.test.list;

import java.util.List;

import com.gs.collections.test.UnmodifiableCollectionTestCase;
import org.junit.Test;

public interface UnmodifiableListTestCase extends UnmodifiableCollectionTestCase, ListTestCase
{
    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void List_set()
    {
        List<Integer> list = this.newWith(1, 2, 3);
        list.set(1, 4);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void List_set_negative()
    {
        List<Integer> list = this.newWith(1, 2, 3);
        list.set(-1, 4);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    default void List_set_out_of_bounds()
    {
        List<Integer> list = this.newWith(1, 2, 3);
        list.set(4, 4);
    }
}
