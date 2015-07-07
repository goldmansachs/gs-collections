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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class FlatCollectTest extends AbstractJMHTestRunner
{
    private static final int COUNT = 10_000;
    private static final int LIST_SIZE = 100;
    private final List<List<Integer>> integersJDK = new ArrayList<>(FastList.<List<Integer>>newWithNValues(COUNT, () -> new ArrayList<>(Interval.oneTo(LIST_SIZE))));
    private final MutableList<MutableList<Integer>> integersGSC = FastList.newWithNValues(COUNT, () -> Interval.oneTo(LIST_SIZE).toList());

    @Benchmark
    public void serial_lazy_jdk()
    {
        List<Integer> flatMap = this.integersJDK.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Benchmark
    public void serial_lazy_streams_gsc()
    {
        List<Integer> flatMap = this.integersGSC.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Benchmark
    public void serial_eager_gsc()
    {
        MutableList<Integer> flatCollect = this.integersGSC.flatCollect(e -> e);
    }

    @Benchmark
    public void serial_lazy_gsc()
    {
        MutableList<Integer> flatCollect = this.integersGSC.asLazy().flatCollect(e -> e).toList();
    }
}
