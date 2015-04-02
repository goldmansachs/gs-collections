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

package com.gs.collections.impl.map.mutable;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import com.gs.collections.impl.test.Verify;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for {@link ConcurrentHashMap}.
 */
public class ConcurrentHashMapAcceptanceTest
{
    private static final MutableMap<Integer, MutableBag<Integer>> BAG_MUTABLE_MAP = Interval.oneTo(1000).groupBy(each -> each % 100).toMap(HashBag::new);

    private ExecutorService executor;

    @Before
    public void setUp()
    {
        this.executor = Executors.newFixedThreadPool(20);
    }

    @After
    public void tearDown()
    {
        this.executor.shutdown();
    }

    @Test
    public void parallelGroupByIntoConcurrentHashMap()
    {
        MutableMap<Integer, MutableBag<Integer>> actual = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(
                Interval.oneTo(1000000),
                each -> actual.getIfAbsentPut(each % 100000, () -> HashBag.<Integer>newBag().asSynchronized()).add(each),
                10,
                this.executor);
        Verify.assertEqualsAndHashCode(Interval.oneTo(1000000).groupBy(each -> each % 100000).toMap(HashBag::new), actual);
    }

    @Test
    public void parallelForEachValue()
    {
        ConcurrentHashMap<Integer, Integer> source =
                ConcurrentHashMap.newMap(Interval.oneTo(1000).toMap(Functions.getIntegerPassThru(), Functions.getIntegerPassThru()));
        MutableMap<Integer, MutableBag<Integer>> actual = ConcurrentHashMap.newMap();
        Procedure<Integer> procedure = each -> actual.getIfAbsentPut(each % 100, () -> HashBag.<Integer>newBag().asSynchronized()).add(each);
        source.parallelForEachValue(FastList.newList(Collections.nCopies(5, procedure)), this.executor);
        Verify.assertEqualsAndHashCode(BAG_MUTABLE_MAP, actual);
    }

    @Test
    public void parallelForEachEntry()
    {
        ConcurrentHashMap<Integer, Integer> source =
                ConcurrentHashMap.newMap(Interval.oneTo(1000).toMap(Functions.getIntegerPassThru(), Functions.getIntegerPassThru()));
        MutableMap<Integer, MutableBag<Integer>> actual = ConcurrentHashMap.newMap();
        Procedure2<Integer, Integer> procedure2 = (key, value) -> actual.getIfAbsentPut(value % 100, () -> HashBag.<Integer>newBag().asSynchronized()).add(value);
        source.parallelForEachKeyValue(FastList.newList(Collections.nCopies(5, procedure2)), this.executor);
        Verify.assertEqualsAndHashCode(BAG_MUTABLE_MAP, actual);
    }

    @Test
    public void putAllInParallelSmallMap()
    {
        ConcurrentHashMap<Integer, Integer> source = ConcurrentHashMap.newMap(Interval.oneTo(1000).toMap(Functions.getIntegerPassThru(), Functions.getIntegerPassThru()));
        ConcurrentHashMap<Integer, Integer> target = ConcurrentHashMap.newMap();
        target.putAllInParallel(source, 10, this.executor);
        Verify.assertEqualsAndHashCode(source, target);
    }

    @Test
    public void putAllInParallelLargeMap()
    {
        ConcurrentHashMap<Integer, Integer> source = ConcurrentHashMap.newMap(Interval.oneTo(60000).toMap(Functions.getIntegerPassThru(), Functions.getIntegerPassThru()));
        ConcurrentHashMap<Integer, Integer> target = ConcurrentHashMap.newMap();
        target.putAllInParallel(source, 10, this.executor);
        Verify.assertEqualsAndHashCode(source, target);
    }

    @Test
    public void concurrentPutGetPutAllRemoveContainsKeyContainsValueGetIfAbsentPutTest()
    {
        ConcurrentHashMap<Integer, Integer> map1 = ConcurrentHashMap.newMap();
        ConcurrentHashMap<Integer, Integer> map2 = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(Interval.oneTo(1000), each -> {
            map1.put(each, each);
            Assert.assertEquals(each, map1.get(each));
            map2.putAll(Maps.mutable.of(each, each));
            map1.remove(each);
            map1.putAll(Maps.mutable.of(each, each));
            Assert.assertEquals(each, map2.get(each));
            map2.remove(each);
            Assert.assertNull(map2.get(each));
            Assert.assertFalse(map2.containsValue(each));
            Assert.assertFalse(map2.containsKey(each));
            Assert.assertEquals(each, map2.getIfAbsentPut(each, Functions.getIntegerPassThru()));
            Assert.assertTrue(map2.containsValue(each));
            Assert.assertTrue(map2.containsKey(each));
            Assert.assertEquals(each, map2.getIfAbsentPut(each, Functions.getIntegerPassThru()));
            map2.remove(each);
            Assert.assertEquals(each, map2.getIfAbsentPutWith(each, Functions.getIntegerPassThru(), each));
            Assert.assertEquals(each, map2.getIfAbsentPutWith(each, Functions.getIntegerPassThru(), each));
            Assert.assertEquals(each, map2.getIfAbsentPut(each, Functions.getIntegerPassThru()));
        }, 10, this.executor);
        Verify.assertEqualsAndHashCode(map1, map2);
    }

    @Test
    public void concurrentPutIfAbsentGetIfPresentPutTest()
    {
        ConcurrentHashMap<Integer, Integer> map1 = ConcurrentHashMap.newMap();
        ConcurrentHashMap<Integer, Integer> map2 = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(Interval.oneTo(1000), each -> {
            map1.put(each, each);
            map1.put(each, each);
            Assert.assertEquals(each, map1.get(each));
            map2.putAll(Maps.mutable.of(each, each));
            map2.putAll(Maps.mutable.of(each, each));
            map1.remove(each);
            Assert.assertNull(map1.putIfAbsentGetIfPresent(each, new KeyTransformer(), new ValueFactory(), null, null));
            Assert.assertEquals(each, map1.putIfAbsentGetIfPresent(each, new KeyTransformer(), new ValueFactory(), null, null));
        }, 10, this.executor);
        Assert.assertEquals(map1, map2);
    }

    @Test
    public void concurrentClear()
    {
        ConcurrentHashMap<Integer, Integer> map = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(Interval.oneTo(1000), each -> {
            for (int i = 0; i < each; i++)
            {
                map.put(each + i * 1000, each);
            }
            map.clear();
            for (int i = 0; i < 100; i++)
            {
                map.put(each + i * 1000, each);
            }
            map.clear();
        }, 10, this.executor);
        Verify.assertEmpty(map);
    }

    @Test
    public void concurrentRemoveAndPutIfAbsent()
    {
        ConcurrentHashMap<Integer, Integer> map1 = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(Interval.oneTo(1000), each -> {
            Assert.assertNull(map1.put(each, each));
            map1.remove(each);
            Assert.assertNull(map1.get(each));
            Assert.assertEquals(each, map1.getIfAbsentPut(each, Functions.getIntegerPassThru()));
            map1.remove(each);
            Assert.assertNull(map1.get(each));
            Assert.assertEquals(each, map1.getIfAbsentPutWith(each, Functions.getIntegerPassThru(), each));
            map1.remove(each);
            Assert.assertNull(map1.get(each));
            for (int i = 0; i < each; i++)
            {
                Assert.assertNull(map1.putIfAbsent(each + i * 1000, each));
            }
            for (int i = 0; i < each; i++)
            {
                Assert.assertEquals(each, map1.putIfAbsent(each + i * 1000, each));
            }
            for (int i = 0; i < each; i++)
            {
                Assert.assertEquals(each, map1.remove(each + i * 1000));
            }
        }, 10, this.executor);
    }

    private static class KeyTransformer implements Function2<Integer, Integer, Integer>
    {
        private static final long serialVersionUID = 1L;

        @Override
        public Integer value(Integer key, Integer value)
        {
            return key;
        }
    }

    private static class ValueFactory implements Function3<Object, Object, Integer, Integer>
    {
        private static final long serialVersionUID = 1L;

        @Override
        public Integer value(Object argument1, Object argument2, Integer key)
        {
            return key;
        }
    }

    @Test
    public void size()
    {
        ConcurrentHashMap<Integer, Integer> map = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(Interval.oneTo(10_000), each -> map.put(each, each));
        Assert.assertEquals(10_000, map.size());
        Assert.assertEquals(10_000, map.keySet().size());
        Assert.assertEquals(10_000, map.values().size());
        Assert.assertEquals(10_000, map.entrySet().size());
    }

    @Test
    public void size_entrySet()
    {
        ConcurrentHashMap<Integer, Integer> map = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(Interval.oneTo(10_000), each -> map.put(each, each));
        Assert.assertEquals(10_000, map.entrySet().size());
    }

    @Test
    public void size_keySet()
    {
        ConcurrentHashMap<Integer, Integer> map = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(Interval.oneTo(10_000), each -> map.put(each, each));
        Assert.assertEquals(10_000, map.keySet().size());
    }

    @Test
    public void size_values()
    {
        ConcurrentHashMap<Integer, Integer> map = ConcurrentHashMap.newMap();
        ParallelIterate.forEach(Interval.oneTo(10_000), each -> map.put(each, each));
        Assert.assertEquals(10_000, map.values().size());
    }
}
