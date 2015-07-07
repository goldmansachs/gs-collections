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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class FunctionalInterfaceSetTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;

    @Param({"0", "1", "2", "3"})
    public int megamorphicWarmupLevel;

    private final List<Integer> integersJDK = new ArrayList<>(Interval.oneTo(SIZE));
    private final FastList<Integer> integersGSC = new FastList<>(Interval.oneTo(SIZE));

    private ExecutorService executorService;

    @Before
    @Setup
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Before
    @Setup(Level.Trial)
    public void setUp_megamorphic()
    {
        this.setUp();

        com.gs.collections.api.block.predicate.Predicate<Integer> predicate1 = each -> (each + 2) % 10_000 != 0;
        com.gs.collections.api.block.predicate.Predicate<Integer> predicate2 = each -> (each + 3) % 10_000 != 0;
        com.gs.collections.api.block.predicate.Predicate<Integer> predicate3 = each -> (each + 4) % 10_000 != 0;
        com.gs.collections.api.block.predicate.Predicate<Integer> predicate4 = each -> (each + 5) % 10_000 != 0;

        com.gs.collections.api.block.function.Function<Integer, String> function1 = each ->
        {
            Assert.assertNotNull(each);
            return String.valueOf(each);
        };

        com.gs.collections.api.block.function.Function<String, Integer> function2 = each -> {
            Assert.assertNotNull(each);
            return Integer.valueOf(each);
        };

        com.gs.collections.api.block.function.Function<Integer, String> function3 = each ->
        {
            Assert.assertSame(each, each);
            return String.valueOf(each);
        };

        com.gs.collections.api.block.function.Function<String, Integer> function4 = each -> {
            Assert.assertSame(each, each);
            return Integer.valueOf(each);
        };

        if (this.megamorphicWarmupLevel > 0)
        {
            Predicate<Integer> predicateJDK1 = each -> (each + 2) % 10_000 != 0;
            Predicate<Integer> predicateJDK2 = each -> (each + 3) % 10_000 != 0;
            Predicate<Integer> predicateJDK3 = each -> (each + 4) % 10_000 != 0;
            Predicate<Integer> predicateJDK4 = each -> (each + 5) % 10_000 != 0;

            Function<Integer, String> mapper1 = each ->
            {
                Assert.assertNotNull(each);
                return String.valueOf(each);
            };

            Function<String, Integer> mapper2 = each -> {
                Assert.assertNotNull(each);
                return Integer.valueOf(each);
            };

            Function<Integer, String> mapper3 = each ->
            {
                Assert.assertSame(each, each);
                return String.valueOf(each);
            };

            Function<String, Integer> mapper4 = each -> {
                Assert.assertSame(each, each);
                return Integer.valueOf(each);
            };

            // serial, lazy, JDK
            {
                Set<Integer> set = this.integersJDK.stream()
                        .filter(predicateJDK1)
                        .map(mapper1)
                        .map(mapper2)
                        .filter(predicateJDK2)
                        .collect(Collectors.toSet());
                Verify.assertSize(999_800, set);

                List<Integer> collection = this.integersJDK.stream()
                        .filter(predicateJDK3)
                        .map(mapper3)
                        .map(mapper4)
                        .filter(predicateJDK4)
                        .collect(Collectors.toCollection(ArrayList::new));
                Verify.assertSize(999_800, collection);
            }

            // parallel, lazy, JDK
            {
                Set<Integer> set = this.integersJDK.parallelStream()
                        .filter(predicateJDK1)
                        .map(mapper1)
                        .map(mapper2)
                        .filter(predicateJDK2)
                        .collect(Collectors.toSet());
                Verify.assertSize(999_800, set);

                List<Integer> collection = this.integersJDK.parallelStream()
                        .filter(predicateJDK3)
                        .map(mapper3)
                        .map(mapper4)
                        .filter(predicateJDK4)
                        .collect(Collectors.toCollection(ArrayList::new));
                Verify.assertSize(999_800, collection);
            }

            // serial, lazy, GSC
            {
                MutableSet<Integer> set = this.integersGSC.asLazy()
                        .select(predicate1)
                        .collect(function1)
                        .collect(function2)
                        .select(predicate2)
                        .toSet();
                Verify.assertSize(999_800, set);

                MutableBag<Integer> bag = this.integersGSC.asLazy()
                        .select(predicate3)
                        .collect(function3)
                        .collect(function4)
                        .select(predicate4)
                        .toBag();
                Verify.assertIterableSize(999_800, bag);
            }

            // parallel, lazy, GSC
            {
                MutableSet<Integer> set = this.integersGSC.asParallel(this.executorService, BATCH_SIZE)
                        .select(predicate1)
                        .collect(function1)
                        .collect(function2)
                        .select(predicate2)
                        .toSet();
                Verify.assertSize(999_800, set);

                MutableBag<Integer> bag = this.integersGSC.asParallel(this.executorService, BATCH_SIZE)
                        .select(predicate3)
                        .collect(function3)
                        .collect(function4)
                        .select(predicate4)
                        .toBag();
                Verify.assertIterableSize(999_800, bag);
            }

            // serial, eager, GSC
            MutableSet<Integer> set = this.integersGSC
                    .select(predicate1)
                    .collect(function1)
                    .collect(function2)
                    .select(predicate2)
                    .toSet();
            Verify.assertSize(999_800, set);

            MutableBag<Integer> bag = this.integersGSC
                    .select(predicate3)
                    .collect(function3)
                    .collect(function4)
                    .select(predicate4)
                    .toBag();
            Verify.assertIterableSize(999_800, bag);
        }

        if (this.megamorphicWarmupLevel > 1)
        {
            // parallel, eager, GSC
            Collection<Integer> select1 = ParallelIterate.select(this.integersGSC, predicate1, new UnifiedSet<>(), true);
            Collection<String> collect1 = ParallelIterate.collect(select1, function1, new UnifiedSet<>(), true);
            Collection<Integer> collect2 = ParallelIterate.collect(collect1, function2, new UnifiedSet<>(), true);
            UnifiedSet<Integer> set = ParallelIterate.select(collect2, predicate2, new UnifiedSet<>(), true);
            Verify.assertSize(999_800, set);

            Collection<Integer> select3 = ParallelIterate.select(this.integersGSC, predicate3, new HashBag<>(), true);
            Collection<String> collect3 = ParallelIterate.collect(select3, function3, new HashBag<>(), true);
            Collection<Integer> collect4 = ParallelIterate.collect(collect3, function4, new HashBag<>(), true);
            HashBag<Integer> bag = ParallelIterate.select(collect4, predicate4, new HashBag<>(), true);
            Verify.assertSize(999_800, bag);
        }

        if (this.megamorphicWarmupLevel > 2)
        {
            // parallel, eager, GSC, executorService
            UnifiedSet<Integer> select1 = ParallelIterate.select(this.integersGSC, predicate1, new UnifiedSet<>(), BATCH_SIZE, this.executorService, true);
            UnifiedSet<String> collect1 = ParallelIterate.collect(select1, function1, new UnifiedSet<>(), BATCH_SIZE, this.executorService, true);
            UnifiedSet<Integer> collect2 = ParallelIterate.collect(collect1, function2, new UnifiedSet<>(), BATCH_SIZE, this.executorService, true);
            UnifiedSet<Integer> set = ParallelIterate.select(collect2, predicate2, new UnifiedSet<>(), BATCH_SIZE, this.executorService, true);
            Verify.assertSize(999_800, set);

            HashBag<Integer> select3 = ParallelIterate.select(this.integersGSC, predicate3, new HashBag<>(), BATCH_SIZE, this.executorService, true);
            HashBag<String> collect3 = ParallelIterate.collect(select3, function3, new HashBag<>(), BATCH_SIZE, this.executorService, true);
            HashBag<Integer> collect4 = ParallelIterate.collect(collect3, function4, new HashBag<>(), BATCH_SIZE, this.executorService, true);
            HashBag<Integer> bag = ParallelIterate.select(collect4, predicate4, new HashBag<>(), BATCH_SIZE, this.executorService, true);
            Verify.assertSize(999_800, bag);
        }

        FunctionalInterfaceScalaTest.megamorphic(this.megamorphicWarmupLevel);
    }

    @After
    @TearDown
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public Set<Integer> serial_lazy_jdk()
    {
        Set<Integer> set = this.integersJDK.stream()
                .filter(each -> each % 10_000 != 0)
                .map(String::valueOf)
                .map(Integer::valueOf)
                .filter(each -> (each + 1) % 10_000 != 0)
                .collect(Collectors.toSet());
        Verify.assertSize(999_800, set);
        return set;
    }

    @Test
    public void test_serial_lazy_jdk()
    {
        Verify.assertSetsEqual(
                Interval.oneToBy(1_000_000, 10_000).flatCollect(each -> Interval.fromTo(each, each + 9_997)).toSet(),
                this.serial_lazy_jdk());
    }

    @Warmup(iterations = 50)
    @Measurement(iterations = 25)
    @Benchmark
    public Set<Integer> parallel_lazy_jdk()
    {
        Set<Integer> set = this.integersJDK.parallelStream()
                .filter(each -> each % 10_000 != 0)
                .map(String::valueOf)
                .map(Integer::valueOf)
                .filter(each -> (each + 1) % 10_000 != 0)
                .collect(Collectors.toSet());
        Verify.assertSize(999_800, set);
        return set;
    }

    @Warmup(iterations = 50)
    @Measurement(iterations = 25)
    @Test
    public void test_parallel_lazy_jdk()
    {
        Verify.assertSetsEqual(
                Interval.oneToBy(1_000_000, 10_000).flatCollect(each -> Interval.fromTo(each, each + 9_997)).toSet(),
                this.parallel_lazy_jdk());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public MutableSet<Integer> serial_eager_gsc()
    {
        FastList<Integer> select1 = this.integersGSC.select(each -> each % 10_000 != 0);
        FastList<String> collect1 = select1.collect(String::valueOf);
        FastList<Integer> collect2 = collect1.collect(Integer::valueOf);
        UnifiedSet<Integer> set = collect2.select(each -> (each + 1) % 10_000 != 0, UnifiedSet.newSet());
        Verify.assertSize(999_800, set);
        return set;
    }

    @Test
    public void test_serial_eager_gsc()
    {
        Verify.assertSetsEqual(
                Interval.oneToBy(1_000_000, 10_000).flatCollect(each -> Interval.fromTo(each, each + 9_997)).toSet(),
                this.serial_eager_gsc());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public MutableSet<Integer> serial_lazy_gsc()
    {
        MutableSet<Integer> set = this.integersGSC
                .asLazy()
                .select(each -> each % 10_000 != 0)
                .collect(String::valueOf)
                .collect(Integer::valueOf)
                .select(each -> (each + 1) % 10_000 != 0)
                .toSet();
        Verify.assertSize(999_800, set);
        return set;
    }

    @Test
    public void test_serial_lazy_gsc()
    {
        Verify.assertSetsEqual(
                Interval.oneToBy(1_000_000, 10_000).flatCollect(each -> Interval.fromTo(each, each + 9_997)).toSet(),
                this.serial_lazy_gsc());
    }

    @Warmup(iterations = 50)
    @Measurement(iterations = 25)
    @Benchmark
    public MutableSet<Integer> parallel_lazy_gsc()
    {
        MutableSet<Integer> set = this.integersGSC
                .asParallel(this.executorService, BATCH_SIZE)
                .select(each -> each % 10_000 != 0)
                .collect(String::valueOf)
                .collect(Integer::valueOf)
                .select(each -> (each + 1) % 10_000 != 0)
                .toSet();
        Verify.assertSize(999_800, set);
        return set;
    }

    @Test
    public void test_parallel_lazy_gsc()
    {
        Verify.assertSetsEqual(
                Interval.oneToBy(1_000_000, 10_000).flatCollect(each -> Interval.fromTo(each, each + 9_997)).toSet(),
                this.parallel_lazy_gsc());
    }
}
