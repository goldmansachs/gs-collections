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

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.api.map.primitive.ObjectDoubleMap;
import com.gs.collections.impl.jmh.domain.Account;
import com.gs.collections.impl.jmh.domain.Position;
import com.gs.collections.impl.jmh.domain.Positions;
import com.gs.collections.impl.jmh.domain.Product;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.parallel.ParallelIterate;
import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SumByDoubleTest extends AbstractJMHTestRunner
{
    private final Positions positions = new Positions().shuffle();

    @Benchmark
    public Map<Product, Double> sumByProduct_serial_lazy_jdk()
    {
        return this.positions.getJdkPositions().stream().collect(
                Collectors.groupingBy(
                        Position::getProduct,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<Product, Double> sumByProduct_serial_lazy_streams_gsc()
    {
        return this.positions.getGscPositions().stream().collect(
                Collectors.groupingBy(
                        Position::getProduct,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<Account, Double> sumByAccount_serial_lazy_jdk()
    {
        return this.positions.getJdkPositions().stream().collect(
                Collectors.groupingBy(
                        Position::getAccount,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<Account, Double> sumByAccount_serial_lazy_streams_gsc()
    {
        return this.positions.getGscPositions().stream().collect(
                Collectors.groupingBy(
                        Position::getAccount,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<String, Double> sumByCategory_serial_lazy_jdk()
    {
        return this.positions.getJdkPositions().stream().collect(
                Collectors.groupingBy(
                        Position::getCategory,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<String, Double> sumByCategory_serial_lazy_streams_gsc()
    {
        return this.positions.getGscPositions().stream().collect(
                Collectors.groupingBy(
                        Position::getCategory,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<Product, Double> sumByProduct_parallel_lazy_jdk()
    {
        return this.positions.getJdkPositions().parallelStream().collect(
                Collectors.groupingBy(
                        Position::getProduct,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<Product, Double> sumByProduct_parallel_lazy_streams_gsc()
    {
        return this.positions.getGscPositions().parallelStream().collect(
                Collectors.groupingBy(
                        Position::getProduct,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<Account, Double> sumByAccount_parallel_lazy_jdk()
    {
        return this.positions.getJdkPositions().parallelStream().collect(
                Collectors.groupingBy(
                        Position::getAccount,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<Account, Double> sumByAccount_parallel_lazy_streams_gsc()
    {
        return this.positions.getGscPositions().parallelStream().collect(
                Collectors.groupingBy(
                        Position::getAccount,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<String, Double> sumByCategory_parallel_lazy_jdk()
    {
        return this.positions.getJdkPositions().parallelStream().collect(
                Collectors.groupingBy(
                        Position::getCategory,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public Map<String, Double> sumByCategory_parallel_lazy_streams_gsc()
    {
        return this.positions.getGscPositions().parallelStream().collect(
                Collectors.groupingBy(
                        Position::getCategory,
                        Collectors.summingDouble(Position::getMarketValue)));
    }

    @Benchmark
    public ObjectDoubleMap<Product> sumByProduct_serial_eager_gsc()
    {
        return this.positions.getGscPositions().sumByDouble(Position::getProduct, Position::getMarketValue);
    }

    @Benchmark
    public ObjectDoubleMap<Product> sumByProduct_parallel_eager_gsc()
    {
        return ParallelIterate.sumByDouble(this.positions.getGscPositions(), Position::getProduct, Position::getMarketValue);
    }

    @Test
    public void sumByProduct_gsc()
    {
        Assert.assertArrayEquals(
                this.sumByProduct_parallel_eager_gsc().values().toSortedArray(),
                this.sumByProduct_serial_eager_gsc().values().toSortedArray(),
                0.001);
        Assert.assertEquals(
                this.sumByProduct_parallel_eager_gsc(),
                this.sumByProduct_serial_eager_gsc());
    }

    @Benchmark
    public ObjectDoubleMap<Account> sumByAccount_serial_eager_gsc()
    {
        return this.positions.getGscPositions().sumByDouble(Position::getAccount, Position::getMarketValue);
    }

    @Benchmark
    public ObjectDoubleMap<Account> sumByAccount_parallel_eager_gsc()
    {
        return ParallelIterate.sumByDouble(this.positions.getGscPositions(), Position::getAccount, Position::getMarketValue);
    }

    @Test
    public void sumByAccount_gsc()
    {
        Assert.assertArrayEquals(
                this.sumByAccount_parallel_eager_gsc().values().toSortedArray(),
                this.sumByAccount_serial_eager_gsc().values().toSortedArray(),
                0.001);
        Assert.assertEquals(
                this.sumByAccount_parallel_eager_gsc(),
                this.sumByAccount_serial_eager_gsc());
    }

    @Benchmark
    public ObjectDoubleMap<String> sumByCategory_serial_eager_gsc()
    {
        return this.positions.getGscPositions().sumByDouble(Position::getCategory, Position::getMarketValue);
    }

    @Benchmark
    public ObjectDoubleMap<String> sumByCategory_parallel_eager_gsc()
    {
        return ParallelIterate.sumByDouble(this.positions.getGscPositions(), Position::getCategory, Position::getMarketValue);
    }

    @Test
    public void sumByCategory_gsc()
    {
        Assert.assertArrayEquals(
                this.sumByCategory_parallel_eager_gsc().values().toSortedArray(),
                this.sumByCategory_serial_eager_gsc().values().toSortedArray(),
                0.001);
        Assert.assertEquals(
                this.sumByCategory_parallel_eager_gsc(),
                this.sumByCategory_serial_eager_gsc());
    }
}
