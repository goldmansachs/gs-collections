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

import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.map.primitive.MutableLongLongMap;
import com.gs.collections.api.set.primitive.MutableLongSet;
import com.gs.collections.impl.jmh.runner.AbstractJMHTestRunner;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.map.mutable.primitive.LongLongHashMap;
import com.gs.collections.impl.set.mutable.primitive.LongHashSet;
import net.openhft.koloboke.collect.map.LongLongMap;
import net.openhft.koloboke.collect.map.hash.HashLongLongMaps;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class LongLongMapLargeStressTest extends AbstractJMHTestRunner
{
    private static final int LOOP_COUNT = 1;
    private static final int KEY_COUNT = 400_000;
    private static final int MAP_SIZE = 1_000_000;

    @Param({"true", "false"})
    public boolean fullyRandom;
    private LongLongMap longLongKoloboke;
    private MutableLongLongMap longLongGsc;
    private long[] gscLongKeysForMap;
    private long[] kolobokeLongKeysForMap;

    private int kolobokeIndex(int key)
    {
        long h = key * 0x9e3779b97f4a7c15L;
        h ^= h >> 32;
        return this.mask((int) (h ^ (h >> 16)));
    }

    private int gscIndex(int element)
    {
        return this.mask(element);
    }

    private int mask(int spread)
    {
        return spread & ((1 << 20) - 1);
    }

    @Setup
    public void setUp()
    {
        this.longLongKoloboke = HashLongLongMaps.newMutableMap(MAP_SIZE);
        this.longLongGsc = new LongLongHashMap(MAP_SIZE);

        Random random = new Random(0x123456789ABCDL);

        int number = 23;
        int lower = Integer.MIN_VALUE;
        int upper = Integer.MAX_VALUE;

        long[] randomNumbersForMap = this.getRandomKeys(random).toArray();

        this.gscLongKeysForMap = this.fullyRandom ? randomNumbersForMap : this.getGSCArray(number, lower, upper, random);
        this.kolobokeLongKeysForMap = this.fullyRandom ? randomNumbersForMap : this.getKolobokeArray(number, lower, upper, random);

        for (int i = 0; i < KEY_COUNT; i++)
        {
            this.longLongKoloboke.put(this.kolobokeLongKeysForMap[i], 5);
            this.longLongGsc.put(this.gscLongKeysForMap[i], 5);
        }

        this.shuffle(this.gscLongKeysForMap, random);
        this.shuffle(this.kolobokeLongKeysForMap, random);
    }

    private MutableLongSet getRandomKeys(Random random)
    {
        MutableLongSet set = new LongHashSet(KEY_COUNT);
        while (set.size() < KEY_COUNT)
        {
            set.add(random.nextLong());
        }
        return set;
    }

    protected long[] getGSCArray(int number, int lower, int upper, Random random)
    {
        long[] gscCollisions = this.getGSCSequenceCollisions(number, lower, upper).toArray();
        this.shuffle(gscCollisions, random);
        return gscCollisions;
    }

    private MutableLongList getGSCSequenceCollisions(int number, int lower, int upper)
    {
        MutableLongList gscCollidingNumbers = new LongArrayList();
        for (int i = lower; i < upper && gscCollidingNumbers.size() < KEY_COUNT; i++)
        {
            int index = this.gscIndex(i);
            if (index >= number && index <= number + 100)
            {
                gscCollidingNumbers.add(i);
            }
        }
        return gscCollidingNumbers;
    }

    protected long[] getKolobokeArray(int number, int lower, int upper, Random random)
    {
        long[] kolobokeCollisions = this.getKolobokeSequenceCollisions(number, lower, upper).toArray();
        this.shuffle(kolobokeCollisions, random);
        return kolobokeCollisions;
    }

    private MutableLongList getKolobokeSequenceCollisions(int number, int lower, int upper)
    {
        MutableLongList kolobokeCollidingNumbers = new LongArrayList();
        for (int i = lower; i < upper && kolobokeCollidingNumbers.size() < KEY_COUNT; i++)
        {
            int index = this.kolobokeIndex(i);
            if (index >= number && index <= number + 100)
            {
                kolobokeCollidingNumbers.add(i);
            }
        }
        return kolobokeCollidingNumbers;
    }

    @Benchmark
    public void kolobokeGet()
    {
        for (int j = 0; j < LOOP_COUNT; j++)
        {
            for (int i = 0; i < KEY_COUNT; i++)
            {
                if (this.longLongKoloboke.get(this.kolobokeLongKeysForMap[i]) == this.longLongKoloboke.defaultValue())
                {
                    throw new AssertionError(this.kolobokeLongKeysForMap[i] + " not in map");
                }
            }
            if (this.longLongKoloboke.size() != KEY_COUNT)
            {
                throw new AssertionError("size is " + this.longLongKoloboke.size());
            }
        }
    }

    @Benchmark
    public void gscGet()
    {
        for (int j = 0; j < LOOP_COUNT; j++)
        {
            for (int i = 0; i < KEY_COUNT; i++)
            {
                if (this.longLongGsc.get(this.gscLongKeysForMap[i]) == 0)
                {
                    throw new AssertionError(this.gscLongKeysForMap[i] + " not in map");
                }
            }
            if (this.longLongGsc.size() != KEY_COUNT)
            {
                throw new AssertionError("size is " + this.longLongGsc.size());
            }
        }
    }

    @Benchmark
    public void kolobokePut()
    {
        for (int j = 0; j < LOOP_COUNT; j++)
        {
            LongLongMap newMap = HashLongLongMaps.newMutableMap(MAP_SIZE);
            for (int i = 0; i < KEY_COUNT; i++)
            {
                newMap.put(this.kolobokeLongKeysForMap[i], 4);
            }
            if (newMap.size() != KEY_COUNT)
            {
                throw new AssertionError("size is " + newMap.size());
            }
        }
    }

    @Benchmark
    public void gscPut()
    {
        for (int j = 0; j < LOOP_COUNT; j++)
        {
            MutableLongLongMap newMap = new LongLongHashMap(MAP_SIZE);
            for (int i = 0; i < KEY_COUNT; i++)
            {
                newMap.put(this.gscLongKeysForMap[i], 4);
            }
            if (newMap.size() != KEY_COUNT)
            {
                throw new AssertionError("size is " + newMap.size());
            }
        }
    }

    @Benchmark
    public void gscRemove()
    {
        for (int j = 0; j < LOOP_COUNT; j++)
        {
            MutableLongLongMap newMap = new LongLongHashMap(this.longLongGsc);
            for (int i = 0; i < KEY_COUNT; i++)
            {
                newMap.remove(this.gscLongKeysForMap[i]);
            }
            if (newMap.size() != 0)
            {
                throw new AssertionError("size is " + newMap.size());
            }
        }
    }

    @Benchmark
    public void kolobokeRemove()
    {
        for (int j = 0; j < LOOP_COUNT; j++)
        {
            LongLongMap newMap = HashLongLongMaps.newMutableMap(this.longLongKoloboke);
            for (int i = 0; i < KEY_COUNT; i++)
            {
                newMap.remove(this.kolobokeLongKeysForMap[i]);
            }
            if (newMap.size() != 0)
            {
                throw new AssertionError("size is " + newMap.size());
            }
        }
    }

    public void shuffle(long[] array, Random rnd)
    {
        for (int i = array.length; i > 1; i--)
        {
            LongLongMapLargeStressTest.swap(array, i - 1, rnd.nextInt(i));
        }
    }

    private static void swap(long[] arr, int i, int j)
    {
        long tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
