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

import com.google.common.collect.ImmutableMultiset;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;

public class ImmutableBagMemoryTest
{
    @Test
    public void memoryForScaledImmutableBags()
    {
        this.memoryForScaledBags(0);
        this.memoryForScaledBags(1);
        this.memoryForScaledBags(2);
        this.memoryForScaledBags(3);
        this.memoryForScaledBags(4);
        this.memoryForScaledBags(5);
        this.memoryForScaledBags(10);
        this.memoryForScaledBags(20);
        this.memoryForScaledBags(100);
    }

    public void memoryForScaledBags(int size)
    {
        MemoryTestBench.on(ImmutableBag.class).printContainerMemoryUsage("ImmutableBag", size, new SizedImmutableGscBagFactory(size));
        MemoryTestBench.on(ImmutableMultiset.class).printContainerMemoryUsage("ImmutableBag", size, new SizedImmutableGuavaMultisetFactory(size));
    }

    public static class SizedImmutableGscBagFactory implements Function0<ImmutableBag<Integer>>
    {
        private final int size;

        protected SizedImmutableGscBagFactory(int size)
        {
            this.size = size;
        }

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
