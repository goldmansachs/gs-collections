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

package com.gs.collections.impl.memory.map;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.carrotsearch.hppc.Containers;
import com.carrotsearch.hppc.ObjectObjectHashMap;
import com.carrotsearch.hppc.ObjectObjectMap;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.memory.TestDataFactory;
import gnu.trove.impl.Constants;
import gnu.trove.map.hash.THashMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.mutable.AnyRefMap;
import scala.collection.mutable.HashTable;

public class MapMemoryTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MapMemoryTest.class);

    @Test
    public void memoryForScaledMaps()
    {
        LOGGER.info(
                "Comparing Items: Scala {}, JDK {}, Trove {}, Trove Presized {}, GSC {}, JDK {}, HPPC {}, Koloboke {}, Scala {}",
                scala.collection.mutable.HashMap.class.getSimpleName(),
                HashMap.class.getSimpleName(),
                THashMap.class.getSimpleName(),
                THashMap.class.getSimpleName(),
                UnifiedMap.class.getSimpleName(),
                Hashtable.class.getSimpleName(),
                ObjectObjectMap.class.getSimpleName(),
                HashObjObjMap.class.getSimpleName(),
                AnyRefMap.class.getSimpleName()
        );

        for (int size = 0; size < 1000001; size += 25000)
        {
            this.memoryForScaledMaps(size);
        }
        LOGGER.info("Ending test: {}", this.getClass().getName());
    }

    public void memoryForScaledMaps(int size)
    {
        Float[] chainingLoadFactors = {0.70f, 0.75f, 0.80f};
        for (Float loadFactor : chainingLoadFactors)
        {
            String suffix = "_loadFactor=" + loadFactor;

            MemoryTestBench.on(scala.collection.mutable.HashMap.class, suffix)
                    .printContainerMemoryUsage("Map", size, new ScalaHashMapFactory(size, loadFactor));
            MemoryTestBench.on(HashMap.class, suffix)
                    .printContainerMemoryUsage("Map", size, new HashMapFactory(size, loadFactor));
            MemoryTestBench.on(UnifiedMap.class, suffix)
                    .printContainerMemoryUsage("Map", size, new UnifiedMapFactory(size, loadFactor));
            MemoryTestBench.on(Hashtable.class, suffix)
                    .printContainerMemoryUsage("Map", size, new HashtableFactory(size, loadFactor));
            MemoryTestBench.on(ObjectObjectHashMap.class, suffix)
                    .printContainerMemoryUsage("Map", size, new HppcMapFactory(size, loadFactor));
        }

        Float[] openAddressingLoadFactors = {0.45f, 0.50f, 0.55f};
        for (Float loadFactor : openAddressingLoadFactors)
        {
            String suffix = "_loadFactor=" + loadFactor;

            MemoryTestBench.on(THashMap.class, suffix)
                    .printContainerMemoryUsage("Map", size, new THashMapFactory(size, loadFactor));
            MemoryTestBench.on(THashMap.class, suffix + "_presized")
                    .printContainerMemoryUsage("Map", size, new PresizedTHashMapFactory(size, loadFactor));
        }

        MemoryTestBench.on(HashObjObjMap.class)
                .printContainerMemoryUsage("Map", size, new KolobokeMapFactory(size));
        MemoryTestBench.on(AnyRefMap.class)
                .printContainerMemoryUsage("Map", size, new ScalaAnyRefMapFactory(size));
    }

    public abstract static class SizedMapFactory
    {
        protected final int size;
        protected final float loadFactor;
        protected final ImmutableList<Integer> data;

        protected SizedMapFactory(int size, float loadFactor)
        {
            this.size = size;
            this.loadFactor = loadFactor;
            this.data = TestDataFactory.createRandomImmutableList(this.size);
        }

        protected <R extends Map<Integer, String>> R fill(final R map)
        {
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    map.put(each, "dummy");
                }
            });
            return map;
        }
    }

    private static final class ScalaHashMapFactory
            extends SizedMapFactory
            implements Function0<scala.collection.mutable.HashMap<Integer, String>>
    {
        private ScalaHashMapFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public scala.collection.mutable.HashMap<Integer, String> value()
        {
            /**
             * @see HashTable#initialSize()
             */
            int defaultInitialSize = 16;
            final scala.collection.mutable.HashMap<Integer, String> map = new PresizableHashMap<>(defaultInitialSize, (int) (this.loadFactor * 1000));
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    map.put(each, "dummy");
                }
            });
            return map;
        }
    }

    private static final class HashMapFactory
            extends SizedMapFactory
            implements Function0<HashMap<Integer, String>>
    {
        private HashMapFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public HashMap<Integer, String> value()
        {
            /**
             * @see HashMap#DEFAULT_INITIAL_CAPACITY
             */
            int defaultInitialCapacity = 16;
            HashMap<Integer, String> map = new HashMap<>(defaultInitialCapacity, this.loadFactor);
            return this.fill(map);
        }
    }

    private static final class THashMapFactory
            extends SizedMapFactory
            implements Function0<THashMap<Integer, String>>
    {
        private THashMapFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public THashMap<Integer, String> value()
        {
            return this.fill(new THashMap<Integer, String>(Constants.DEFAULT_CAPACITY, this.loadFactor));
        }
    }

    private static final class PresizedTHashMapFactory
            extends SizedMapFactory
            implements Function0<THashMap<Integer, String>>
    {
        private PresizedTHashMapFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public THashMap<Integer, String> value()
        {
            return this.fill(new THashMap<Integer, String>(this.size, this.loadFactor));
        }
    }

    private static final class HashtableFactory
            extends SizedMapFactory
            implements Function0<Hashtable<Integer, String>>
    {
        private HashtableFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        @SuppressWarnings("UseOfObsoleteCollectionType")
        public Hashtable<Integer, String> value()
        {
            /**
             * @see Hashtable#Hashtable()
             */
            int defaultSize = 11;
            Hashtable<Integer, String> map = new Hashtable<>(defaultSize, this.loadFactor);
            return this.fill(map);
        }
    }

    private static final class UnifiedMapFactory
            extends SizedMapFactory
            implements Function0<UnifiedMap<Integer, String>>
    {
        private UnifiedMapFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public UnifiedMap<Integer, String> value()
        {
            /**
             * @see UnifiedMap#DEFAULT_INITIAL_CAPACITY
             */
            int defaultSize = 8;
            UnifiedMap<Integer, String> map = new UnifiedMap<Integer, String>(defaultSize, this.loadFactor);
            return this.fill(map);
        }
    }

    private static final class HppcMapFactory
            extends SizedMapFactory
            implements Function0<ObjectObjectMap<Integer, String>>
    {
        private HppcMapFactory(int size, float loadFactor)
        {
            super(size, loadFactor);
        }

        @Override
        public ObjectObjectMap<Integer, String> value()
        {
            final ObjectObjectMap<Integer, String> map = new ObjectObjectHashMap<>(Containers.DEFAULT_EXPECTED_ELEMENTS, this.loadFactor);

            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    map.put(each, "dummy");
                }
            });
            return map;
        }
    }

    private static final class KolobokeMapFactory
            extends SizedMapFactory
            implements Function0<HashObjObjMap<Integer, String>>
    {
        private KolobokeMapFactory(int size)
        {
            super(size, 0.0f);
        }

        @Override
        public HashObjObjMap<Integer, String> value()
        {
            return this.fill(HashObjObjMaps.<Integer, String>newMutableMap());
        }
    }

    private static final class ScalaAnyRefMapFactory
            extends SizedMapFactory
            implements Function0<AnyRefMap<Integer, String>>
    {
        private ScalaAnyRefMapFactory(int size)
        {
            super(size, 0.0f);
        }

        @Override
        public AnyRefMap<Integer, String> value()
        {
            final AnyRefMap<Integer, String> map = new AnyRefMap<>();
            this.data.forEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    map.put(each, "dummy");
                }
            });
            return map;
        }
    }
}
