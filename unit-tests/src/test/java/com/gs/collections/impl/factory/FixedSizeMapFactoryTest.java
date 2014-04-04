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

package com.gs.collections.impl.factory;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.test.domain.Key;
import org.junit.Assert;
import org.junit.Test;

public class FixedSizeMapFactoryTest
{
    @Test
    public void create0()
    {
        MutableMap<String, String> map = Maps.fixedSize.of();
        Verify.assertEmpty(map);
    }

    @Test
    public void create1()
    {
        MutableMap<String, String> map1 = Maps.fixedSize.of("key1", "value1");
        Verify.assertSize(1, map1);
        Verify.assertContainsKeyValue("key1", "value1", map1);

        MutableMap<String, String> map2 = Maps.fixedSize.of((String) null, (String) null);
        Verify.assertSize(1, map2);
        Verify.assertContainsKeyValue(null, null, map2);

        MutableMap<String, String> map3 = Maps.fixedSize.of((String) null, "value1");
        Verify.assertSize(1, map3);
        Verify.assertContainsKeyValue(null, "value1", map3);
    }

    @Test
    public void create2()
    {
        MutableMap<String, String> map1 = Maps.fixedSize.of("key1", "value1", "key2", "value2");
        Verify.assertSize(2, map1);
        Verify.assertContainsAllKeyValues(map1, "key1", "value1", "key2", "value2");

        MutableMap<String, String> map2 = Maps.fixedSize.of(null, "value1", "key2", "value2");
        Verify.assertSize(2, map2);
        Verify.assertContainsKeyValue(null, "value1", map2);
        Verify.assertContainsKeyValue("key2", "value2", map2);
    }

    @Test
    public void create3()
    {
        MutableMap<String, String> map1 = Maps.fixedSize.of("key1", "value1", "key2", "value2", "key3", "value3");
        Verify.assertSize(3, map1);
        Verify.assertContainsAllKeyValues(map1, "key1", "value1", "key2", "value2", "key3", "value3");

        MutableMap<String, String> map2 = Maps.fixedSize.of("key1", "value1", "key2", null, null, "value3");
        Verify.assertContainsKeyValue("key2", null, map2);
        Verify.assertContainsKeyValue(null, "value3", map2);
    }

    @Test
    public void createWithDuplicates()
    {
        MutableMap<String, String> map1 = Maps.fixedSize.of("k1", "v1", "k1", "v2");
        Verify.assertSize(1, map1);
        Verify.assertContainsKey("k1", map1);
        Verify.assertContainsKeyValue("k1", "v2", map1);

        MutableMap<String, String> map2 = Maps.fixedSize.of("k1", "v1", "k1", "v2", "k1", "v3");
        Verify.assertSize(1, map2);
        Verify.assertContainsKey("k1", map2);
        Verify.assertContainsKeyValue("k1", "v3", map2);

        MutableMap<String, String> map3 = Maps.fixedSize.of("k2", "v1", "k3", "v2", "k2", "v3");
        Verify.assertSize(2, map3);
        Verify.assertContainsKey("k2", map3);
        Verify.assertContainsKey("k3", map3);
        Verify.assertContainsKeyValue("k2", "v3", map3);

        MutableMap<String, String> map4 = Maps.fixedSize.of("k3", "v1", "k4", "v2", "k4", "v3");
        Verify.assertSize(2, map4);
        Verify.assertContainsKey("k3", map4);
        Verify.assertContainsKey("k4", map4);
        Verify.assertContainsKeyValue("k4", "v3", map4);
    }

    @Test
    public void keyPreservation()
    {
        Key key = new Key("key");

        Key duplicateKey1 = new Key("key");
        MutableMap<Key, Integer> map1 = Maps.fixedSize.of(key, 1, duplicateKey1, 2);
        Verify.assertSize(1, map1);
        Verify.assertContainsKeyValue(key, 2, map1);
        Assert.assertSame(key, map1.keysView().getFirst());

        Key duplicateKey2 = new Key("key");
        MutableMap<Key, Integer> map2 = Maps.fixedSize.of(key, 1, duplicateKey1, 2, duplicateKey2, 3);
        Verify.assertSize(1, map2);
        Verify.assertContainsKeyValue(key, 3, map2);
        Assert.assertSame(key, map1.keysView().getFirst());

        Key duplicateKey3 = new Key("key");
        MutableMap<Key, Integer> map3 = Maps.fixedSize.of(key, 1, new Key("not a dupe"), 2, duplicateKey3, 3);
        Verify.assertSize(2, map3);
        Verify.assertContainsAllKeyValues(map3, key, 3, new Key("not a dupe"), 2);
        Assert.assertSame(key, map3.keysView().detect(key::equals));
    }
}
