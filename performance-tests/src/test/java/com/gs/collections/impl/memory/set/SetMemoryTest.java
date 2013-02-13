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

package com.gs.collections.impl.memory.set;

import java.util.HashSet;
import java.util.Set;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.Iterate;
import gnu.trove.set.hash.THashSet;
import org.junit.Test;

public class SetMemoryTest
{
    @Test
    public void memoryForScaledSets()
    {
        this.memoryForScaledSets(0);
        this.memoryForScaledSets(10);
        this.memoryForScaledSets(50);
        this.memoryForScaledSets(100);
        this.memoryForScaledSets(500);
        this.memoryForScaledSets(1000);
        this.memoryForScaledSets(5000);
        this.memoryForScaledSets(10000);
        this.memoryForScaledSets(50000);
        this.memoryForScaledSets(100000);
        this.memoryForScaledSets(500000);
        this.memoryForScaledSets(1000000);
    }

    public void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(HashSet.class)
                .printContainerMemoryUsage("Set", size, new HashSetFactory(size));
        MemoryTestBench.on(THashSet.class)
                .printContainerMemoryUsage("Set", size, new THashSetFactory(size));
        MemoryTestBench.on(UnifiedSet.class)
                .printContainerMemoryUsage("Set", size, new UnifiedSetFactory(size));
    }

    public abstract static class SizedSetFactory
    {
        private final ImmutableList<Integer> data;

        protected SizedSetFactory(int size)
        {
            this.data = TestDataFactory.createImmutableList(size);
        }

        protected <R extends Set<Integer>> R fill(R set)
        {
            Iterate.addAllTo(this.data, set);
            return set;
        }
    }

    public static class HashSetFactory
            extends SizedSetFactory
            implements Function0<HashSet<Integer>>
    {
        protected HashSetFactory(int size)
        {
            super(size);
        }

        public HashSet<Integer> value()
        {
            return this.fill(new HashSet<Integer>());
        }
    }

    public static class THashSetFactory
            extends SizedSetFactory
            implements Function0<THashSet<Integer>>
    {
        protected THashSetFactory(int size)
        {
            super(size);
        }

        public THashSet<Integer> value()
        {
            return this.fill(new THashSet());
        }
    }

    public static class UnifiedSetFactory
            extends SizedSetFactory
            implements Function0<UnifiedSet<Integer>>
    {
        protected UnifiedSetFactory(int size)
        {
            super(size);
        }

        public UnifiedSet<Integer> value()
        {
            return this.fill(new UnifiedSet<Integer>());
        }
    }
}
