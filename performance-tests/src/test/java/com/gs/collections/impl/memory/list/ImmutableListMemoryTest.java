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

package com.gs.collections.impl.memory.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.impl.MemoryTests;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.mutable.ListBuffer;

public class ImmutableListMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImmutableListMemoryTest.class);

    @Test
    @Category(MemoryTests.class)
    public void memoryForScaledImmutableLists()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, GSC {}, Guava {}",
                scala.collection.immutable.List.class.getSimpleName(),
                List.class.getSimpleName(),
                com.gs.collections.api.list.ImmutableList.class.getSimpleName(),
                ImmutableList.class.getSimpleName());

        IntProcedure procedure = new IntProcedure()
        {
            public void value(int size)
            {
                ImmutableListMemoryTest.this.memoryForScaledLists(size);
            }
        };
        IntInterval.zeroTo(9).forEach(procedure);
        IntInterval.fromToBy(10, 100, 10).forEach(procedure);
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledLists(int size)
    {
        MemoryTestBench.on(scala.collection.immutable.List.class)
                .printContainerMemoryUsage("ImmutableList", size, new SizedImmutableScalaListFactory(size));
        MemoryTestBench.on(List.class)
                .printContainerMemoryUsage("ImmutableList", size, new SizedUnmodifiableArrayListFactory(size));
        MemoryTestBench.on(com.gs.collections.api.list.ImmutableList.class)
                .printContainerMemoryUsage("ImmutableList", size, new SizedImmutableGscListFactory(size));
        MemoryTestBench.on(ImmutableList.class)
                .printContainerMemoryUsage("ImmutableList", size, new SizedImmutableGuavaListFactory(size));
    }

    private static final class SizedImmutableGscListFactory implements Function0<com.gs.collections.api.list.ImmutableList<String>>
    {
        private final int size;

        private SizedImmutableGscListFactory(int size)
        {
            this.size = size;
        }

        @Override
        public com.gs.collections.api.list.ImmutableList<String> value()
        {
            return FastList.newList(Collections.nCopies(this.size, "dummy")).toImmutable();
        }
    }

    private static final class SizedImmutableGuavaListFactory implements Function0<ImmutableList<String>>
    {
        private final int size;

        private SizedImmutableGuavaListFactory(int size)
        {
            this.size = size;
        }

        @Override
        public ImmutableList<String> value()
        {
            return ImmutableList.<String>builder().addAll(Collections.nCopies(this.size, "dummy")).build();
        }
    }

    private static final class SizedUnmodifiableArrayListFactory implements Function0<List<String>>
    {
        private final int size;

        private SizedUnmodifiableArrayListFactory(int size)
        {
            this.size = size;
        }

        @Override
        public List<String> value()
        {
            if (this.size == 0)
            {
                return Collections.emptyList();
            }
            if (this.size == 1)
            {
                return Collections.singletonList("dummy");
            }
            return Collections.unmodifiableList(new ArrayList<String>(Collections.nCopies(this.size, "dummy")));
        }
    }

    private static final class SizedImmutableScalaListFactory implements Function0<scala.collection.immutable.List<String>>
    {
        private final int size;

        private SizedImmutableScalaListFactory(int size)
        {
            this.size = size;
        }

        @Override
        public scala.collection.immutable.List<String> value()
        {
            ListBuffer<String> listBuffer = new ListBuffer<String>();
            for (int i = 0; i < this.size; i++)
            {
                listBuffer.$plus$eq("dummy");
            }

            return listBuffer.toList();
        }
    }
}
