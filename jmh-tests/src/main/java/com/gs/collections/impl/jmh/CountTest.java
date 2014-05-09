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

package com.gs.collections.impl.jmh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class CountTest
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final FastList<Integer> integersGSC = new FastList<>(Interval.oneTo(SIZE));

    private ExecutorService executorService;

    @Before
    @Setup
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Before
    @Setup(Level.Trial)
    public void megamorphic()
    {
        Predicate<Integer> jdkEven = new Predicate<Integer>()
        {
            @Override
            public boolean test(Integer each)
            {
                return each % 2 == 0;
            }
        };
        Predicate<Integer> jdkOdd = new Predicate<Integer>()
        {
            @Override
            public boolean test(Integer each)
            {
                return each % 2 == 1;
            }
        };

        // serial, lazy, JDK
        {
            long evens = this.integersJDK.stream().filter(each -> each % 2 == 0).count();
            Assert.assertEquals(SIZE / 2, evens);
            long odds = this.integersJDK.stream().filter(each -> each % 2 == 1).count();
            Assert.assertEquals(SIZE / 2, odds);
            long evens2 = this.integersJDK.stream().filter(jdkEven).count();
            Assert.assertEquals(SIZE / 2, evens2);
            long odds2 = this.integersJDK.stream().filter(jdkOdd).count();
            Assert.assertEquals(SIZE / 2, odds2);
        }

        // parallel, lazy, JDK
        {
            long evens = this.integersJDK.parallelStream().filter(each -> each % 2 == 0).count();
            Assert.assertEquals(SIZE / 2, evens);
            long odds = this.integersJDK.parallelStream().filter(each -> each % 2 == 1).count();
            Assert.assertEquals(SIZE / 2, odds);
            long evens2 = this.integersJDK.parallelStream().filter(jdkEven).count();
            Assert.assertEquals(SIZE / 2, evens2);
            long odds2 = this.integersJDK.parallelStream().filter(jdkOdd).count();
            Assert.assertEquals(SIZE / 2, odds2);
        }

        com.gs.collections.api.block.predicate.Predicate<Integer> gscEven = new com.gs.collections.api.block.predicate.Predicate<Integer>()
        {
            @Override
            public boolean accept(Integer each)
            {
                return each % 2 == 0;
            }
        };
        com.gs.collections.api.block.predicate.Predicate<Integer> gscOdd = new com.gs.collections.api.block.predicate.Predicate<Integer>()
        {
            @Override
            public boolean accept(Integer each)
            {
                return each % 2 == 1;
            }
        };

        // serial, lazy, GSC
        {
            long evens = this.integersGSC.asLazy().count(each -> each % 2 == 0);
            Assert.assertEquals(SIZE / 2, evens);
            long odds = this.integersGSC.asLazy().count(each -> each % 2 == 1);
            Assert.assertEquals(SIZE / 2, odds);
            long evens2 = this.integersGSC.asLazy().count(gscEven);
            Assert.assertEquals(SIZE / 2, evens2);
            long odds2 = this.integersGSC.asLazy().count(gscOdd);
            Assert.assertEquals(SIZE / 2, odds2);
        }

        // parallel, lazy, GSC
        {
            long evens = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(each -> each % 2 == 0);
            Assert.assertEquals(SIZE / 2, evens);
            long odds = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(each -> each % 2 == 1);
            Assert.assertEquals(SIZE / 2, odds);
            long evens2 = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(gscEven);
            Assert.assertEquals(SIZE / 2, evens2);
            long odds2 = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(gscOdd);
            Assert.assertEquals(SIZE / 2, odds2);
        }

        // serial, eager, GSC
        {
            long evens = this.integersGSC.count(each -> each % 2 == 0);
            Assert.assertEquals(SIZE / 2, evens);
            long odds = this.integersGSC.count(each -> each % 2 == 1);
            Assert.assertEquals(SIZE / 2, odds);
            long evens2 = this.integersGSC.count(gscEven);
            Assert.assertEquals(SIZE / 2, evens2);
            long odds2 = this.integersGSC.count(gscOdd);
            Assert.assertEquals(SIZE / 2, odds2);
        }

        // parallel, eager, GSC
        {
            long evens = ParallelIterate.count(this.integersGSC, each -> each % 2 == 0);
            Assert.assertEquals(SIZE / 2, evens);
            long odds = ParallelIterate.count(this.integersGSC, each -> each % 2 == 1);
            Assert.assertEquals(SIZE / 2, odds);
            long evens2 = ParallelIterate.count(this.integersGSC, gscEven);
            Assert.assertEquals(SIZE / 2, evens2);
            long odds2 = ParallelIterate.count(this.integersGSC, gscOdd);
            Assert.assertEquals(SIZE / 2, odds2);
        }
    }

    @After
    @TearDown
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
    }

    @GenerateMicroBenchmark
    public void serial_lazy_jdk()
    {
        long evens = this.integersJDK.stream().filter(each -> each % 2 == 0).count();
        Assert.assertEquals(SIZE / 2, evens);
    }

    @GenerateMicroBenchmark
    public void parallel_lazy_jdk()
    {
        long evens = this.integersJDK.parallelStream().filter(each -> each % 2 == 0).count();
        Assert.assertEquals(SIZE / 2, evens);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void serial_eager_gsc()
    {
        int evens = this.integersGSC.count(each -> each % 2 == 0);
        Assert.assertEquals(SIZE / 2, evens);
    }

    @GenerateMicroBenchmark
    public void serial_lazy_gsc()
    {
        int evens = this.integersGSC.asLazy().count(each -> each % 2 == 0);
        Assert.assertEquals(SIZE / 2, evens);
    }

    @GenerateMicroBenchmark
    public void parallel_eager_gsc()
    {
        int evens = ParallelIterate.count(this.integersGSC, each -> each % 2 == 0, BATCH_SIZE, this.executorService);
        Assert.assertEquals(SIZE / 2, evens);
    }

    @GenerateMicroBenchmark
    public void parallel_lazy_gsc()
    {
        int evens = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).count(each -> each % 2 == 0);
        Assert.assertEquals(SIZE / 2, evens);
    }

    @GenerateMicroBenchmark
    public void serial_eager_scala()
    {
        CountScalaTest.serial_eager_scala();
    }

    @GenerateMicroBenchmark
    public void serial_lazy_scala()
    {
        CountScalaTest.serial_lazy_scala();
    }

    @GenerateMicroBenchmark
    public void parallel_lazy_scala()
    {
        CountScalaTest.parallel_lazy_scala();
    }
}
