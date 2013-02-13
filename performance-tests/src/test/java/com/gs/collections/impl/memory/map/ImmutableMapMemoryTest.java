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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;

public class ImmutableMapMemoryTest
{
    @Test
    public void memoryForScaledImmutableMaps()
    {
        this.memoryForScaledMaps(0);
        this.memoryForScaledMaps(1);
        this.memoryForScaledMaps(2);
        this.memoryForScaledMaps(3);
        this.memoryForScaledMaps(4);
        this.memoryForScaledMaps(5);
        this.memoryForScaledMaps(6);
        this.memoryForScaledMaps(7);
        this.memoryForScaledMaps(8);
        this.memoryForScaledMaps(9);
        this.memoryForScaledMaps(10);
        this.memoryForScaledMaps(11);
        this.memoryForScaledMaps(100);
    }

    public void memoryForScaledMaps(int size)
    {
        MemoryTestBench.on(Map.class)
                .printContainerMemoryUsage("ImmutableMap", size, new SizedUnmodifiableHashMapFactory(size));
        MemoryTestBench.on(com.gs.collections.api.map.ImmutableMap.class)
                .printContainerMemoryUsage("ImmutableMap", size, new SizedImmutableGscMapFactory(size));
        MemoryTestBench.on(ImmutableMap.class)
                .printContainerMemoryUsage("ImmutableMap", size, new SizedImmutableGuavaMapFactory(size));
    }

    public static class SizedImmutableGscMapFactory implements Function0<com.gs.collections.api.map.ImmutableMap<Integer, String>>
    {
        private final int size;

        protected SizedImmutableGscMapFactory(int size)
        {
            this.size = size;
        }

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

    public static class SizedImmutableGuavaMapFactory implements Function0<ImmutableMap<Integer, String>>
    {
        private final int size;

        protected SizedImmutableGuavaMapFactory(int size)
        {
            this.size = size;
        }

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

    public static class SizedUnmodifiableHashMapFactory implements Function0<Map<Integer, String>>
    {
        private final int size;

        protected SizedUnmodifiableHashMapFactory(int size)
        {
            this.size = size;
        }

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
}
