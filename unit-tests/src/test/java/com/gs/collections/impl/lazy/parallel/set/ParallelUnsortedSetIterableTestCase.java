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

package com.gs.collections.impl.lazy.parallel.set;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.lazy.parallel.ParallelIterableTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public abstract class ParallelUnsortedSetIterableTestCase extends ParallelIterableTestCase
{
    @Override
    protected abstract ParallelUnsortedSetIterable<Integer> classUnderTest();

    @Override
    protected abstract ParallelUnsortedSetIterable<Integer> newWith(Integer... littleElements);

    @Override
    protected MutableSet<Integer> getExpected()
    {
        return UnifiedSet.newSetWith(1, 2, 3, 4);
    }

    @Override
    protected MutableSet<Integer> getExpectedWith(Integer... littleElements)
    {
        return UnifiedSet.newSetWith(littleElements);
    }

    @Override
    protected RichIterable<Integer> getExpectedCollect()
    {
        return HashBag.newBagWith(1, 2, 3, 4);
    }

    @Override
    protected boolean isOrdered()
    {
        return false;
    }

    @Override
    protected boolean isUnique()
    {
        return true;
    }
}
