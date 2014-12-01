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

package com.gs.collections.impl.parallel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ParallelMapIterateTest
{
    @Test
    public void forEachKeyValueWithNoParameters()
    {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, concurrentMap::put);
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithExecutor()
    {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, concurrentMap::put, Executors.newSingleThreadExecutor());
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithMinForkSizeTaskCountAndExecutorParallel()
    {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, concurrentMap::put, 1, 3, Executors.newSingleThreadExecutor());
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithMinForkSizeTaskCountAndExecutorSerial()
    {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, concurrentMap::put, 5, 3, Executors.newSingleThreadExecutor());
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithMinForkSizeAndTaskCountParallel()
    {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, concurrentMap::put, 1, 3);
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void forEachKeyValueWithMinForkSizeAndTaskCountSerial()
    {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        MutableMap<String, String> map = UnifiedMap.newWithKeysValues("1", "One", "2", "Two", "3", "Three");
        ParallelMapIterate.forEachKeyValue(map, concurrentMap::put, 5, 3);
        Verify.assertMapsEqual(concurrentMap, map);
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(ParallelMapIterate.class);
    }
}
