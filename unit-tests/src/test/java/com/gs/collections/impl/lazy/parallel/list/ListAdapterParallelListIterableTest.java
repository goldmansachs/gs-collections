/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.lazy.parallel.list;

import java.util.LinkedList;

import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.ListAdapter;
import org.junit.Test;

public class ListAdapterParallelListIterableTest
{
    private ParallelListIterable<Integer> classUnderTest()
    {
        return ListAdapter.adapt(new LinkedList<Integer>(Lists.mutable.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4))).asParallel(null, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void forEach()
    {
        this.classUnderTest().forEach(x -> { });
    }
}
