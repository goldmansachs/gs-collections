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

import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
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
public class AllSatisfyTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final FastList<Integer> integersGSC = FastList.newList(Interval.oneTo(SIZE));

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

    @Benchmark
    public void short_circuit_middle_serial_lazy_jdk()
    {
        Assert.assertFalse(this.integersJDK.stream().allMatch(each -> each < SIZE / 2));
    }

    @Benchmark
    public void short_circuit_middle_serial_lazy_streams_gsc()
    {
        Assert.assertFalse(this.integersGSC.stream().allMatch(each -> each < SIZE / 2));
    }

    @Benchmark
    public void process_all_serial_lazy_jdk()
    {
        Assert.assertTrue(this.integersJDK.stream().allMatch(each -> each > 0));
    }

    @Benchmark
    public void process_all_serial_lazy_streams_gsc()
    {
        Assert.assertTrue(this.integersGSC.stream().allMatch(each -> each > 0));
    }

    @Benchmark
    public void short_circuit_middle_serial_eager_gsc()
    {
        Assert.assertFalse(this.integersGSC.allSatisfy(each -> each < SIZE / 2));
    }

    @Benchmark
    public void process_all_serial_eager_gsc()
    {
        Assert.assertTrue(this.integersGSC.allSatisfy(each -> each > 0));
    }

    @Benchmark
    public void short_circuit_middle_serial_lazy_gsc()
    {
        Assert.assertFalse(this.integersGSC.asLazy().allSatisfy(each -> each < SIZE / 2));
    }

    @Benchmark
    public void process_all_serial_lazy_gsc()
    {
        Assert.assertTrue(this.integersGSC.asLazy().allSatisfy(each -> each > 0));
    }

    @Benchmark
    public void short_circuit_middle_parallel_lazy_jdk()
    {
        Assert.assertFalse(this.integersJDK.parallelStream().allMatch(each -> each != SIZE / 2 - 1));
    }

    @Benchmark
    public void short_circuit_middle_parallel_lazy_streams_gsc()
    {
        Assert.assertFalse(this.integersGSC.parallelStream().allMatch(each -> each != SIZE / 2 - 1));
    }

    @Benchmark
    public void process_all_parallel_lazy_jdk()
    {
        Assert.assertTrue(this.integersJDK.parallelStream().allMatch(each -> each > 0));
    }

    @Benchmark
    public void process_all_parallel_lazy_streams_gsc()
    {
        Assert.assertTrue(this.integersGSC.parallelStream().allMatch(each -> each > 0));
    }

    @Benchmark
    public void short_circuit_middle_parallel_lazy_gsc()
    {
        Assert.assertFalse(this.integersGSC.asParallel(this.executorService, BATCH_SIZE).allSatisfy(each -> each != SIZE / 2 - 1));
    }

    @Benchmark
    public void process_all_parallel_lazy_gsc()
    {
        Assert.assertTrue(this.integersGSC.asParallel(this.executorService, BATCH_SIZE).allSatisfy(each -> each > 0));
    }
}
