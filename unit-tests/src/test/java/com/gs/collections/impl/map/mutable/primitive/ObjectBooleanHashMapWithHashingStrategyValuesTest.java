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

package com.gs.collections.impl.map.mutable.primitive;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.collection.mutable.primitive.AbstractMutableBooleanCollectionTestCase;
import com.gs.collections.impl.list.mutable.FastList;

public class ObjectBooleanHashMapWithHashingStrategyValuesTest extends ObjectBooleanHashMapValuesTestCase
{
    private static final HashingStrategy<Integer> INT_MOD_10_STRATEGY = HashingStrategies.nullSafeHashingStrategy(new HashingStrategy<Integer>()
    {
        public int computeHashCode(Integer object)
        {
            return object.intValue() % 10;
        }

        public boolean equals(Integer object1, Integer object2)
        {
            return computeHashCode(object1) == computeHashCode(object2);
        }
    });

    @Override
    protected MutableBooleanCollection classUnderTest()
    {
        return ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(INT_MOD_10_STRATEGY, 1, true, 2, false, 3, true).values();
    }

    @Override
    protected MutableBooleanCollection newWith(boolean... elements)
    {
        ObjectBooleanHashMapWithHashingStrategy<Integer> map = new ObjectBooleanHashMapWithHashingStrategy<>(INT_MOD_10_STRATEGY);
        for (int i = 0; i < elements.length; i++)
        {
            map.put(i, elements[i]);
        }
        return map.values();
    }

    @Override
    protected MutableBooleanCollection newMutableCollectionWith(boolean... elements)
    {
        return this.newWith(elements);
    }

    @Override
    protected MutableList<Object> newObjectCollectionWith(Object... elements)
    {
        return FastList.newListWith(elements);
    }
}
