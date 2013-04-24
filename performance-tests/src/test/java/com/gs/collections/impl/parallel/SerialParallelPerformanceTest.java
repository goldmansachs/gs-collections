/*
 * Copyright 2012 Goldman Sachs.
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.ParallelTests;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.CompositeFastList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class SerialParallelPerformanceTest
{
    private static final int SCALE_FACTOR = Integer.parseInt(System.getProperty("scaleFactor", "100"));

    private static final int WARM_UP_COUNT = Integer.parseInt(System.getProperty("WarmupCount", "100"));
    private static final int PARALLEL_RUN_COUNT = Integer.parseInt(System.getProperty("ParallelRunCount", "200"));
    private static final int SERIAL_RUN_COUNT = Integer.parseInt(System.getProperty("SerialRunCount", "200"));
    private static final int NUMBER_OF_USER_THREADS = Integer.parseInt(System.getProperty("UserThreads", "1"));

    private static final int VERY_SMALL_COUNT = 10 * SCALE_FACTOR;
    private static final int SMALL_COUNT = 100 * SCALE_FACTOR;
    private static final int MEDIUM_COUNT = 1000 * SCALE_FACTOR;
    private static final int LARGE_COUNT = 10000 * SCALE_FACTOR;

    private static final Function<Integer, Pair<Integer, Integer>> PAIR_FUNCTION =
            new Function<Integer, Pair<Integer, Integer>>()
            {
                public Pair<Integer, Integer> valueOf(Integer value)
                {
                    return Tuples.pair(value, value);
                }
            };

    private static final Function<Integer, Long> LONG_FUNCTION =
            new Function<Integer, Long>()
            {
                public Long valueOf(Integer value)
                {
                    return value.longValue();
                }
            };

    private static final Function<Integer, Short> SHORT_FUNCTION =
            new Function<Integer, Short>()
            {
                public Short valueOf(Integer value)
                {
                    return value.shortValue();
                }
            };

    private static final Function<String, Alphagram> ALPHAGRAM_FUNCTION =
            new Function<String, Alphagram>()
            {
                @SuppressWarnings("ClassEscapesDefinedScope")
                public Alphagram valueOf(String value)
                {
                    return new Alphagram(value);
                }
            };

    private static final Function0<Integer> INTEGER_NEW = new Function0<Integer>()
    {
        public Integer value()
        {
            return Integer.valueOf(0);
        }
    };

    private static final Function2<Integer, String, Integer> COUNT_AGGREGATOR = new Function2<Integer, String, Integer>()
    {
        public Integer value(Integer aggregate, String word)
        {
            return aggregate + 1;
        }
    };

    public FastList<String> generateWords(int count)
    {
        final FastList<String> words = FastList.newList();
        Interval.oneTo(count).forEach(new IntProcedure()
        {
            public void value(int each)
            {
                words.add(RandomStringUtils.randomAlphabetic(2).toLowerCase());
            }
        });
        return words;
    }

    @After
    public void tearDown()
    {
        System.gc();
        System.gc();
        Thread.yield();
        System.gc();
        Thread.yield();
    }

    public void printMachineAndTestConfiguration()
    {
        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Default Thread Pool Size: " + ParallelIterate.getDefaultMaxThreadPoolSize());
        System.out.println("Default Task Count: " + ParallelIterate.getDefaultTaskCount());
        System.out.println("Scale Factor: " + SCALE_FACTOR);
        System.out.println("Warm up count: " + WARM_UP_COUNT);
        System.out.println("Parallel Run Count: " + PARALLEL_RUN_COUNT);
        System.out.println("Serial** Run Count: " + SERIAL_RUN_COUNT);
        System.out.println("Number of User Threads: " + NUMBER_OF_USER_THREADS);
    }

    @Test
    @Category(ParallelTests.class)
    public void parallelAndSerialTest()
    {
        this.printMachineAndTestConfiguration();
        this.basicTestParallelAndSerialGSCollectionsFastList(100);  // Warm everything up
        MutableIntList sizes = IntArrayList.newListWith(LARGE_COUNT, MEDIUM_COUNT, SMALL_COUNT, VERY_SMALL_COUNT);
        IntProcedure procedure = new IntProcedure()
        {
            public void value(int size)
            {
                SerialParallelPerformanceTest.this.basicTestParallelAndSerialGSCollectionsFastList(size);
            }
        };
        sizes.forEach(procedure);
        sizes.reverseThis().forEach(procedure);
        sizes.forEach(procedure);
        sizes.reverseThis().forEach(procedure);
    }

    private void basicTestParallelAndSerialGSCollectionsFastList(int count)
    {
        FastList<Integer> fastList = (FastList<Integer>) Interval.oneTo(count).toList();
        Collections.shuffle(fastList);
        FastList<String> words = this.generateWords(count);
        Collections.shuffle(words);
        this.basicSerialAndParallelGSCollectionsPerformanceComparison(fastList, words);
    }

    private void basicSerialAndParallelGSCollectionsPerformanceComparison(Iterable<Integer> collection, Iterable<String> words)
    {
        Predicate<Integer> operation1 = Predicates.greaterThan(0).and(IntegerPredicates.isEven());
        Predicate<Integer> operation2 = IntegerPredicates.isPositive().and(IntegerPredicates.isEven());
        Predicate<Integer> operation3 = IntegerPredicates.isEven().and(IntegerPredicates.isPositive());
        MutableList<Predicate<Integer>> predicates = FastList.newListWith(operation1, operation2, operation3);

        this.basicSerialSelectPerformance(collection, predicates, SERIAL_RUN_COUNT);
        this.basicParallelSelectPerformance(collection, predicates, PARALLEL_RUN_COUNT);

        this.basicSerialRejectPerformance(collection, predicates, SERIAL_RUN_COUNT);
        this.basicParallelRejectPerformance(collection, predicates, PARALLEL_RUN_COUNT);

        this.basicSerialCountPerformance(collection, predicates, SERIAL_RUN_COUNT);
        this.basicParallelCountPerformance(collection, predicates, PARALLEL_RUN_COUNT);

        this.basicSerialCollectIfPerformance(collection, predicates, SERIAL_RUN_COUNT);
        this.basicParallelCollectIfPerformance(collection, predicates, PARALLEL_RUN_COUNT);

        this.basicSerialCollectPerformance(collection, SERIAL_RUN_COUNT);
        this.basicParallelCollectPerformance(collection, PARALLEL_RUN_COUNT);

        this.basicSerialGroupByPerformance(words, SERIAL_RUN_COUNT);
        this.basicParallelGroupByPerformance(words, PARALLEL_RUN_COUNT);

        this.basicSerialAggregateInPlaceByPerformance(words, SERIAL_RUN_COUNT);
        this.basicParallelAggregateInPlaceByPerformance(words, PARALLEL_RUN_COUNT);

        this.basicSerialAggregateByPerformance(words, SERIAL_RUN_COUNT);
        this.basicParallelAggregateByPerformance(words, PARALLEL_RUN_COUNT);
    }

    private double basicSerialSelectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Serial** Select: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(Iterate.select(iterable, predicateList.get(0), FastList.<Integer>newList()));
                Verify.assertNotEmpty(Iterate.select(iterable, predicateList.get(1), FastList.<Integer>newList()));
                Verify.assertNotEmpty(Iterate.select(iterable, predicateList.get(2), FastList.<Integer>newList()));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private String formatSizeOf(Iterable<?> iterable)
    {
        return NumberFormat.getInstance().format(Iterate.sizeOf(iterable));
    }

    private double basicParallelSelectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Parallel Select: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(ParallelIterate.select(
                        iterable,
                        predicateList.get(0),
                        new CompositeFastList<Integer>(),
                        true));
                Verify.assertNotEmpty(ParallelIterate.select(
                        iterable,
                        predicateList.get(1),
                        new CompositeFastList<Integer>(),
                        true));
                Verify.assertNotEmpty(ParallelIterate.select(
                        iterable,
                        predicateList.get(2),
                        new CompositeFastList<Integer>(),
                        true));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private String getSimpleName(Object collection)
    {
        return collection.getClass().getSimpleName();
    }

    private double basicSerialCountPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Serial** Count: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Assert.assertTrue(Iterate.count(iterable, predicateList.get(0)) > 0);
                Assert.assertTrue(Iterate.count(iterable, predicateList.get(1)) > 0);
                Assert.assertTrue(Iterate.count(iterable, predicateList.get(2)) > 0);
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicParallelCountPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Parallel Count: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Assert.assertTrue(ParallelIterate.count(iterable, predicateList.get(0)) > 0);
                Assert.assertTrue(ParallelIterate.count(iterable, predicateList.get(1)) > 0);
                Assert.assertTrue(ParallelIterate.count(iterable, predicateList.get(2)) > 0);
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicSerialRejectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Serial** Reject: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(Iterate.reject(iterable, predicateList.get(0), FastList.<Integer>newList()));
                Verify.assertNotEmpty(Iterate.reject(iterable, predicateList.get(1), FastList.<Integer>newList()));
                Verify.assertNotEmpty(Iterate.reject(iterable, predicateList.get(2), FastList.<Integer>newList()));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicParallelRejectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Parallel Reject: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(ParallelIterate.reject(
                        iterable,
                        predicateList.get(0),
                        new CompositeFastList<Integer>(),
                        true));
                Verify.assertNotEmpty(ParallelIterate.reject(
                        iterable,
                        predicateList.get(1),
                        new CompositeFastList<Integer>(),
                        true));
                Verify.assertNotEmpty(ParallelIterate.reject(
                        iterable,
                        predicateList.get(2),
                        new CompositeFastList<Integer>(),
                        true));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicParallelCollectIfPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicates,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Parallel CollectIf: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(ParallelIterate.collectIf(
                        iterable,
                        predicates.get(0),
                        PAIR_FUNCTION,
                        new CompositeFastList<Pair<Integer, Integer>>(),
                        true));
                Verify.assertNotEmpty(ParallelIterate.collectIf(
                        iterable,
                        predicates.get(1),
                        LONG_FUNCTION,
                        new CompositeFastList<Long>(),
                        true));
                Verify.assertNotEmpty(ParallelIterate.collectIf(
                        iterable,
                        predicates.get(0),
                        SHORT_FUNCTION,
                        new CompositeFastList<Short>(),
                        true));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicSerialCollectIfPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicates,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Serial** CollectIf: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(Iterate.collectIf(
                        iterable,
                        predicates.get(0),
                        PAIR_FUNCTION,
                        FastList.<Pair<Integer, Integer>>newList()));
                Verify.assertNotEmpty(Iterate.collectIf(
                        iterable,
                        predicates.get(1),
                        LONG_FUNCTION,
                        FastList.<Long>newList()));
                Verify.assertNotEmpty(Iterate.collectIf(
                        iterable,
                        predicates.get(2),
                        SHORT_FUNCTION,
                        FastList.<Short>newList()));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicSerialCollectPerformance(
            final Iterable<Integer> iterable,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Serial** Collect: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                int initialCapacity = Iterate.sizeOf(iterable);
                Verify.assertNotEmpty(Iterate.collect(
                        iterable,
                        PAIR_FUNCTION,
                        FastList.<Pair<Integer, Integer>>newList(initialCapacity)));
                Verify.assertNotEmpty(Iterate.collect(
                        iterable,
                        LONG_FUNCTION,
                        FastList.<Long>newList(initialCapacity)));
                Verify.assertNotEmpty(Iterate.collect(
                        iterable,
                        SHORT_FUNCTION,
                        FastList.<Short>newList(initialCapacity)));
            }
        }, count, 10, 1);
    }

    private double basicSerialGroupByPerformance(
            final Iterable<String> iterable,
            int count)
    {
        Assert.assertEquals(HashBagMultimap.newMultimap(ParallelIterate.groupBy(iterable, ALPHAGRAM_FUNCTION)),
                HashBagMultimap.newMultimap(Iterate.groupBy(iterable, ALPHAGRAM_FUNCTION)));
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Serial** GroupBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(Iterate.groupBy(
                        iterable,
                        ALPHAGRAM_FUNCTION));
            }
        }, count, 10, 1);
    }

    private double basicSerialAggregateInPlaceByPerformance(
            final Iterable<String> iterable,
            int count)
    {
        Assert.assertEquals(
                ParallelIterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT),
                Iterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT));
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Serial** AggregateInPlaceBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(
                        Iterate.aggregateInPlaceBy(
                                iterable,
                                ALPHAGRAM_FUNCTION,
                                AtomicIntegerWithEquals.NEW_INSTANCE,
                                AtomicIntegerWithEquals.INCREMENT));
            }
        }, count, 10, 1);
    }

    private double basicSerialAggregateByPerformance(
            final Iterable<String> iterable,
            int count)
    {
        Assert.assertEquals(
                ParallelIterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR),
                Iterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR));
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Serial** AggregateBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(
                        Iterate.aggregateBy(
                                iterable,
                                ALPHAGRAM_FUNCTION,
                                INTEGER_NEW,
                                COUNT_AGGREGATOR));
            }
        }, count, 10, 1);
    }

    private double basicParallelCollectPerformance(final Iterable<Integer> iterable, int count)
    {
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Parallel Collect: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(ParallelIterate.collect(
                        iterable,
                        PAIR_FUNCTION,
                        new CompositeFastList<Pair<Integer, Integer>>(),
                        true));
                Verify.assertNotEmpty(ParallelIterate.collect(
                        iterable,
                        LONG_FUNCTION,
                        new CompositeFastList<Long>(),
                        true));
                Verify.assertNotEmpty(ParallelIterate.collect(
                        iterable,
                        SHORT_FUNCTION,
                        new CompositeFastList<Short>(),
                        true));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicParallelGroupByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(HashBagMultimap.newMultimap(ParallelIterate.groupBy(iterable, ALPHAGRAM_FUNCTION)),
                HashBagMultimap.newMultimap(Iterate.groupBy(iterable, ALPHAGRAM_FUNCTION)));
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Parallel GroupBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(ParallelIterate.groupBy(
                        iterable,
                        ALPHAGRAM_FUNCTION));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicParallelAggregateInPlaceByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(
                ParallelIterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT),
                Iterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT));
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Parallel AggregateInPlaceBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(
                        ParallelIterate.aggregateInPlaceBy(
                                iterable,
                                ALPHAGRAM_FUNCTION,
                                AtomicIntegerWithEquals.NEW_INSTANCE,
                                AtomicIntegerWithEquals.INCREMENT));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
    }

    private double basicParallelAggregateByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(
                ParallelIterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR),
                Iterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR));
        return TimeKeeper.logAverageMillisecondsToRunInParallel("Parallel AggregateBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(
                        ParallelIterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR));
            }
        }, count, WARM_UP_COUNT, NUMBER_OF_USER_THREADS);
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
            return TimeKeeper.calcElapsedTime(start, end);
        }

        private static long calcElapsedTime(long start, long end)
        {
            return end - start;
        }

        private static long getCurrentTimeAsNanos()
        {
            return TimeKeeper.currentTimeNanoseconds();
        }

        private static void doLog(String message, int count, double total, double average)
        {
            System.out.println(message + " Count: " + count + " Total(ms): " + TimeKeeper.nanosToMillis(total) + " Avg(ms): " + TimeKeeper.nanosToMillis(average));
        }

        private static double logInParallel(String message, Runnable runnable, int count, int threads)
        {
            ExecutorService executor =
                    ParallelIterate.newPooledExecutor(threads, "logAverageMillisecondsToRunInParallel", true);
            try
            {
                long[] nanos = new long[count];
                long start = TimeKeeper.getCurrentTimeAsNanos();
                long runStart = start;
                for (int i = 0; i < count; i++)
                {
                    TimeKeeper.executeNumberOfTimes(runnable, threads, executor);
                    long end = TimeKeeper.getCurrentTimeAsNanos();
                    nanos[i] = end - runStart;
                    runStart = end;
                }
                long totalNanos = TimeKeeper.calcElapsedTime(start, TimeKeeper.getCurrentTimeAsNanos());
                double averageTime = (double) totalNanos / (double) count;
                TimeKeeper.doLog(message, count, (double) totalNanos, averageTime);
                return averageTime / (double) TimeKeeper.MILLIS_TO_NANOS;
            }
            finally
            {
                executor.shutdown();
            }
        }

        public static double logAverageMillisecondsToRunInParallel(
                String message,
                Runnable runnable,
                int count,
                int threads)
        {
            return TimeKeeper.logInParallel(message, runnable, count, threads);
        }

        private static void executeNumberOfTimes(final Runnable runnable, int threads, Executor executor)
        {
            try
            {
                final CountDownLatch latch = new CountDownLatch(threads);
                for (int j = 0; j < threads; j++)
                {
                    executor.execute(new Runnable()
                    {
                        public void run()
                        {
                            runnable.run();
                            latch.countDown();
                        }
                    });
                }
                latch.await();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }

        private static String nanosToMillis(double nanos)
        {
            return NumberFormat.getInstance().format(nanos / 1000000.0);
        }

        public static double logAverageMillisecondsToRunInParallel(
                String message,
                Runnable runnable,
                int count,
                int warmUpCount,
                int threads)
        {
            TimeKeeper.warmUp(warmUpCount, runnable);
            TimeKeeper.gcAndYield();
            return TimeKeeper.logAverageMillisecondsToRunInParallel(message, runnable, count, threads);
        }

        private static void gcAndYield()
        {
            System.gc();
            Thread.yield();
            System.gc();
            Thread.yield();
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
