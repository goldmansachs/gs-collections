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
import java.util.stream.Collectors;

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
public class CollectIfTest
{
    private static final int SIZE = 1000000;
    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final MutableList<Integer> integersGSC = Interval.oneTo(SIZE).toList();

    @GenerateMicroBenchmark
    public void jdk8SerialFilterMap()
    {
        List<String> evenStrings = this.integersJDK.stream().filter(e -> e % 2 == 0).map(Object::toString).collect(Collectors.toList());
        List<String> oddStrings = this.integersJDK.stream().filter(e -> e % 2 == 1).map(Object::toString).collect(Collectors.toList());
    }

    @GenerateMicroBenchmark
    public void gscEagerSerialCollectIf()
    {
        MutableList<String> evenStrings = this.integersGSC.collectIf(e -> e % 2 == 0, Object::toString);
        MutableList<String> oddStrings = this.integersGSC.collectIf(e -> e % 2 == 1, Object::toString);
    }

    @GenerateMicroBenchmark
    public void gscLazySerialCollectIf()
    {
        MutableList<String> evenStrings = this.integersGSC.asLazy().collectIf(e -> e % 2 == 0, Object::toString).toList();
        MutableList<String> oddStrings = this.integersGSC.asLazy().collectIf(e -> e % 2 == 1, Object::toString).toList();
    }

    @GenerateMicroBenchmark
    public void gscLazySerialSelectCollect()
    {
        MutableList<String> evenStrings = this.integersGSC.asLazy().select(e -> e % 2 == 0).collect(Object::toString).toList();
        MutableList<String> oddStrings = this.integersGSC.asLazy().select(e -> e % 2 == 1).collect(Object::toString).toList();
    }
}
