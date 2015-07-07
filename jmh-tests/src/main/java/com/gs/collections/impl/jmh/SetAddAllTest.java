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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SetAddAllTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1000;
    private final Set<Integer> integersJDK = new HashSet<>(Interval.oneTo(SIZE));
    private final MutableSet<Integer> integersGSC = Interval.oneTo(SIZE).toSet();

    @Benchmark
    public void jdk()
    {
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < 1000; i++)
        {
            result.addAll(this.integersJDK);
        }
    }

    @Benchmark
    public void gsc()
    {
        MutableSet<Integer> result = UnifiedSet.newSet();
        for (int i = 0; i < 1000; i++)
        {
            result.addAll(this.integersGSC);
        }
    }
}
