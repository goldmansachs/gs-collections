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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class FlatCollectTest
{
    private static final int COUNT = 10_000;
    private static final int LIST_SIZE = 100;
    private final List<List<Integer>> integersJDK = new ArrayList<>(FastList.<List<Integer>>newWithNValues(COUNT, () -> new ArrayList<Integer>(Interval.oneTo(LIST_SIZE))));
    private final MutableList<MutableList<Integer>> integersGSC = FastList.newWithNValues(COUNT, () -> Interval.oneTo(LIST_SIZE).toList());

    @GenerateMicroBenchmark
    public void jdk8SerialFlatMap()
    {
        List<Integer> flatMap = this.integersJDK.stream().flatMap(e -> e.stream()).collect(Collectors.toList());
    }

    @GenerateMicroBenchmark
    public void gscEagerSerialFlatCollect()
    {
        MutableList<Integer> flatCollect = this.integersGSC.flatCollect(e -> e);
    }

    @GenerateMicroBenchmark
    public void gscLazySerialFlatCollect()
    {
        MutableList<Integer> flatCollect = this.integersGSC.asLazy().flatCollect(e -> e).toList();
    }
}
