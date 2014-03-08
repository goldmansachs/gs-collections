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

package com.gs.collections.impl.memory.set;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.impl.MemoryTests;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmutableSet0To100MemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableSet0To100MemoryTest.class);

    @Test
    @Category(MemoryTests.class)
    public void memoryForScaledImmutableSets()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, GSC {}, Guava {}",
                scala.collection.immutable.Set.class.getSimpleName(),
                Set.class.getSimpleName(),
                com.gs.collections.api.set.ImmutableSet.class.getSimpleName(),
                ImmutableSet.class.getSimpleName());

        IntInterval.fromToBy(0, 100, 10).forEach(this::memoryForScaledSets);
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(scala.collection.immutable.Set.class)
                .printContainerMemoryUsage("ImmutableSet_0to100", size, new SizedImmutableScalaSetFactory(size));
        MemoryTestBench.on(Set.class)
                .printContainerMemoryUsage("ImmutableSet_0to100", size, new SizedUnmodifiableHashSetFactory(size));
        MemoryTestBench.on(com.gs.collections.api.set.ImmutableSet.class)
                .printContainerMemoryUsage("ImmutableSet_0to100", size, new SizedImmutableGscSetFactory(size));
        MemoryTestBench.on(ImmutableSet.class)
                .printContainerMemoryUsage("ImmutableSet_0to100", size, new SizedImmutableGuavaSetFactory(size));
    }

    private static final class SizedImmutableGscSetFactory implements Function0<com.gs.collections.api.set.ImmutableSet<Integer>>
    {
        private final int size;

        private SizedImmutableGscSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public com.gs.collections.api.set.ImmutableSet<Integer> value()
        {
            UnifiedSet<Integer> set = UnifiedSet.newSet(this.size);
            for (int i = 0; i < this.size; i++)
            {
                set.add(Integer.valueOf(i));
            }
            return set.toImmutable();
        }
    }

    private static final class SizedImmutableGuavaSetFactory implements Function0<ImmutableSet<Integer>>
    {
        private final int size;

        private SizedImmutableGuavaSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public ImmutableSet<Integer> value()
        {
            ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
            for (int i = 0; i < this.size; i++)
            {
                builder.add(Integer.valueOf(i));
            }
            return builder.build();
        }
    }

    private static final class SizedUnmodifiableHashSetFactory implements Function0<Set<Integer>>
    {
        private final int size;

        private SizedUnmodifiableHashSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public Set<Integer> value()
        {
            if (this.size == 0)
            {
                return Collections.emptySet();
            }
            if (this.size == 1)
            {
                return Collections.singleton(Integer.valueOf(0));
            }
            HashSet<Integer> set = new HashSet<Integer>(this.size);
            for (int i = 0; i < this.size; i++)
            {
                set.add(Integer.valueOf(i));
            }
            return Collections.unmodifiableSet(set);
        }
    }

    private static final class SizedImmutableScalaSetFactory implements Function0<scala.collection.immutable.Set<Integer>>
    {
        private final int size;

        private SizedImmutableScalaSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public scala.collection.immutable.Set<Integer> value()
        {
            scala.collection.mutable.HashSet<Integer> mutableSet = new scala.collection.mutable.HashSet<Integer>();
            for (int i = 0; i < this.size; i++)
            {
                mutableSet.$plus$eq(Integer.valueOf(i));
            }
            return mutableSet.toSet();
        }
    }
}
