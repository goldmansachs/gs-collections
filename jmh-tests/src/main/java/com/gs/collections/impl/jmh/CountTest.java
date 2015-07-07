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

package com.gs.collections.impl.jmh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.block.procedure.CountProcedure;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import org.junit.Assert;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class CountTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;

    @Param({"0", "1", "2", "3"})
    public int megamorphicWarmupLevel;

    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final FastList<Integer> integersGSC = new FastList<>(Interval.oneTo(SIZE));

    private ExecutorService executorService;

    @Setup
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @TearDown
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
    }

    @Setup(Level.Trial)
    public void setUp_megamorphic()
    {
        if (this.megamorphicWarmupLevel > 0)
        {
            // serial, lazy, JDK
            {
                long evens = this.integersJDK.stream().filter(each -> each % 2 == 0).count();
                Assert.assertEquals(SIZE / 2, evens);
                long odds = this.integersJDK.stream().filter(each -> each % 2 == 1).count();
                Assert.assertEquals(SIZE / 2, odds);
                long evens2 = this.integersJDK.stream().filter(each -> (each & 1) == 0).count();
                Assert.assertEquals(SIZE / 2, evens2);
            }

            // parallel, lazy, JDK
            {
                long evens = this.integersJDK.parallelStream().filter(each -> each % 2 == 0).count();
                Assert.assertEquals(SIZE / 2, evens);
                long odds = this.integersJDK.parallelStream().filter(each -> each % 2 == 1).count();
                Assert.assertEquals(SIZE / 2, odds);
                long evens2 = this.integersJDK.parallelStream().filter(each -> (each & 1) == 0).count();
                Assert.assertEquals(SIZE / 2, evens2);
            }

            // serial, lazy, GSC
            {
                long evens = this.integersGSC.asLazy().count(each -> each % 2 == 0);
                Assert.assertEquals(SIZE / 2, evens);
                long odds = this.integersGSC.asLazy().count(each -> each % 2 == 1);
                Assert.assertEquals(SIZE / 2, odds);
                long evens2 = this.integersGSC.asLazy().count(each -> (each & 1) == 0);
                Assert.assertEquals(SIZE / 2, evens2);
            }

            // parallel, lazy, GSC
            {
                long evens = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(each -> each % 2 == 0);
                Assert.assertEquals(SIZE / 2, evens);
                long odds = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(each -> each % 2 == 1);
                Assert.assertEquals(SIZE / 2, odds);
                long evens2 = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(each -> (each & 1) == 0);
                Assert.assertEquals(SIZE / 2, evens2);
            }

            // serial, eager, GSC
            {
                long evens = this.integersGSC.count(each -> each % 2 == 0);
                Assert.assertEquals(SIZE / 2, evens);
                long odds = this.integersGSC.count(each -> each % 2 == 1);
                Assert.assertEquals(SIZE / 2, odds);
                long evens2 = this.integersGSC.count(each -> (each & 1) == 0);
                Assert.assertEquals(SIZE / 2, evens2);
            }

            // parallel, eager, GSC
            long evens = ParallelIterate.count(this.integersGSC, each -> each % 2 == 0);
            Assert.assertEquals(SIZE / 2, evens);
            long odds = ParallelIterate.count(this.integersGSC, each -> each % 2 == 1);
            Assert.assertEquals(SIZE / 2, odds);
            long evens2 = ParallelIterate.count(this.integersGSC, each -> (each & 1) == 0);
            Assert.assertEquals(SIZE / 2, evens2);
        }

        if (this.megamorphicWarmupLevel > 1)
        {
            // stream().mapToLong().reduce()
            Assert.assertEquals(
                    500001500000L,
                    this.integersJDK.stream().mapToLong(each -> each + 1).reduce(0, (accum, each) -> accum + each));

            Assert.assertEquals(
                    500002500000L,
                    this.integersJDK.stream().mapToLong(each -> each + 2).reduce(0, (accum, each) -> {
                        Assert.assertTrue(each >= 0);
                        return accum + each;
                    }));

            Assert.assertEquals(
                    500003500000L,
                    this.integersJDK.stream().mapToLong(each -> each + 3).reduce(0, (accum, each) -> {
                        long result = accum + each;
                        Assert.assertTrue(each >= 0);
                        return result;
                    }));

            // parallelStream().mapToLong().reduce()
            Assert.assertEquals(
                    500001500000L,
                    this.integersJDK.parallelStream().mapToLong(each -> each + 1).reduce(0, (accum, each) -> accum + each));

            Assert.assertEquals(
                    500002500000L,
                    this.integersJDK.parallelStream().mapToLong(each -> each + 2).reduce(0, (accum, each) -> {
                        Assert.assertTrue(each >= 0);
                        return accum + each;
                    }));

            Assert.assertEquals(
                    500003500000L,
                    this.integersJDK.parallelStream().mapToLong(each -> each + 3).reduce(0, (accum, each) -> {
                        long result = accum + each;
                        Assert.assertTrue(each >= 0);
                        return result;
                    }));
        }

        if (this.megamorphicWarmupLevel > 2)
        {
            this.integersGSC.asLazy().forEach(Procedures.cast(Assert::assertNotNull));
            this.integersGSC.asLazy().forEach(Procedures.cast(each -> Assert.assertEquals(each, each)));
            this.integersGSC.asLazy().forEach(new CountProcedure<>());

            this.integersGSC.asParallel(this.executorService, BATCH_SIZE).forEach(Assert::assertNotNull);
            this.integersGSC.asParallel(this.executorService, BATCH_SIZE).forEach(each -> Assert.assertEquals(each, each));
            this.integersGSC.asParallel(this.executorService, BATCH_SIZE).forEach(new CountProcedure<>());

            this.integersJDK.stream().forEach(Assert::assertNotNull);
            this.integersJDK.stream().forEach(each -> Assert.assertEquals(each, each));

            this.integersJDK.parallelStream().forEach(Assert::assertNotNull);
            this.integersJDK.parallelStream().forEach(each -> Assert.assertEquals(each, each));
        }

        CountScalaTest.megamorphic(this.megamorphicWarmupLevel);
    }

    @Benchmark
    public void serial_lazy_jdk()
    {
        long evens = this.integersJDK.stream().filter(each -> each % 2 == 0).count();
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Benchmark
    public void serial_lazy_streams_gsc()
    {
        long evens = this.integersGSC.stream().filter(each -> each % 2 == 0).count();
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Benchmark
    public void parallel_lazy_jdk()
    {
        long evens = this.integersJDK.parallelStream().filter(each -> each % 2 == 0).count();
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Benchmark
    public void parallel_lazy_streams_gsc()
    {
        long evens = this.integersGSC.parallelStream().filter(each -> each % 2 == 0).count();
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Benchmark
    public void serial_eager_gsc()
    {
        int evens = this.integersGSC.count(each -> each % 2 == 0);
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Benchmark
    public void serial_lazy_gsc()
    {
        int evens = this.integersGSC.asLazy().count(each -> each % 2 == 0);
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Benchmark
    public void parallel_eager_gsc()
    {
        int evens = ParallelIterate.count(this.integersGSC, each -> each % 2 == 0, BATCH_SIZE, this.executorService);
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Benchmark
    public void parallel_lazy_gsc()
    {
        int evens = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(each -> each % 2 == 0);
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Benchmark
    public void serial_eager_scala()
    {
        CountScalaTest.serial_eager_scala();
    }

    @Benchmark
    public void serial_lazy_scala()
    {
        CountScalaTest.serial_lazy_scala();
    }

    @Benchmark
    public void parallel_lazy_scala()
    {
        CountScalaTest.parallel_lazy_scala();
    }
}
