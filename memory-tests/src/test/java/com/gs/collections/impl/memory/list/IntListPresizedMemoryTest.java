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

package com.gs.collections.impl.memory.list;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.memory.MemoryTestBench;
import gnu.trove.list.array.TIntArrayList;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntListPresizedMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IntListPresizedMemoryTest.class);

    @Test
    public void memoryForScaledLists()
    {
        LOGGER.info("Comparing Items: Trove {}, GSC {}",
                TIntArrayList.class.getSimpleName(),
                IntArrayList.class.getSimpleName());
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledLists(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledLists(int size)
    {
        MemoryTestBench.on(TIntArrayList.class)
                .printContainerMemoryUsage("IntList", size, new TIntArrayListFactory(size));
        MemoryTestBench.on(IntArrayList.class)
                .printContainerMemoryUsage("IntList", size, new IntArrayListFactory(size));
    }

    public static class IntArrayListFactory implements Function0<IntArrayList>
    {
        private final int size;

        public IntArrayListFactory(int size)
        {
            this.size = size;
        }

        @Override
        public IntArrayList value()
        {
            final IntArrayList list = new IntArrayList(this.size);
            if (this.size > 0)
            {
                Interval.oneTo(this.size).forEach(new IntProcedure()
                {
                    public void value(int each)
                    {
                        list.add(each);
                    }
                });
            }
            return list;
        }
    }

    public static class TIntArrayListFactory implements Function0<TIntArrayList>
    {
        private final int size;

        public TIntArrayListFactory(int size)
        {
            this.size = size;
        }

        @Override
        public TIntArrayList value()
        {
            final TIntArrayList list = new TIntArrayList(this.size);
            if (this.size > 0)
            {
                Interval.oneTo(this.size).forEach(new IntProcedure()
                {
                    public void value(int each)
                    {
                        list.add(each);
                    }
                });
            }
            return list;
        }
    }
}
