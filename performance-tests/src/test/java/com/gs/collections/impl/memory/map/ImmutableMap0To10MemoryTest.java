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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.impl.MemoryTests;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.immutable.HashMap$;

public class ImmutableMap0To10MemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableMap0To10MemoryTest.class);

    @Test
    @Category(MemoryTests.class)
    public void memoryForScaledImmutableMaps()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, GSC {}, Guava {}",
                scala.collection.immutable.Map.class.getSimpleName(),
                Map.class.getSimpleName(),
                com.gs.collections.api.map.ImmutableMap.class.getSimpleName(),
                ImmutableMap.class.getSimpleName());
        IntInterval.zeroTo(10).forEach(this::memoryForScaledMaps);
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledMaps(int size)
    {
        MemoryTestBench.on(scala.collection.immutable.Map.class)
                .printContainerMemoryUsage("ImmutableMap_0to10", size, new SizedImmutableScalaMapFactory(size));
        MemoryTestBench.on(Map.class)
                .printContainerMemoryUsage("ImmutableMap_0to10", size, new SizedUnmodifiableHashMapFactory(size));
        MemoryTestBench.on(com.gs.collections.api.map.ImmutableMap.class)
                .printContainerMemoryUsage("ImmutableMap_0to10", size, new SizedImmutableGscMapFactory(size));
        MemoryTestBench.on(ImmutableMap.class)
                .printContainerMemoryUsage("ImmutableMap_0to10", size, new SizedImmutableGuavaMapFactory(size));
    }

    private static final class SizedImmutableGscMapFactory implements Function0<com.gs.collections.api.map.ImmutableMap<Integer, String>>
    {
        private final int size;

        private SizedImmutableGscMapFactory(int size)
        {
            this.size = size;
        }

        @Override
        public com.gs.collections.api.map.ImmutableMap<Integer, String> value()
        {
            UnifiedMap<Integer, String> map = UnifiedMap.newMap(this.size);
            for (int i = 0; i < this.size; i++)
            {
                map.put(Integer.valueOf(i), "dummy");
            }
            return map.toImmutable();
        }
    }

    private static final class SizedImmutableGuavaMapFactory implements Function0<ImmutableMap<Integer, String>>
    {
        private final int size;

        private SizedImmutableGuavaMapFactory(int size)
        {
            this.size = size;
        }

        @Override
        public ImmutableMap<Integer, String> value()
        {
            ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();
            for (int i = 0; i < this.size; i++)
            {
                builder.put(Integer.valueOf(i), "dummy");
            }
            return builder.build();
        }
    }

    private static final class SizedUnmodifiableHashMapFactory implements Function0<Map<Integer, String>>
    {
        private final int size;

        private SizedUnmodifiableHashMapFactory(int size)
        {
            this.size = size;
        }

        @Override
        public Map<Integer, String> value()
        {
            if (this.size == 0)
            {
                return Collections.emptyMap();
            }
            if (this.size == 1)
            {
                return Collections.singletonMap(Integer.valueOf(0), "dummy");
            }
            HashMap<Integer, String> map = new HashMap<Integer, String>(this.size);
            for (int i = 0; i < this.size; i++)
            {
                map.put(Integer.valueOf(i), "dummy");
            }
            return Collections.unmodifiableMap(map);
        }
    }

    private static final class SizedImmutableScalaMapFactory implements Function0<scala.collection.immutable.Map<Integer, String>>
    {
        private final int size;

        private SizedImmutableScalaMapFactory(int size)
        {
            this.size = size;
        }

        @Override
        public scala.collection.immutable.Map<Integer, String> value()
        {
            scala.collection.immutable.HashMap<Integer, String> hashMap = HashMap$.MODULE$.empty();
            for (int i = 0; i < this.size; i++)
            {
                hashMap = hashMap.updated(Integer.valueOf(i), "dummy");
            }
            return hashMap;
        }
    }
}
