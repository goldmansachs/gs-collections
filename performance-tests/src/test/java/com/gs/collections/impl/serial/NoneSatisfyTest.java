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

package com.gs.collections.impl.serial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.Interval;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class NoneSatisfyTest
{
    private static final int SIZE = 1000000;
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final MutableList<Integer> integersGSC = Interval.oneTo(SIZE).toList();

    @GenerateMicroBenchmark
    public void jdk8SerialNoneMatch()
    {
        boolean result1 = this.integersJDK.stream().noneMatch(each -> each % 2 == 1);
        boolean result2 = this.integersJDK.stream().noneMatch(each -> each % 2 == 0);
        boolean result3 = this.integersJDK.stream().noneMatch(each -> each > SIZE - 1);
        boolean result4 = this.integersJDK.stream().noneMatch(each -> each > SIZE / 2);
    }

    @GenerateMicroBenchmark
    public void gscEagerSerialNoneSatisfy()
    {
        boolean result1 = this.integersGSC.noneSatisfy(each -> each % 2 == 1);
        boolean result2 = this.integersGSC.noneSatisfy(each -> each % 2 == 0);
        boolean result3 = this.integersGSC.noneSatisfy(each -> each > SIZE - 1);
        boolean result4 = this.integersGSC.noneSatisfy(each -> each > SIZE / 2);
    }

    @GenerateMicroBenchmark
    public void gscLazySerialNoneSatisfy()
    {
        boolean result1 = this.integersGSC.asLazy().noneSatisfy(each -> each % 2 == 1);
        boolean result2 = this.integersGSC.asLazy().noneSatisfy(each -> each % 2 == 0);
        boolean result3 = this.integersGSC.asLazy().noneSatisfy(each -> each > SIZE - 1);
        boolean result4 = this.integersGSC.asLazy().noneSatisfy(each -> each > SIZE / 2);
    }
}
