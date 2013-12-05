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
import java.util.LinkedList;
import java.util.List;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.impl.MemoryTests;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.mutable.ArrayBuffer;
import scala.collection.mutable.ListBuffer;

public class ListAddMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ListAddMemoryTest.class);

    @Test
    @Category(MemoryTests.class)
    public void memoryForScaledLists()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, GSC {}, Scala {}, JDK {}",
                ArrayBuffer.class.getSimpleName(),
                ArrayList.class.getSimpleName(),
                FastList.class.getSimpleName(),
                ListBuffer.class.getSimpleName(),
                LinkedList.class.getSimpleName());
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledLists(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledLists(int size)
    {
        MemoryTestBench.on(ArrayBuffer.class)
                .printContainerMemoryUsage("ListAdd", size, new ArrayBufferFactory(size));
        MemoryTestBench.on(ArrayList.class)
                .printContainerMemoryUsage("ListAdd", size, new ArrayListFactory(size));
        MemoryTestBench.on(FastList.class)
                .printContainerMemoryUsage("ListAdd", size, new FastListFactory(size));
        MemoryTestBench.on(ListBuffer.class)
                .printContainerMemoryUsage("ListAdd", size, new ListBufferFactory(size));
        MemoryTestBench.on(LinkedList.class)
                .printContainerMemoryUsage("ListAdd", size, new LinkedListFactory(size));
    }

    private abstract static class SizedListFactory
    {
        protected final int size;

        protected SizedListFactory(int size)
        {
            this.size = size;
        }

        protected <R extends List<String>> R fill(R list)
        {
            for (int i = 0; i < this.size; i++)
            {
                list.add("dummy");
            }
            return list;
        }
    }

    private static final class ArrayListFactory
            extends SizedListFactory
            implements Function0<ArrayList<String>>
    {
        private ArrayListFactory(int size)
        {
            super(size);
        }

        @Override
        public ArrayList<String> value()
        {
            return this.fill(new ArrayList<String>());
        }
    }

    private static final class LinkedListFactory
            extends SizedListFactory
            implements Function0<LinkedList<String>>
    {
        private LinkedListFactory(int size)
        {
            super(size);
        }

        @Override
        public LinkedList<String> value()
        {
            return this.fill(new LinkedList<String>());
        }
    }

    private static final class FastListFactory
            extends SizedListFactory
            implements Function0<FastList<String>>
    {
        private FastListFactory(int size)
        {
            super(size);
        }

        @Override
        public FastList<String> value()
        {
            return this.fill(FastList.<String>newList());
        }
    }

    private static final class ListBufferFactory
            extends SizedListFactory
            implements Function0<ListBuffer<String>>
    {
        private ListBufferFactory(int size)
        {
            super(size);
        }

        @Override
        public ListBuffer<String> value()
        {
            ListBuffer<String> list = new ListBuffer<String>();
            for (int i = 0; i < this.size; i++)
            {
                list.$plus$eq("dummy");
            }
            return list;
        }
    }

    private static final class ArrayBufferFactory
            extends SizedListFactory
            implements Function0<ArrayBuffer<String>>
    {
        private ArrayBufferFactory(int size)
        {
            super(size);
        }

        @Override
        public ArrayBuffer<String> value()
        {
            ArrayBuffer<String> list = new ArrayBuffer<String>();
            for (int i = 0; i < this.size; i++)
            {
                list.$plus$eq("dummy");
            }
            return list;
        }
    }
}
