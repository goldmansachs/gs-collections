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

import com.gs.collections.api.set.ParallelSetIterable;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class ParallelUnsortedSetIterableTest extends AbstractParallelUnsortedSetIterableTestCase
{
    @Override
    protected ParallelSetIterable<Integer> classUnderTest()
    {
        return UnifiedSet.newSetWith(1, 2, 3, 4).asParallel(this.executorService, 2);
    }

//    @Test
//    public void groupBy()
//    {
//        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
//        {
//            public Boolean valueOf(Integer object)
//            {
//                return IntegerPredicates.isOdd().accept(object);
//            }
//        };
//
//        MutableMap<Boolean, RichIterable<Integer>> expected =
//                UnifiedMap.<Boolean, RichIterable<Integer>>newWithKeysValues(
//                        Boolean.TRUE, UnifiedSet.newSetWith(1, 3, 5, 7),
//                        Boolean.FALSE, UnifiedSet.newSetWith(2, 4, 6));
//
//        Multimap<Boolean, Integer> multimap =
//                this.newWith(1, 2, 3, 4, 5, 6, 7).groupBy(isOddFunction);
//        Assert.assertEquals(expected, multimap.toMap());
//
//        Multimap<Boolean, Integer> multimap2 =
//                this.newWith(1, 2, 3, 4, 5, 6, 7).groupBy(isOddFunction, UnifiedSetMultimap.<Boolean, Integer>newMultimap());
//        Assert.assertEquals(expected, multimap2.toMap());
//    }
//
//    @Test
//    public void groupByEach()
//    {
//        MutableMultimap<Integer, Integer> expected = UnifiedSetMultimap.newMultimap();
//        for (int i = 1; i < 8; i++)
//        {
//            expected.putAll(-i, Interval.fromTo(i, 7));
//        }
//
//        Multimap<Integer, Integer> actual =
//                this.newWith(1, 2, 3, 4, 5, 6, 7).groupByEach(new NegativeIntervalFunction());
//        Assert.assertEquals(expected, actual);
//
//        Multimap<Integer, Integer> actualWithTarget =
//                this.newWith(1, 2, 3, 4, 5, 6, 7).groupByEach(new NegativeIntervalFunction(), UnifiedSetMultimap.<Integer, Integer>newMultimap());
//        Assert.assertEquals(expected, actualWithTarget);
//    }
}
