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
public class SumOfDoubleTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 3_000_000;
    private static final int BATCH_SIZE = 10_000;
    private static final Stream<Double> DOUBLES = new Random().doubles(1.0d, 100.0d).boxed();

    private final List<Double> doublesJDK = DOUBLES.limit(SIZE).collect(Collectors.toList());
    private final MutableList<Double> doublesGSC = FastList.newListWith(this.doublesJDK.toArray(new Double[SIZE]));

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
    public double serial_lazy_collectDoubleSum_jdk()
    {
        return this.doublesJDK.stream().mapToDouble(each -> each).sum();
    }

    @Benchmark
    public double serial_lazy_collectDoubleSum_streams_gsc()
    {
        return this.doublesGSC.stream().mapToDouble(each -> each).sum();
    }

    @Benchmark
    public double parallel_lazy_collectDoubleSum_jdk()
    {
        return this.doublesJDK.parallelStream().mapToDouble(each -> each).sum();
    }

    @Benchmark
    public double parallel_lazy_collectDoubleSum_streams_gsc()
    {
        return this.doublesGSC.parallelStream().mapToDouble(each -> each).sum();
    }

    @Benchmark
    public double serial_eager_directSumOfDouble_gsc()
    {
        return this.doublesGSC.sumOfDouble(each -> each);
    }

    @Benchmark
    public double serial_eager_collectDoubleSum_gsc()
    {
        return this.doublesGSC.collectDouble(each -> each).sum();
    }

    @Benchmark
    public double serial_lazy_collectDoubleSum_gsc()
    {
        return this.doublesGSC.asLazy().collectDouble(each -> each).sum();
    }

    @Benchmark
    public double parallel_lazy_directSumOfDouble_gsc()
    {
        return this.doublesGSC.asParallel(this.executorService, BATCH_SIZE).sumOfDouble(Double::doubleValue);
    }

    @Benchmark
    public double serial_lazy_directSumOfDouble_gsc()
    {
        return this.doublesGSC.asLazy().sumOfDouble(each -> each);
    }
}
