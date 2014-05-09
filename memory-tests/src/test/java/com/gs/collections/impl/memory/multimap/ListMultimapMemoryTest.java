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

package com.gs.collections.impl.memory.multimap;

import com.google.common.collect.ArrayListMultimap;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListMultimapMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ListMultimapMemoryTest.class);

    @Test
    public void memoryForScaledMultimaps()
    {
        LOGGER.info("Comparing Items: Guava {}, GSC {}",
                ArrayListMultimap.class.getSimpleName(),
                FastListMultimap.class.getSimpleName());
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledMultimaps(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledMultimaps(int size)
    {
        MemoryTestBench.on(ArrayListMultimap.class)
                .printContainerMemoryUsage("ListMultimap", size, new SizedGuavaMultimapFactory(size));
        MemoryTestBench.on(FastListMultimap.class)
                .printContainerMemoryUsage("ListMultimap", size, new SizedGscMultimapFactory(size));
    }

    public abstract static class SizedMultimapFactory
    {
        protected final ImmutableList<Integer> data;

        protected SizedMultimapFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }
    }

    public static class SizedGuavaMultimapFactory
            extends SizedMultimapFactory
            implements Function0<ArrayListMultimap<Integer, String>>
    {
        public SizedGuavaMultimapFactory(int size)
        {
            super(size);
        }

        @Override
        public ArrayListMultimap<Integer, String> value()
        {
            final ArrayListMultimap<Integer, String> mm = ArrayListMultimap.create();
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    for (int j = 0; j < 10; j++)
                    {
                        mm.put(each, "dummy");
                    }
                }
            });
            return mm;
        }
    }

    public static class SizedGscMultimapFactory
            extends SizedMultimapFactory
            implements Function0<FastListMultimap<Integer, String>>
    {
        public SizedGscMultimapFactory(int size)
        {
            super(size);
        }

        @Override
        public FastListMultimap<Integer, String> value()
        {
            final FastListMultimap<Integer, String> mm = FastListMultimap.newMultimap();
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    for (int j = 0; j < 10; j++)
                    {
                        mm.put(each, "dummy");
                    }
                }
            });
            return mm;
        }
    }
}
