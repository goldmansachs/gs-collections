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

package com.gs.collections.impl.memory.bag;

import com.google.common.collect.ImmutableMultiset;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmutableBag0To10MemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableBag0To10MemoryTest.class);

    @Test
    public void memoryForScaledImmutableBags()
    {
        LOGGER.info("Comparing Items: GSC {}, Guava {}", ImmutableBag.class.getSimpleName(), ImmutableMultiset.class.getSimpleName());
        IntProcedure procedure = new IntProcedure()
        {
            public void value(int size)
            {
                ImmutableBag0To10MemoryTest.this.memoryForScaledBags(size);
            }
        };
        IntInterval.zeroTo(10).forEach(procedure);
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledBags(int size)
    {
        MemoryTestBench.on(ImmutableBag.class).printContainerMemoryUsage("ImmutableBag_0to10", size, new SizedImmutableGscBagFactory(size));
        MemoryTestBench.on(ImmutableMultiset.class).printContainerMemoryUsage("ImmutableBag_0to10", size, new SizedImmutableGuavaMultisetFactory(size));
    }

    public static class SizedImmutableGscBagFactory implements Function0<ImmutableBag<Integer>>
    {
        private final int size;

        protected SizedImmutableGscBagFactory(int size)
        {
            this.size = size;
        }

        @Override
        public ImmutableBag<Integer> value()
        {
            HashBag<Integer> bag = HashBag.newBag();
            for (int i = 0; i < this.size; i++)
            {
                bag.addOccurrences(Integer.valueOf(i), i + 1);
            }
            return bag.toImmutable();
        }
    }

    public static class SizedImmutableGuavaMultisetFactory implements Function0<ImmutableMultiset<Integer>>
    {
        private final int size;

        protected SizedImmutableGuavaMultisetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public ImmutableMultiset<Integer> value()
        {
            ImmutableMultiset.Builder<Integer> builder = ImmutableMultiset.builder();
            for (int i = 0; i < this.size; i++)
            {
                builder.addCopies(Integer.valueOf(i), i + 1);
            }
            return builder.build();
        }
    }
}
