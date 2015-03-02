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

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.immutable.HashSet;

public class ImmutableSetMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableSetMemoryTest.class);

    @Test
    public void memoryForScaledSets()
    {
        LOGGER.info("Comparing Items: Scala {}, GSC {}, Guava {}",
                HashSet.class.getSimpleName(),
                ImmutableSet.class.getSimpleName(),
                com.google.common.collect.ImmutableSet.class.getSimpleName());
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledSets(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    private void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(HashSet.class).printContainerMemoryUsage("Set", size, new ScalaImmutableSetFactory(size));
        MemoryTestBench.on(ImmutableSet.class).printContainerMemoryUsage("Set", size, new ImmutableSetFactory(size));
        MemoryTestBench.on(com.google.common.collect.ImmutableSet.class).printContainerMemoryUsage("Set", size, new GuavaImmutableSetFactory(size));
    }

    private static final class ImmutableSetFactory
            implements Function0<ImmutableSet<Integer>>
    {
        private final ImmutableList<Integer> data;

        private ImmutableSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }

        @Override
        public ImmutableSet<Integer> value()
        {
            final MutableSet<Integer> integers = new UnifiedSet<>();
            this.data.forEach(new Procedure<Integer>()
            {
                @Override
                public void value(Integer each)
                {
                    integers.add(each);
                }
            });
            return integers.toImmutable();
        }
    }

    private static final class ScalaImmutableSetFactory
            implements Function0<HashSet<Integer>>
    {
        private final ImmutableList<Integer> data;

        private ScalaImmutableSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }

        @Override
        public HashSet<Integer> value()
        {
            final HashSet[] set = {new HashSet()};
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    set[0] = set[0].$plus(each);
                }
            });
            return set[0];
        }
    }

    private static final class GuavaImmutableSetFactory
            implements Function0<com.google.common.collect.ImmutableSet<Integer>>
    {
        private final ImmutableList<Integer> data;

        private GuavaImmutableSetFactory(int size)
        {
            this.data = TestDataFactory.createRandomImmutableList(size);
        }

        @Override
        public com.google.common.collect.ImmutableSet<Integer> value()
        {
            final com.google.common.collect.ImmutableSet.Builder<Integer> builder = com.google.common.collect.ImmutableSet.builder();
            this.data.forEach(new Procedure<Integer>()
            {
                @Override
                public void value(Integer each)
                {
                    builder.add(each);
                }
            });
            return builder.build();
        }
    }
}
