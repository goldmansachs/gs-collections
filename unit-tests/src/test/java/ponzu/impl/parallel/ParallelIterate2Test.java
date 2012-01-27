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

package ponzu.impl.parallel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import ponzu.impl.list.Interval;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ParallelIterate}.
 */
public class ParallelIterate2Test
{
    /**
     * crude test to check that creation works and that all tasks are executed
     */
    @Test
    public void creationAndExecution() throws InterruptedException
    {
        int howManyTimes = 200;
        final AtomicInteger counter = new AtomicInteger(0);

        final Callable<Integer> task = new Callable<Integer>()
        {
            public Integer call()
            {
                return counter.getAndIncrement();
            }
        };

        final Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();

        Interval.oneTo(howManyTimes).run(new Runnable()
        {
            public void run()
            {
                tasks.add(task);
            }
        });

        ExecutorService executorService1 = ParallelIterate.newPooledExecutor(4, "test pool 2 4", true);
        executorService1.invokeAll(tasks);
        Assert.assertEquals(howManyTimes, counter.get());

        counter.set(0);
        ExecutorService executorService2 = ParallelIterate.newPooledExecutor(2, "test pool 2", true);
        executorService2.invokeAll(tasks);
        Assert.assertEquals(howManyTimes, counter.get());
    }
}
