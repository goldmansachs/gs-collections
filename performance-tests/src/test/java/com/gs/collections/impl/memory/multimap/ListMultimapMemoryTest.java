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

package com.gs.collections.impl.memory.multimap;

import com.google.common.collect.ArrayListMultimap;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import org.junit.Test;

public class ListMultimapMemoryTest
{
    @Test
    public void memoryForScaledMultimaps()
    {
        this.memoryForScaledMultimaps(0);
        this.memoryForScaledMultimaps(10);
        this.memoryForScaledMultimaps(50);
        this.memoryForScaledMultimaps(100);
        this.memoryForScaledMultimaps(500);
        this.memoryForScaledMultimaps(1000);
        this.memoryForScaledMultimaps(5000);
        this.memoryForScaledMultimaps(10000);
        this.memoryForScaledMultimaps(50000);
        this.memoryForScaledMultimaps(100000);
        this.memoryForScaledMultimaps(500000);
        this.memoryForScaledMultimaps(1000000);
    }

    public void memoryForScaledMultimaps(int size)
    {
        MemoryTestBench.on(ArrayListMultimap.class)
                .printContainerMemoryUsage("ListMultimap", size, new SizedGuavaMultimapFactory(size));
        MemoryTestBench.on(FastListMultimap.class)
                .printContainerMemoryUsage("ListMultimap", size, new SizedGscMultimapFactory(size));
    }

    public static class SizedGuavaMultimapFactory implements Function0<ArrayListMultimap<Integer, String>>
    {
        private final ImmutableList<Integer> data;

        public SizedGuavaMultimapFactory(int size)
        {
            this.data = TestDataFactory.createImmutableList(size);
        }

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

    public static class SizedGscMultimapFactory implements Function0<FastListMultimap<Integer, String>>
    {
        private final ImmutableList<Integer> data;

        public SizedGscMultimapFactory(int size)
        {
            this.data = TestDataFactory.createImmutableList(size);
        }

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
