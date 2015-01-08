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

package com.gs.collections.impl.bimap.immutable;

import com.gs.collections.api.bimap.ImmutableBiMap;
import com.gs.collections.impl.bimap.mutable.HashBiMap;
import com.gs.collections.impl.factory.BiMaps;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class ImmutableHashBiMapInverseTest extends AbstractImmutableBiMapTestCase
{
    @Override
    protected ImmutableBiMap<Integer, String> classUnderTest()
    {
        return BiMaps.immutable.with("1", 1, "2", 2, "3", 3, "4", 4).inverse();
    }

    @Override
    protected ImmutableBiMap<Integer, String> newEmpty()
    {
        return BiMaps.immutable.<String, Integer>empty().inverse();
    }

    @Override
    protected ImmutableBiMap<Integer, String> newWithMap()
    {
        return BiMaps.immutable.withAll(UnifiedMap.newWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4)).inverse();
    }

    @Override
    protected ImmutableBiMap<Integer, String> newWithHashBiMap()
    {
        return BiMaps.immutable.withAll(HashBiMap.newWithKeysValues("1", 1, "2", 2, "3", 3, "4", 4)).inverse();
    }

    @Override
    protected ImmutableBiMap<Integer, String> newWithImmutableMap()
    {
        return BiMaps.immutable.withAll(Maps.immutable.of("1", 1, "2", 2, "3", 3, "4", 4)).inverse();
    }
}
