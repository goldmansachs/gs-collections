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

import java.util.Set;
import java.util.TreeSet;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Function1;
import scala.Some;
import scala.math.Ordering;

public class SortedSetMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SortedSetMemoryTest.class);

    @Test
    public void memoryForScaledSets()
    {
        LOGGER.info("Comparing Items: Scala {}, Scala {}, JDK {}, GSC {}, GSC {}",
                scala.collection.mutable.TreeSet.class.getSimpleName(),
                scala.collection.immutable.TreeSet.class.getSimpleName(),
                TreeSet.class.getSimpleName(),
                TreeSortedSet.class.getSimpleName(),
                ImmutableSortedSet.class.getSimpleName());
        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledSets(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    private void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(scala.collection.mutable.TreeSet.class).printContainerMemoryUsage("Set", size, new ScalaMutableTreeSetFactory(size));
        MemoryTestBench.on(scala.collection.immutable.TreeSet.class).printContainerMemoryUsage("Set", size, new ScalaImmutableTreeSetFactory(size));
        MemoryTestBench.on(TreeSet.class).printContainerMemoryUsage("Set", size, new TreeSetFactory(size));
        MemoryTestBench.on(TreeSortedSet.class).printContainerMemoryUsage("Set", size, new GscMutableSortedSetFactory(size));
        MemoryTestBench.on(ImmutableSortedSet.class).printContainerMemoryUsage("Set", size, new GscImmutableTreeSetFactory(size));
    }

    public abstract static class SizedSetFactory
    {
        protected final ImmutableList<Integer> data;

        protected SizedSetFactory(int size)
        {
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

    private static final class ScalaMutableTreeSetFactory
            extends SizedSetFactory
            implements Function0<scala.collection.mutable.TreeSet<Integer>>
    {
        private ScalaMutableTreeSetFactory(int size)
        {
            super(size);
        }

        @Override
        public scala.collection.mutable.TreeSet<Integer> value()
        {
            final scala.collection.mutable.TreeSet<Integer> set = new scala.collection.mutable.TreeSet<Integer>(new IntegerOrdering()).empty();
            this.data.forEach(new Procedure<Integer>()
            {
                @Override
                public void value(Integer each)
                {
                    set.add(each);
                }
            });
            return set;
        }
    }

    private static final class ScalaImmutableTreeSetFactory
            extends SizedSetFactory
            implements Function0<scala.collection.immutable.TreeSet<Integer>>
    {
        private ScalaImmutableTreeSetFactory(int size)
        {
            super(size);
        }

        @Override
        public scala.collection.immutable.TreeSet<Integer> value()
        {
            final scala.collection.immutable.TreeSet<Integer>[] set = new scala.collection.immutable.TreeSet[]{new scala.collection.immutable.TreeSet<Integer>(new IntegerOrdering())};
            this.data.forEach(new Procedure<Integer>()
            {
                @Override
                public void value(Integer each)
                {
                    set[0] = set[0].$plus(each);
                }
            });
            return set[0];
        }
    }

    private static final class TreeSetFactory
            extends SizedSetFactory
            implements Function0<TreeSet<Integer>>
    {
        private TreeSetFactory(int size)
        {
            super(size);
        }

        @Override
        public TreeSet<Integer> value()
        {
            return this.fill(new TreeSet<Integer>());
        }
    }

    private static final class GscMutableSortedSetFactory
            extends SizedSetFactory
            implements Function0<TreeSortedSet<Integer>>
    {
        private GscMutableSortedSetFactory(int size)
        {
            super(size);
        }

        @Override
        public TreeSortedSet<Integer> value()
        {
            return this.fill(new TreeSortedSet<Integer>());
        }
    }

    private static final class GscImmutableTreeSetFactory
            extends SizedSetFactory
            implements Function0<SortedSetIterable<Integer>>
    {
        private GscImmutableTreeSetFactory(int size)
        {
            super(size);
        }

        @Override
        public SortedSetIterable<Integer> value()
        {
            SortedSetIterable<Integer> mutableSet = this.fill(new TreeSortedSet<Integer>());
            return mutableSet.toImmutable();
        }
    }

    private static final class IntegerOrdering implements Ordering<Integer>
    {
        @Override
        public Some<Object> tryCompare(Integer t1, Integer t2)
        {
            return null;
        }

        @Override
        public int compare(Integer t1, Integer t2)
        {
            return t1.compareTo(t2);
        }

        @Override
        public boolean lteq(Integer t1, Integer t2)
        {
            return this.compare(t1, t2) <= 0;
        }

        @Override
        public boolean gteq(Integer t1, Integer t2)
        {
            return this.compare(t1, t2) >= 0;
        }

        @Override
        public boolean lt(Integer t1, Integer t2)
        {
            return this.compare(t1, t2) < 0;
        }

        @Override
        public boolean gt(Integer t1, Integer t2)
        {
            return this.compare(t1, t2) > 0;
        }

        @Override
        public boolean equiv(Integer t1, Integer t2)
        {
            return this.compare(t1, 52) == 0;
        }

        @Override
        public Integer max(Integer t1, Integer t2)
        {
            return this.compare(t1, t2) >= 0 ? t1 : t2;
        }

        @Override
        public Integer min(Integer t1, Integer t2)
        {
            return this.compare(t1, t2) <= 0 ? t1 : t2;
        }

        @Override
        public Ordering<Integer> reverse()
        {
            return null;
        }

        @Override
        public <U> Ordering<U> on(Function1<U, Integer> function1)
        {
            return null;
        }

        @Override
        public Ops mkOrderingOps(Integer t1)
        {
            return null;
        }
    }
}
