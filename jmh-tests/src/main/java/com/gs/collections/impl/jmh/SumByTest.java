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
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.map.primitive.ObjectDoubleMap;
import com.gs.collections.api.set.Pool;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.parallel.ParallelIterate;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
public class SumByTest
{
    private static final int SIZE = 1_000_000;
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final PrimitiveIterator.OfInt INTS = RANDOM.ints(1, 10).iterator();
    private static final PrimitiveIterator.OfDouble DOUBLES = RANDOM.ints(1, 100).asDoubleStream().iterator();
    private final Pool<Account> accountPool = UnifiedSet.newSet();
    private final Pool<Product> productPool = UnifiedSet.newSet();
    private final Pool<String> categoryPool = UnifiedSet.newSet();
    private final FastList<Position> gscPositions = FastList.newWithNValues(SIZE, Position::new);
    private final ArrayList<Position> jdkPositions = new ArrayList<>(this.gscPositions);

    @Before
    @Setup(Level.Iteration)
    public void setUp()
    {
        Collections.shuffle(this.gscPositions);
        Collections.shuffle(this.jdkPositions);
    }

    @After
    @TearDown(Level.Iteration)
    public void tearDown() throws InterruptedException
    {
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public Map<Product, Double> sumByProduct_serial_lazy_jdk()
    {
        Map<Product, Double> result =
                this.jdkPositions.stream().collect(
                        Collectors.groupingBy(
                                Position::getProduct,
                                Collectors.summingDouble(Position::getMarketValue)));
        Assert.assertNotNull(result);
        return result;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public Map<Account, Double> sumByAccount_serial_lazy_jdk()
    {
        Map<Account, Double> accountDoubleMap =
                this.jdkPositions.stream().collect(
                        Collectors.groupingBy(
                                Position::getAccount,
                                Collectors.summingDouble(Position::getMarketValue)));
        Assert.assertNotNull(accountDoubleMap);
        return accountDoubleMap;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public Map<String, Double> sumByCategory_serial_lazy_jdk()
    {
        Map<String, Double> categoryDoubleMap =
                this.jdkPositions.stream().collect(
                        Collectors.groupingBy(
                                Position::getCategory,
                                Collectors.summingDouble(Position::getMarketValue)));
        Assert.assertNotNull(categoryDoubleMap);
        return categoryDoubleMap;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public Map<Product, Double> sumByProduct_parallel_lazy_jdk()
    {
        Map<Product, Double> result =
                this.jdkPositions.parallelStream().collect(
                        Collectors.groupingBy(
                                Position::getProduct,
                                Collectors.summingDouble(Position::getMarketValue)));
        Assert.assertNotNull(result);
        return result;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public Map<Account, Double> sumByAccount_parallel_lazy_jdk()
    {
        Map<Account, Double> result =
                this.jdkPositions.parallelStream().collect(
                        Collectors.groupingBy(
                                Position::getAccount,
                                Collectors.summingDouble(Position::getMarketValue)));
        Assert.assertNotNull(result);
        return result;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public Map<String, Double> sumByCategory_parallel_lazy_jdk()
    {
        Map<String, Double> result =
                this.jdkPositions.parallelStream().collect(
                        Collectors.groupingBy(
                                Position::getCategory,
                                Collectors.summingDouble(Position::getMarketValue)));
        Assert.assertNotNull(result);
        return result;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public ObjectDoubleMap<Product> sumByProduct_serial_eager_gsc()
    {
        ObjectDoubleMap<Product> result =
                this.gscPositions.sumByDouble(Position::getProduct, Position::getMarketValue);
        Assert.assertNotNull(result);
        return result;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public ObjectDoubleMap<Product> sumByProduct_parallel_eager_gsc()
    {
        ObjectDoubleMap<Product> result =
                ParallelIterate.sumByDouble(this.gscPositions, Position::getProduct, Position::getMarketValue);
        Assert.assertNotNull(result);
        return result;
    }

    @Test
    public void test_sumByProduct_gsc()
    {
        Assert.assertArrayEquals(
                this.sumByProduct_parallel_eager_gsc().values().toSortedArray(),
                this.sumByProduct_serial_eager_gsc().values().toSortedArray(),
                0.001);
        Assert.assertEquals(
                this.sumByProduct_parallel_eager_gsc(),
                this.sumByProduct_serial_eager_gsc());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public ObjectDoubleMap<Account> sumByAccount_serial_eager_gsc()
    {
        ObjectDoubleMap<Account> result =
                this.gscPositions.sumByDouble(Position::getAccount, Position::getMarketValue);
        Assert.assertNotNull(result);
        return result;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public ObjectDoubleMap<Account> sumByAccount_parallel_eager_gsc()
    {
        ObjectDoubleMap<Account> result =
                ParallelIterate.sumByDouble(this.gscPositions, Position::getAccount, Position::getMarketValue);
        Assert.assertNotNull(result);
        return result;
    }

    @Test
    public void test_sumByAccount_gsc()
    {
        Assert.assertArrayEquals(
                this.sumByAccount_parallel_eager_gsc().values().toSortedArray(),
                this.sumByAccount_serial_eager_gsc().values().toSortedArray(),
                0.001);
        Assert.assertEquals(
                this.sumByAccount_parallel_eager_gsc(),
                this.sumByAccount_serial_eager_gsc());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public ObjectDoubleMap<String> sumByCategory_serial_eager_gsc()
    {
        ObjectDoubleMap<String> result =
                this.gscPositions.sumByDouble(Position::getCategory, Position::getMarketValue);
        Assert.assertNotNull(result);
        return result;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @GenerateMicroBenchmark
    public ObjectDoubleMap<String> sumByCategory_parallel_eager_gsc()
    {
        ObjectDoubleMap<String> result =
                ParallelIterate.sumByDouble(this.gscPositions, Position::getCategory, Position::getMarketValue);
        Assert.assertNotNull(result);
        return result;
    }

    @Test
    public void test_sumByCategory_gsc()
    {
        Assert.assertArrayEquals(
                this.sumByCategory_parallel_eager_gsc().values().toSortedArray(),
                this.sumByCategory_serial_eager_gsc().values().toSortedArray(),
                0.001);
        Assert.assertEquals(
                this.sumByCategory_parallel_eager_gsc(),
                this.sumByCategory_serial_eager_gsc());
    }

    private final class Position
    {
        private final Account account = SumByTest.this.accountPool.put(new Account());
        private final Product product = SumByTest.this.productPool.put(new Product());
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

    private static final class Account
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
        private final String category = SumByTest.this.categoryPool.put(RandomStringUtils.randomAlphabetic(1).toUpperCase());
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

        @Override
        public String toString()
        {
            return "Product{" +
                    "name='" + this.name + '\'' +
                    ", category='" + this.category + '\'' +
                    ", price=" + this.price +
                    '}';
        }
    }
}
