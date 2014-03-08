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

package com.gs.collections.impl.parallel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.ParallelTests;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialParallelLazyPerformanceTest
{
    public static final Predicate<Integer> PREDICATE_1 = item -> item > 0 && (item & 1) != 0;
    public static final Predicate<Integer> PREDICATE_2 = item -> item > 0 && (item & 1) == 0;
    public static final Predicate<Integer> PREDICATE_3 = item -> item < 0 && (item & 1) != 0;
    public static final MutableList<Predicate<Integer>> PREDICATES = FastList.newListWith(PREDICATE_1, PREDICATE_2, PREDICATE_3);

    private static final Function<Integer, Pair<Integer, Integer>> PAIR_FUNCTION = value -> Tuples.pair(value, value);

    private static final Function<Integer, Long> LONG_FUNCTION = value -> value.longValue();

    private static final Function<Integer, Short> SHORT_FUNCTION = value -> value.shortValue();

    private static final Function<String, Alphagram> ALPHAGRAM_FUNCTION = value -> new Alphagram(value);

    private static final Logger LOGGER = LoggerFactory.getLogger(SerialParallelLazyPerformanceTest.class);

    private static final int SCALE_FACTOR = Integer.parseInt(System.getProperty("scaleFactor", "100"));

    private static final int WARM_UP_COUNT = Integer.parseInt(System.getProperty("WarmupCount", "100"));
    private static final int PARALLEL_RUN_COUNT = Integer.parseInt(System.getProperty("ParallelRunCount", "200"));
    private static final int SERIAL_RUN_COUNT = Integer.parseInt(System.getProperty("SerialRunCount", "200"));

    private static final int SMALL_COUNT = 100 * SCALE_FACTOR;
    private static final int MEDIUM_COUNT = 1000 * SCALE_FACTOR;
    private static final int LARGE_COUNT = 10000 * SCALE_FACTOR;

    @Test
    @Category(ParallelTests.class)
    public void toList()
    {
        this.measureAlgorithmForIntegerIterable("toList", each -> this.toList(each.value()), true);
    }

    @Test
    @Category(ParallelTests.class)
    public void select()
    {
        this.measureAlgorithmForIntegerIterable("Select", each -> this.select(each.value()), true);
    }

    @Test
    @Category(ParallelTests.class)
    public void reject()
    {
        this.measureAlgorithmForIntegerIterable("Reject", each -> this.reject(each.value()), true);
    }

    @Test
    @Category(ParallelTests.class)
    public void anySatisfyShortCircuitInBeginning()
    {
        this.measureAlgorithmForIntegerIterable("AnySatisfy Short Circuit In The Beginning", (Procedure<Function0<FastList<Integer>>>) each -> this.anySatisfy(each.value(), PREDICATES.get(2), true), false);
    }

    @Test
    @Category(ParallelTests.class)
    public void anySatisfyShortCircuitInMiddle()
    {
        this.measureAlgorithmForIntegerIterable("AnySatisfy Short Circuit In The Middle", each -> this.anySatisfy(each.value(), PREDICATES.get(0), true), false);
    }

    @Test
    @Category(ParallelTests.class)
    public void anySatisfyShortCircuitInEnd()
    {
        this.measureAlgorithmForIntegerIterable("AnySatisfy Short Circuit In The End", each -> this.anySatisfy(each.value(), Predicates.greaterThan(1000000), false), false);
    }

    @Test
    @Category(ParallelTests.class)
    public void collect()
    {
        this.measureAlgorithmForIntegerIterable("Collect", each -> this.collect(each.value()), true);
    }

    @Test
    @Category(ParallelTests.class)
    public void groupBy()
    {
        this.measureAlgorithmForRandomStringIterable("GroupBy", each -> this.groupBy(each.value()));
    }

    @After
    public void tearDown()
    {
        SerialParallelLazyPerformanceTest.forceGC();
    }

    private static void forceGC()
    {
        IntInterval.oneTo(20).forEach((IntProcedure) each -> {
            System.gc();
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        });
    }

    public void printMachineAndTestConfiguration(String serialParallelAlgorithm)
    {
        LOGGER.info("*** Algorithm: {}", serialParallelAlgorithm);
        LOGGER.info("Available Processors: {}", Runtime.getRuntime().availableProcessors());
        LOGGER.info("Default Thread Pool Size: {}", ParallelIterate.getDefaultMaxThreadPoolSize());
        LOGGER.info("Default Task Count: {}", ParallelIterate.getDefaultTaskCount());
        LOGGER.info("Scale Factor: {}", SCALE_FACTOR);
        LOGGER.info("Warm up count: {}", WARM_UP_COUNT);
        LOGGER.info("Parallel Run Count: {}", PARALLEL_RUN_COUNT);
        LOGGER.info("Serial** Run Count: {}", SERIAL_RUN_COUNT);
    }

    private MutableList<Integer> getSizes()
    {
        MutableList<Integer> sizes = FastList.newListWith(LARGE_COUNT, MEDIUM_COUNT, SMALL_COUNT);
        Collections.shuffle(sizes);
        return sizes;
    }

    private MutableList<Function0<FastList<Integer>>> getIntegerListGenerators(int count, boolean shuffle)
    {
        Interval interval = Interval.fromTo(-(count / 2), count / 2 - 1);
        MutableList<Function0<FastList<Integer>>> generators = FastList.newList();
        generators.add(() -> {
            FastList<Integer> integers = FastList.newList(interval);
            if (shuffle)
            {
                Collections.shuffle(integers);
            }
            return integers;
        });
        Collections.shuffle(generators);
        return generators;
    }

    private void measureAlgorithmForIntegerIterable(String algorithmName, Procedure<Function0<FastList<Integer>>> algorithm, boolean shuffle)
    {
        this.printMachineAndTestConfiguration(algorithmName);
        for (int i = 0; i < 4; i++)
        {
            this.getSizes().forEach((Procedure<Integer>) count -> this.getIntegerListGenerators(count, shuffle).forEach(algorithm));
        }
    }

    private MutableList<Function0<UnifiedSet<String>>> getRandomWordsGenerators(int count)
    {
        MutableList<Function0<UnifiedSet<String>>> generators = FastList.newList();
        generators.add(() -> this.generateWordsList(count));
        return generators;
    }

    public UnifiedSet<String> generateWordsList(int count)
    {
        UnifiedSet<String> words = UnifiedSet.newSet();
        while (words.size() < count)
        {
            words.add(RandomStringUtils.randomAlphabetic(5));
        }
        return words;
    }

    private void measureAlgorithmForRandomStringIterable(String algorithmName, Procedure<Function0<UnifiedSet<String>>> algorithm)
    {
        this.printMachineAndTestConfiguration(algorithmName);
        for (int i = 0; i < 4; i++)
        {
            this.getSizes().forEach((Procedure<Integer>) count -> this.getRandomWordsGenerators(count).forEach(algorithm));
        }
    }

    private void shuffleAndRun(MutableList<Runnable> runnables)
    {
        Collections.shuffle(runnables);
        runnables.forEach((Procedure<Runnable>) Runnable::run);
    }

    private void toList(FastList<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(() -> {
            this.basicSerialToListPerformance(collection, SERIAL_RUN_COUNT);
        });
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        runnables.add(() -> {
            this.basicParallelLazyToListPerformance(collection, PARALLEL_RUN_COUNT, cores, service);
        });
        this.shuffleAndRun(runnables);
        service.shutdown();
        try
        {
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void select(FastList<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(() -> {
            this.basicSerialSelectPerformance(collection, PREDICATES, SERIAL_RUN_COUNT);
        });
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        runnables.add(() -> {
            this.basicParallelLazySelectPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT, cores, service);
        });

        List<Integer> arrayList = new ArrayList<Integer>(collection);
        runnables.add(() -> {
            this.basicJava8ParallelLazySelectPerformance(arrayList, PARALLEL_RUN_COUNT);
        });
        this.shuffleAndRun(runnables);
        service.shutdown();
        try
        {
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void reject(FastList<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(() -> {
            this.basicSerialRejectPerformance(collection, PREDICATES, SERIAL_RUN_COUNT);
        });
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        runnables.add(() -> {
            this.basicParallelLazyRejectPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT, cores, service);
        });
        this.shuffleAndRun(runnables);
        service.shutdown();
        try
        {
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void anySatisfy(FastList<Integer> collection, Predicate<? super Integer> predicate, boolean expectedResult)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(() -> {
            this.basicSerialAnySatisfyPerformance(collection, predicate, expectedResult, SERIAL_RUN_COUNT);
        });
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        runnables.add(() -> {
            this.basicParallelLazyAnySatisfyPerformance(collection, predicate, expectedResult, PARALLEL_RUN_COUNT, cores, service);
        });
        this.shuffleAndRun(runnables);
        service.shutdown();
        try
        {
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void collect(FastList<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(() -> {
            this.basicSerialCollectPerformance(collection, SERIAL_RUN_COUNT);
        });
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        runnables.add(() -> {
            this.basicParallelLazyCollectPerformance(collection, PARALLEL_RUN_COUNT, cores, service);
        });
        this.shuffleAndRun(runnables);
        service.shutdown();
        try
        {
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void groupBy(UnifiedSet<String> words)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(() -> {
            this.basicSerialGroupByPerformance(words, SERIAL_RUN_COUNT);
        });
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        runnables.add(() -> {
            this.basicParallelGroupByPerformance(words, PARALLEL_RUN_COUNT, cores, service);
        });
        this.shuffleAndRun(runnables);
        service.shutdown();
        try
        {
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private double basicSerialToListPerformance(
            FastList<Integer> iterable,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial******* toList: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: 1", () -> Verify.assertNotEmpty(iterable.toList()), count, WARM_UP_COUNT);
    }

    private double basicSerialSelectPerformance(
            FastList<Integer> iterable,
            MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial******* Select: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: 1", () -> {
                    Verify.assertNotEmpty(iterable.select(predicateList.get(0)).toList());
                    Verify.assertNotEmpty(iterable.select(predicateList.get(1)).toList());
                    Verify.assertNotEmpty(iterable.select(predicateList.get(2)).toList());
                }, count, WARM_UP_COUNT);
    }

    private double basicSerialRejectPerformance(
            FastList<Integer> iterable,
            MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial******* Reject: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: 1", () -> {
                    Verify.assertNotEmpty(iterable.reject(predicateList.get(0)).toList());
                    Verify.assertNotEmpty(iterable.reject(predicateList.get(1)).toList());
                    Verify.assertNotEmpty(iterable.reject(predicateList.get(2)).toList());
                }, count, WARM_UP_COUNT);
    }

    private double basicSerialAnySatisfyPerformance(
            FastList<Integer> iterable,
            Predicate<? super Integer> predicate,
            boolean expectedResult,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial******* AnySatisfy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: 1", () -> Assert.assertEquals(expectedResult, iterable.anySatisfy(predicate)), count, WARM_UP_COUNT);
    }

    private double basicSerialCollectPerformance(
            FastList<Integer> iterable,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial******* Collect: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: 1", () -> {
                    Verify.assertNotEmpty(iterable.collect(PAIR_FUNCTION).toList());
                    Verify.assertNotEmpty(iterable.collect(SHORT_FUNCTION).toList());
                    Verify.assertNotEmpty(iterable.collect(LONG_FUNCTION).toList());
                }, count, 10);
    }

    private double basicSerialGroupByPerformance(
            UnifiedSet<String> iterable,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial******* GroupBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: 1", () -> Verify.assertNotEmpty(iterable.groupBy(ALPHAGRAM_FUNCTION)), count, 10);
    }

    private String formatSizeOf(Iterable<?> iterable)
    {
        return NumberFormat.getInstance().format(Iterate.sizeOf(iterable));
    }

    private static int getBatchSizeFor(Collection<?> iterable)
    {
        return iterable.size() == 1000000 ? 10000 : 1000;
    }

    private double basicParallelLazyToListPerformance(
            FastList<Integer> iterable,
            int count,
            int cores,
            ExecutorService service)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Lazy toList: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: " + cores, () -> Verify.assertSize(iterable.size(), iterable.asParallel(service, iterable.size() / (cores * 3)).toList()), count, WARM_UP_COUNT);
    }

    private double basicJava8ParallelLazySelectPerformance(
            List<Integer> iterable,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Java8 Select: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: ?", () -> {
                    iterable.parallelStream().filter(item -> item > 0 && (item & 1) != 0).collect(Collectors.toList());
                    iterable.parallelStream().filter(item -> item > 0 && (item & 1) == 0).collect(Collectors.toList());
                    iterable.parallelStream().filter(item -> item < 0 && (item & 1) != 0).collect(Collectors.toList());
                }, count, WARM_UP_COUNT);
    }

    private double basicParallelLazySelectPerformance(
            FastList<Integer> iterable,
            MutableList<Predicate<Integer>> predicateList,
            int count,
            int cores,
            ExecutorService service)
    {
        int batchSize = SerialParallelLazyPerformanceTest.getBatchSizeFor(iterable);
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Lazy Select: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: " + cores, () -> {
                    iterable.asParallel(service, batchSize).select(predicateList.get(0)).toList();
                    iterable.asParallel(service, batchSize).select(predicateList.get(1)).toList();
                    iterable.asParallel(service, batchSize).select(predicateList.get(2)).toList();
                }, count, WARM_UP_COUNT);
    }

    private double basicParallelLazyRejectPerformance(
            FastList<Integer> iterable,
            MutableList<Predicate<Integer>> predicateList,
            int count,
            int cores,
            ExecutorService service)
    {
        int batchSize = SerialParallelLazyPerformanceTest.getBatchSizeFor(iterable);
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Lazy Reject: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: " + cores, () -> {
                    Verify.assertNotEmpty(iterable.asParallel(service, batchSize).reject(predicateList.get(0)).toList());
                    Verify.assertNotEmpty(iterable.asParallel(service, batchSize).reject(predicateList.get(1)).toList());
                    Verify.assertNotEmpty(iterable.asParallel(service, batchSize).reject(predicateList.get(2)).toList());
                }, count, WARM_UP_COUNT);
    }

    private double basicParallelLazyAnySatisfyPerformance(
            FastList<Integer> iterable,
            Predicate<? super Integer> predicate,
            boolean expectedResult,
            int count,
            int cores,
            ExecutorService service)
    {
        int batchSize = SerialParallelLazyPerformanceTest.getBatchSizeFor(iterable);
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Lazy AnySatisfy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: " + cores, () -> Assert.assertEquals(expectedResult, iterable.asParallel(service, batchSize).anySatisfy(predicate)), count, WARM_UP_COUNT);
    }

    private double basicParallelLazyCollectPerformance(
            FastList<Integer> iterable,
            int count,
            int cores,
            ExecutorService service)
    {
        int batchSize = SerialParallelLazyPerformanceTest.getBatchSizeFor(iterable);

        return TimeKeeper.logAverageMillisecondsToRun("Parallel Lazy Collect: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: " + cores, () -> {
                    Verify.assertNotEmpty(iterable.asParallel(service, batchSize).collect(PAIR_FUNCTION).toList());
                    Verify.assertNotEmpty(iterable.asParallel(service, batchSize).collect(LONG_FUNCTION).toList());
                    Verify.assertNotEmpty(iterable.asParallel(service, batchSize).collect(SHORT_FUNCTION).toList());
                }, count, 10);
    }

    private double basicParallelGroupByPerformance(UnifiedSet<String> iterable,
            int count,
            int cores,
            ExecutorService service)
    {
        int batchSize = SerialParallelLazyPerformanceTest.getBatchSizeFor(iterable);
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Lazy GroupBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: " + cores, () -> Verify.assertNotEmpty(iterable.asParallel(service, batchSize).groupBy(ALPHAGRAM_FUNCTION)), count, WARM_UP_COUNT);
    }

    private String getSimpleName(Object collection)
    {
        return collection.getClass().getSimpleName();
    }

    static final class TimeKeeper
    {
        private static final SystemTimeProvider PROVIDER = new SystemTimeProvider();
        private static final long PAIN_THRESHOLD = 10000L;
        private static final int MILLIS_TO_NANOS = 1000000;

        private TimeKeeper()
        {
            throw new AssertionError("Suppress default constructor for noninstantiability");
        }

        /**
         * This method can take either a Runnable or a RunnableWithSetup.  In the case of RunnableWithSetup, the setup
         * method will be called first, without impacting the timing.
         */
        public static long millisecondsToRun(Runnable runnable)
        {
            return TimeKeeper.nanosecondsToRun(runnable) / (long) MILLIS_TO_NANOS;
        }

        public static long currentTimeNanoseconds()
        {
            return PROVIDER.currentTimeNanoseconds();
        }

        public static long currentTimeMilliseconds()
        {
            return PROVIDER.currentTimeMilliseconds();
        }

        /**
         * This method can take either a Runnable or a RunnableWithSetup.  In the case of RunnableWithSetup, the setup
         * method will be called first, without impacting the timing.
         */
        public static long nanosecondsToRun(Runnable runnable)
        {
            long start = TimeKeeper.getCurrentTimeAsNanos();
            runnable.run();
            long end = TimeKeeper.getCurrentTimeAsNanos();
            return end - start;
        }

        private static long getCurrentTimeAsNanos()
        {
            return TimeKeeper.currentTimeNanoseconds();
        }

        private static void doLog(String message, int count, long total, double average)
        {
            LOGGER.info("{} Count: {} Total(ms): {} Avg(ms): {}", message, count, TimeKeeper.longNanosToMillisString(total), TimeKeeper.doubleNanosToMillisString(average));
        }

        public static double logAverageMillisecondsToRun(
                String message,
                Runnable runnable,
                int count)
        {
            long start = TimeKeeper.getCurrentTimeAsNanos();
            for (int i = 0; i < count; i++)
            {
                runnable.run();
            }
            long totalNanos = TimeKeeper.getCurrentTimeAsNanos() - start;
            double averageTime = (double) totalNanos / (double) count;
            TimeKeeper.doLog(message, count, totalNanos, averageTime);
            return averageTime / (double) TimeKeeper.MILLIS_TO_NANOS;
        }

        private static String doubleNanosToMillisString(double nanos)
        {
            return NumberFormat.getInstance().format(nanos / MILLIS_TO_NANOS);
        }

        private static String longNanosToMillisString(long nanos)
        {
            return NumberFormat.getInstance().format(nanos / MILLIS_TO_NANOS);
        }

        public static double logAverageMillisecondsToRun(
                String message,
                Runnable runnable,
                int count,
                int warmUpCount)
        {
            TimeKeeper.warmUp(warmUpCount, runnable);
            TimeKeeper.gcAndYield();
            return TimeKeeper.logAverageMillisecondsToRun(message, runnable, count);
        }

        private static void gcAndYield()
        {
            SerialParallelLazyPerformanceTest.forceGC();
        }

        private static void warmUp(int warmUpCount, Runnable runnable)
        {
            long start = TimeKeeper.currentTimeMilliseconds();
            for (int i = 0; i < warmUpCount; i++)
            {
                TimeKeeper.millisecondsToRun(runnable);
                if (TimeKeeper.currentTimeMilliseconds() - start > PAIN_THRESHOLD)
                {
                    break;
                }
            }
        }

        private static class SystemTimeProvider
        {
            public long currentTimeMilliseconds()
            {
                return System.currentTimeMillis();
            }

            public long currentTimeNanoseconds()
            {
                return System.nanoTime();
            }
        }
    }

    private static final class Alphagram
    {
        private final char[] key;
        private final int hashCode;

        private Alphagram(String string)
        {
            this.key = string.toLowerCase().toCharArray();
            Arrays.sort(this.key);
            this.hashCode = Arrays.hashCode(this.key);
        }

        @Override
        public boolean equals(Object o)
        {
            return this == o || Arrays.equals(this.key, ((Alphagram) o).key);
        }

        @Override
        public int hashCode()
        {
            return this.hashCode;
        }

        @Override
        public String toString()
        {
            return new String(this.key);
        }
    }

    public static final class AtomicIntegerWithEquals extends AtomicInteger
    {
        private AtomicIntegerWithEquals(int initialValue)
        {
            super(initialValue);
        }

        @Override
        public int hashCode()
        {
            return this.get();
        }

        @Override
        public boolean equals(Object obj)
        {
            return (obj instanceof AtomicIntegerWithEquals) && ((AtomicIntegerWithEquals) obj).get() == this.get();
        }
    }
}
