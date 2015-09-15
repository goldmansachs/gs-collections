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

package com.gs.collections.test.bag.immutable.sorted;

import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.SortedBags;
import com.gs.collections.test.IterableTestCase;
import com.gs.junit.runners.Java8Runner;
import org.junit.runner.RunWith;

@RunWith(Java8Runner.class)
public class ImmutableSortedBagImplTest implements ImmutableSortedBagTestCase
{
    @SafeVarargs
    @Override
    public final <T> ImmutableSortedBag<T> newWith(T... elements)
    {
        MutableSortedBag<T> result = SortedBags.mutable.with(Comparators.reverseNaturalOrder());
        IterableTestCase.addAllTo(elements, result);
        return result.toImmutable();
    }
}
