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

import com.google.common.collect.HashMultiset;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BagMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BagMemoryTest.class);

    @Test
    public void memoryForScaledBags()
    {
        LOGGER.info("Comparing Items: Guava {}, GSC {}", HashMultiset.class.getSimpleName(), HashBag.class.getSimpleName());

        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledBags(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledBags(int size)
    {
        MemoryTestBench.on(HashMultiset.class).printContainerMemoryUsage("Bag", size, new SizedGuavaMultisetFactory(size));
        MemoryTestBench.on(HashBag.class).printContainerMemoryUsage("Bag", size, new SizedGscBagFactory(size));
    }

    public static class SizedGuavaMultisetFactory implements Function0<HashMultiset<Integer>>
    {
        private final ImmutableList<Integer> data;

        public SizedGuavaMultisetFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }

        @Override
        public HashMultiset<Integer> value()
        {
            final HashMultiset<Integer> hashMultiset = HashMultiset.create();
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    hashMultiset.add(each, 10);
                }
            });
            return hashMultiset;
        }
    }

    public static class SizedGscBagFactory implements Function0<HashBag<Integer>>
    {
        private final ImmutableList<Integer> data;

        public SizedGscBagFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }

        @Override
        public HashBag<Integer> value()
        {
            final HashBag<Integer> hashBag = HashBag.newBag();
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    hashBag.addOccurrences(each, 10);
                }
            });
            return hashBag;
        }
    }
}
