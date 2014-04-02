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

package com.gs.collections.impl.memory.map;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.MemoryTests;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import gnu.trove.map.hash.THashMap;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MapMemoryTest.class);

    @Test
    @Category(MemoryTests.class)
    public void memoryForScaledMaps()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, Trove {}, GSC {}, JDK {}",
                scala.collection.mutable.HashMap.class.getSimpleName(),
                HashMap.class.getSimpleName(),
                THashMap.class.getSimpleName(),
                UnifiedMap.class.getSimpleName(),
                Hashtable.class.getSimpleName());

        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledMaps(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledMaps(int size)
    {
        MemoryTestBench.on(scala.collection.mutable.HashMap.class)
                .printContainerMemoryUsage("Map", size, new ScalaHashMapFactory(size));
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
        protected final ImmutableList<Integer> data;

        protected SizedMapFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }

        protected <R extends Map<Integer, String>> R fill(R map)
        {
            this.data.forEach(Procedures.cast(each -> { map.put(each, "dummy"); }));
            return map;
        }
    }

    private static final class HashMapFactory
            extends SizedMapFactory
            implements Function0<HashMap<Integer, String>>
    {
        private HashMapFactory(int size)
        {
            super(size);
        }

        @Override
        public HashMap<Integer, String> value()
        {
            return this.fill(new HashMap<Integer, String>());
        }
    }

    private static final class THashMapFactory
            extends SizedMapFactory
            implements Function0<THashMap<Integer, String>>
    {
        private THashMapFactory(int size)
        {
            super(size);
        }

        @Override
        public THashMap<Integer, String> value()
        {
            return this.fill(new THashMap<Integer, String>());
        }
    }

    private static final class HashtableFactory
            extends SizedMapFactory
            implements Function0<Hashtable<Integer, String>>
    {
        private HashtableFactory(int size)
        {
            super(size);
        }

        @Override
        @SuppressWarnings("UseOfObsoleteCollectionType")
        public Hashtable<Integer, String> value()
        {
            return this.fill(new Hashtable<Integer, String>());
        }
    }

    private static final class UnifiedMapFactory
            extends SizedMapFactory
            implements Function0<UnifiedMap<Integer, String>>
    {
        private UnifiedMapFactory(int size)
        {
            super(size);
        }

        @Override
        public UnifiedMap<Integer, String> value()
        {
            return this.fill(new UnifiedMap<Integer, String>());
        }
    }

    private static final class ScalaHashMapFactory
            extends SizedMapFactory
            implements Function0<scala.collection.mutable.HashMap<Integer, String>>
    {
        private ScalaHashMapFactory(int size)
        {
            super(size);
        }

        @Override
        public scala.collection.mutable.HashMap<Integer, String> value()
        {
            scala.collection.mutable.HashMap<Integer, String> map = new scala.collection.mutable.HashMap<Integer, String>();
            this.data.forEach(Procedures.cast(each -> { map.put(each, "dummy"); }));
            return map;
        }
    }
}
