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

package com.gs.collections.impl.memory.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.immutable.HashMap$;

@SuppressWarnings("UnnecessaryFullyQualifiedName")
public abstract class AbstractImmutableMapMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImmutableMapMemoryTest.class);

    protected abstract IntList getData();

    protected abstract String getTestType();

    protected abstract Function<Integer, ? extends Object> getKeyFactory();

    @Test
    public void memoryForScaledImmutableMaps()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, GSC {}, Guava {}",
                scala.collection.immutable.Map.class.getSimpleName(),
                Map.class.getSimpleName(),
                com.gs.collections.api.map.ImmutableMap.class.getSimpleName(),
                com.google.common.collect.ImmutableMap.class.getSimpleName());
        IntProcedure procedure = new IntProcedure()
        {
            public void value(int size)
            {
                AbstractImmutableMapMemoryTest.this.memoryForScaledMaps(size);
            }
        };
        this.getData().forEach(procedure);
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledMaps(int size)
    {
        MemoryTestBench.on(scala.collection.immutable.Map.class)
                .printContainerMemoryUsage(this.getTestType(), size, new SizedImmutableScalaMapFactory(size));
        MemoryTestBench.on(Map.class)
                .printContainerMemoryUsage(this.getTestType(), size, new SizedUnmodifiableHashMapFactory(size));
        MemoryTestBench.on(com.gs.collections.api.map.ImmutableMap.class)
                .printContainerMemoryUsage(this.getTestType(), size, new SizedImmutableGscMapFactory(size));
        MemoryTestBench.on(com.google.common.collect.ImmutableMap.class)
                .printContainerMemoryUsage(this.getTestType(), size, new SizedImmutableGuavaMapFactory(size));
    }

    private final class SizedImmutableGscMapFactory implements Function0<com.gs.collections.api.map.ImmutableMap<? extends Object, String>>
    {
        private final int size;

        private SizedImmutableGscMapFactory(int size)
        {
            this.size = size;
        }

        @Override
        public com.gs.collections.api.map.ImmutableMap<? extends Object, String> value()
        {
            UnifiedMap<Object, String> map = UnifiedMap.newMap(this.size);
            for (int i = 0; i < this.size; i++)
            {
                map.put(AbstractImmutableMapMemoryTest.this.getKeyFactory(), "dummy");
            }
            return map.toImmutable();
        }
    }

    private final class SizedImmutableGuavaMapFactory implements Function0<com.google.common.collect.ImmutableMap<? extends Object, String>>
    {
        private final int size;

        private SizedImmutableGuavaMapFactory(int size)
        {
            this.size = size;
        }

        @Override
        public com.google.common.collect.ImmutableMap<? extends Object, String> value()
        {
            com.google.common.collect.ImmutableMap.Builder<Object, String> builder = com.google.common.collect.ImmutableMap.builder();
            for (int i = 0; i < this.size; i++)
            {
                builder.put(AbstractImmutableMapMemoryTest.this.getKeyFactory().valueOf(i), "dummy");
            }
            return builder.build();
        }
    }

    private final class SizedUnmodifiableHashMapFactory implements Function0<Map<? extends Object, String>>
    {
        private final int size;

        private SizedUnmodifiableHashMapFactory(int size)
        {
            this.size = size;
        }

        @Override
        public Map<? extends Object, String> value()
        {
            if (this.size == 0)
            {
                return Collections.emptyMap();
            }
            if (this.size == 1)
            {
                return Collections.singletonMap(Integer.valueOf(0), "dummy");
            }
            HashMap<Object, String> map = new HashMap<>(this.size);
            for (int i = 0; i < this.size; i++)
            {
                map.put(AbstractImmutableMapMemoryTest.this.getKeyFactory(), "dummy");
            }
            return Collections.unmodifiableMap(map);
        }
    }

    private final class SizedImmutableScalaMapFactory implements Function0<scala.collection.immutable.Map<? extends Object, String>>
    {
        private final int size;

        private SizedImmutableScalaMapFactory(int size)
        {
            this.size = size;
        }

        @Override
        public scala.collection.immutable.Map<? extends Object, String> value()
        {
            scala.collection.immutable.HashMap<Object, String> hashMap = HashMap$.MODULE$.empty();
            for (int i = 0; i < this.size; i++)
            {
                hashMap = hashMap.updated(AbstractImmutableMapMemoryTest.this.getKeyFactory(), "dummy");
            }
            return hashMap;
        }
    }
}
