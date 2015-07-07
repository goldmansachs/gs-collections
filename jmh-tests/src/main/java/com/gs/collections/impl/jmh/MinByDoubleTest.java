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

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.gs.collections.impl.jmh.domain.Position;
import com.gs.collections.impl.jmh.domain.Positions;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
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
public class MinByDoubleTest extends AbstractJMHTestRunner
{
    private static final int SIZE = 3_000_000;
    private static final int BATCH_SIZE = 10_000;

    // Comparator which autoboxes doubles: slow
    private static final Comparator<Position> MARKET_VALUE_COMPARATOR_METHODREF =
            Comparator.comparing(Position::getMarketValue);

    private static final Comparator<Position> MARKET_VALUE_COMPARATOR_LAMBDA =
            (Position p1, Position p2) -> Double.compare(p1.getMarketValue(), p2.getMarketValue());

    private final Positions positions = new Positions(SIZE).shuffle();

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
    public Position minByMarketValue_serial_lazy_direct_methodref_jdk()
    {
        return this.positions.getJdkPositions().stream().min(MARKET_VALUE_COMPARATOR_METHODREF).get();
    }

    @Benchmark
    public Position minByMarketValue_serial_lazy_direct_lambda_jdk()
    {
        return this.positions.getJdkPositions().stream().min(MARKET_VALUE_COMPARATOR_LAMBDA).get();
    }

    @Benchmark
    public Position minByMarketValue_serial_lazy_collect_methodref_jdk()
    {
        return this.positions.getJdkPositions().stream().collect(
                Collectors.minBy(MARKET_VALUE_COMPARATOR_METHODREF)).get();
    }

    @Benchmark
    public Position minByMarketValue_serial_lazy_collect_lambda_jdk()
    {
        return this.positions.getJdkPositions().stream().collect(
                Collectors.minBy(MARKET_VALUE_COMPARATOR_LAMBDA)).get();
    }

    @Benchmark
    public Position minByMarketValue_parallel_lazy_direct_methodref_jdk()
    {
        return this.positions.getJdkPositions().parallelStream().min(
                MARKET_VALUE_COMPARATOR_METHODREF).get();
    }

    @Benchmark
    public Position minByMarketValue_parallel_lazy_direct_lambda_jdk()
    {
        return this.positions.getJdkPositions().parallelStream().min(
                MARKET_VALUE_COMPARATOR_LAMBDA).get();
    }

    @Benchmark
    public Position minByMarketValue_parallel_lazy_collect_methodref_jdk()
    {
        return this.positions.getJdkPositions().parallelStream().collect(
                Collectors.minBy(MARKET_VALUE_COMPARATOR_METHODREF)).get();
    }

    @Benchmark
    public Position minByMarketValue_parallel_lazy_collect_lambda_jdk()
    {
        return this.positions.getJdkPositions().parallelStream().collect(
                Collectors.minBy(MARKET_VALUE_COMPARATOR_LAMBDA)).get();
    }

    @Benchmark
    public Position minByMarketValue_serial_eager_gsc()
    {
        return this.positions.getGscPositions().minBy(Position::getMarketValue);
    }

    @Benchmark
    public Position minByMarketValue_serial_lazy_gsc()
    {
        return this.positions.getGscPositions().asLazy().minBy(Position::getMarketValue);
    }

    @Benchmark
    public Position minByMarketValue_parallel_lazy_gsc()
    {
        return this.positions.getGscPositions().asParallel(this.executorService, BATCH_SIZE).minBy(
                Position::getMarketValue);
    }
}
