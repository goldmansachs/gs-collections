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

package com.gs.collections.impl.jmh.list;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import org.junit.After;
import org.junit.Before;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ListIterationTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;

    private final MutableList<Integer> gscMutable = Lists.mutable.withAll(Interval.zeroTo(SIZE));
    private final ImmutableList<Integer> gscImmutable = Lists.immutable.withAll(Interval.zeroTo(SIZE));

    private ExecutorService executorService;

    @Before
    @Setup
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @After
    @TearDown
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
    }

    @Benchmark
    public void serial_mutable_gsc()
    {
        int count = this.gscMutable
                .asLazy()
                .select(each -> each % 10_000 != 0)
                .collect(String::valueOf)
                .collect(Integer::valueOf)
                .count(each -> (each + 1) % 10_000 != 0);
        if (count != 999_800)
        {
            throw new AssertionError();
        }
    }

    @Benchmark
    public void serial_immutable_gsc()
    {
        int count = this.gscImmutable
                .asLazy()
                .select(each -> each % 10_000 != 0)
                .collect(String::valueOf)
                .collect(Integer::valueOf)
                .count(each -> (each + 1) % 10_000 != 0);
        if (count != 999_800)
        {
            throw new AssertionError();
        }
    }

    @Benchmark
    public void parallel_mutable_gsc()
    {
        int count = this.gscMutable
                .asParallel(this.executorService, BATCH_SIZE)
                .select(each -> each % 10_000 != 0)
                .collect(String::valueOf)
                .collect(Integer::valueOf)
                .count(each -> (each + 1) % 10_000 != 0);
        if (count != 999_800)
        {
            throw new AssertionError();
        }
    }

    @Benchmark
    public void parallel_immutable_gsc()
    {
        int count = this.gscImmutable
                .asParallel(this.executorService, BATCH_SIZE)
                .select(each -> each % 10_000 != 0)
                .collect(String::valueOf)
                .collect(Integer::valueOf)
                .count(each -> (each + 1) % 10_000 != 0);
        if (count != 999_800)
        {
            throw new AssertionError();
        }
    }

    @Benchmark
    public void serial_mutable_scala()
    {
        ScalaListIterationTest.serial_mutable_scala();
    }

    @Benchmark
    public void parallel_mutable_scala()
    {
        ScalaListIterationTest.parallel_mutable_scala();
    }
}
