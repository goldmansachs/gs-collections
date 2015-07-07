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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.collect.Multimaps;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class GroupByListTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final MutableList<Integer> integersGSC = Interval.oneTo(SIZE).toList();

    @Benchmark
    public void groupBy_2_keys_serial_lazy_jdk()
    {
        Verify.assertSize(2, this.integersJDK.stream().collect(Collectors.groupingBy(each -> each % 2 == 0)));
    }

    @Benchmark
    public void groupBy_2_keys_serial_lazy_streams_gsc()
    {
        Verify.assertSize(2, this.integersGSC.stream().collect(Collectors.groupingBy(each -> each % 2 == 0)));
    }

    @Benchmark
    public void groupBy_100_keys_serial_lazy_jdk()
    {
        Verify.assertSize(100, this.integersJDK.stream().collect(Collectors.groupingBy(each -> each % 100)));
    }

    @Benchmark
    public void groupBy_100_keys_serial_lazy_streams_gsc()
    {
        Verify.assertSize(100, this.integersGSC.stream().collect(Collectors.groupingBy(each -> each % 100)));
    }

    @Benchmark
    public void groupBy_10000_keys_serial_lazy_jdk()
    {
        Verify.assertSize(10_000, this.integersJDK.stream().collect(Collectors.groupingBy(each -> each % 10_000)));
    }

    @Benchmark
    public void groupBy_10000_keys_serial_lazy_streams_gsc()
    {
        Verify.assertSize(10_000, this.integersGSC.stream().collect(Collectors.groupingBy(each -> each % 10_000)));
    }

    @Benchmark
    public void groupBy_2_keys_serial_eager_guava()
    {
        Verify.assertSize(2, Multimaps.index(this.integersJDK, each -> each % 2 == 0).asMap());
    }

    @Benchmark
    public void groupBy_100_keys_serial_eager_guava()
    {
        Verify.assertSize(100, Multimaps.index(this.integersJDK, each -> each % 100).asMap());
    }

    @Benchmark
    public void groupBy_10000_keys_serial_eager_guava()
    {
        Verify.assertSize(10_000, Multimaps.index(this.integersJDK, each -> each % 10000).asMap());
    }

    @Benchmark
    public void groupBy_2_keys_serial_eager_gsc()
    {
        Assert.assertEquals(2, this.integersGSC.groupBy(each -> each % 2 == 0).sizeDistinct());
    }

    @Benchmark
    public void groupBy_100_keys_serial_eager_gsc()
    {
        Assert.assertEquals(100, this.integersGSC.groupBy(each -> each % 100).sizeDistinct());
    }

    @Benchmark
    public void groupBy_10000_keys_serial_eager_gsc()
    {
        Assert.assertEquals(10_000, this.integersGSC.groupBy(each -> each % 10_000).sizeDistinct());
    }

    @Benchmark
    public void groupBy_2_keys_serial_lazy_gsc()
    {
        Assert.assertEquals(2, this.integersGSC.asLazy().groupBy(each -> each % 2 == 0).sizeDistinct());
    }

    @Benchmark
    public void groupBy_100_keys_serial_lazy_gsc()
    {
        Assert.assertEquals(100, this.integersGSC.asLazy().groupBy(each -> each % 100).sizeDistinct());
    }

    @Benchmark
    public void groupBy_10000_keys_serial_lazy_gsc()
    {
        Assert.assertEquals(10_000, this.integersGSC.asLazy().groupBy(each -> each % 10_000).sizeDistinct());
    }

    @Benchmark
    public void groupBy_2_keys_serial_eager_scala()
    {
        GroupByScalaTest.groupBy_2_keys_serial_eager_scala();
    }

    @Benchmark
    public void groupBy_100_keys_serial_eager_scala()
    {
        GroupByScalaTest.groupBy_100_keys_serial_eager_scala();
    }

    @Benchmark
    public void groupBy_10000_keys_serial_eager_scala()
    {
        GroupByScalaTest.groupBy_10000_keys_serial_eager_scala();
    }

    @Benchmark
    public void groupBy_2_keys_serial_lazy_scala()
    {
        GroupByScalaTest.groupBy_2_keys_serial_lazy_scala();
    }

    @Benchmark
    public void groupBy_100_keys_serial_lazy_scala()
    {
        GroupByScalaTest.groupBy_100_keys_serial_lazy_scala();
    }

    @Benchmark
    public void groupBy_10000_keys_serial_lazy_scala()
    {
        GroupByScalaTest.groupBy_10000_keys_serial_lazy_scala();
    }
}
