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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractImmutableSetMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImmutableSetMemoryTest.class);

    protected abstract IntList getData();

    protected abstract String getTestType();

    protected abstract Function<Integer, ? extends Object> getKeyFactory();

    @Test
    public void memoryForScaledImmutableSets()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, GSC {}, Guava {}",
                scala.collection.immutable.Set.class.getSimpleName(),
                Set.class.getSimpleName(),
                com.gs.collections.api.set.ImmutableSet.class.getSimpleName(),
                ImmutableSet.class.getSimpleName());

        IntProcedure procedure = new IntProcedure()
        {
            public void value(int size)
            {
                AbstractImmutableSetMemoryTest.this.memoryForScaledSets(size);
            }
        };
        this.getData().forEach(procedure);
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(scala.collection.immutable.Set.class)
                .printContainerMemoryUsage(this.getTestType(), size, new SizedImmutableScalaSetFactory(size));
        MemoryTestBench.on(Set.class)
                .printContainerMemoryUsage(this.getTestType(), size, new SizedUnmodifiableHashSetFactory(size));
        MemoryTestBench.on(com.gs.collections.api.set.ImmutableSet.class)
                .printContainerMemoryUsage(this.getTestType(), size, new SizedImmutableGscSetFactory(size));
        MemoryTestBench.on(ImmutableSet.class)
                .printContainerMemoryUsage(this.getTestType(), size, new SizedImmutableGuavaSetFactory(size));
    }

    private final class SizedImmutableGscSetFactory implements Function0<com.gs.collections.api.set.ImmutableSet<Object>>
    {
        private final int size;

        private SizedImmutableGscSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public com.gs.collections.api.set.ImmutableSet<Object> value()
        {
            UnifiedSet<Object> set = UnifiedSet.newSet(this.size);
            for (int i = 0; i < this.size; i++)
            {
                set.add(AbstractImmutableSetMemoryTest.this.getKeyFactory().valueOf(i));
            }
            return set.toImmutable();
        }
    }

    private final class SizedImmutableGuavaSetFactory implements Function0<ImmutableSet<Object>>
    {
        private final int size;

        private SizedImmutableGuavaSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public ImmutableSet<Object> value()
        {
            ImmutableSet.Builder<Object> builder = ImmutableSet.builder();
            for (int i = 0; i < this.size; i++)
            {
                builder.add(AbstractImmutableSetMemoryTest.this.getKeyFactory().valueOf(i));
            }
            return builder.build();
        }
    }

    private final class SizedUnmodifiableHashSetFactory implements Function0<Set<Object>>
    {
        private final int size;

        private SizedUnmodifiableHashSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public Set<Object> value()
        {
            if (this.size == 0)
            {
                return Collections.emptySet();
            }
            if (this.size == 1)
            {
                return Collections.singleton(AbstractImmutableSetMemoryTest.this.getKeyFactory().valueOf(0));
            }
            HashSet<Object> set = new HashSet<>(this.size);
            for (int i = 0; i < this.size; i++)
            {
                set.add(AbstractImmutableSetMemoryTest.this.getKeyFactory().valueOf(i));
            }
            return Collections.unmodifiableSet(set);
        }
    }

    private final class SizedImmutableScalaSetFactory implements Function0<scala.collection.immutable.Set<Object>>
    {
        private final int size;

        private SizedImmutableScalaSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public scala.collection.immutable.Set<Object> value()
        {
            scala.collection.mutable.HashSet<Object> mutableSet = new scala.collection.mutable.HashSet<Object>();
            for (int i = 0; i < this.size; i++)
            {
                mutableSet.$plus$eq(AbstractImmutableSetMemoryTest.this.getKeyFactory().valueOf(i));
            }
            return mutableSet.toSet();
        }
    }
}
