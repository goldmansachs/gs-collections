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
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class PartitionTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final MutableList<Integer> integersGSC = Interval.oneTo(SIZE).toList();

    @Benchmark
    public Map<Boolean, List<Integer>> serial_lazy_jdk()
    {
        return this.integersJDK.stream().collect(Collectors.partitioningBy(each -> each % 2 == 0));
    }

    @Benchmark
    public Map<Boolean, List<Integer>> serial_lazy_streams_gsc()
    {
        return this.integersGSC.stream().collect(Collectors.partitioningBy(each -> each % 2 == 0));
    }

    @Benchmark
    public PartitionMutableList<Integer> serial_eager_gsc()
    {
        return this.integersGSC.partition(each -> each % 2 == 0);
    }

    @Benchmark
    public PartitionIterable<Integer> serial_lazy_gsc()
    {
        return this.integersGSC.asLazy().partition(each -> each % 2 == 0);
    }
}
