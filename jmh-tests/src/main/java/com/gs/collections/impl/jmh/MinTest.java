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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
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
public class MinTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 3_000_000;
    private static final int BATCH_SIZE = 10_000;

    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final MutableList<Integer> integersGSC = Interval.oneTo(SIZE).toList();

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
    public int serial_lazy_jdk()
    {
        return this.integersJDK.stream().min(Comparator.<Integer>naturalOrder()).get();
    }

    @Benchmark
    public int serial_lazy_streams_gsc()
    {
        return this.integersGSC.stream().min(Comparator.<Integer>naturalOrder()).get();
    }

    @Benchmark
    public int serial_lazy_reverse_jdk()
    {
        return this.integersJDK.stream().min(Comparator.<Integer>reverseOrder()).get();
    }

    @Benchmark
    public int serial_lazy_reverse_streams_gsc()
    {
        return this.integersGSC.stream().min(Comparator.<Integer>reverseOrder()).get();
    }

    @Benchmark
    public int serial_lazy_intstream_jdk()
    {
        return this.integersJDK.stream().mapToInt(Integer::intValue).min().getAsInt();
    }

    @Benchmark
    public int serial_lazy_intstream_streams_gsc()
    {
        return this.integersGSC.stream().mapToInt(Integer::intValue).min().getAsInt();
    }

    @Benchmark
    public int parallel_lazy_jdk()
    {
        return this.integersJDK.parallelStream().min(Comparator.<Integer>naturalOrder()).get();
    }

    @Benchmark
    public int parallel_lazy_streams_gsc()
    {
        return this.integersGSC.parallelStream().min(Comparator.<Integer>naturalOrder()).get();
    }

    @Benchmark
    public int parallel_lazy_reverse_jdk()
    {
        return this.integersJDK.parallelStream().min(Comparator.<Integer>reverseOrder()).get();
    }

    @Benchmark
    public int parallel_lazy_reverse_streams_gsc()
    {
        return this.integersGSC.parallelStream().min(Comparator.<Integer>reverseOrder()).get();
    }

    @Benchmark
    public int parallel_lazy_intstream_jdk()
    {
        return this.integersJDK.parallelStream().mapToInt(Integer::intValue).min().getAsInt();
    }

    @Benchmark
    public int parallel_lazy_intstream_streams_gsc()
    {
        return this.integersGSC.parallelStream().mapToInt(Integer::intValue).min().getAsInt();
    }

    @Benchmark
    public int serial_eager_gsc()
    {
        return this.integersGSC.min(Comparator.<Integer>naturalOrder());
    }

    @Benchmark
    public int serial_eager_reverse_gsc()
    {
        return this.integersGSC.min(Comparator.<Integer>reverseOrder());
    }

    @Benchmark
    public int serial_lazy_gsc()
    {
        return this.integersGSC.asLazy().min(Comparator.<Integer>naturalOrder());
    }

    @Benchmark
    public int serial_lazy_reverse_gsc()
    {
        return this.integersGSC.asLazy().min(Comparator.<Integer>reverseOrder());
    }

    @Benchmark
    public int parallel_lazy_gsc()
    {
        return this.integersGSC.asParallel(this.executorService, BATCH_SIZE).min(Comparator.<Integer>naturalOrder());
    }

    @Benchmark
    public int parallel_lazy_reverse_gsc()
    {
        return this.integersGSC.asParallel(this.executorService, BATCH_SIZE).min(Comparator.<Integer>reverseOrder());
    }
}
