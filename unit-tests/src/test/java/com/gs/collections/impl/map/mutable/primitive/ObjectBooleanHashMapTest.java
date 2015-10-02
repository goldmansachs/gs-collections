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

package com.gs.collections.impl.map.mutable.primitive;

public class ObjectBooleanHashMapTest extends ObjectBooleanHashMapTestCase
{
    private final ObjectBooleanHashMap<String> map = this.classUnderTest();

    @Override
    protected ObjectBooleanHashMap<String> classUnderTest()
    {
        return ObjectBooleanHashMap.newWithKeysValues("0", true, "1", true, "2", false);
    }

    @Override
    protected <T> ObjectBooleanHashMap<T> newWithKeysValues(T key1, boolean value1)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1);
    }

    @Override
    protected <T> ObjectBooleanHashMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2);
    }

    @Override
    protected <T> ObjectBooleanHashMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2, T key3, boolean value3)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2, key3, value3);
    }

    @Override
    protected <T> ObjectBooleanHashMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2, T key3, boolean value3, T key4, boolean value4)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Override
    protected <T> ObjectBooleanHashMap<T> getEmptyMap()
    {
        return new ObjectBooleanHashMap<>();
    }

    @Override
    protected <T> ObjectBooleanHashMap<T> newMapWithInitialCapacity(int size)
    {
        return new ObjectBooleanHashMap<>(size);
    }

    @Override
    protected Class<?> getTargetClass()
    {
        return ObjectBooleanHashMap.class;
    }
}
