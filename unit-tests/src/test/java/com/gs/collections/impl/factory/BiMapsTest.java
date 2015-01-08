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

package com.gs.collections.impl.factory;

import java.util.Map;

import com.gs.collections.api.bimap.ImmutableBiMap;
import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.api.factory.bimap.ImmutableBiMapFactory;
import com.gs.collections.api.factory.bimap.MutableBiMapFactory;
import com.gs.collections.impl.bimap.mutable.HashBiMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public class BiMapsTest
{
    @Test
    public void immutable()
    {
        ImmutableBiMapFactory factory = BiMaps.immutable;
        Assert.assertEquals(HashBiMap.newMap(), factory.of());
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.of());
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2"), factory.of(1, "2"));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.of(1, "2"));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4"), factory.of(1, "2", 3, "4"));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.of(1, "2", 3, "4"));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6"), factory.of(1, "2", 3, "4", 5, "6"));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.of(1, "2", 3, "4", 5, "6"));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8"), factory.of(1, "2", 3, "4", 5, "6", 7, "8"));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.of(1, "2", 3, "4", 5, "6", 7, "8"));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8"), factory.ofAll(UnifiedMap.newMapWith(Tuples.pair(1, "2"), Tuples.pair(3, "4"), Tuples.pair(5, "6"), Tuples.pair(7, "8"))));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.ofAll(UnifiedMap.newMapWith(Tuples.pair(1, "2"), Tuples.pair(3, "4"), Tuples.pair(5, "6"), Tuples.pair(7, 8))));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8"), factory.ofAll(Maps.immutable.ofAll(UnifiedMap.newMapWith(Tuples.pair(1, "2"), Tuples.pair(3, "4"), Tuples.pair(5, "6"), Tuples.pair(7, "8")))));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.ofAll(Maps.immutable.ofAll(UnifiedMap.newMapWith(Tuples.pair(1, "2"), Tuples.pair(3, "4"), Tuples.pair(5, "6"), Tuples.pair(7, "8")))));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8"), factory.ofAll(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8")));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.ofAll(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8")));
        Map<Integer, String> map1 = HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8");
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8"), factory.ofAll(map1));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.ofAll(map1));
        ImmutableBiMap<Integer, String> map2 = BiMaps.immutable.with(1, "2", 3, "4", 5, "6", 7, "8");
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8"), factory.ofAll(map2.castToMap()));
        Verify.assertInstanceOf(ImmutableBiMap.class, factory.ofAll(map2.castToMap()));
        ImmutableBiMap<Integer, String> immutableBiMap1 = factory.ofAll(Maps.immutable.of(1, "2", 3, "4", 5, "6", 7, "8"));
    }

    @Test
    public void mutable()
    {
        MutableBiMapFactory factory = BiMaps.mutable;
        Assert.assertEquals(HashBiMap.newMap(), factory.of());
        Verify.assertInstanceOf(MutableBiMap.class, factory.of());
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2"), factory.of(1, "2"));
        Verify.assertInstanceOf(MutableBiMap.class, factory.of(1, "2"));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4"), factory.of(1, "2", 3, "4"));
        Verify.assertInstanceOf(MutableBiMap.class, factory.of(1, "2", 3, "4"));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6"), factory.of(1, "2", 3, "4", 5, "6"));
        Verify.assertInstanceOf(MutableBiMap.class, factory.of(1, "2", 3, "4", 5, "6"));
        Assert.assertEquals(HashBiMap.newWithKeysValues(1, "2", 3, "4", 5, "6", 7, "8"), factory.of(1, "2", 3, "4", 5, "6", 7, "8"));
        Verify.assertInstanceOf(MutableBiMap.class, factory.of(1, "2", 3, "4", 5, "6", 7, "8"));
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(BiMaps.class);
    }
}
