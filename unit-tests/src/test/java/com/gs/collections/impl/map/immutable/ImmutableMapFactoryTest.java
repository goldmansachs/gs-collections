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

package com.gs.collections.impl.map.immutable;

import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.test.domain.Key;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableMapFactoryTest
{
    @Test
    public void create0()
    {
        ImmutableMap<String, String> map = Maps.immutable.of();
        Verify.assertEmpty(map);
    }

    @Test
    public void create1()
    {
        ImmutableMap<String, String> map1 = Maps.immutable.of("key1", "value1");
        Verify.assertSize(1, map1);
        Verify.assertContainsKeyValue("key1", "value1", map1);

        ImmutableMap<String, String> map2 = Maps.immutable.of((String) null, (String) null);
        Verify.assertSize(1, map2);
        Verify.assertContainsKeyValue(null, null, map2);

        ImmutableMap<String, String> map3 = Maps.immutable.of((String) null, "value1");
        Verify.assertSize(1, map3);
        Verify.assertContainsKeyValue(null, "value1", map3);
    }

    @Test
    public void create2()
    {
        ImmutableMap<String, String> map1 = Maps.immutable.of("key1", "value1", "key2", "value2");
        Verify.assertSize(2, map1);
        Verify.assertContainsAllKeyValues(map1, "key1", "value1", "key2", "value2");

        ImmutableMap<String, String> map2 = Maps.immutable.of(null, "value1", "key2", "value2");
        Verify.assertContainsKeyValue(null, "value1", map2);
        Verify.assertContainsKeyValue("key2", "value2", map2);
    }

    @Test
    public void create3()
    {
        ImmutableMap<String, String> map1 = Maps.immutable.of("key1", "value1", "key2", "value2", "key3", "value3");
        Verify.assertSize(3, map1);
        Verify.assertContainsAllKeyValues(map1, "key1", "value1", "key2", "value2", "key3", "value3");

        ImmutableMap<String, String> map2 = Maps.immutable.of("key1", "value1", "key2", null, null, "value3");
        Verify.assertContainsKeyValue("key2", null, map2);
        Verify.assertContainsKeyValue(null, "value3", map2);
    }

    @Test
    public void createWithDuplicates()
    {
        ImmutableMap<String, String> map1 = Maps.immutable.of("k1", "v1", "k1", "v2");
        Verify.assertSize(1, map1);
        Verify.assertContainsKey("k1", map1);
        Verify.assertContainsKeyValue("k1", "v2", map1);

        ImmutableMap<String, String> map2 = Maps.immutable.of("k1", "v1", "k1", "v2", "k1", "v3");
        Verify.assertSize(1, map2);
        Verify.assertContainsKey("k1", map2);
        Verify.assertContainsKeyValue("k1", "v3", map2);

        ImmutableMap<String, String> map3 = Maps.immutable.of("k2", "v1", "k3", "v2", "k2", "v3");
        Verify.assertSize(2, map3);
        Verify.assertContainsKey("k2", map3);
        Verify.assertContainsKey("k3", map3);
        Verify.assertContainsKeyValue("k2", "v3", map3);

        ImmutableMap<String, String> map4 = Maps.immutable.of("k3", "v1", "k4", "v2", "k4", "v3");
        Verify.assertSize(2, map4);
        Verify.assertContainsKey("k3", map4);
        Verify.assertContainsKey("k4", map4);
        Verify.assertContainsKeyValue("k4", "v3", map4);
    }

    @Test
    public void keyPreservation()
    {
        Key key = new Key("key");
        Key duplicateKey = new Key("key");
        ImmutableMap<Key, Integer> map4 = new ImmutableMapFactoryImpl().of(key, 1, new Key("still not a dupe"), 2, new Key("me neither"), 3, duplicateKey, 4);
        Verify.assertSize(3, map4);
        Verify.assertContainsAllKeyValues(map4, key, 4, new Key("still not a dupe"), 2, new Key("me neither"), 3);
        Assert.assertSame(key, map4.keysView().detect(key::equals));
    }
}
