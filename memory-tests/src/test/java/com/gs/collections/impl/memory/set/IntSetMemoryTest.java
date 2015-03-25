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

import java.util.HashSet;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.set.primitive.IntSet;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import gnu.trove.set.hash.TIntHashSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntSetMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IntSetMemoryTest.class);

    @Test
    public void memoryForScaledSets()
    {
        LOGGER.info("Comparing Items: Trove {}, GSC {}, JDK {}",
                TIntHashSet.class.getSimpleName(),
                IntHashSet.class.getSimpleName(),
                HashSet.class.getSimpleName());

        for (int i = 0; i < 1000001; i += 25000)
        {
            this.memoryForScaledSets(i);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(TIntHashSet.class)
                .printContainerMemoryUsage("IntSet", size, new TIntHashSetFactory(size));
        MemoryTestBench.on(IntHashSet.class)
                .printContainerMemoryUsage("IntSet", size, new IntHashSetFactory(size));
        MemoryTestBench.on(HashSet.class)
                .printContainerMemoryUsage("IntSet", size, new IntegerHashSetFactory(size));
        MemoryTestBench.on(UnifiedSet.class)
                .printContainerMemoryUsage("IntSet", size, new IntegerUnifiedSetFactory(size));
        MemoryTestBench.on(scala.collection.mutable.HashSet.class)
                .printContainerMemoryUsage("IntSet", size, new IntegerScalaHashSetFactory(size));
    }

    public static class IntHashSetFactory implements Function0<IntHashSet>
    {
        private final IntSet data;

        public IntHashSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomSet(size);
        }

        @Override
        public IntHashSet value()
        {
            final IntHashSet set = new IntHashSet();
            this.data.forEach(new IntProcedure()
            {
                public void value(int each)
                {
                    set.add(each);
                }
            });
            return set;
        }
    }

    public static class TIntHashSetFactory implements Function0<TIntHashSet>
    {
        private final IntSet data;

        public TIntHashSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomSet(size);
        }

        @Override
        public TIntHashSet value()
        {
            final TIntHashSet set = new TIntHashSet();
            this.data.forEach(new IntProcedure()
            {
                public void value(int each)
                {
                    set.add(each);
                }
            });
            return set;
        }
    }

    public static class IntegerHashSetFactory implements Function0<HashSet<Integer>>
    {
        private final IntSet data;

        public IntegerHashSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomSet(size);
        }

        @Override
        public HashSet<Integer> value()
        {
            final HashSet<Integer> set = new HashSet<>();
            this.data.forEach(new IntProcedure()
            {
                public void value(int each)
                {
                    set.add(each);
                }
            });
            return set;
        }
    }

    public static class IntegerUnifiedSetFactory implements Function0<UnifiedSet<Integer>>
    {
        private final IntSet data;

        public IntegerUnifiedSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomSet(size);
        }

        @Override
        public UnifiedSet<Integer> value()
        {
            final UnifiedSet<Integer> set = new UnifiedSet<>();
            this.data.forEach(new IntProcedure()
            {
                public void value(int each)
                {
                    set.add(each);
                }
            });
            return set;
        }
    }

    public static class IntegerScalaHashSetFactory implements Function0<scala.collection.mutable.HashSet<Integer>>
    {
        private final IntSet data;

        public IntegerScalaHashSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomSet(size);
        }

        @Override
        public scala.collection.mutable.HashSet<Integer> value()
        {
            final scala.collection.mutable.HashSet<Integer> set = new scala.collection.mutable.HashSet<>();
            this.data.forEach(new IntProcedure()
            {
                public void value(int each)
                {
                    set.add(each);
                }
            });
            return set;
        }
    }
}
