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

package com.gs.collections.test.list.mutable;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.test.IterableTestCase;
import com.gs.collections.test.NoIteratorTestCase;
import com.gs.junit.runners.Java8Runner;
import org.junit.runner.RunWith;

@RunWith(Java8Runner.class)
public class FastListNoIteratorTest implements MutableListTestCase, NoIteratorTestCase
{
    @SafeVarargs
    @Override
    public final <T> MutableList<T> newWith(T... elements)
    {
        MutableList<T> result = new FastListNoIterator<T>();
        IterableTestCase.addAllTo(elements, result);
        return result;
    }

    @Override
    public void Iterable_remove()
    {
        NoIteratorTestCase.super.Iterable_remove();
    }

    @Override
    public void OrderedIterable_next()
    {
        // Not applicable
    }
}
