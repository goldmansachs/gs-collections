/*
 * Copyright 2013 Goldman Sachs.
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
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.ParallelTests;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.forkjoin.FJIterate;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.CompositeFastList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
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
    public static final Predicate<Integer> PREDICATE_1 = Predicates.greaterThan(0).and(IntegerPredicates.isOdd());
    public static final Predicate<Integer> PREDICATE_2 = IntegerPredicates.isPositive().and(IntegerPredicates.isEven());
    public static final Predicate<Integer> PREDICATE_3 = IntegerPredicates.isOdd().and(IntegerPredicates.isNegative());
    public static final MutableList<Predicate<Integer>> PREDICATES = FastList.newListWith(PREDICATE_1, PREDICATE_2, PREDICATE_3);

    @Test
    @Category(ParallelTests.class)
    public void select()
    {
        this.measureAlgorithmForIntegerIterable("Select", new Procedure<Function0<Iterable<Integer>>>()
        {
            public void value(Function0<Iterable<Integer>> each)
            {
                SerialParallelPerformanceTest.this.select(each.value());
            }
        });
    }

    @Test
    @Category(ParallelTests.class)
    public void reject()
    {
        this.measureAlgorithmForIntegerIterable("Reject", new Procedure<Function0<Iterable<Integer>>>()
        {
            public void value(Function0<Iterable<Integer>> each)
            {
                SerialParallelPerformanceTest.this.reject(each.value());
            }
        });
    }

    @Test
    @Category(ParallelTests.class)
    public void count()
    {
        this.measureAlgorithmForIntegerIterable("Count", new Procedure<Function0<Iterable<Integer>>>()
        {
            public void value(Function0<Iterable<Integer>> each)
            {
                SerialParallelPerformanceTest.this.count(each.value());
            }
        });
    }

    @Test
    @Category(ParallelTests.class)
    public void collectIf()
    {
        this.measureAlgorithmForIntegerIterable("CollectIf", new Procedure<Function0<Iterable<Integer>>>()
        {
            public void value(Function0<Iterable<Integer>> each)
            {
                SerialParallelPerformanceTest.this.collectIf(each.value());
            }
        });
    }

    @Test
    @Category(ParallelTests.class)
    public void collect()
    {
        this.measureAlgorithmForIntegerIterable("Collect", new Procedure<Function0<Iterable<Integer>>>()
        {
            public void value(Function0<Iterable<Integer>> each)
            {
                SerialParallelPerformanceTest.this.collect(each.value());
            }
        });
    }

    @Test
    @Category(ParallelTests.class)
    public void groupBy()
    {
        this.measureAlgorithmForRandomStringIterable("GroupBy", new Procedure<Function0<Iterable<String>>>()
        {
            public void value(Function0<Iterable<String>> each)
            {
                SerialParallelPerformanceTest.this.groupBy(each.value());
            }
        });
    }

    @Test
    @Category(ParallelTests.class)
    public void aggregateBy()
    {
        this.measureAlgorithmForRandomStringIterable("AggregateBy", new Procedure<Function0<Iterable<String>>>()
        {
            public void value(Function0<Iterable<String>> each)
            {
                SerialParallelPerformanceTest.this.aggregateBy(each.value());
            }
        });
    }

    @Test
    @Category(ParallelTests.class)
    public void aggregateInPlaceBy()
    {
        this.measureAlgorithmForRandomStringIterable("AggregateInPlaceBy", new Procedure<Function0<Iterable<String>>>()
        {
            public void value(Function0<Iterable<String>> each)
            {
                SerialParallelPerformanceTest.this.aggregateInPlaceBy(each.value());
            }
        });
    }

    public MutableList<String> generateWordsList(int count)
    {
        final FastList<String> words = FastList.newList();
        Interval.oneTo(count).forEach(new IntProcedure()
        {
            public void value(int each)
            {
                words.add(RandomStringUtils.randomAlphabetic(2));
            }
        });
        return words;
    }

    public MutableSet<String> generateWordsSet(int count)
    {
        UnifiedSet<String> words = UnifiedSet.newSet();
        while (words.size() < count)
        {
            words.add(RandomStringUtils.randomAlphabetic(5));
        }
        return words;
    }

    @After
    public void tearDown()
    {
        SerialParallelPerformanceTest.forceGC();
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
        System.out.println("*** Algorithm: " + serialParallelAlgorithm);
        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Default Thread Pool Size: " + ParallelIterate.getDefaultMaxThreadPoolSize());
        System.out.println("Default Task Count: " + ParallelIterate.getDefaultTaskCount());
        System.out.println("Scale Factor: " + SCALE_FACTOR);
        System.out.println("Warm up count: " + WARM_UP_COUNT);
        System.out.println("Parallel Run Count: " + PARALLEL_RUN_COUNT);
        System.out.println("Serial** Run Count: " + SERIAL_RUN_COUNT);
    }

    private MutableList<Integer> getSizes()
    {
        MutableList<Integer> sizes = FastList.newListWith(LARGE_COUNT, MEDIUM_COUNT, SMALL_COUNT);
        Collections.shuffle(sizes);
        return sizes;
    }

    private MutableList<Function0<Iterable<Integer>>> getIntegerListGenerators(int count)
    {
        final Interval interval = Interval.fromTo(-(count / 2), count / 2 - 1);
        MutableList<Function0<Iterable<Integer>>> generators = FastList.newList();
        generators.add(new Function0<Iterable<Integer>>()
        {
            public Iterable<Integer> value()
            {
                MutableList<Integer> integers = interval.toList();
                Collections.shuffle(integers);
                return integers;
            }
        });
        generators.add(new Function0<Iterable<Integer>>()
        {
            public Iterable<Integer> value()
            {
                MutableList<Integer> integers = interval.toList();
                Collections.shuffle(integers);
                return integers.toImmutable();
            }
        });
        generators.add(new Function0<Iterable<Integer>>()
        {
            public Iterable<Integer> value()
            {
                return interval.toSet();
            }
        });
        Collections.shuffle(generators);
        return generators;
    }

    private MutableList<Function0<Iterable<String>>> getRandomWordsGenerators(final int count)
    {
        MutableList<Function0<Iterable<String>>> generators = FastList.newList();
        generators.add(new Function0<Iterable<String>>()
        {
            public Iterable<String> value()
            {
                return SerialParallelPerformanceTest.this.generateWordsList(count);
            }
        });
        generators.add(new Function0<Iterable<String>>()
        {
            public Iterable<String> value()
            {
                return SerialParallelPerformanceTest.this.generateWordsList(count).toImmutable();
            }
        });
        generators.add(new Function0<Iterable<String>>()
        {
            public Iterable<String> value()
            {
                return SerialParallelPerformanceTest.this.generateWordsSet(count);
            }
        });
        Collections.shuffle(generators);
        return generators;
    }

    private void measureAlgorithmForIntegerIterable(String algorithmName, final Procedure<Function0<Iterable<Integer>>> algorithm)
    {
        this.printMachineAndTestConfiguration(algorithmName);
        for (int i = 0; i < 4; i++)
        {
            this.getSizes().forEach(new Procedure<Integer>()
            {
                public void value(Integer count)
                {
                    SerialParallelPerformanceTest.this.getIntegerListGenerators(count).forEach(algorithm);
                }
            });
        }
    }

    private void measureAlgorithmForRandomStringIterable(String algorithmName, final Procedure<Function0<Iterable<String>>> algorithm)
    {
        this.printMachineAndTestConfiguration(algorithmName);
        for (int i = 0; i < 4; i++)
        {
            this.getSizes().forEach(new Procedure<Integer>()
            {
                public void value(Integer count)
                {
                    SerialParallelPerformanceTest.this.getRandomWordsGenerators(count).forEach(algorithm);
                }
            });
        }
    }

    private void aggregateBy(final Iterable<String> words)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicSerialAggregateByPerformance(words, SERIAL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicParallelAggregateByPerformance(words, PARALLEL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicForkJoinAggregateByPerformance(words, PARALLEL_RUN_COUNT);
            }
        });
        this.shuffleAndRun(runnables);
    }

    private void aggregateInPlaceBy(final Iterable<String> words)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicSerialAggregateInPlaceByPerformance(words, SERIAL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicParallelAggregateInPlaceByPerformance(words, PARALLEL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicForkJoinAggregateInPlaceByPerformance(words, PARALLEL_RUN_COUNT);
            }
        });
        this.shuffleAndRun(runnables);
    }

    private void groupBy(final Iterable<String> words)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicSerialGroupByPerformance(words, SERIAL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicParallelGroupByPerformance(words, PARALLEL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicForkJoinGroupByPerformance(words, PARALLEL_RUN_COUNT);
            }
        });
        this.shuffleAndRun(runnables);
    }

    private void collect(final Iterable<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicSerialCollectPerformance(collection, SERIAL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicParallelCollectPerformance(collection, PARALLEL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicForkJoinCollectPerformance(collection, PARALLEL_RUN_COUNT);
            }
        });
        this.shuffleAndRun(runnables);
    }

    private void collectIf(final Iterable<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicSerialCollectIfPerformance(collection, PREDICATES, SERIAL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicParallelCollectIfPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicForkJoinCollectIfPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT);
            }
        });
        this.shuffleAndRun(runnables);
    }

    private void count(final Iterable<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicSerialCountPerformance(collection, PREDICATES, SERIAL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicParallelCountPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicForkJoinCountPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT);
            }
        });
        this.shuffleAndRun(runnables);
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

    private void reject(final Iterable<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicSerialRejectPerformance(collection, PREDICATES, SERIAL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicParallelRejectPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicForkJoinRejectPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT);
            }
        });
        this.shuffleAndRun(runnables);
    }

    private void select(final Iterable<Integer> collection)
    {
        MutableList<Runnable> runnables = FastList.newList();
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicSerialSelectPerformance(collection, PREDICATES, SERIAL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicParallelSelectPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT);
            }
        });
        runnables.add(new Runnable()
        {
            public void run()
            {
                SerialParallelPerformanceTest.this.basicForkJoinSelectPerformance(collection, PREDICATES, PARALLEL_RUN_COUNT);
            }
        });
        this.shuffleAndRun(runnables);
    }

    private double basicSerialSelectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial** Select: "
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
        }, count, WARM_UP_COUNT);
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
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Select: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicForkJoinSelectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("ForkJoin Select: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(FJIterate.select(
                        iterable,
                        predicateList.get(0),
                        new CompositeFastList<Integer>(),
                        true));
                Verify.assertNotEmpty(FJIterate.select(
                        iterable,
                        predicateList.get(1),
                        new CompositeFastList<Integer>(),
                        true));
                Verify.assertNotEmpty(FJIterate.select(
                        iterable,
                        predicateList.get(2),
                        new CompositeFastList<Integer>(),
                        true));
            }
        }, count, WARM_UP_COUNT);
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
        return TimeKeeper.logAverageMillisecondsToRun("Serial** Count: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicParallelCountPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Count: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicForkJoinCountPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("ForkJoin Count: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Assert.assertTrue(FJIterate.count(iterable, predicateList.get(0)) > 0);
                Assert.assertTrue(FJIterate.count(iterable, predicateList.get(1)) > 0);
                Assert.assertTrue(FJIterate.count(iterable, predicateList.get(2)) > 0);
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicSerialRejectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial** Reject: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicParallelRejectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Reject: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicForkJoinRejectPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicateList,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("ForkJoin Reject: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(FJIterate.reject(
                        iterable,
                        predicateList.get(0),
                        new CompositeFastList<Integer>(),
                        true));
                Verify.assertNotEmpty(FJIterate.reject(
                        iterable,
                        predicateList.get(1),
                        new CompositeFastList<Integer>(),
                        true));
                Verify.assertNotEmpty(FJIterate.reject(
                        iterable,
                        predicateList.get(2),
                        new CompositeFastList<Integer>(),
                        true));
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicParallelCollectIfPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicates,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Parallel CollectIf: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicForkJoinCollectIfPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicates,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("ForkJoin CollectIf: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(FJIterate.collectIf(
                        iterable,
                        predicates.get(0),
                        PAIR_FUNCTION,
                        new CompositeFastList<Pair<Integer, Integer>>(),
                        true));
                Verify.assertNotEmpty(FJIterate.collectIf(
                        iterable,
                        predicates.get(1),
                        LONG_FUNCTION,
                        new CompositeFastList<Long>(),
                        true));
                Verify.assertNotEmpty(FJIterate.collectIf(
                        iterable,
                        predicates.get(0),
                        SHORT_FUNCTION,
                        new CompositeFastList<Short>(),
                        true));
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicSerialCollectIfPerformance(
            final Iterable<Integer> iterable,
            final MutableList<Predicate<Integer>> predicates,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial** CollectIf: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicSerialCollectPerformance(
            final Iterable<Integer> iterable,
            int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Serial** Collect: "
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
        }, count, 10);
    }

    private double basicSerialGroupByPerformance(
            final Iterable<String> iterable,
            int count)
    {
        Assert.assertEquals(HashBagMultimap.newMultimap(ParallelIterate.groupBy(iterable, ALPHAGRAM_FUNCTION)),
                HashBagMultimap.newMultimap(Iterate.groupBy(iterable, ALPHAGRAM_FUNCTION)));
        return TimeKeeper.logAverageMillisecondsToRun("Serial** GroupBy: "
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
        }, count, 10);
    }

    private double basicSerialAggregateInPlaceByPerformance(
            final Iterable<String> iterable,
            int count)
    {
        Assert.assertEquals(
                ParallelIterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT),
                Iterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT));
        return TimeKeeper.logAverageMillisecondsToRun("Serial** AggregateInPlaceBy: "
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
        }, count, 10);
    }

    private double basicSerialAggregateByPerformance(
            final Iterable<String> iterable,
            int count)
    {
        Assert.assertEquals(
                ParallelIterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR),
                Iterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR));
        return TimeKeeper.logAverageMillisecondsToRun("Serial** AggregateBy: "
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
        }, count, 10);
    }

    private double basicParallelCollectPerformance(final Iterable<Integer> iterable, int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("Parallel Collect: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicForkJoinCollectPerformance(final Iterable<Integer> iterable, int count)
    {
        return TimeKeeper.logAverageMillisecondsToRun("ForkJoin Collect: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(FJIterate.collect(
                        iterable,
                        PAIR_FUNCTION,
                        new CompositeFastList<Pair<Integer, Integer>>(),
                        true));
                Verify.assertNotEmpty(FJIterate.collect(
                        iterable,
                        LONG_FUNCTION,
                        new CompositeFastList<Long>(),
                        true));
                Verify.assertNotEmpty(FJIterate.collect(
                        iterable,
                        SHORT_FUNCTION,
                        new CompositeFastList<Short>(),
                        true));
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicParallelGroupByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(HashBagMultimap.newMultimap(ParallelIterate.groupBy(iterable, ALPHAGRAM_FUNCTION)),
                HashBagMultimap.newMultimap(Iterate.groupBy(iterable, ALPHAGRAM_FUNCTION)));
        return TimeKeeper.logAverageMillisecondsToRun("Parallel GroupBy: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicForkJoinGroupByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(HashBagMultimap.newMultimap(FJIterate.groupBy(iterable, ALPHAGRAM_FUNCTION)),
                HashBagMultimap.newMultimap(Iterate.groupBy(iterable, ALPHAGRAM_FUNCTION)));
        return TimeKeeper.logAverageMillisecondsToRun("ForkJoin GroupBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(FJIterate.groupBy(
                        iterable,
                        ALPHAGRAM_FUNCTION));
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicParallelAggregateInPlaceByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(
                ParallelIterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT),
                Iterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT));
        return TimeKeeper.logAverageMillisecondsToRun("Parallel AggregateInPlaceBy: "
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
        }, count, WARM_UP_COUNT);
    }

    private double basicForkJoinAggregateInPlaceByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(
                FJIterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT),
                Iterate.aggregateInPlaceBy(iterable, ALPHAGRAM_FUNCTION, AtomicIntegerWithEquals.NEW_INSTANCE, AtomicIntegerWithEquals.INCREMENT));
        return TimeKeeper.logAverageMillisecondsToRun("ForkJoin AggregateInPlaceBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(
                        FJIterate.aggregateInPlaceBy(
                                iterable,
                                ALPHAGRAM_FUNCTION,
                                AtomicIntegerWithEquals.NEW_INSTANCE,
                                AtomicIntegerWithEquals.INCREMENT));
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicParallelAggregateByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(
                ParallelIterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR),
                Iterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR));
        return TimeKeeper.logAverageMillisecondsToRun("Parallel AggregateBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(
                        ParallelIterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR));
            }
        }, count, WARM_UP_COUNT);
    }

    private double basicForkJoinAggregateByPerformance(final Iterable<String> iterable, int count)
    {
        Assert.assertEquals(
                FJIterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR),
                Iterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR));
        return TimeKeeper.logAverageMillisecondsToRun("ForkJoin AggregateBy: "
                + this.getSimpleName(iterable)
                + " size: "
                + this.formatSizeOf(iterable), new Runnable()
        {
            public void run()
            {
                Verify.assertNotEmpty(
                        FJIterate.aggregateBy(iterable, ALPHAGRAM_FUNCTION, INTEGER_NEW, COUNT_AGGREGATOR));
            }
        }, count, WARM_UP_COUNT);
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
            System.out.println(message + " Count: " + count + " Total(ms): " + TimeKeeper.longNanosToMillisString(total) + " Avg(ms): " + TimeKeeper.doubleNanosToMillisString(average));
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
            SerialParallelPerformanceTest.forceGC();
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
