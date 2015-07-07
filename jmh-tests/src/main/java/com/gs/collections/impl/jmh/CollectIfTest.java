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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.ParallelListIterable;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class CollectIfTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;
    private final ExecutorService service = ParallelIterate.newPooledExecutor(CollectTest.class.getSimpleName(), true);
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final FastList<Integer> integersGSC = FastList.newList(Interval.oneTo(SIZE));

    @Benchmark
    public void serial_lazy_jdk()
    {
        List<String> evenStrings = this.integersJDK.stream().filter(e -> e % 2 == 0).map(Object::toString).collect(Collectors.toList());
        List<String> oddStrings = this.integersJDK.stream().filter(e -> e % 2 == 1).map(Object::toString).collect(Collectors.toList());
    }

    @Benchmark
    public void serial_lazy_streams_gsc()
    {
        List<String> evenStrings = this.integersGSC.stream().filter(e -> e % 2 == 0).map(Object::toString).collect(Collectors.toList());
        List<String> oddStrings = this.integersGSC.stream().filter(e -> e % 2 == 1).map(Object::toString).collect(Collectors.toList());
    }

    @Benchmark
    public void parallel_lazy_jdk()
    {
        List<String> evenStrings = this.integersJDK.parallelStream().filter(e -> e % 2 == 0).map(Object::toString).collect(Collectors.toList());
        List<String> oddStrings = this.integersJDK.parallelStream().filter(e -> e % 2 == 1).map(Object::toString).collect(Collectors.toList());
    }

    @Benchmark
    public void parallel_lazy_streams_gsc()
    {
        List<String> evenStrings = this.integersGSC.parallelStream().filter(e -> e % 2 == 0).map(Object::toString).collect(Collectors.toList());
        List<String> oddStrings = this.integersGSC.parallelStream().filter(e -> e % 2 == 1).map(Object::toString).collect(Collectors.toList());
    }

    @Benchmark
    public void serial_eager_gsc()
    {
        MutableList<String> evenStrings = this.integersGSC.collectIf(e -> e % 2 == 0, Object::toString);
        MutableList<String> oddStrings = this.integersGSC.collectIf(e -> e % 2 == 1, Object::toString);
    }

    @Benchmark
    public void parallel_eager_gsc()
    {
        Collection<String> evenStrings = ParallelIterate.collectIf(this.integersGSC, e -> e % 2 == 0, Object::toString);
        Collection<String> oddStrings = ParallelIterate.collectIf(this.integersGSC, e -> e % 2 == 1, Object::toString);
    }

    @Benchmark
    public void serial_lazy_gsc()
    {
        MutableList<String> evenStrings = this.integersGSC.asLazy().select(e -> e % 2 == 0).collect(Object::toString).toList();
        MutableList<String> oddStrings = this.integersGSC.asLazy().select(e -> e % 2 == 1).collect(Object::toString).toList();
    }

    @Benchmark
    public void parallel_lazy_gsc()
    {
        ParallelListIterable<Integer> parallelListIterable = this.integersGSC.asParallel(this.service, BATCH_SIZE);
        MutableList<String> evenStrings = parallelListIterable.select(e -> e % 2 == 0).collect(Object::toString).toList();
        MutableList<String> oddStrings = parallelListIterable.select(e -> e % 2 == 1).collect(Object::toString).toList();
    }
}
