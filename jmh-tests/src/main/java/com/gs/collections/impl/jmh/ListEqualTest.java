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
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ListEqualTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private final List<Integer> integersJDK1 = new ArrayList<>(Interval.oneTo(SIZE));
    private final List<Integer> integersJDK2 = new ArrayList<>(Interval.oneTo(SIZE));
    private final List<Integer> integersJDK3 = new ArrayList<>(Interval.oneTo(SIZE / 2));
    private final MutableList<Integer> integersGSC1 = Interval.oneTo(SIZE).toList();
    private final MutableList<Integer> integersGSC2 = Interval.oneTo(SIZE).toList();
    private final MutableList<Integer> integersGSC3 = Interval.oneTo(SIZE / 2).toList();

    @Benchmark
    public void jdk()
    {
        if (!this.integersJDK1.equals(this.integersJDK1))
        {
            throw new AssertionError();
        }
        if (!this.integersJDK1.equals(this.integersJDK2))
        {
            throw new AssertionError();
        }
        if (this.integersJDK1.equals(this.integersJDK3))
        {
            throw new AssertionError();
        }
        if (!this.integersJDK1.equals(this.integersGSC1))
        {
            throw new AssertionError();
        }
        if (this.integersJDK1.equals(this.integersGSC3))
        {
            throw new AssertionError();
        }
    }

    @Benchmark
    public void gsc()
    {
        if (!this.integersGSC1.equals(this.integersGSC1))
        {
            throw new AssertionError();
        }
        if (!this.integersGSC1.equals(this.integersGSC2))
        {
            throw new AssertionError();
        }
        if (this.integersGSC1.equals(this.integersGSC3))
        {
            throw new AssertionError();
        }
        if (!this.integersGSC1.equals(this.integersJDK1))
        {
            throw new AssertionError();
        }
        if (this.integersGSC1.equals(this.integersJDK3))
        {
            throw new AssertionError();
        }
    }
}
