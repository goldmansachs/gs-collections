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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.collect.Multimaps;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
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
public class GroupBySetTest
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;
    private final Set<Integer> integersJDK = new HashSet<>(Interval.oneTo(SIZE));
    private final UnifiedSet<Integer> integersGSC = new UnifiedSet<>(Interval.oneTo(SIZE));

    private ExecutorService executorService;

    @Setup(Level.Iteration)
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @TearDown(Level.Iteration)
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_serial_lazy_jdk()
    {
        Verify.assertSize(2, this.integersJDK.stream().collect(Collectors.groupingBy(each -> each % 2 == 0)));
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_serial_lazy_jdk()
    {
        Verify.assertSize(100, this.integersJDK.stream().collect(Collectors.groupingBy(each -> each % 100)));
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_serial_lazy_jdk()
    {
        Verify.assertSize(10_000, this.integersJDK.stream().collect(Collectors.groupingBy(each -> each % 10_000)));
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_parallel_lazy_jdk()
    {
        Verify.assertSize(2, this.integersJDK.parallelStream().collect(Collectors.groupingBy(each -> each % 2 == 0)));
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_parallel_lazy_jdk()
    {
        Verify.assertSize(100, this.integersJDK.parallelStream().collect(Collectors.groupingBy(each -> each % 100)));
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_parallel_lazy_jdk()
    {
        Verify.assertSize(10_000, this.integersJDK.parallelStream().collect(Collectors.groupingBy(each -> each % 10_000)));
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_serial_eager_guava()
    {
        Verify.assertSize(2, Multimaps.index(this.integersJDK, each -> each % 2 == 0).asMap());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_serial_eager_guava()
    {
        Verify.assertSize(100, Multimaps.index(this.integersJDK, each -> each % 100).asMap());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_serial_eager_guava()
    {
        Verify.assertSize(10_000, Multimaps.index(this.integersJDK, each -> each % 10000).asMap());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_serial_eager_gsc()
    {
        Assert.assertEquals(2, this.integersGSC.groupBy(each -> each % 2 == 0).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_serial_eager_gsc()
    {
        Assert.assertEquals(100, this.integersGSC.groupBy(each -> each % 100).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_serial_eager_gsc()
    {
        Assert.assertEquals(10_000, this.integersGSC.groupBy(each -> each % 10_000).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_serial_lazy_gsc()
    {
        Assert.assertEquals(2, this.integersGSC.asLazy().groupBy(each -> each % 2 == 0).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_serial_lazy_gsc()
    {
        Assert.assertEquals(100, this.integersGSC.asLazy().groupBy(each -> each % 100).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_serial_lazy_gsc()
    {
        Assert.assertEquals(10_000, this.integersGSC.asLazy().groupBy(each -> each % 10_000).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_parallel_lazy_gsc()
    {
        Assert.assertEquals(2, this.integersGSC.asParallel(this.executorService, BATCH_SIZE).groupBy(each -> each % 2 == 0).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_parallel_lazy_gsc()
    {
        Assert.assertEquals(100, this.integersGSC.asParallel(this.executorService, BATCH_SIZE).groupBy(each -> each % 100).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_parallel_lazy_gsc()
    {
        Assert.assertEquals(10_000, this.integersGSC.asParallel(this.executorService, BATCH_SIZE).groupBy(each -> each % 10_000).sizeDistinct());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.groupBy_2_keys_serial_eager_scala();
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.groupBy_100_keys_serial_eager_scala();
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.groupBy_10000_keys_serial_eager_scala();
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_2_keys_serial_lazy_scala();
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_100_keys_serial_lazy_scala();
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_10000_keys_serial_lazy_scala();
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_2_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_2_keys_parallel_lazy_scala();
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_100_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_100_keys_parallel_lazy_scala();
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void groupBy_10000_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_10000_keys_parallel_lazy_scala();
    }
}
