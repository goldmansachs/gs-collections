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

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.gs.collections.api.map.primitive.MutableLongIntMap;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.map.mutable.primitive.LongIntHashMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class LongIntMapTest extends AbstractJMHTestRunner
{
    @Param({"1", "10", "100", "10000", "100000"})
    public int mapSizeDividedBy16000;
    @Param({"true", "false"})
    public boolean fullyRandom;
    private MutableLongIntMap longIntMap;
    private int[] randomIntegersForMap;
    private long[] randomLongsForMap;

    @Setup
    public void setUp()
    {
        long[] highMasks = new long[64];
        for (int i = 0; i < 64; i++)
        {
            highMasks[i] = (long) i << 58;
        }

        long[] randomLongsGet = new Random(0x123456789ABCDL).longs().limit(this.mapSizeDividedBy16000).toArray();

        this.randomLongsForMap = new long[this.mapSizeDividedBy16000 * 64];
        Random randomSeeds = new Random(0x123456789ABCDL);
        for (int i = 0; i < randomLongsGet.length; i++)
        {
            for (int j = 0; j < 64; j++)
            {
                if (this.fullyRandom)
                {
                    this.randomLongsForMap[i * 64 + j] = randomSeeds.nextLong();
                }
                else
                {
                    this.randomLongsForMap[i * 64 + j] = randomLongsGet[i] ^ highMasks[j];
                }
            }
        }
        this.randomIntegersForMap = new Random(0x123456789ABCDL).ints().limit((long) (this.mapSizeDividedBy16000 * 64)).toArray();

        this.longIntMap = new LongIntHashMap();
        for (int i = 0; i < this.mapSizeDividedBy16000 * 64; i++)
        {
            this.longIntMap.put(this.randomLongsForMap[i], this.randomIntegersForMap[i]);
        }

        this.shuffle(this.randomLongsForMap, randomSeeds);
    }

    private static void swap(long[] arr, int i, int j)
    {
        long tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void get(Blackhole blackHole)
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy16000 / 64; j++)
        {
            for (int i = 0; i < this.mapSizeDividedBy16000 * 64; i++)
            {
                blackHole.consume(this.longIntMap.get(this.randomLongsForMap[i]));
            }
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void put()
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy16000 / 64; j++)
        {
            MutableLongIntMap newMap = new LongIntHashMap();
            for (int i = 0; i < this.mapSizeDividedBy16000 * 64; i++)
            {
                newMap.put(this.randomLongsForMap[i], this.randomIntegersForMap[i]);
            }
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void presizedPut()
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy16000 / 64; j++)
        {
            MutableLongIntMap newMap = new LongIntHashMap(this.mapSizeDividedBy16000);
            for (int i = 0; i < this.mapSizeDividedBy16000 * 64; i++)
            {
                newMap.put(this.randomLongsForMap[i], this.randomIntegersForMap[i]);
            }
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void remove()
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy16000 / 64; j++)
        {
            MutableLongIntMap newMap = new LongIntHashMap(this.longIntMap);
            for (int i = 0; i < this.mapSizeDividedBy16000 * 64; i++)
            {
                newMap.remove(this.randomLongsForMap[i]);
            }
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void copyTest(Blackhole blackHole)
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy16000 / 64; j++)
        {
            MutableLongIntMap newMap = new LongIntHashMap(this.longIntMap);
            blackHole.consume(newMap.get(0));
        }
    }

    public void shuffle(long[] longArray, Random rnd)
    {
        for (int i = longArray.length; i > 1; i--)
        {
            LongIntMapTest.swap(longArray, i - 1, rnd.nextInt(i));
        }
    }
}
