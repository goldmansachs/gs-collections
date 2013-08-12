/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.parallel;

import java.util.concurrent.ExecutorService;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.set.mutable.MultiReaderUnifiedSet;
import org.junit.Assert;
import org.junit.Test;

public class ParallelArrayIterateAcceptanceTest
{
    private int count = 0;
    private final MutableSet<String> threadNames = MultiReaderUnifiedSet.newSet();

    @Test
    public void oneLevelCall()
    {
        new RecursiveProcedure().value(1);

        synchronized (this)
        {
            Assert.assertEquals("all iterations completed", 20000, this.count);
        }
    }

    @Test
    public void nestedCall()
    {
        new RecursiveProcedure().value(2);

        synchronized (this)
        {
            Assert.assertEquals("all iterations completed", 419980, this.count);
        }
        Assert.assertTrue("uses multiple threads", this.threadNames.size() > 1);
    }

    private class RecursiveProcedure implements Procedure<Integer>
    {
        private static final long serialVersionUID = 1L;
        private final ExecutorService executorService = ParallelIterate.newPooledExecutor("ParallelArrayIterateAcceptanceTest", false);

        @Override
        public void value(Integer object)
        {
            int level = object.intValue();
            if (level > 0)
            {
                ParallelArrayIterateAcceptanceTest.this.threadNames.add(Thread.currentThread().getName());
                this.executeParallelIterate(level - 1, this.executorService);
            }
            else
            {
                this.simulateWork();
            }
        }

        private void simulateWork()
        {
            synchronized (ParallelArrayIterateAcceptanceTest.this)
            {
                ParallelArrayIterateAcceptanceTest.this.count++;
            }
        }

        private void executeParallelIterate(int level, ExecutorService executorService)
        {
            MutableList<Integer> items = Lists.mutable.of();
            for (int i = 0; i < 20000; i++)
            {
                items.add(i % 1000 == 0 ? level : 0);
            }
            ParallelIterate.forEach(items, new RecursiveProcedure(), executorService);
        }
    }
}
