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
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.ParallelTests;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.Iterate;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialParallelLazyPerformanceTest
{
    public static final Predicate<Integer> PREDICATE_1 = Predicates.greaterThan(0).and(IntegerPredicates.isOdd());
    public static final Predicate<Integer> PREDICATE_2 = IntegerPredicates.isPositive().and(IntegerPredicates.isEven());
    public static final Predicate<Integer> PREDICATE_3 = IntegerPredicates.isOdd().and(IntegerPredicates.isNegative());
    public static final MutableList<Predicate<Integer>> PREDICATES = FastList.newListWith(PREDICATE_1, PREDICATE_2, PREDICATE_3);

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
        this.measureAlgorithmForIntegerIterable("toList", new Procedure<Function0<UnifiedSet<Integer>>>()
        {
            public void value(Function0<UnifiedSet<Integer>> each)
            {
                SerialParallelLazyPerformanceTest.this.toList(each.value());
            }
        });
    }

    @Test
    @Category(ParallelTests.class)
    public void select()
    {
        this.measureAlgorithmForIntegerIterable("toList", new Procedure<Function0<UnifiedSet<Integer>>>()
        {
            public void value(Function0<UnifiedSet<Integer>> each)
            {
                SerialParallelLazyPerformanceTest.this.select(each.value());
            }
        });
    }

    @After
    public void tearDown()
    {
        SerialParallelLazyPerformanceTest.forceGC();
    }

    private static void forceGC()
    {
        IntInterval.oneTo(20).forEach(new IntProcedure()
        {
            public void value(int each)
            {
                System.gc();
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
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

    private MutableList<Function0<UnifiedSet<Integer>>> getIntegerListGenerators(int count)
    {
        final Interval interval = Interval.fromTo(-(count / 2), count / 2 - 1);
        MutableList<Function0<UnifiedSet<Integer>>> generators = FastList.newList();
        generators.add(new Function0<UnifiedSet<Integer>>()
        {
            public UnifiedSet<Integer> value()
            {
                return UnifiedSet.newSet(interval);
            }
        });
        Collections.shuffle(generators);
        return generators;
    }

    private void measureAlgorithmForIntegerIterable(String algorithmName, final Procedure<Function0<UnifiedSet<Integer>>> algorithm)
    {
        this.printMachineAndTestConfiguration(algorithmName);
        for (int i = 0; i < 4; i++)
        {
            this.getSizes().forEach(new Procedure<Integer>()
            {
                public void value(Integer count)
                {
                    SerialParallelLazyPerformanceTest.this.getIntegerListGenerators(count).forEach(algorithm);
                }
            });
        }
    }

    private void shuffleAndRun(MutableList<Runnable> runnables)
    {
        Collections.shuffle(runnables);
        runnables.forEach(new Procedure<Runnable>()
        {
            public void value(Runnable runnable)
            {
                runnable.run();
            }
        });
    }

    private void toList(final UnifiedSet<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelLazyPerformanceTest.this.basicSerialToListPerformance(collection, SERIAL_RUN_COUNT);
            }
        });
        final int cores = Runtime.getRuntime().availableProcessors();
        final ExecutorService service = Executors.newFixedThreadPool(cores);
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelLazyPerformanceTest.this.basicParallelLazyToListPerformance(collection, PARALLEL_RUN_COUNT, cores, service);
            }
        });
        this.shuffleAndRun(runnables);
        service.shutdown();
    }

    private void select(final UnifiedSet<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelLazyPerformanceTest.this.basicSerialSelectPerformance(collection, PREDICATES, SERIAL_RUN_COUNT);
            }
        });
        final int cores = Runtime.getRuntime().availableProcessors();
        final ExecutorService service = Executors.newFixedThreadPool(cores);
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelLazyPerformanceTest.this.basicParallelLazySelectPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT, cores, service);
            }
        });
        this.shuffleAndRun(runnables);
        service.shutdown();
    }

    private double basicSerialToListPerformance(
            final UnifiedSet<Integer> iterable,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial******* toList: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: 1", new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(iterable.toList());
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicSerialSelectPerformance(
            final UnifiedSet<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial******* Select: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: 1", new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(iterable.select(predicateList.get(0)).toList());
                Verify.assertNotEmpty(iterable.select(predicateList.get(1)).toList());
                Verify.assertNotEmpty(iterable.select(predicateList.get(2)).toList());
            }
        }, count, WARM_UP_COUNT);
    }

    private String formatSizeOf(Iterable<?> iterable)
    {
        return NumberFormat.getInstance().format(Iterate.sizeOf(iterable));
    }

    private double basicParallelLazyToListPerformance(
            final UnifiedSet<Integer> iterable,
            int count,
            int cores,
            final ExecutorService service)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Lazy toList: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: " + cores, new Runnable()
        {
            public void run()
            {
                Verify.assertSize(iterable.size(), iterable.asParallel(service, iterable.size() / 8).toList());
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicParallelLazySelectPerformance(
            final UnifiedSet<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count,
            int cores,
            final ExecutorService service)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Lazy Select: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable) + " cores: " + cores, new Runnable()
        {
            public void run()
            {
                iterable.asParallel(service, iterable.size() / 8).select(predicateList.get(0)).toList();
                iterable.asParallel(service, iterable.size() / 8).select(predicateList.get(1)).toList();
                iterable.asParallel(service, iterable.size() / 8).select(predicateList.get(2)).toList();
            }
        }, count, WARM_UP_COUNT);
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
        private static final Function0<AtomicIntegerWithEquals> NEW_INSTANCE = new Function0<AtomicIntegerWithEquals>()
        {
            public AtomicIntegerWithEquals value()
            {
                return new AtomicIntegerWithEquals(0);
            }
        };

        private static final Procedure2<AtomicIntegerWithEquals, String> INCREMENT = new Procedure2<AtomicIntegerWithEquals, String>()
        {
            public void value(AtomicIntegerWithEquals value, String each)
            {
                value.incrementAndGet();
            }
        };

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
