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

package com.gs.collections.impl.parallel;

import java.util.Map;
import java.util.concurrent.Executor;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.utility.MapIterate;

/**
 * The ParallelMapIterate class contains parallel algorithms that work with Maps.
 * <p>
 * The forEachEtry algorithm employs a batching fork and join approach approach which does
 * not yet allow for specification of a Factory for the blocks or a Combiner for the results.
 * This means that forEachKeyValue can only support pure forking or forking with a shared
 * thread-safe data structure collecting results.
 */
public final class ParallelMapIterate
{
    private ParallelMapIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * A parallel form of forEachKeyValue.
     *
     * @see MapIterate#forEachKeyValue(Map, Procedure2)
     * @see ParallelIterate
     */
    public static <K, V> void forEachKeyValue(Map<K, V> map, Procedure2<? super K, ? super V> procedure2)
    {
        ParallelMapIterate.forEachKeyValue(map, procedure2, 2, map.size());
    }

    /**
     * A parallel form of forEachKeyValue.
     *
     * @see MapIterate#forEachKeyValue(Map, Procedure2)
     * @see ParallelIterate
     */
    public static <K, V> void forEachKeyValue(
            Map<K, V> map,
            Procedure2<? super K, ? super V> procedure,
            Executor executor)
    {
        ParallelMapIterate.forEachKeyValue(map, procedure, 2, map.size(), executor);
    }

    /**
     * A parallel form of forEachKeyValue.
     *
     * @see MapIterate#forEachKeyValue(Map, Procedure2)
     * @see ParallelIterate
     */
    public static <K, V> void forEachKeyValue(
            Map<K, V> map,
            Procedure2<? super K, ? super V> procedure,
            int minForkSize,
            int taskCount)
    {
        if (map.size() > minForkSize)
        {
            Procedure<Pair<K, V>> pairProcedure = new PairProcedure<K, V>(procedure);
            ParallelIterate.forEach(MapIterate.toListOfPairs(map), new PassThruProcedureFactory<Procedure<Pair<K, V>>>(pairProcedure), new PassThruCombiner<Procedure<Pair<K, V>>>(), minForkSize, taskCount);
        }
        else
        {
            MapIterate.forEachKeyValue(map, procedure);
        }
    }

    /**
     * A parallel form of forEachKeyValue.
     *
     * @see MapIterate#forEachKeyValue(Map, Procedure2)
     * @see ParallelIterate
     */
    public static <K, V> void forEachKeyValue(
            Map<K, V> map,
            Procedure2<? super K, ? super V> procedure,
            int minForkSize,
            int taskCount,
            Executor executor)
    {
        if (map.size() > minForkSize)
        {
            Procedure<Pair<K, V>> pairProcedure = new PairProcedure<K, V>(procedure);
            ParallelIterate.forEachInListOnExecutor(
                    MapIterate.toListOfPairs(map),
                    new PassThruProcedureFactory<Procedure<Pair<K, V>>>(pairProcedure),
                    new PassThruCombiner<Procedure<Pair<K, V>>>(),
                    minForkSize,
                    taskCount,
                    executor);
        }
        else
        {
            MapIterate.forEachKeyValue(map, procedure);
        }
    }

    private static final class PairProcedure<T1, T2> implements Procedure<Pair<T1, T2>>
    {
        private static final long serialVersionUID = 1L;

        private final Procedure2<? super T1, ? super T2> procedure;

        private PairProcedure(Procedure2<? super T1, ? super T2> procedure)
        {
            this.procedure = procedure;
        }

        public void value(Pair<T1, T2> pair)
        {
            this.procedure.value(pair.getOne(), pair.getTwo());
        }
    }
}
