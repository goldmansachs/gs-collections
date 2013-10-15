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

public class HashBiMapInverseKeySetTest extends AbstractMutableBiMapKeySetTestCase
{
    @Override
    protected MutableBiMap<String, Integer> newMapWithKeysValues(String key1, int value1, String key2, int value2, String key3, int value3)
    {
        return HashBiMap.newWithKeysValues(value1, key1, value2, key2, value3, key3).inverse();
    }

    @Override
    protected MutableBiMap<String, Integer> newMapWithKeysValues(String key1, int value1, String key2, int value2, String key3, int value3, String key4, int value4)
    {
        return HashBiMap.newWithKeysValues(value1, key1, value2, key2, value3, key3, value4, key4).inverse();
    }
}
