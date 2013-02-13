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
import java.util.LinkedList;
import java.util.List;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;

public class ListAddAllMemoryTest
{
    @Test
    public void memoryForScaledLists()
    {
        this.memoryForScaledLists(0);
        this.memoryForScaledLists(10);
        this.memoryForScaledLists(50);
        this.memoryForScaledLists(100);
        this.memoryForScaledLists(500);
        this.memoryForScaledLists(1000);
        this.memoryForScaledLists(5000);
        this.memoryForScaledLists(10000);
        this.memoryForScaledLists(50000);
        this.memoryForScaledLists(100000);
        this.memoryForScaledLists(500000);
        this.memoryForScaledLists(1000000);
    }

    public void memoryForScaledLists(int size)
    {
        MemoryTestBench.on(ArrayList.class)
                .printContainerMemoryUsage("ListAddAll", size, new ArrayListFactory(size));
        MemoryTestBench.on(FastList.class)
                .printContainerMemoryUsage("ListAddAll", size, new FastListFactory(size));
        MemoryTestBench.on(LinkedList.class)
                .printContainerMemoryUsage("ListAddAll", size, new LinkedListFactory(size));
    }

    public abstract static class SizedListFactory
    {
        private final int size;

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

    public static class ArrayListFactory
            extends SizedListFactory
            implements Function0<ArrayList<String>>
    {
        protected ArrayListFactory(int size)
        {
            super(size);
        }

        public ArrayList<String> value()
        {
            return this.fill(new ArrayList<String>(0));
        }
    }

    public static class LinkedListFactory
            extends SizedListFactory
            implements Function0<LinkedList<String>>
    {
        protected LinkedListFactory(int size)
        {
            super(size);
        }

        public LinkedList<String> value()
        {
            return this.fill(new LinkedList());
        }
    }

    public static class FastListFactory
            extends SizedListFactory
            implements Function0<FastList<String>>
    {
        protected FastListFactory(int size)
        {
            super(size);
        }

        public FastList<String> value()
        {
            return this.fill(FastList.<String>newList(0));
        }
    }
}
