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

package com.gs.collections.impl.set.strategy.immutable;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.math.SumProcedure;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.set.immutable.AbstractImmutableUnifiedSetTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableUnifiedSetWithHashingStrategy}.
 */
public class ImmutableUnifiedSetWithHashingStrategyTest extends AbstractImmutableUnifiedSetTestCase
{
    //Not using the static factor method in order to have concrete types for test cases
    private static final HashingStrategy<Integer> HASHING_STRATEGY = HashingStrategies.nullSafeHashingStrategy(new HashingStrategy<Integer>()
    {
        public int computeHashCode(Integer object)
        {
            return object.hashCode();
        }

        public boolean equals(Integer object1, Integer object2)
        {
            return object1.equals(object2);
        }
    });

    @Override
    public ImmutableSet<Integer> newSet(Integer... elements)
    {
        return ImmutableUnifiedSetWithHashingStrategy.newSetWith(HASHING_STRATEGY, elements);
    }

    @Override
    public ImmutableSet<Integer> newSetWith(int one, int two)
    {
        return ImmutableUnifiedSetWithHashingStrategy.newSetWith(HASHING_STRATEGY, one, two);
    }

    @Override
    public ImmutableSet<Integer> newSetWith(int one, int two, int three)
    {
        return ImmutableUnifiedSetWithHashingStrategy.newSetWith(HASHING_STRATEGY, one, two, three);
    }

    @Override
    public ImmutableSet<Integer> newSetWith(int... littleElements)
    {
        Integer[] bigElements = new Integer[littleElements.length];
        for (int i = 0; i < littleElements.length; i++)
        {
            bigElements[i] = littleElements[i];
        }
        return ImmutableUnifiedSetWithHashingStrategy.newSetWith(HASHING_STRATEGY, bigElements);
    }

    @Override
    @Test
    public void newCollection()
    {
        super.newCollection();
        ImmutableSet<Integer> set = ImmutableUnifiedSetWithHashingStrategy.newSet(HASHING_STRATEGY, UnifiedSet.<Integer>newSet());
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

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        ImmutableSet<Integer> deserialized = SerializeTestHelper.serializeDeserialize(this.newSet(1, 2, 3, 4, 5));
        Verify.assertInstanceOf(ImmutableUnifiedSetWithHashingStrategy.class, deserialized);
    }
}
