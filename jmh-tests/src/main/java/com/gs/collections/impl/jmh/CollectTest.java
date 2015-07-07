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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.CompositeFastList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import com.gs.collections.impl.test.Verify;
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
public class CollectTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;
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

    @Benchmark
    public void serial_lazy_jdk()
    {
        List<String> strings = this.integersJDK.stream().map(Object::toString).collect(Collectors.toList());
        Verify.assertSize(SIZE, strings);
    }

    @Benchmark
    public void serial_lazy_streams_gsc()
    {
        List<String> strings = this.integersGSC.stream().map(Object::toString).collect(Collectors.toList());
        Verify.assertSize(SIZE, strings);
    }

    @Benchmark
    public void parallel_lazy_jdk()
    {
        List<String> strings = this.integersJDK.parallelStream().map(Object::toString).collect(Collectors.toList());
        Verify.assertSize(SIZE, strings);
    }

    @Benchmark
    public void parallel_lazy_streams_gsc()
    {
        List<String> strings = this.integersGSC.parallelStream().map(Object::toString).collect(Collectors.toList());
        Verify.assertSize(SIZE, strings);
    }

    @Benchmark
    public void serial_eager_scala()
    {
        CollectScalaTest.serial_eager_scala();
    }

    @Benchmark
    public void serial_lazy_scala()
    {
        CollectScalaTest.serial_lazy_scala();
    }

    @Benchmark
    public void parallel_lazy_scala()
    {
        CollectScalaTest.parallel_lazy_scala();
    }

    @Benchmark
    public void serial_eager_gsc()
    {
        MutableList<String> strings = this.integersGSC.collect(Object::toString);
        Verify.assertSize(SIZE, strings);
    }

    @Benchmark
    public void parallel_eager_gsc()
    {
        Collection<String> strings = ParallelIterate.collect(this.integersGSC, Object::toString);
        Verify.assertSize(SIZE, strings);
    }

    @Benchmark
    public void parallel_eager_fixed_pool_gsc()
    {
        Collection<String> strings = ParallelIterate.collect(
                this.integersGSC,
                Object::toString,
                new CompositeFastList<>(),
                BATCH_SIZE,
                this.executorService,
                false);
        Verify.assertSize(SIZE, strings);
    }

    @Benchmark
    public void serial_lazy_gsc()
    {
        MutableList<String> strings = this.integersGSC.asLazy().collect(Object::toString).toList();
        Verify.assertSize(SIZE, strings);
    }

    @Benchmark
    public void parallel_lazy_gsc()
    {
        MutableList<String> strings = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).collect(Object::toString).toList();
        Verify.assertSize(SIZE, strings);
    }
}
