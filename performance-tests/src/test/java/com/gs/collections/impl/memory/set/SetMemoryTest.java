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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.MemoryTests;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import gnu.trove.set.hash.THashSet;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class SetMemoryTest
{
    @Test
    @Category(MemoryTests.class)
    public void memoryForScaledSets()
    {
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledSets(size);
        }
    }

    private void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(scala.collection.mutable.HashSet.class).printContainerMemoryUsage("Set", size, new ScalaMutableSetFactory(size));
        MemoryTestBench.on(THashSet.class).printContainerMemoryUsage("Set", size, new THashSetFactory(size));
        MemoryTestBench.on(UnifiedSet.class).printContainerMemoryUsage("Set", size, new UnifiedSetFactory(size));
        MemoryTestBench.on(HashSet.class).printContainerMemoryUsage("Set", size, new HashSetFactory(size));
    }

    public abstract static class SizedSetFactory
    {
        protected final ImmutableList<Integer> data;

        protected SizedSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }

        protected <R extends Set<Integer>> R fill(final R set)
        {
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    set.add(each);
                }
            });
            return set;
        }
    }

    private static final class HashSetFactory
            extends SizedSetFactory
            implements Function0<HashSet<Integer>>
    {
        private HashSetFactory(int size)
        {
            super(size);
        }

        @Override
        public HashSet<Integer> value()
        {
            return this.fill(new HashSet<Integer>());
        }
    }

    private static final class THashSetFactory
            extends SizedSetFactory
            implements Function0<THashSet<Integer>>
    {
        private THashSetFactory(int size)
        {
            super(size);
        }

        @Override
        public THashSet<Integer> value()
        {
            return this.fill(new THashSet<Integer>());
        }
    }

    private static final class UnifiedSetFactory
            extends SizedSetFactory
            implements Function0<UnifiedSet<Integer>>
    {
        private UnifiedSetFactory(int size)
        {
            super(size);
        }

        @Override
        public UnifiedSet<Integer> value()
        {
            return this.fill(new UnifiedSet<Integer>());
        }
    }

    private static final class ScalaMutableSetFactory
            extends SizedSetFactory
            implements Function0<scala.collection.mutable.HashSet<Integer>>
    {
        private ScalaMutableSetFactory(int size)
        {
            super(size);
        }

        @Override
        public scala.collection.mutable.HashSet<Integer> value()
        {
            final scala.collection.mutable.HashSet<Integer> set = new scala.collection.mutable.HashSet<Integer>();
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    set.add(each);
                }
            });
            return set;
        }
    }
}
