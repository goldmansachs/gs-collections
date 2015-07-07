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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.ParallelUnsortedBag;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.bag.UnsortedBagMultimap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.forkjoin.FJIterate;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.parallel.ParallelIterate;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
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
public class AnagramBagTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;

    private static final int SIZE_THRESHOLD = 10;
    private final HashBag<String> gscWords = HashBag.newBag(FastList.newWithNValues(SIZE, () -> RandomStringUtils.randomAlphabetic(5).toUpperCase()));
    private final Multiset<String> guavaWords = HashMultiset.create(this.gscWords);

    private ExecutorService executorService;

    @Setup
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @TearDown
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
    }

    @Benchmark
    public void serial_eager_gsc()
    {
        MutableListMultimap<Alphagram, String> groupBy = this.gscWords.groupBy(Alphagram::new, FastListMultimap.newMultimap());
        groupBy.multiValuesView()
                .select(iterable -> iterable.size() >= SIZE_THRESHOLD)
                .toSortedList(Comparators.<RichIterable<String>>byIntFunction(RichIterable::size))
                .asReversed()
                .collect(iterable -> iterable.size() + ": " + iterable)
                .forEach(Procedures.cast(e -> Assert.assertFalse(e.isEmpty())));
    }

    @Benchmark
    public void parallel_eager_gsc()
    {
        MutableMultimap<Alphagram, String> groupBy = ParallelIterate.groupBy(this.gscWords, Alphagram::new);
        groupBy.multiValuesView()
                .select(iterable -> iterable.size() >= SIZE_THRESHOLD)
                .toSortedList(Comparators.<RichIterable<String>>byIntFunction(RichIterable::size))
                .asReversed()
                .collect(iterable -> iterable.size() + ": " + iterable)
                .forEach(Procedures.cast(e -> Assert.assertFalse(e.isEmpty())));
    }

    @Benchmark
    public void parallel_lazy_gsc()
    {
        ParallelUnsortedBag<String> parallelUnsortedBag = this.gscWords.asParallel(this.executorService, BATCH_SIZE);
        UnsortedBagMultimap<Alphagram, String> groupBy = parallelUnsortedBag.groupBy(Alphagram::new);
        groupBy.multiValuesView()
                .select(iterable -> iterable.size() >= SIZE_THRESHOLD)
                .toSortedList(Comparators.<RichIterable<String>>byIntFunction(RichIterable::size))
                .asReversed()
                .collect(iterable -> iterable.size() + ": " + iterable)
                .forEach(Procedures.cast(e -> Assert.assertFalse(e.isEmpty())));
    }

    @Benchmark
    public void parallel_eager_forkjoin_gsc()
    {
        MutableMultimap<Alphagram, String> groupBy = FJIterate.groupBy(this.gscWords, Alphagram::new);
        groupBy.multiValuesView()
                .select(iterable -> iterable.size() >= SIZE_THRESHOLD)
                .toSortedList(Comparators.<RichIterable<String>>byIntFunction(RichIterable::size))
                .asReversed()
                .collect(iterable -> iterable.size() + ": " + iterable)
                .forEach(Procedures.cast(e -> Assert.assertFalse(e.isEmpty())));
    }

    @Benchmark
    public void serial_lazy_jdk()
    {
        Map<Alphagram, List<String>> groupBy = this.guavaWords.stream().collect(Collectors.groupingBy(Alphagram::new));
        groupBy.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(list -> list.size() >= SIZE_THRESHOLD)
                .sorted(Comparator.<List<String>>comparingInt(List::size).reversed())
                .map(list -> list.size() + ": " + list)
                .forEach(e -> Assert.assertFalse(e.isEmpty()));
    }

    @Benchmark
    public void serial_lazy_streams_gsc()
    {
        Map<Alphagram, List<String>> groupBy = this.gscWords.stream().collect(Collectors.groupingBy(Alphagram::new));
        groupBy.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(list -> list.size() >= SIZE_THRESHOLD)
                .sorted(Comparator.<List<String>>comparingInt(List::size).reversed())
                .map(list -> list.size() + ": " + list)
                .forEach(e -> Assert.assertFalse(e.isEmpty()));
    }

    @Benchmark
    public void parallel_lazy_jdk()
    {
        Map<Alphagram, List<String>> groupBy = this.guavaWords.parallelStream().collect(Collectors.groupingBy(Alphagram::new));
        groupBy.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .filter(list -> list.size() >= SIZE_THRESHOLD)
                .sorted(Comparator.<List<String>>comparingInt(List::size).reversed())
                .map(list -> list.size() + ": " + list)
                .forEach(e -> Assert.assertFalse(e.isEmpty()));
    }

    @Benchmark
    public void parallel_lazy_streams_gsc()
    {
        Map<Alphagram, List<String>> groupBy = this.gscWords.parallelStream().collect(Collectors.groupingBy(Alphagram::new));
        groupBy.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .filter(list -> list.size() >= SIZE_THRESHOLD)
                .sorted(Comparator.<List<String>>comparingInt(List::size).reversed())
                .map(list -> list.size() + ": " + list)
                .forEach(e -> Assert.assertFalse(e.isEmpty()));
    }

    private static final class Alphagram
    {
        private final char[] key;

        private Alphagram(String string)
        {
            this.key = string.toCharArray();
            Arrays.sort(this.key);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || this.getClass() != o.getClass())
            {
                return false;
            }
            Alphagram alphagram = (Alphagram) o;
            return Arrays.equals(this.key, alphagram.key);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(this.key);
        }

        @Override
        public String toString()
        {
            return new String(this.key);
        }
    }
}
