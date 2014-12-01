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
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.factory.HashingStrategySets;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.immutable.AbstractImmutableEmptySetTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableEmptySetWithHashingStrategyTest extends AbstractImmutableEmptySetTestCase
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
    protected ImmutableSet<Integer> classUnderTest()
    {
        return new ImmutableEmptySetWithHashingStrategy<>(HASHING_STRATEGY);
    }

    @Override
    @Test
    public void newWithout()
    {
        Assert.assertEquals(
                HashingStrategySets.immutable.of(HASHING_STRATEGY),
                HashingStrategySets.immutable.of(HASHING_STRATEGY).newWithout(1));
        Assert.assertEquals(
                HashingStrategySets.immutable.of(HASHING_STRATEGY),
                HashingStrategySets.immutable.of(HASHING_STRATEGY).newWithoutAll(Interval.oneTo(3)));
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        ImmutableSet<Integer> immutable = this.classUnderTest();
        MutableSet<Integer> mutable = UnifiedSet.newSet(immutable);
        Verify.assertEqualsAndHashCode(mutable, immutable);
        Verify.assertPostSerializedEqualsAndHashCode(immutable);
        Assert.assertNotEquals(FastList.newList(mutable), immutable);
    }
}
