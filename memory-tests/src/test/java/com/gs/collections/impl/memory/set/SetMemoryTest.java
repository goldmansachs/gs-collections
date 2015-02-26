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

package com.gs.collections.impl.memory.set;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import gnu.trove.impl.Constants;
import gnu.trove.set.hash.THashSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SetMemoryTest.class);

    @Test
    public void memoryForScaledSets()
    {
        LOGGER.info("Comparing Items: Scala {}, Trove {}, GSC {}, JDK {}",
                scala.collection.mutable.HashSet.class.getSimpleName(),
                THashSet.class.getSimpleName(),
                UnifiedSet.class.getSimpleName(),
                HashSet.class.getSimpleName());
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledSets(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    private void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(scala.collection.mutable.HashSet.class).printContainerMemoryUsage("Set", size, new ScalaMutableSetFactory(size));
        Float[] chainingLoadFactors = {0.70f, 0.75f, 0.80f};
        for (Float loadFactor : chainingLoadFactors)
        {
            String suffix = "_loadFactor=" + loadFactor;

            MemoryTestBench.on(THashSet.class, suffix).printContainerMemoryUsage("Set", size, new THashSetFactory(size, loadFactor));
            MemoryTestBench.on(UnifiedSet.class, suffix).printContainerMemoryUsage("Set", size, new UnifiedSetFactory(size, loadFactor));
            MemoryTestBench.on(HashSet.class, suffix).printContainerMemoryUsage("Set", size, new HashSetFactory(size, loadFactor));
        }
    }

    public abstract static class SizedSetFactory
    {
        protected final float loadFactor;
        protected final ImmutableList<Integer> data;

        protected SizedSetFactory(int size)
        {
            this(size, 0.75f);
        }

        protected SizedSetFactory(int size, float loadFactor)
        {
            this.loadFactor = loadFactor;
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
        private HashSetFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public HashSet<Integer> value()
        {
            /**
             * Backing <tt>HashMap</tt> instance for HashSet has
             * default initial capacity (16)
             * @see HashMap#DEFAULT_INITIAL_CAPACITY
             */
            int defaultInitialCapacity = 16;
            return this.fill(new HashSet<Integer>(defaultInitialCapacity, this.loadFactor));
        }
    }

    private static final class THashSetFactory
            extends SizedSetFactory
            implements Function0<THashSet<Integer>>
    {
        private THashSetFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public THashSet<Integer> value()
        {
            return this.fill(new THashSet<Integer>(Constants.DEFAULT_CAPACITY, this.loadFactor));
        }
    }

    private static final class UnifiedSetFactory
            extends SizedSetFactory
            implements Function0<UnifiedSet<Integer>>
    {
        private UnifiedSetFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public UnifiedSet<Integer> value()
        {
            /**
             * @see UnifiedSet#DEFAULT_INITIAL_CAPACITY
             */
            int defaultInitialCapacity = 8;
            return this.fill(new UnifiedSet<Integer>(defaultInitialCapacity, this.loadFactor));
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
