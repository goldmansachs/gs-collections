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

package com.gs.collections.test.list.immutable;

import java.util.List;

import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.test.collection.immutable.ImmutableCollectionTestCase;
import com.gs.collections.test.list.ListIterableTestCase;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public interface ImmutableListTestCase extends ImmutableCollectionTestCase, ListIterableTestCase
{
    @Override
    <T> ImmutableList<T> newWith(T... elements);

    @Test
    default void ImmutableList_castToList()
    {
        ImmutableList<Integer> immutableList = this.newWith(3, 3, 3, 2, 2, 1);
        List<Integer> list = immutableList.castToList();
        assertSame(immutableList, list);
    }
}
