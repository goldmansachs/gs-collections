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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Predicates;
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
public class RejectTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final MutableList<Integer> integersGSC = Interval.oneTo(SIZE).toList();

    @Benchmark
    public void serial_lazy_jdk_lambda_not()
    {
        List<Integer> evens = this.integersJDK.stream().filter(each -> each % 2 != 1).collect(Collectors.toList());
        List<Integer> odds = this.integersJDK.stream().filter(each -> each % 2 != 0).collect(Collectors.toList());
    }

    @Benchmark
    public void serial_lazy_streams_gsc_lambda_not()
    {
        List<Integer> evens = this.integersGSC.stream().filter(each -> each % 2 != 1).collect(Collectors.toList());
        List<Integer> odds = this.integersGSC.stream().filter(each -> each % 2 != 0).collect(Collectors.toList());
    }

    @Benchmark
    public void serial_lazy_jdk_lambda_negate()
    {
        Predicate<Integer> predicate1 = each -> (each % 2 == 1);
        List<Integer> evens = this.integersJDK.stream().filter(predicate1.negate()).collect(Collectors.toList());
        Predicate<Integer> predicate2 = each -> (each % 2 == 0);
        List<Integer> odds = this.integersJDK.stream().filter(predicate2.negate()).collect(Collectors.toList());
    }

    @Benchmark
    public void serial_lazy_streams_gsc_lambda_negate()
    {
        Predicate<Integer> predicate1 = each -> (each % 2 == 1);
        List<Integer> evens = this.integersGSC.stream().filter(predicate1.negate()).collect(Collectors.toList());
        Predicate<Integer> predicate2 = each -> (each % 2 == 0);
        List<Integer> odds = this.integersGSC.stream().filter(predicate2.negate()).collect(Collectors.toList());
    }

    @Benchmark
    public void serial_eager_gsc_select_predicates_not()
    {
        MutableList<Integer> evens = this.integersGSC.select(Predicates.not(each -> each % 2 == 1));
        MutableList<Integer> odds = this.integersGSC.select(Predicates.not(each -> each % 2 == 0));
    }

    @Benchmark
    public void serial_eager_gsc()
    {
        MutableList<Integer> evens = this.integersGSC.reject(each -> each % 2 == 1);
        MutableList<Integer> odds = this.integersGSC.reject(each -> each % 2 == 0);
    }

    @Benchmark
    public void serial_lazy_gsc()
    {
        MutableList<Integer> evens = this.integersGSC.asLazy().reject(each -> each % 2 == 1).toList();
        MutableList<Integer> odds = this.integersGSC.asLazy().reject(each -> each % 2 == 0).toList();
    }
}
