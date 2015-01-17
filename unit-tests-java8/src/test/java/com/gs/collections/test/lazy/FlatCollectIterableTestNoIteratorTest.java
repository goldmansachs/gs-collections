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

package com.gs.collections.test.lazy;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.impl.lazy.FlatCollectIterable;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.test.LazyNoIteratorTestCase;
import com.gs.collections.test.list.mutable.FastListNoIterator;
import com.gs.junit.runners.Java8Runner;
import org.junit.runner.RunWith;

@RunWith(Java8Runner.class)
public class FlatCollectIterableTestNoIteratorTest implements LazyNoIteratorTestCase
{
    @Override
    public <T> LazyIterable<T> newWith(T... elements)
    {
        return new FlatCollectIterable<>(new FastListNoIterator<T>().with(elements), each -> FastList.newListWith(each));
    }
}
