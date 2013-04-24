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

package com.gs.collections.impl.memory.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.MemoryTests;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ConcurrentMapMemoryTest
{
    @Test
    @Category(MemoryTests.class)
    public void memoryForScaledConcurrentMaps()
    {
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledConcurrentMaps(size);
        }
    }

    public void memoryForScaledConcurrentMaps(int size)
    {
        MemoryTestBench.on(ConcurrentHashMap.class)
                .printContainerMemoryUsage("ConcurrentMap", size, new JDKConcurrentMapFactory(size));
        MemoryTestBench.on(com.gs.collections.impl.map.mutable.ConcurrentHashMap.class)
                .printContainerMemoryUsage("ConcurrentMap", size, new GSCConcurrentMapFactory(size));
    }

    public abstract static class SizedConcurrentMapFactory
    {
        private final ImmutableList<Integer> data;

        protected SizedConcurrentMapFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }

        protected <R extends Map<Integer, String>> R fill(final R map)
        {
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    map.put(each, "dummy");
                }
            });
            return map;
        }
    }

    public static class JDKConcurrentMapFactory
            extends SizedConcurrentMapFactory
            implements Function0<ConcurrentHashMap<Integer, String>>
    {
        protected JDKConcurrentMapFactory(int size)
        {
            super(size);
        }

        public ConcurrentHashMap<Integer, String> value()
        {
            return this.fill(new ConcurrentHashMap<Integer, String>());
        }
    }

    public static class GSCConcurrentMapFactory
            extends SizedConcurrentMapFactory
            implements Function0<com.gs.collections.impl.map.mutable.ConcurrentHashMap<Integer, String>>
    {
        protected GSCConcurrentMapFactory(int size)
        {
            super(size);
        }

        public com.gs.collections.impl.map.mutable.ConcurrentHashMap<Integer, String> value()
        {
            return this.fill(new com.gs.collections.impl.map.mutable.ConcurrentHashMap<Integer, String>());
        }
    }
}
