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

import java.util.ArrayList;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.Pool;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class AggregateByTest
{
    private static final int SIZE = 1_000_000;
    private static final int BATCH_SIZE = 10_000;
    private static final PrimitiveIterator.OfInt INTS = new Random(System.currentTimeMillis()).ints(1, 10).iterator();
    private static final PrimitiveIterator.OfDouble DOUBLES = new Random(System.currentTimeMillis()).doubles(1.0d, 100.0d).iterator();
    private final Pool<Account> accountPool = UnifiedSet.newSet();
    private final Pool<Product> productPool = UnifiedSet.newSet();
    private final Pool<String> categoryPool = UnifiedSet.newSet();
    private final FastList<Position> gscPositions = FastList.newWithNValues(SIZE, Position::new);
    private final ArrayList<Position> jdkPositions = new ArrayList<>(this.gscPositions);

    private ExecutorService executorService;

    @Setup(Level.Iteration)
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Collections.shuffle(this.gscPositions);
        Collections.shuffle(this.jdkPositions);
    }

    @TearDown(Level.Iteration)
    public void tearDown() throws InterruptedException
    {
        this.executorService.shutdownNow();
        this.executorService.awaitTermination(1L, TimeUnit.SECONDS);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByProduct_serial_lazy_jdk()
    {
        Map<Product, DoubleSummaryStatistics> productDoubleMap =
                this.jdkPositions.stream().collect(
                        Collectors.groupingBy(
                                Position::getProduct,
                                Collectors.summarizingDouble(Position::getMarketValue)));
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByAccount_serial_lazy_jdk()
    {
        Map<Account, DoubleSummaryStatistics> accountDoubleMap =
                this.jdkPositions.stream().collect(
                        Collectors.groupingBy(
                                Position::getAccount,
                                Collectors.summarizingDouble(Position::getMarketValue)));
        Assert.assertNotNull(accountDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByCategory_serial_lazy_jdk()
    {
        Map<String, DoubleSummaryStatistics> categoryDoubleMap =
                this.jdkPositions.stream().collect(
                        Collectors.groupingBy(
                                Position::getCategory,
                                Collectors.summarizingDouble(Position::getMarketValue)));
        Assert.assertNotNull(categoryDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByProduct_Parallel_lazy_jdk()
    {
        Map<Product, DoubleSummaryStatistics> productDoubleMap =
                this.jdkPositions.parallelStream().collect(
                        Collectors.groupingBy(
                                Position::getProduct,
                                Collectors.summarizingDouble(Position::getMarketValue)));
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByAccount_Parallel_lazy_jdk()
    {
        Map<Account, DoubleSummaryStatistics> productDoubleMap =
                this.jdkPositions.parallelStream().collect(
                        Collectors.groupingBy(
                                Position::getAccount,
                                Collectors.summarizingDouble(Position::getMarketValue)));
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByCategory_Parallel_lazy_jdk()
    {
        Map<String, DoubleSummaryStatistics> productDoubleMap =
                this.jdkPositions.parallelStream().collect(
                        Collectors.groupingBy(Position::getCategory, Collectors.summarizingDouble(Position::getMarketValue)));
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByProduct_serial_eager_gsc()
    {
        MutableMap<Product, MarketValueStatistics> productDoubleMap =
                this.gscPositions.aggregateBy(
                        Position::getProduct,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByAccount_serial_eager_gsc()
    {
        MutableMap<Account, MarketValueStatistics> productDoubleMap =
                this.gscPositions.aggregateBy(
                        Position::getAccount,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByCategory_serial_eager_gsc()
    {
        MutableMap<String, MarketValueStatistics> productDoubleMap =
                this.gscPositions.aggregateBy(
                        Position::getCategory,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByProduct_parallel_eager_gsc()
    {
        MutableMap<Product, MarketValueStatistics> productDoubleMap =
                ParallelIterate.aggregateBy(this.gscPositions,
                        Position::getProduct,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByAccount_parallel_eager_gsc()
    {
        MutableMap<Account, MarketValueStatistics> productDoubleMap =
                ParallelIterate.aggregateBy(this.gscPositions,
                        Position::getAccount,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByCategory_parallel_eager_gsc()
    {
        MutableMap<String, MarketValueStatistics> productDoubleMap =
                ParallelIterate.aggregateBy(this.gscPositions,
                        Position::getCategory,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByProduct_serial_lazy_gsc()
    {
        MapIterable<Product, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asLazy().aggregateBy(
                        Position::getProduct,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByAccount_serial_lazy_gsc()
    {
        MapIterable<Account, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asLazy().aggregateBy(
                        Position::getAccount,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByCategory_serial_lazy_gsc()
    {
        MapIterable<String, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asLazy().aggregateBy(
                        Position::getCategory,
                        MarketValueStatistics::new,
                        MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByProduct_parallel_lazy_gsc()
    {
        MapIterable<Product, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asParallel(this.executorService, 10_000)
                        .aggregateBy(
                                Position::getProduct,
                                MarketValueStatistics::new,
                                MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByAccount_parallel_lazy_gsc()
    {
        MapIterable<Account, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asParallel(this.executorService, 10_000)
                        .aggregateBy(
                                Position::getAccount,
                                MarketValueStatistics::new,
                                MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateByCategory_parallel_lazy_gsc()
    {
        MapIterable<String, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asParallel(this.executorService, 10_000)
                        .aggregateBy(
                                Position::getCategory,
                                MarketValueStatistics::new,
                                MarketValueStatistics::acceptThis);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByProduct_serial_eager_gsc()
    {
        MutableMap<Product, MarketValueStatistics> productDoubleMap =
                this.gscPositions.aggregateInPlaceBy(
                        Position::getProduct,
                        MarketValueStatistics::new,
                        MarketValueStatistics::accept);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByAccount_serial_eager_gsc()
    {
        MutableMap<Account, MarketValueStatistics> productDoubleMap =
                this.gscPositions.aggregateInPlaceBy(
                        Position::getAccount,
                        MarketValueStatistics::new,
                        MarketValueStatistics::accept);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByCategory_serial_eager_gsc()
    {
        MutableMap<String, MarketValueStatistics> productDoubleMap =
                this.gscPositions.aggregateInPlaceBy(
                        Position::getCategory,
                        MarketValueStatistics::new,
                        MarketValueStatistics::accept);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByProduct_parallel_eager_gsc()
    {
        MutableMap<Product, MarketValueStatistics> productDoubleMap =
                ParallelIterate.aggregateInPlaceBy(this.gscPositions,
                        Position::getProduct,
                        MarketValueStatistics::new,
                        MarketValueStatistics::syncAccept);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByAccount_parallel_eager_gsc()
    {
        MutableMap<Account, MarketValueStatistics> productDoubleMap =
                ParallelIterate.aggregateInPlaceBy(this.gscPositions,
                        Position::getAccount,
                        MarketValueStatistics::new,
                        MarketValueStatistics::syncAccept);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByCategory_parallel_eager_gsc()
    {
        MutableMap<String, MarketValueStatistics> productDoubleMap =
                ParallelIterate.aggregateInPlaceBy(this.gscPositions,
                        Position::getCategory,
                        MarketValueStatistics::new,
                        MarketValueStatistics::syncAccept);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByProduct_parallel_lazy_gsc()
    {
        MapIterable<Product, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asParallel(this.executorService, 10_000)
                        .aggregateInPlaceBy(
                                Position::getProduct,
                                MarketValueStatistics::new,
                                MarketValueStatistics::syncAccept);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByAccount_parallel_lazy_gsc()
    {
        MapIterable<Account, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asParallel(this.executorService, 10_000)
                        .aggregateInPlaceBy(
                                Position::getAccount,
                                MarketValueStatistics::new,
                                MarketValueStatistics::syncAccept);
        Assert.assertNotNull(productDoubleMap);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public void aggregateInPlaceByCategory_parallel_lazy_gsc()
    {
        MapIterable<String, MarketValueStatistics> productDoubleMap =
                this.gscPositions.asParallel(this.executorService, 10_000)
                        .aggregateInPlaceBy(
                                Position::getCategory,
                                MarketValueStatistics::new,
                                MarketValueStatistics::syncAccept);
        Assert.assertNotNull(productDoubleMap);
    }

    private static final class MarketValueStatistics extends DoubleSummaryStatistics
    {
        public void accept(Position position)
        {
            this.accept(position.getMarketValue());
        }

        public MarketValueStatistics acceptThis(Position position)
        {
            this.accept(position.getMarketValue());
            return this;
        }

        public synchronized void syncAccept(Position position)
        {
            this.accept(position);
        }
    }

    private final class Position
    {
        private final Account account = AggregateByTest.this.accountPool.put(new Account());
        private final Product product = AggregateByTest.this.productPool.put(new Product());
        private final int quantity = INTS.nextInt();

        public Account getAccount()
        {
            return this.account;
        }

        public Product getProduct()
        {
            return this.product;
        }

        public String getCategory()
        {
            return this.product.getCategory();
        }

        public int getQuantity()
        {
            return this.quantity;
        }

        public double getMarketValue()
        {
            return this.quantity * this.product.getPrice();
        }
    }

    private final class Account
    {
        private final String name = RandomStringUtils.randomNumeric(5);

        public String getName()
        {
            return this.name;
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

            Account account = (Account) o;

            return this.name.equals(account.name);
        }

        @Override
        public int hashCode()
        {
            return this.name.hashCode();
        }
    }

    private final class Product
    {
        private final String name = RandomStringUtils.randomNumeric(3);
        private final String category = AggregateByTest.this.categoryPool.put(RandomStringUtils.randomAlphabetic(1).toUpperCase());
        private final double price = DOUBLES.nextDouble();

        public String getName()
        {
            return this.name;
        }

        public double getPrice()
        {
            return this.price;
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

            Product account = (Product) o;

            return this.name.equals(account.name);
        }

        public String getCategory()
        {
            return this.category;
        }

        @Override
        public int hashCode()
        {
            return this.name.hashCode();
        }
    }
}
