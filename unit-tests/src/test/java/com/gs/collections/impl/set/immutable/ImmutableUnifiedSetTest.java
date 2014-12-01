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

package com.gs.collections.impl.set.immutable;

import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.math.SumProcedure;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableUnifiedSet}.
 */
public class ImmutableUnifiedSetTest extends AbstractImmutableUnifiedSetTestCase
{
    @Override
    public ImmutableSet<Integer> newSet(Integer... elements)
    {
        return ImmutableUnifiedSet.newSetWith(elements);
    }

    @Override
    public ImmutableSet<Integer> newSetWith(int one, int two)
    {
        return ImmutableUnifiedSet.newSetWith(one, two);
    }

    @Override
    public ImmutableSet<Integer> newSetWith(int one, int two, int three)
    {
        return ImmutableUnifiedSet.newSetWith(one, two, three);
    }

    @Override
    public ImmutableSet<Integer> newSetWith(int... littleElements)
    {
        Integer[] bigElements = new Integer[littleElements.length];
        for (int i = 0; i < littleElements.length; i++)
        {
            bigElements[i] = littleElements[i];
        }
        return ImmutableUnifiedSet.newSetWith(bigElements);
    }

    @Override
    @Test
    public void newCollection()
    {
        super.newCollection();
        ImmutableSet<Integer> set = ImmutableUnifiedSet.newSet(UnifiedSet.<Integer>newSet());
        Assert.assertTrue(set.isEmpty());
        Verify.assertSize(0, set);
    }

    @Test
    public void getBatchCount()
    {
        BatchIterable<Integer> integerBatchIterable = (BatchIterable<Integer>) this.newSet(1, 2, 3, 4, 5, 6);
        Assert.assertEquals(2, integerBatchIterable.getBatchCount(3));
    }

    @Test
    public void batchForEach()
    {
        Sum sum = new IntegerSum(0);
        BatchIterable<Integer> integerBatchIterable = (BatchIterable<Integer>) this.newSet(1, 2, 3, 4, 5);
        integerBatchIterable.batchForEach(new SumProcedure<>(sum), 0, 1);
        Assert.assertEquals(15, sum.getValue());
    }
}
