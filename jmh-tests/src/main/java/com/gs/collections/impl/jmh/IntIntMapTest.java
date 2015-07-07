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

import com.gs.collections.api.map.primitive.MutableIntIntMap;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.map.mutable.primitive.IntIntHashMap;
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
public class IntIntMapTest extends AbstractJMHTestRunner
{
    @Param({"1", "10", "100", "10000", "30000", "100000"})
    public int mapSizeDividedBy64;

    @Param({"true", "false"})
    public boolean fullyRandom;

    private MutableIntIntMap intIntMap;
    private int[] randomIntsForKeys;
    private int[] randomIntsForValues;

    private static void swap(int[] arr, int i, int j)
    {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    @Setup
    public void setUp()
    {
        int[] highMasks = new int[64];
        for (int i = 0; i < 64; i++)
        {
            highMasks[i] = i << 26;
        }

        int[] randomSeeds = new Random(0x123456789ABCDL).ints().limit((long) this.mapSizeDividedBy64).toArray();

        this.randomIntsForKeys = new int[this.mapSizeDividedBy64 * 64];
        Random randomSeeds2 = new Random(0x123456789ABCDL);
        for (int i = 0; i < randomSeeds.length; i++)
        {
            for (int j = 0; j < 64; j++)
            {
                if (this.fullyRandom)
                {
                    this.randomIntsForKeys[i * 64 + j] = randomSeeds2.nextInt();
                }
                else
                {
                    this.randomIntsForKeys[i * 64 + j] = randomSeeds[i] ^ highMasks[j];
                }
            }
        }
        this.randomIntsForValues = new Random(0x123456789ABCDL).ints().limit((long) (this.mapSizeDividedBy64 * 64)).toArray();

        this.intIntMap = new IntIntHashMap();
        for (int i = 0; i < this.mapSizeDividedBy64 * 64; i++)
        {
            this.intIntMap.put(this.randomIntsForKeys[i], this.randomIntsForValues[i]);
        }
        this.shuffle(this.randomIntsForKeys, randomSeeds2);
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void get(Blackhole blackHole)
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy64 / 64; j++)
        {
            for (int i = 0; i < this.mapSizeDividedBy64 * 64; i++)
            {
                blackHole.consume(this.intIntMap.get(this.randomIntsForKeys[i]));
            }
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void put()
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy64 / 64; j++)
        {
            MutableIntIntMap newMap = new IntIntHashMap();
            for (int i = 0; i < this.mapSizeDividedBy64 * 64; i++)
            {
                newMap.put(this.randomIntsForKeys[i], this.randomIntsForValues[i]);
            }
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void presizedPut()
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy64 / 64; j++)
        {
            MutableIntIntMap newMap = new IntIntHashMap(this.mapSizeDividedBy64);
            for (int i = 0; i < this.mapSizeDividedBy64 * 64; i++)
            {
                newMap.put(this.randomIntsForKeys[i], this.randomIntsForValues[i]);
            }
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void remove()
    {
        for (int j = 0; j < 10_000_000 / this.mapSizeDividedBy64 / 64; j++)
        {
            MutableIntIntMap newMap = new IntIntHashMap(this.intIntMap);
            for (int i = 0; i < this.mapSizeDividedBy64 * 64; i++)
            {
                newMap.remove(this.randomIntsForKeys[i]);
            }
        }
    }

    public void shuffle(int[] intArray, Random rnd)
    {
        for (int i = intArray.length; i > 1; i--)
        {
            IntIntMapTest.swap(intArray, i - 1, rnd.nextInt(i));
        }
    }
}
