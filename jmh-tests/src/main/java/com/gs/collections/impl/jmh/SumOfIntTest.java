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

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.mutable.FastList;
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
public class SumOfIntTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 3_000_000;
    private static final int BATCH_SIZE = 10_000;
    private static final Stream<Integer> INTEGERS = new Random().ints(0, 10_000).boxed();

    private final List<Integer> integersJDK = INTEGERS.limit(SIZE).collect(Collectors.toList());
    private final MutableList<Integer> integersGSC = FastList.newListWith(this.integersJDK.toArray(new Integer[SIZE]));

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
    public int serial_lazy_collectIntSum_jdk()
    {
        return this.integersJDK.stream().mapToInt(each -> each).sum();
    }

    @Benchmark
    public int serial_lazy_collectIntSum_streams_gsc()
    {
        return this.integersGSC.stream().mapToInt(each -> each).sum();
    }

    @Benchmark
    public long serial_lazy_collectLongSum_jdk()
    {
        return this.integersJDK.stream().mapToLong(each -> each).sum();
    }

    @Benchmark
    public long serial_lazy_collectLongSum_streams_gsc()
    {
        return this.integersGSC.stream().mapToLong(each -> each).sum();
    }

    @Benchmark
    public int parallel_lazy_collectIntSum_jdk()
    {
        return this.integersJDK.parallelStream().mapToInt(Integer::intValue).sum();
    }

    @Benchmark
    public int parallel_lazy_collectIntSum_streams_gsc()
    {
        return this.integersGSC.parallelStream().mapToInt(Integer::intValue).sum();
    }

    @Benchmark
    public long parallel_lazy_collectLongSum_jdk()
    {
        return this.integersJDK.parallelStream().mapToLong(Integer::longValue).sum();
    }

    @Benchmark
    public long parallel_lazy_collectLongSum_streams_gsc()
    {
        return this.integersGSC.parallelStream().mapToLong(Integer::longValue).sum();
    }

    @Benchmark
    public long serial_eager_directSumOfInt_gsc()
    {
        return this.integersGSC.sumOfInt(each -> each);
    }

    @Benchmark
    public long serial_eager_collectIntSum_gsc()
    {
        return this.integersGSC.collectInt(each -> each).sum();
    }

    @Benchmark
    public long serial_lazy_collectIntSum_gsc()
    {
        return this.integersGSC.asLazy().collectInt(each -> each).sum();
    }

    @Benchmark
    public long parallel_lazy_directSumOfInt_gsc()
    {
        return this.integersGSC.asParallel(this.executorService, BATCH_SIZE).sumOfInt(each -> each);
    }

    @Benchmark
    public long serial_lazy_directSumOfInt_gsc()
    {
        return this.integersGSC.asLazy().sumOfInt(each -> each);
    }
}
