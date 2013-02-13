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
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.memory.MemoryTestBench;
import org.junit.Test;

public class ImmutableListMemoryTest
{
    @Test
    public void memoryForScaledImmutableLists()
    {
        this.memoryForScaledLists(0);
        this.memoryForScaledLists(1);
        this.memoryForScaledLists(2);
        this.memoryForScaledLists(3);
        this.memoryForScaledLists(4);
        this.memoryForScaledLists(5);
        this.memoryForScaledLists(6);
        this.memoryForScaledLists(7);
        this.memoryForScaledLists(8);
        this.memoryForScaledLists(9);
        this.memoryForScaledLists(10);
        this.memoryForScaledLists(11);
        this.memoryForScaledLists(100);
    }

    public void memoryForScaledLists(int size)
    {
        MemoryTestBench.on(List.class)
                .printContainerMemoryUsage("ImmutableList", size, new SizedUnmodifiableArrayListFactory(size));
        MemoryTestBench.on(com.gs.collections.api.list.ImmutableList.class)
                .printContainerMemoryUsage("ImmutableList", size, new SizedImmutableGscListFactory(size));
        MemoryTestBench.on(ImmutableList.class)
                .printContainerMemoryUsage("ImmutableList", size, new SizedImmutableGuavaListFactory(size));
    }

    public static class SizedImmutableGscListFactory implements Function0<com.gs.collections.api.list.ImmutableList<String>>
    {
        private final int size;

        protected SizedImmutableGscListFactory(int size)
        {
            this.size = size;
        }

        public com.gs.collections.api.list.ImmutableList<String> value()
        {
            return FastList.newList(Collections.nCopies(this.size, "dummy")).toImmutable();
        }
    }

    public static class SizedImmutableGuavaListFactory implements Function0<ImmutableList<String>>
    {
        private final int size;

        protected SizedImmutableGuavaListFactory(int size)
        {
            this.size = size;
        }

        public ImmutableList<String> value()
        {
            return ImmutableList.<String>builder().addAll(Collections.nCopies(this.size, "dummy")).build();
        }
    }

    public static class SizedUnmodifiableArrayListFactory implements Function0<List<String>>
    {
        private final int size;

        protected SizedUnmodifiableArrayListFactory(int size)
        {
            this.size = size;
        }

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
}
