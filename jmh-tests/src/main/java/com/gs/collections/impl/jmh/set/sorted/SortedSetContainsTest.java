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

package com.gs.collections.impl.jmh.set.sorted;

import java.util.concurrent.TimeUnit;

import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.factory.SortedSets;
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
public class SortedSetContainsTest
{
    private static final int SIZE = 2_000_000;

    private final MutableSortedSet<Integer> gscMutable = SortedSets.mutable.withAll(Interval.zeroToBy(SIZE, 2));
    private final ImmutableSortedSet<Integer> gscImmutable = SortedSets.immutable.withAll(Interval.zeroToBy(SIZE, 2));

    @Benchmark
    public void contains_mutable_gsc()
    {
        int size = SIZE;
        MutableSortedSet<Integer> localGscMutable = this.gscMutable;

        for (int i = 0; i < size; i += 2)
        {
            if (!localGscMutable.contains(i))
            {
                throw new AssertionError(i);
            }
        }

        for (int i = 1; i < size; i += 2)
        {
            if (localGscMutable.contains(i))
            {
                throw new AssertionError(i);
            }
        }
    }

    @Benchmark
    public void contains_immutable_gsc()
    {
        int size = SIZE;
        ImmutableSortedSet<Integer> localGscImmutable = this.gscImmutable;

        for (int i = 0; i < size; i += 2)
        {
            if (!localGscImmutable.contains(i))
            {
                throw new AssertionError(i);
            }
        }

        for (int i = 1; i < size; i += 2)
        {
            if (localGscImmutable.contains(i))
            {
                throw new AssertionError(i);
            }
        }
    }

    @Benchmark
    public void contains_mutable_scala()
    {
        SortedSetContainsScalaTest.contains_mutable_scala();
    }

    @Benchmark
    public void contains_immutable_scala()
    {
        SortedSetContainsScalaTest.contains_immutable_scala();
    }
}
