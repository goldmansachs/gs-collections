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

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.jmh.domain.Account;
import com.gs.collections.impl.jmh.domain.Position;
import com.gs.collections.impl.jmh.domain.Positions;
import com.gs.collections.impl.jmh.domain.Product;
import com.gs.collections.impl.parallel.ParallelIterate;
import com.gs.collections.impl.utility.Iterate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
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
public class SumByBigDecimalTest
{
    private final Positions positions = new Positions().shuffle();

    @Before
    @Setup(Level.Iteration)
    public void setUp()
    {
        this.positions.shuffle();
    }

    @After
    @TearDown(Level.Iteration)
    public void tearDown() throws InterruptedException
    {
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public MutableMap<Product, BigDecimal> sumByBigDecimalProduct_serial_eager_gsc()
    {
        return Iterate.sumByBigDecimal(this.positions.getGscPositions(), Position::getProduct, Position::getPreciseMarketValue);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public MutableMap<Product, BigDecimal> sumByBigDecimalProduct_parallel_eager_gsc()
    {
        return ParallelIterate.sumByBigDecimal(this.positions.getGscPositions(), Position::getProduct, Position::getPreciseMarketValue);
    }

    @Test
    public void sumByProduct_gsc()
    {
        Assert.assertEquals(
                this.sumByBigDecimalProduct_parallel_eager_gsc(),
                this.sumByBigDecimalProduct_serial_eager_gsc());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public MutableMap<Account, BigDecimal> sumByBigDecimalAccount_serial_eager_gsc()
    {
        return Iterate.sumByBigDecimal(this.positions.getGscPositions(), Position::getAccount, Position::getPreciseMarketValue);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public MutableMap<Account, BigDecimal> sumByBigDecimalAccount_parallel_eager_gsc()
    {
        return ParallelIterate.sumByBigDecimal(this.positions.getGscPositions(), Position::getAccount, Position::getPreciseMarketValue);
    }

    @Test
    public void sumByAccount_gsc()
    {
        Assert.assertEquals(
                this.sumByBigDecimalAccount_parallel_eager_gsc(),
                this.sumByBigDecimalAccount_serial_eager_gsc());
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public MutableMap<String, BigDecimal> sumByBigDecimalCategory_serial_eager_gsc()
    {
        return Iterate.sumByBigDecimal(this.positions.getGscPositions(), Position::getCategory, Position::getPreciseMarketValue);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public MutableMap<String, BigDecimal> sumByBigDecimalCategory_parallel_eager_gsc()
    {
        return ParallelIterate.sumByBigDecimal(this.positions.getGscPositions(), Position::getCategory, Position::getPreciseMarketValue);
    }

    @Test
    public void sumByCategory_gsc()
    {
        Assert.assertEquals(
                this.sumByBigDecimalCategory_parallel_eager_gsc(),
                this.sumByBigDecimalCategory_serial_eager_gsc());
    }
}
