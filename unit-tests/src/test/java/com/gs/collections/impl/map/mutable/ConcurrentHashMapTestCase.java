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

import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.ConcurrentMutableMap;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class ConcurrentHashMapTestCase extends MutableMapTestCase
{
    protected ExecutorService executor;

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

    @Override
    protected abstract <K, V> ConcurrentMutableMap<K, V> newMap();

    @Override
    @Test
    public void updateValue()
    {
        super.updateValue();

        ConcurrentMutableMap<Integer, Integer> map = this.newMap();
        ParallelIterate.forEach(Interval.oneTo(100), each -> map.updateValue(each % 10, () -> 0, integer -> integer + 1), 1, this.executor);
        Assert.assertEquals(Interval.zeroTo(9).toSet(), map.keySet());
        Assert.assertEquals(FastList.newList(Collections.nCopies(10, 10)), FastList.newList(map.values()));
    }

    @Override
    @Test
    public void updateValue_collisions()
    {
        super.updateValue_collisions();

        ConcurrentMutableMap<Integer, Integer> map = this.newMap();
        MutableList<Integer> list = Interval.oneTo(100).toList().shuffleThis();
        ParallelIterate.forEach(list, each -> map.updateValue(each % 50, () -> 0, integer -> integer + 1), 1, this.executor);
        Assert.assertEquals(Interval.zeroTo(49).toSet(), map.keySet());
        Assert.assertEquals(
                HashBag.newBag(map.values()).toStringOfItemToCount(),
                FastList.newList(Collections.nCopies(50, 2)),
                FastList.newList(map.values()));
    }

    @Override
    @Test
    public void updateValueWith()
    {
        super.updateValueWith();

        ConcurrentMutableMap<Integer, Integer> map = this.newMap();
        ParallelIterate.forEach(Interval.oneTo(100), each -> map.updateValueWith(each % 10, () -> 0, (integer, parameter) -> {
            Assert.assertEquals("test", parameter);
            return integer + 1;
        }, "test"), 1, this.executor);
        Assert.assertEquals(Interval.zeroTo(9).toSet(), map.keySet());
        Assert.assertEquals(FastList.newList(Collections.nCopies(10, 10)), FastList.newList(map.values()));
    }

    @Override
    @Test
    public void updateValueWith_collisions()
    {
        super.updateValueWith_collisions();

        ConcurrentMutableMap<Integer, Integer> map = this.newMap();
        MutableList<Integer> list = Interval.oneTo(200).toList().shuffleThis();
        ParallelIterate.forEach(list, each -> map.updateValueWith(each % 100, () -> 0, (integer, parameter) -> {
            Assert.assertEquals("test", parameter);
            return integer + 1;
        }, "test"), 1, this.executor);
        Assert.assertEquals(Interval.zeroTo(99).toSet(), map.keySet());
        Assert.assertEquals(
                HashBag.newBag(map.values()).toStringOfItemToCount(),
                FastList.newList(Collections.nCopies(100, 2)),
                FastList.newList(map.values()));
    }
}
