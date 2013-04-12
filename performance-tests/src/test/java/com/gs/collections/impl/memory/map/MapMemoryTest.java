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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import gnu.trove.map.hash.THashMap;
import org.junit.Test;

public class MapMemoryTest
{
    @Test
    public void memoryForScaledMaps()
    {
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledMaps(size);
        }
    }

    public void memoryForScaledMaps(int size)
    {
        MemoryTestBench.on(HashMap.class)
                .printContainerMemoryUsage("Map", size, new HashMapFactory(size));
        MemoryTestBench.on(THashMap.class)
                .printContainerMemoryUsage("Map", size, new THashMapFactory(size));
        MemoryTestBench.on(UnifiedMap.class)
                .printContainerMemoryUsage("Map", size, new UnifiedMapFactory(size));
        MemoryTestBench.on(Hashtable.class)
                .printContainerMemoryUsage("Map", size, new HashtableFactory(size));
    }

    public abstract static class SizedMapFactory
    {
        private final ImmutableList<Integer> data;

        protected SizedMapFactory(int size)
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

    public static class HashMapFactory
            extends SizedMapFactory
            implements Function0<HashMap<Integer, String>>
    {
        protected HashMapFactory(int size)
        {
            super(size);
        }

        public HashMap<Integer, String> value()
        {
            return this.fill(new HashMap<Integer, String>());
        }
    }

    public static class THashMapFactory
            extends SizedMapFactory
            implements Function0<THashMap<Integer, String>>
    {
        protected THashMapFactory(int size)
        {
            super(size);
        }

        public THashMap<Integer, String> value()
        {
            return this.fill(new THashMap());
        }
    }

    public static class HashtableFactory
            extends SizedMapFactory
            implements Function0<Hashtable<Integer, String>>
    {
        protected HashtableFactory(int size)
        {
            super(size);
        }

        @SuppressWarnings("UseOfObsoleteCollectionType")
        public Hashtable<Integer, String> value()
        {
            return this.fill(new Hashtable<Integer, String>());
        }
    }

    public static class UnifiedMapFactory
            extends SizedMapFactory
            implements Function0<UnifiedMap<Integer, String>>
    {
        protected UnifiedMapFactory(int size)
        {
            super(size);
        }

        public UnifiedMap<Integer, String> value()
        {
            return this.fill(new UnifiedMap<Integer, String>());
        }
    }
}
