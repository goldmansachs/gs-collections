/*
 * Copyright (C) 2000-2013 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.memory;

import java.text.NumberFormat;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.impl.list.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MemoryTestBench
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryTestBench.class);

    private static final GCAndSleepProcedure GC_AND_SLEEP_PROCEDURE = new GCAndSleepProcedure();
    private static final Interval GC_INTERVAL = Interval.oneTo(20);
    private final Class<?> clazz;
    private final String suffix;

    private MemoryTestBench(Class<?> clazz, String suffix)
    {
        this.clazz = clazz;
        this.suffix = suffix;
    }

    public static MemoryTestBench on(Class<?> clazz)
    {
        return MemoryTestBench.on(clazz, "");
    }

    public static MemoryTestBench on(Class<?> clazz, String suffix)
    {
        return new MemoryTestBench(clazz, suffix);
    }

    /**
     * From newsletter 193
     * (http://www.javaspecialists.eu/archive/Issue193.html).  Used
     * to estimate memory usage by objects.
     */
    public long calculateMemoryUsage(Function0<?> factory)
    {
        // Clean the slate and prep
        this.forceGCAndSleepMultipleTimes();
        Object container = factory.value();
        if (!this.clazz.isInstance(container))
        {
            throw new RuntimeException(container.getClass().getCanonicalName());
        }
        long memory = this.currentUsedMemory();
        //noinspection UnusedAssignment,ReuseOfLocalVariable
        container = null;
        this.forceGCAndSleepMultipleTimes();

        // Calculate memory before creation
        long memory2 = this.currentUsedMemory();
        //noinspection UnusedAssignment,ReuseOfLocalVariable
        container = factory.value();
        // Get rid of transient garbage
        this.forceGCAndSleepMultipleTimes();
        // Calculate new used memory
        return this.currentUsedMemory() - memory2;
    }

    private long currentUsedMemory()
    {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private void forceGCAndSleepMultipleTimes()
    {
        GC_INTERVAL.forEach(GC_AND_SLEEP_PROCEDURE);
    }

    public void printContainerMemoryUsage(String category, int size, Function0<?> factory)
    {
        String memoryUsedInBytes = NumberFormat.getInstance().format(this.calculateMemoryUsage(factory));
        String sizeFormatted = NumberFormat.getInstance().format(size);
        LOGGER.info("{} {}{} size {} bytes {}", category, this.clazz.getName(), this.suffix, sizeFormatted, memoryUsedInBytes);
    }

    private static class GCAndSleepProcedure implements IntProcedure
    {
        @Override
        public void value(int each)
        {
            System.gc();
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
