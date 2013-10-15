/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.bimap.mutable;

import com.gs.collections.api.bimap.MutableBiMap;

public class HashBiMapValuesTest extends AbstractMutableBiMapValuesTestCase
{
    @Override
    public MutableBiMap<Float, String> newMapWithKeysValues(float key1, String value1, float key2, String value2)
    {
        return HashBiMap.newWithKeysValues(key1, value1, key2, value2);
    }

    @Override
    public HashBiMap<Float, Integer> newMapWithKeysValues(float key1, Integer value1, float key2, Integer value2, float key3, Integer value3)
    {
        return HashBiMap.newWithKeysValues(key1, value1, key2, value2, key3, value3);
    }

    @Override
    public HashBiMap<Float, Integer> newMapWithKeysValues(float key1, Integer value1, float key2, Integer value2, float key3, Integer value3, float key4, Integer value4)
    {
        return HashBiMap.newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4);
    }
}
