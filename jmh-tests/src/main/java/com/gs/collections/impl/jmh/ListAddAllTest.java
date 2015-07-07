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
public class ListAddAllTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1000;
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final MutableList<Integer> integersGSC = FastList.newList(Interval.oneTo(SIZE));

    @Benchmark
    public void jdk()
    {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < 1000; i++)
        {
            result.addAll(this.integersJDK);
        }
        if (result.size() != 1_000_000)
        {
            throw new AssertionError();
        }
    }

    @Benchmark
    public void gsc()
    {
        MutableList<Integer> result = FastList.newList();
        for (int i = 0; i < 1000; i++)
        {
            result.addAll(this.integersGSC);
        }
        if (result.size() != 1_000_000)
        {
            throw new AssertionError();
        }
    }
}
