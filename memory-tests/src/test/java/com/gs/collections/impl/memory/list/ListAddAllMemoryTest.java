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

package com.gs.collections.impl.memory.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.mutable.ArrayBuffer;
import scala.collection.mutable.ListBuffer;

public class ListAddAllMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ListAddAllMemoryTest.class);

    @Test
    public void memoryForScaledLists()
    {
        LOGGER.info("Comparing Items: Scala {}, JDK {}, GSC {}, Scala {}, JDK {}",
                ArrayBuffer.class.getSimpleName(),
                ArrayList.class.getSimpleName(),
                FastList.class.getSimpleName(),
                ListBuffer.class.getSimpleName(),
                LinkedList.class.getSimpleName());
        for (int size = 0; size < 1000001; size += 100000)
        {
            this.memoryForScaledLists(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledLists(int size)
    {
        MemoryTestBench.on(ArrayBuffer.class)
                .printContainerMemoryUsage("ListAddAll", size, new ArrayBufferFactory(size));
        MemoryTestBench.on(ArrayList.class)
                .printContainerMemoryUsage("ListAddAll", size, new ArrayListFactory(size));
        MemoryTestBench.on(FastList.class)
                .printContainerMemoryUsage("ListAddAll", size, new FastListFactory(size));
        MemoryTestBench.on(ListBuffer.class)
                .printContainerMemoryUsage("ListAddAll", size, new ListBufferFactory(size));
        MemoryTestBench.on(LinkedList.class)
                .printContainerMemoryUsage("ListAddAll", size, new LinkedListFactory(size));
    }

    public abstract static class SizedListFactory
    {
        protected final int size;

        protected SizedListFactory(int size)
        {
            this.size = size;
        }

        protected <R extends List<String>> R fill(R list)
        {
            if (this.size > 0)
            {
                list.addAll(Collections.nCopies(this.size, "dummy"));
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
            return this.fill(new ArrayList<String>(0));
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
            return this.fill(FastList.<String>newList(0));
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
            ListBuffer<String> nonPresized = new ListBuffer<String>();
            for (int i = 0; i < this.size; i++)
            {
                nonPresized.$plus$eq("dummy");
            }
            ListBuffer<String> list = new ListBuffer<String>();
            if (this.size > 0)
            {
                list.$plus$plus$eq(nonPresized);
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
            ArrayBuffer<String> nonPresized = new ArrayBuffer<String>();
            for (int i = 0; i < this.size; i++)
            {
                nonPresized.$plus$eq("dummy");
            }
            ArrayBuffer<String> list = new ArrayBuffer<String>();
            if (this.size > 0)
            {
                list.$plus$plus$eq(nonPresized.seq());
            }
            return list;
        }
    }
}
