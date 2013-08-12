/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl;

import java.util.Random;

import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnifiedSetAddTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiedSetAddTest.class);

    @Test
    @Category(PerformanceTests.class)
    public void testUnifiedPutMonomorphic()
    {
        this.runIntegerUnifiedPut("monomorphic");
    }

    @Test
    @Category(PerformanceTests.class)
    public void testUnifiedPutPolymorphic()
    {
        this.runIntegerUnifiedPut("monomorphic");
        this.runLongUnifiedPut("bimorphic");
        this.runIntegerUnifiedPut("bimorphic");
        this.runStringUnifiedPut("polymorphic");
        this.runIntegerUnifiedPut("polymorphic");
        this.runLongUnifiedPut("polymorphic");
        this.runIntegerUnifiedPut("polymorphic");
        this.runStringUnifiedPut("polymorphic");
    }

    private void runIntegerUnifiedPut(String type)
    {
        System.currentTimeMillis();
        Random r = new Random(123412123);
        Integer[] ints = new Integer[1000000];
        for (int i = 0; i < ints.length; i++)
        {
            ints[i] = r.nextInt();
        }
        this.runUnifiedPutForType(type, ints);
    }

    private void runUnifiedPutForType(String type, Object[] objects)
    {
        for (int i = 0; i < 100; i++)
        {
            this.runUnifiedPut(objects, 1000, 1000);
        }
        for (int i = 0; i < 100; i++)
        {
            this.runUnifiedPut(objects, 1000000, 1);
        }
        long now = System.currentTimeMillis();
        for (int i = 0; i < 100; i++)
        {
            this.runUnifiedPut(objects, 1000000, 1);
        }
        long time = System.currentTimeMillis() - now;
        LOGGER.info("UnifiedSet, set size 1,000,000, {} {} puts/msec: {}", type, objects[0].getClass(), 100000000 / time);
    }

    private void runLongUnifiedPut(String type)
    {
        System.currentTimeMillis();
        Random r = new Random(123412123);
        Long[] longs = new Long[1000000];
        for (int i = 0; i < longs.length; i++)
        {
            longs[i] = r.nextLong();
        }
        this.runUnifiedPutForType(type, longs);
    }

    private void runStringUnifiedPut(String type)
    {
        System.currentTimeMillis();
        Random r = new Random(123412123);
        String[] strings = new String[1000000];
        for (int i = 0; i < strings.length; i++)
        {
            strings[i] = String.valueOf(r.nextLong());
        }
        this.runUnifiedPutForType(type, strings);
    }

    public void runUnifiedPut(Object[] objects, int length, int runs)
    {
        for (int i = 0; i < runs; i++)
        {
            UnifiedSet<Object> set = UnifiedSet.newSet(8);
            set.add(null);
            for (int j = 0; j < length; j++)
            {
                set.add(objects[j]);
            }
        }
    }
}
