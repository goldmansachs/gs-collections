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
import com.gs.collections.api.map.primitive.ObjectBooleanMap;
import com.gs.collections.impl.block.factory.HashingStrategies;

/**
 * JUnit test for {@link ObjectBooleanHashMapWithHashingStrategy#keyValuesView()}.
 */
public class ObjectBooleanHashMapWithHashingStrategyKeyValuesViewTest extends AbstractObjectBooleanMapKeyValuesViewTestCase
{
    private static final HashingStrategy<Integer> INTEGER_HASHING_STRATEGY = HashingStrategies.nullSafeHashingStrategy(new HashingStrategy<Integer>()
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
    public <T> ObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2, T key3, boolean value3)
    {
        return ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<T>defaultStrategy()), key1, value1, key2, value2, key3, value3);
    }

    @Override
    public <T> ObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2)
    {
        return ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<T>defaultStrategy()), key1, value1, key2, value2);
    }

    @Override
    public <T> ObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1)
    {
        return ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<T>defaultStrategy()), key1, value1);
    }

    @Override
    public ObjectBooleanMap<Integer> newEmpty()
    {
        return ObjectBooleanHashMapWithHashingStrategy.newMap(INTEGER_HASHING_STRATEGY);
    }
}
