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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.set.UnsortedSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class GroupBySetTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;
    private final Set<Integer> integersJDK = new HashSet<>(Interval.zeroTo(SIZE - 1));
    private final UnifiedSet<Integer> integersGSC = new UnifiedSet<>(Interval.zeroTo(SIZE - 1));

    private ExecutorService executorService;

    @Before
    @Setup
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @After
    @TearDown
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
    }

    @Benchmark
    public Map<Boolean, Set<Integer>> groupBy_2_keys_serial_lazy_jdk()
    {
        Map<Boolean, Set<Integer>> multimap = this.integersJDK.stream()
                .collect(Collectors.groupingBy(each -> each % 2 == 0, Collectors.toSet()));
        Verify.assertSize(2, multimap);
        return multimap;
    }

    @Benchmark
    public Map<Boolean, Set<Integer>> groupBy_2_keys_serial_lazy_streams_gsc()
    {
        Map<Boolean, Set<Integer>> multimap = this.integersGSC.stream()
                .collect(Collectors.groupingBy(each -> each % 2 == 0, Collectors.toSet()));
        Verify.assertSize(2, multimap);
        return multimap;
    }

    @Test
    public void test_groupBy_2_keys_serial_lazy_jdk()
    {
        Map<Boolean, Set<Integer>> multimap = this.groupBy_2_keys_serial_lazy_jdk();
        Set<Integer> odds = multimap.get(false);
        Set<Integer> evens = multimap.get(true);
        Verify.assertSetsEqual(Interval.fromToBy(0, 999_999, 2).toSet(), evens);
        Verify.assertSetsEqual(Interval.fromToBy(1, 999_999, 2).toSet(), odds);
    }

    @Test
    public void test_groupBy_2_keys_serial_lazy_streams_gsc()
    {
        Map<Boolean, Set<Integer>> multimap = this.groupBy_2_keys_serial_lazy_streams_gsc();
        Set<Integer> odds = multimap.get(false);
        Set<Integer> evens = multimap.get(true);
        Verify.assertSetsEqual(Interval.fromToBy(0, 999_999, 2).toSet(), evens);
        Verify.assertSetsEqual(Interval.fromToBy(1, 999_999, 2).toSet(), odds);
    }

    @Benchmark
    public Map<Integer, Set<Integer>> groupBy_100_keys_serial_lazy_jdk()
    {
        Map<Integer, Set<Integer>> multimap = this.integersJDK.stream().collect(Collectors.groupingBy(each -> each % 100, Collectors.toSet()));
        Verify.assertSize(100, multimap);
        return multimap;
    }

    @Benchmark
    public Map<Integer, Set<Integer>> groupBy_100_keys_serial_lazy_streams_gsc()
    {
        Map<Integer, Set<Integer>> multimap = this.integersGSC.stream().collect(Collectors.groupingBy(each -> each % 100, Collectors.toSet()));
        Verify.assertSize(100, multimap);
        return multimap;
    }

    @Test
    public void test_groupBy_100_keys_serial_lazy_jdk()
    {
        Map<Integer, Set<Integer>> multimap = this.groupBy_100_keys_serial_lazy_jdk();
        for (int i = 0; i < 100; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(10_000, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 100).toSet(), integers);
        }
    }

    @Test
    public void test_groupBy_100_keys_serial_lazy_streams_gsc()
    {
        Map<Integer, Set<Integer>> multimap = this.groupBy_100_keys_serial_lazy_streams_gsc();
        for (int i = 0; i < 100; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(10_000, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 100).toSet(), integers);
        }
    }

    @Benchmark
    public Map<Integer, Set<Integer>> groupBy_10000_keys_serial_lazy_jdk()
    {
        Map<Integer, Set<Integer>> multimap = this.integersJDK.stream().collect(Collectors.groupingBy(each -> each % 10_000, Collectors.toSet()));
        Verify.assertSize(10_000, multimap);
        return multimap;
    }

    @Benchmark
    public Map<Integer, Set<Integer>> groupBy_10000_keys_serial_lazy_streams_gsc()
    {
        Map<Integer, Set<Integer>> multimap = this.integersGSC.stream().collect(Collectors.groupingBy(each -> each % 10_000, Collectors.toSet()));
        Verify.assertSize(10_000, multimap);
        return multimap;
    }

    @Test
    public void test_groupBy_10000_keys_serial_lazy_jdk()
    {
        Map<Integer, Set<Integer>> multimap = this.groupBy_10000_keys_serial_lazy_jdk();
        for (int i = 0; i < 10_000; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(100, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 10_000).toSet(), integers);
        }
    }

    @Test
    public void test_groupBy_10000_keys_serial_lazy_streams_gsc()
    {
        Map<Integer, Set<Integer>> multimap = this.groupBy_10000_keys_serial_lazy_streams_gsc();
        for (int i = 0; i < 10_000; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(100, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 10_000).toSet(), integers);
        }
    }

    @Benchmark
    public Map<Boolean, Set<Integer>> groupBy_2_keys_parallel_lazy_jdk()
    {
        Map<Boolean, Set<Integer>> multimap = this.integersJDK.parallelStream().collect(Collectors.groupingBy(each -> each % 2 == 0, Collectors.toSet()));
        Verify.assertSize(2, multimap);
        return multimap;
    }

    @Benchmark
    public Map<Boolean, Set<Integer>> groupBy_2_keys_parallel_lazy_streams_gsc()
    {
        Map<Boolean, Set<Integer>> multimap = this.integersGSC.parallelStream().collect(Collectors.groupingBy(each -> each % 2 == 0, Collectors.toSet()));
        Verify.assertSize(2, multimap);
        return multimap;
    }

    @Test
    public void test_groupBy_2_keys_parallel_lazy_jdk()
    {
        Map<Boolean, Set<Integer>> multimap = this.groupBy_2_keys_parallel_lazy_jdk();
        Set<Integer> odds = multimap.get(false);
        Set<Integer> evens = multimap.get(true);
        Verify.assertSetsEqual(Interval.fromToBy(0, 999_999, 2).toSet(), evens);
        Verify.assertSetsEqual(Interval.fromToBy(1, 999_999, 2).toSet(), odds);
    }

    @Test
    public void test_groupBy_2_keys_parallel_lazy_streams_gsc()
    {
        Map<Boolean, Set<Integer>> multimap = this.groupBy_2_keys_parallel_lazy_streams_gsc();
        Set<Integer> odds = multimap.get(false);
        Set<Integer> evens = multimap.get(true);
        Verify.assertSetsEqual(Interval.fromToBy(0, 999_999, 2).toSet(), evens);
        Verify.assertSetsEqual(Interval.fromToBy(1, 999_999, 2).toSet(), odds);
    }

    @Benchmark
    public Map<Integer, Set<Integer>> groupBy_100_keys_parallel_lazy_jdk()
    {
        Map<Integer, Set<Integer>> multimap = this.integersJDK.parallelStream().collect(Collectors.groupingBy(each -> each % 100, Collectors.toSet()));
        Verify.assertSize(100, multimap);
        return multimap;
    }

    @Benchmark
    public Map<Integer, Set<Integer>> groupBy_100_keys_parallel_lazy_streams_gsc()
    {
        Map<Integer, Set<Integer>> multimap = this.integersGSC.parallelStream().collect(Collectors.groupingBy(each -> each % 100, Collectors.toSet()));
        Verify.assertSize(100, multimap);
        return multimap;
    }

    @Test
    public void test_groupBy_100_keys_parallel_lazy_jdk()
    {
        Map<Integer, Set<Integer>> multimap = this.groupBy_100_keys_parallel_lazy_jdk();
        for (int i = 0; i < 100; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(10_000, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 100).toSet(), integers);
        }
    }

    @Test
    public void test_groupBy_100_keys_parallel_lazy_streams_gsc()
    {
        Map<Integer, Set<Integer>> multimap = this.groupBy_100_keys_parallel_lazy_streams_gsc();
        for (int i = 0; i < 100; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(10_000, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 100).toSet(), integers);
        }
    }

    @Benchmark
    public Map<Integer, Set<Integer>> groupBy_10000_keys_parallel_lazy_jdk()
    {
        Map<Integer, Set<Integer>> multimap = this.integersJDK.parallelStream().collect(Collectors.groupingBy(each -> each % 10_000, Collectors.toSet()));
        Verify.assertSize(10_000, multimap);
        return multimap;
    }

    @Benchmark
    public Map<Integer, Set<Integer>> groupBy_10000_keys_parallel_lazy_streams_gsc()
    {
        Map<Integer, Set<Integer>> multimap = this.integersGSC.parallelStream().collect(Collectors.groupingBy(each -> each % 10_000, Collectors.toSet()));
        Verify.assertSize(10_000, multimap);
        return multimap;
    }

    @Test
    public void test_groupBy_10000_keys_parallel_lazy_jdk()
    {
        Map<Integer, Set<Integer>> multimap = this.groupBy_10000_keys_parallel_lazy_jdk();
        for (int i = 0; i < 10_000; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(100, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 10_000).toSet(), integers);
        }
    }

    @Test
    public void test_groupBy_10000_keys_parallel_lazy_streams_gsc()
    {
        Map<Integer, Set<Integer>> multimap = this.groupBy_10000_keys_parallel_lazy_streams_gsc();
        for (int i = 0; i < 10_000; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(100, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 10_000).toSet(), integers);
        }
    }

    @Benchmark
    public ImmutableListMultimap<Boolean, Integer> groupBy_unordered_lists_2_keys_serial_eager_guava()
    {
        ImmutableListMultimap<Boolean, Integer> multimap = Multimaps.index(this.integersJDK, each -> each % 2 == 0);
        Verify.assertSize(2, multimap.asMap());
        return multimap;
    }

    @Ignore("Why is Guava reordering values?")
    @Test
    public void test_groupBy_unordered_lists_2_keys_serial_eager_guava()
    {
        ImmutableListMultimap<Boolean, Integer> multimap = this.groupBy_unordered_lists_2_keys_serial_eager_guava();
        ImmutableList<Integer> odds = multimap.get(false);
        ImmutableList<Integer> evens = multimap.get(true);
        Verify.assertListsEqual(Interval.fromToBy(0, 999_999, 2), evens);
        Verify.assertListsEqual(Interval.fromToBy(1, 999_999, 2), odds);
    }

    @Benchmark
    public ImmutableListMultimap<Integer, Integer> groupBy_unordered_lists_100_keys_serial_eager_guava()
    {
        ImmutableListMultimap<Integer, Integer> multimap = Multimaps.index(this.integersJDK, each -> each % 100);
        Verify.assertSize(100, multimap.asMap());
        return multimap;
    }

    @Test
    public void test_groupBy_unordered_lists_100_keys_serial_eager_guava()
    {
        ImmutableListMultimap<Integer, Integer> multimap = this.groupBy_unordered_lists_100_keys_serial_eager_guava();
        for (int i = 0; i < 100; i++)
        {
            ImmutableList<Integer> integers = multimap.get(i);
            Verify.assertSize(10_000, integers);
            Assert.assertEquals(Interval.fromToBy(i, 999_999, 100), integers);
        }
    }

    @Benchmark
    public ImmutableListMultimap<Integer, Integer> groupBy_unordered_lists_10000_keys_serial_eager_guava()
    {
        ImmutableListMultimap<Integer, Integer> multimap = Multimaps.index(this.integersJDK, each -> each % 10000);
        Verify.assertSize(10_000, multimap.asMap());
        return multimap;
    }

    @Test
    public void test_groupBy_unordered_lists_10000_keys_serial_eager_guava()
    {
        ImmutableListMultimap<Integer, Integer> multimap = this.groupBy_unordered_lists_10000_keys_serial_eager_guava();
        for (int i = 0; i < 10_000; i++)
        {
            ImmutableList<Integer> integers = multimap.get(i);
            Verify.assertSize(100, integers);
            Assert.assertEquals(Interval.fromToBy(i, 999_999, 10_000), integers);
        }
    }

    @Benchmark
    public UnifiedSetMultimap<Boolean, Integer> groupBy_2_keys_serial_eager_gsc()
    {
        UnifiedSetMultimap<Boolean, Integer> multimap = this.integersGSC.groupBy(each -> each % 2 == 0);
        Assert.assertEquals(2, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_2_keys_serial_eager_gsc()
    {
        UnifiedSetMultimap<Boolean, Integer> multimap = this.groupBy_2_keys_serial_eager_gsc();
        Set<Integer> odds = multimap.get(false);
        Set<Integer> evens = multimap.get(true);
        Verify.assertSetsEqual(Interval.fromToBy(0, 999_999, 2).toSet(), evens);
        Verify.assertSetsEqual(Interval.fromToBy(1, 999_999, 2).toSet(), odds);
    }

    @Benchmark
    public UnifiedSetMultimap<Integer, Integer> groupBy_100_keys_serial_eager_gsc()
    {
        UnifiedSetMultimap<Integer, Integer> multimap = this.integersGSC.groupBy(each -> each % 100);
        Assert.assertEquals(100, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_100_keys_serial_eager_gsc()
    {
        UnifiedSetMultimap<Integer, Integer> multimap = this.groupBy_100_keys_serial_eager_gsc();
        for (int i = 0; i < 100; i++)
        {
            MutableSet<Integer> integers = multimap.get(i);
            Verify.assertSize(10_000, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 100).toSet(), integers);
        }
    }

    @Benchmark
    public UnifiedSetMultimap<Integer, Integer> groupBy_10000_keys_serial_eager_gsc()
    {
        UnifiedSetMultimap<Integer, Integer> multimap = this.integersGSC.groupBy(each -> each % 10_000);
        Assert.assertEquals(10_000, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_10000_keys_serial_eager_gsc()
    {
        UnifiedSetMultimap<Integer, Integer> multimap = this.groupBy_10000_keys_serial_eager_gsc();
        for (int i = 0; i < 10_000; i++)
        {
            Set<Integer> integers = multimap.get(i);
            Verify.assertSize(100, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 10_000).toSet(), integers);
        }
    }

    @Benchmark
    public Multimap<Boolean, Integer> groupBy_unordered_lists_2_keys_serial_lazy_gsc()
    {
        Multimap<Boolean, Integer> multimap = this.integersGSC.asLazy().groupBy(each -> each % 2 == 0);
        Assert.assertEquals(2, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_unordered_lists_2_keys_serial_lazy_gsc()
    {
        Multimap<Boolean, Integer> multimap = this.groupBy_unordered_lists_2_keys_serial_lazy_gsc();
        RichIterable<Integer> odds = multimap.get(false);
        RichIterable<Integer> evens = multimap.get(true);
        Verify.assertSetsEqual(Interval.fromToBy(0, 999_999, 2).toSet(), evens.toSet());
        Verify.assertSetsEqual(Interval.fromToBy(1, 999_999, 2).toSet(), odds.toSet());
    }

    @Benchmark
    public Multimap<Integer, Integer> groupBy_unordered_lists_100_keys_serial_lazy_gsc()
    {
        Multimap<Integer, Integer> multimap = this.integersGSC.asLazy().groupBy(each -> each % 100);
        Assert.assertEquals(100, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_unordered_lists_100_keys_serial_lazy_gsc()
    {
        Multimap<Integer, Integer> multimap = this.groupBy_unordered_lists_100_keys_serial_lazy_gsc();
        for (int i = 0; i < 100; i++)
        {
            RichIterable<Integer> integers = multimap.get(i);
            Verify.assertIterableSize(10_000, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 100).toSet(), integers.toSet());
        }
    }

    @Benchmark
    public Multimap<Integer, Integer> groupBy_unordered_lists_10000_keys_serial_lazy_gsc()
    {
        Multimap<Integer, Integer> multimap = this.integersGSC.asLazy().groupBy(each -> each % 10_000);
        Assert.assertEquals(10_000, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_unordered_lists_10000_keys_serial_lazy_gsc()
    {
        Multimap<Integer, Integer> multimap = this.groupBy_unordered_lists_10000_keys_serial_lazy_gsc();
        for (int i = 0; i < 10_000; i++)
        {
            RichIterable<Integer> integers = multimap.get(i);
            Verify.assertIterableSize(100, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 10_000).toSet(), integers.toSet());
        }
    }

    @Benchmark
    public UnsortedSetMultimap<Boolean, Integer> groupBy_2_keys_parallel_lazy_gsc()
    {
        UnsortedSetMultimap<Boolean, Integer> multimap = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).groupBy(each -> each % 2 == 0);
        Assert.assertEquals(2, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_2_keys_parallel_lazy_gsc()
    {
        UnsortedSetMultimap<Boolean, Integer> multimap = this.groupBy_2_keys_parallel_lazy_gsc();
        UnsortedSetIterable<Integer> odds = multimap.get(false);
        UnsortedSetIterable<Integer> evens = multimap.get(true);
        Verify.assertSetsEqual(Interval.fromToBy(0, 999_999, 2).toSet(), (Set<?>) evens);
        Verify.assertSetsEqual(Interval.fromToBy(1, 999_999, 2).toSet(), (Set<?>) odds);
    }

    @Benchmark
    public UnsortedSetMultimap<Integer, Integer> groupBy_100_keys_parallel_lazy_gsc()
    {
        UnsortedSetMultimap<Integer, Integer> multimap = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).groupBy(each -> each % 100);
        Assert.assertEquals(100, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_100_keys_parallel_lazy_gsc()
    {
        UnsortedSetMultimap<Integer, Integer> multimap = this.groupBy_100_keys_parallel_lazy_gsc();
        for (int i = 0; i < 100; i++)
        {
            UnsortedSetIterable<Integer> integers = multimap.get(i);
            Verify.assertIterableSize(10_000, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 100).toSet(), (Set<?>) integers);
        }
    }

    @Benchmark
    public UnsortedSetMultimap<Integer, Integer> groupBy_10000_keys_parallel_lazy_gsc()
    {
        UnsortedSetMultimap<Integer, Integer> multimap = this.integersGSC.asParallel(this.executorService, BATCH_SIZE).groupBy(each -> each % 10_000);
        Assert.assertEquals(10_000, multimap.sizeDistinct());
        return multimap;
    }

    @Test
    public void test_groupBy_10000_keys_parallel_lazy_gsc()
    {
        UnsortedSetMultimap<Integer, Integer> multimap = this.groupBy_10000_keys_parallel_lazy_gsc();
        for (int i = 0; i < 10_000; i++)
        {
            UnsortedSetIterable<Integer> integers = multimap.get(i);
            Verify.assertIterableSize(100, integers);
            Verify.assertSetsEqual(Interval.fromToBy(i, 999_999, 10_000).toSet(), (Set<?>) integers);
        }
    }

    @Benchmark
    public void groupBy_2_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.groupBy_2_keys_serial_eager_scala();
    }

    @Test
    public void test_groupBy_2_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.test_groupBy_2_keys_serial_eager_scala();
    }

    @Benchmark
    public void groupBy_100_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.groupBy_100_keys_serial_eager_scala();
    }

    @Test
    public void test_groupBy_100_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.test_groupBy_100_keys_serial_eager_scala();
    }

    @Benchmark
    public void groupBy_10000_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.groupBy_10000_keys_serial_eager_scala();
    }

    @Test
    public void test_groupBy_10000_keys_serial_eager_scala()
    {
        GroupBySetScalaTest.test_groupBy_10000_keys_serial_eager_scala();
    }

    @Benchmark
    public void groupBy_2_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_unordered_lists_2_keys_serial_lazy_scala();
    }

    @Test
    public void test_groupBy_unordered_lists_2_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.test_groupBy_unordered_lists_2_keys_serial_lazy_scala();
    }

    @Benchmark
    public void groupBy_100_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_unordered_lists_100_keys_serial_lazy_scala();
    }

    @Test
    public void test_groupBy_unordered_lists_100_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.test_groupBy_unordered_lists_100_keys_serial_lazy_scala();
    }

    @Benchmark
    public void groupBy_10000_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_unordered_lists_10000_keys_serial_lazy_scala();
    }

    @Test
    public void test_groupBy_unordered_lists_10000_keys_serial_lazy_scala()
    {
        GroupBySetScalaTest.test_groupBy_unordered_lists_10000_keys_serial_lazy_scala();
    }

    @Benchmark
    public void groupBy_2_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_2_keys_parallel_lazy_scala();
    }

    @Test
    public void test_groupBy_2_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.test_groupBy_2_keys_parallel_lazy_scala();
    }

    @Benchmark
    public void groupBy_100_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_100_keys_parallel_lazy_scala();
    }

    @Test
    public void test_groupBy_100_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.test_groupBy_100_keys_parallel_lazy_scala();
    }

    @Benchmark
    public void groupBy_10000_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.groupBy_10000_keys_parallel_lazy_scala();
    }

    @Test
    public void test_groupBy_10000_keys_parallel_lazy_scala()
    {
        GroupBySetScalaTest.test_groupBy_10000_keys_parallel_lazy_scala();
    }
}
