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

import java.util.Map;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import scala.collection.immutable.HashMap$;

@SuppressWarnings("UnnecessaryFullyQualifiedName")
public class ImmutableMapMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableMapMemoryTest.class);

    @Test
    public void memoryForScaledMaps()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, GSC {}, Guava {}",
                scala.collection.immutable.HashMap.class.getSimpleName(),
                com.gs.collections.impl.map.immutable.ImmutableUnifiedMap.class.getSimpleName(),
                com.google.common.collect.ImmutableMap.class.getSimpleName());

        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledMaps(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledMaps(int size)
    {
        MemoryTestBench.on(scala.collection.immutable.HashMap.class)
                .printContainerMemoryUsage("ImmutableMap", size, new ScalaFactory(size));
        MemoryTestBench.on(com.gs.collections.api.map.ImmutableMap.class)
                .printContainerMemoryUsage("ImmutableMap", size, new GSCollectionsFactory(size));
        MemoryTestBench.on(com.google.common.collect.ImmutableMap.class)
                .printContainerMemoryUsage("ImmutableMap", size, new GuavaFactory(size));
    }

    public abstract static class SizedMapFactory
    {
        protected final ImmutableList<Integer> data;

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

    private static final class ScalaFactory
            extends SizedMapFactory
            implements Function0<scala.collection.immutable.HashMap<Integer, String>>
    {
        private ScalaFactory(int size)
        {
            super(size);
        }

        @Override
        public scala.collection.immutable.HashMap<Integer, String> value()
        {
            scala.collection.immutable.HashMap<Integer, String> map = HashMap$.MODULE$.empty();

            for (Integer each : this.data)
            {
                map = map.$plus(new Tuple2<Integer, String>(each, "dummy"));
            }
            return map;
        }
    }

    private static final class GSCollectionsFactory
            extends SizedMapFactory
            implements Function0<com.gs.collections.api.map.ImmutableMap<Integer, String>>
    {
        private GSCollectionsFactory(int size)
        {
            super(size);
        }

        @Override
        public com.gs.collections.api.map.ImmutableMap<Integer, String> value()
        {
            MutableMap<Integer, String> map = new UnifiedMap<Integer, String>();
            return this.fill(map).toImmutable();
        }
    }

    private static final class GuavaFactory
            extends SizedMapFactory
            implements Function0<com.google.common.collect.ImmutableMap<Integer, String>>
    {
        private GuavaFactory(int size)
        {
            super(size);
        }

        @Override
        public com.google.common.collect.ImmutableMap<Integer, String> value()
        {
            com.google.common.collect.ImmutableMap.Builder<Integer, String> builder = com.google.common.collect.ImmutableMap.builder();
            for (Integer each : this.data)
            {
                builder.put(each, "dummy");
            }
            return builder.build();
        }
    }
}
