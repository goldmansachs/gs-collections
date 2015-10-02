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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.gs.collections.api.iterator.MutableBooleanIterator;
import com.gs.collections.api.map.primitive.MutableObjectBooleanMap;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ObjectBooleanHashMap#keySet()}.
 */
public class ObjectBooleanHashMapKeySetTest extends ObjectBooleanHashMapKeySetTestCase
{
    @Override
    public MutableObjectBooleanMap<String> newMapWithKeysValues(String key1, boolean value1)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1);
    }

    @Override
    public MutableObjectBooleanMap<String> newMapWithKeysValues(String key1, boolean value1, String key2, boolean value2)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2);
    }

    @Override
    public MutableObjectBooleanMap<String> newMapWithKeysValues(String key1, boolean value1, String key2, boolean value2, String key3, boolean value3)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2, key3, value3);
    }

    @Override
    public MutableObjectBooleanMap<String> newMapWithKeysValues(String key1, boolean value1, String key2, boolean value2, String key3, boolean value3, String key4, boolean value4)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Override
    public MutableObjectBooleanMap<String> newEmptyMap()
    {
        return ObjectBooleanHashMap.newMap();
    }
}
