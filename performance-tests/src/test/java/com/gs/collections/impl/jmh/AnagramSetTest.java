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

package com.gs.collections.impl.jmh;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.forkjoin.FJIterate;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class AnagramSetTest
{
    private static final int SIZE_THRESHOLD = 10;
    private static final MutableSet<String> GSC_WORDS = FastList.newWithNValues(1000000, () -> RandomStringUtils.randomAlphabetic(5).toUpperCase()).toSet();
    private static final Set<String> JDK_WORDS = new HashSet<>(GSC_WORDS);

    private MutableSet<String> getGSCWords()
    {
        return GSC_WORDS;
    }

    private Set<String> getJdk8Words()
    {
        return JDK_WORDS;
    }

    @Test
    @GenerateMicroBenchmark
    public void gscSerialAnagrams()
    {
        MutableSetMultimap<Alphagram, String> groupBy = this.getGSCWords().groupBy(Alphagram::new);
        groupBy.multiValuesView()
                .select(iterable -> iterable.size() >= SIZE_THRESHOLD)
                .toSortedList(Comparators.<RichIterable<String>>byIntFunction(RichIterable::size))
                .asReversed()
                .collect(iterable -> iterable.size() + ": " + iterable)
                .forEach(Procedures.cast(e -> {e.length();}));
    }

    @Test
    @GenerateMicroBenchmark
    public void gscParallelEagerAnagrams()
    {
        MutableMultimap<Alphagram, String> groupBy = ParallelIterate.groupBy(this.getGSCWords(), Alphagram::new);
        groupBy.multiValuesView()
                .select(iterable -> iterable.size() >= SIZE_THRESHOLD)
                .toSortedList(Comparators.<RichIterable<String>>byIntFunction(RichIterable::size))
                .asReversed()
                .collect(iterable -> iterable.size() + ": " + iterable)
                .forEach(Procedures.cast(e -> {e.length();}));
    }

    @Test
    @GenerateMicroBenchmark
    public void gscForkJoinEagerAnagrams()
    {
        MutableMultimap<Alphagram, String> groupBy = FJIterate.groupBy(this.getGSCWords(), Alphagram::new);
        groupBy.multiValuesView()
                .select(iterable -> iterable.size() >= SIZE_THRESHOLD)
                .toSortedList(Comparators.<RichIterable<String>>byIntFunction(RichIterable::size))
                .asReversed()
                .collect(iterable -> iterable.size() + ": " + iterable)
                .forEach(Procedures.cast(e -> {e.length();}));
    }

    @Test
    @GenerateMicroBenchmark
    public void jdk8SerialLazyAnagrams()
    {
        Map<Alphagram, Set<String>> groupBy = this.getJdk8Words().stream().collect(Collectors.groupingBy(Alphagram::new, Collectors.<String>toSet()));
        groupBy.entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .filter(list -> list.size() >= SIZE_THRESHOLD)
                .sorted(Comparator.<Set<String>>comparingInt(Set::size).reversed())
                .map(list -> list.size() + ": " + list)
                .forEach(e -> {e.length();});
    }

    @Test
    @GenerateMicroBenchmark
    public void jdk8ParallelLazyAnagrams()
    {
        Map<Alphagram, Set<String>> groupBy = this.getJdk8Words().parallelStream().collect(Collectors.groupingBy(Alphagram::new, Collectors.<String>toSet()));
        groupBy.entrySet()
                .parallelStream()
                .map(entry -> entry.getValue())
                .filter(list -> list.size() >= SIZE_THRESHOLD)
                .sorted(Comparator.<Set<String>>comparingInt(Set::size).reversed())
                .map(list -> list.size() + ": " + list)
                .forEach(e -> {e.length();});
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
