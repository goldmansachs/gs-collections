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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class CollectTest
{
    private static final int SIZE = 1_000_000;
    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private final ExecutorService service = ParallelIterate.newPooledExecutor(CollectTest.class.getSimpleName(), true);
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final FastList<Integer> integersGSC = FastList.newList(Interval.oneTo(SIZE));

    @GenerateMicroBenchmark
    public void jdk8SerialMap()
    {
        List<String> strings = this.integersJDK.stream().map(Object::toString).collect(Collectors.toList());
    }

    @GenerateMicroBenchmark
    public void jdk8ParallelMap()
    {
        List<String> strings = this.integersJDK.parallelStream().map(Object::toString).collect(Collectors.toList());
    }

    @GenerateMicroBenchmark
    public void gscEagerSerialCollect()
    {
        MutableList<String> strings = this.integersGSC.collect(Object::toString);
    }

    @GenerateMicroBenchmark
    public void gscEagerParallelCollect()
    {
        Collection<String> strings = ParallelIterate.collect(this.integersGSC, Object::toString);
    }

    @GenerateMicroBenchmark
    public void gscLazySerialCollect()
    {
        MutableList<String> strings = this.integersGSC.asLazy().collect(Object::toString).toList();
    }

    @GenerateMicroBenchmark
    public void gscLazyParallelCollect()
    {
        MutableList<String> strings = this.integersGSC.asParallel(this.service, SIZE / CORES).collect(Object::toString).toList();
    }
}
