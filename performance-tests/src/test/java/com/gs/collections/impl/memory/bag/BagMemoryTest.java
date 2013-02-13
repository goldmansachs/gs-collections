/*
 * Copyright 2013 Goldman Sachs.
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

public class BagMemoryTest
{
    @Test
    public void memoryForScaledBags()
    {
        this.memoryForScaledBags(0);
        this.memoryForScaledBags(10);
        this.memoryForScaledBags(50);
        this.memoryForScaledBags(100);
        this.memoryForScaledBags(500);
        this.memoryForScaledBags(1000);
        this.memoryForScaledBags(5000);
        this.memoryForScaledBags(10000);
        this.memoryForScaledBags(50000);
        this.memoryForScaledBags(100000);
        this.memoryForScaledBags(500000);
        this.memoryForScaledBags(1000000);
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
            this.data = TestDataFactory.createImmutableList(size);
        }

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
            this.data = TestDataFactory.createImmutableList(size);
        }

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
