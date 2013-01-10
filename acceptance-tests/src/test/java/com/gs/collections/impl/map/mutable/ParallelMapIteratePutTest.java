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

package com.gs.collections.impl.map.mutable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.impl.set.mutable.UnifiedSet;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;

public class ParallelMapIteratePutTest
{

    private static final long SEED = 0x12345678ABCDL;

    private static final long PUT_REPEAT = 100;
    private static final int CHUNK_SIZE = 16000;
    private static final int MAX_THREADS = 48;

    @After
    public void tearDown()
    {
        fullGc();
    }

    private static void fullGc()
    {
        System.gc();
        Thread.yield();
        System.gc();
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            //ignore
        }
    }

    //    @Ignore
    @Test
    public void testMapIteratePut() throws Exception
    {
        int constSize = 100000;
        int size = 10000000;
        Integer[] contents = new Integer[size];
        Integer[] constContents = new Integer[constSize];
        for(int i=0;i<size;i++)
        {
            contents[i] = i;
            if (i < constSize/2)
            {
                constContents[i] = i;
            }
            else if (i < constSize)
            {
                constContents[i] = size - i;
            }
        }
        Collections.shuffle(Arrays.asList(contents), new Random(SEED));
        this.runAllPutTests(contents, constContents);

    }

    private void runAllPutTests(Integer[] contents, Integer[] constContents)
    {
        ExecutorService executorService = new ThreadPoolExecutor(MAX_THREADS, MAX_THREADS, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(MAX_THREADS));
        int threads = 10;
        this.runPutTest1(threads, contents, constContents, executorService, false);
        executorService.shutdown();
    }

    private void runPutTest1(int threadCount, Integer[] contents, Integer[] constContents, ExecutorService executorService, boolean warmup)
    {
        long ops = (warmup ? 1000000/contents.length : 1000000*PUT_REPEAT/contents.length) + 1;
        Future[] futures = new Future[threadCount];
        for(int i=0; i < ops; i++)
        {
            ConcurrentHashMap map = new ConcurrentHashMap(constContents.length);
            UnifiedSet setToRemove = new UnifiedSet(constContents.length);
            for(Integer x: constContents)
            {
                map.put(x,x);
                setToRemove.put(x);
            }
            AtomicInteger currentPos = new AtomicInteger();
            for(int t=0;t<threadCount;t++)
            {
                futures[t] = executorService.submit(new PutRunner1(map, contents, currentPos));
            }
            int count = 0;
            UnifiedSet setToAdd = new UnifiedSet(constContents.length);
            for(Iterator it = map.keySet().iterator(); it.hasNext();)
            {
                Object next = it.next();
                setToRemove.remove(next);
                Assert.assertTrue(setToAdd.add(next));
                count++;
            }
            Assert.assertTrue(count >= constContents.length);
            Assert.assertEquals(0, setToRemove.size());
            for(Future future: futures)
            {
                try
                {
                    future.get();
                }
                catch (Exception e)
                {
                    throw new RuntimeException("unexpected", e);
                }
            }
            if (map.size() != contents.length)
            {
                System.out.println("wrong");
            }
        }
    }


    private static class PutRunner1 implements Runnable
    {
        private final Map map;
        private final Integer[] contents;
        private long total;
        private final AtomicInteger queuePosition;

        private PutRunner1(Map map, Integer[] contents, AtomicInteger queuePosition)
        {
            this.map = map;
            this.contents = contents;
            this.queuePosition = queuePosition;
        }

        public void run()
        {
            while(this.queuePosition.get() < this.contents.length)
            {
                int end = this.queuePosition.addAndGet(CHUNK_SIZE);
                int start = end - CHUNK_SIZE;
                if (start < this.contents.length)
                {
                    if (end > this.contents.length)
                    {
                        end = this.contents.length;
                    }
                    for(int i=start;i<end;i++)
                    {
                        if (this.map.put(this.contents[i], this.contents[i]) != null)
                        {
                            this.total++;
                        }
                    }
                }
            }
            if (this.total < 0)
            {
                System.out.println("wrong"); // never gets here, but it can't be optimized away
            }
        }
    }

}
