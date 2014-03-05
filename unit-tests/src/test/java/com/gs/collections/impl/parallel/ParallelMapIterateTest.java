/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.parallel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ParallelMapIterateTest
{
    @Test
    public void forEachKeyValueWithNoParameters()
    {
        final ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<String, String>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, new Procedure2<String, String>()
        {
            public void value(String key, String value)
            {
                concurrentMap.put(key, value);
            }
        });
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithExecutor()
    {
        final ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<String, String>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, new Procedure2<String, String>()
        {
            public void value(String key, String value)
            {
                concurrentMap.put(key, value);
            }
        }, Executors.newSingleThreadExecutor());
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithMinForkSizeTaskCountAndExecutorParallel()
    {
        final ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<String, String>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, new Procedure2<String, String>()
        {
            public void value(String key, String value)
            {
                concurrentMap.put(key, value);
            }
        }, 1, 3, Executors.newSingleThreadExecutor());
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithMinForkSizeTaskCountAndExecutorSerial()
    {
        final ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<String, String>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, new Procedure2<String, String>()
        {
            public void value(String key, String value)
            {
                concurrentMap.put(key, value);
            }
        }, 5, 3, Executors.newSingleThreadExecutor());
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithMinForkSizeAndTaskCountParallel()
    {
        final ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<String, String>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, new Procedure2<String, String>()
        {
            public void value(String key, String value)
            {
                concurrentMap.put(key, value);
            }
        }, 1, 3);
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithMinForkSizeAndTaskCountSerial()
    {
        final ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<String, String>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, new Procedure2<String, String>()
        {
            public void value(String key, String value)
            {
                concurrentMap.put(key, value);
            }
        }, 5, 3);
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(ParallelMapIterate.class);
    }
}
