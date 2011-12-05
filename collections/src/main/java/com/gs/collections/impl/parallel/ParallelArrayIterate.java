/*
 * Copyright 2011 Goldman Sachs.
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

import java.util.concurrent.Executor;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.utility.ArrayIterate;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * The ParallelArrayIterate class contains a parallel forEach algorithm that work with Java arrays.  The forEach
 * algorithm employs a batching fork and join approach approach.  All Collections that are not array based use
 * ParallelArrayIterate to parallelize, by converting themselves to an array using toArray().
 */
public final class ParallelArrayIterate
{
    private ParallelArrayIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T, BT extends Procedure<? super T>> void forEach(
            T[] array,
            ProcedureFactory<BT> procedureFactory,
            Combiner<BT> combiner)
    {
        int taskCount = Math.max(ParallelIterate.DEFAULT_PARALLEL_TASK_COUNT, array.length / ParallelIterate.DEFAULT_MIN_FORK_SIZE);
        ParallelArrayIterate.forEach(array, procedureFactory, combiner, ParallelIterate.DEFAULT_MIN_FORK_SIZE, taskCount);
    }

    public static <T, BT extends Procedure<? super T>> void forEach(
            T[] array,
            ProcedureFactory<BT> procedureFactory,
            Combiner<BT> combiner,
            int minForkSize,
            int taskCount)
    {
        ParallelArrayIterate.forEachOn(array, procedureFactory, combiner, minForkSize, taskCount, ParallelIterate.EXECUTOR_SERVICE);
    }

    public static <T, BT extends Procedure<? super T>> void forEachOn(
            T[] array,
            ProcedureFactory<BT> procedureFactory,
            Combiner<BT> combiner,
            int minForkSize,
            int taskCount,
            Executor executor)
    {
        if (ArrayIterate.notEmpty(array))
        {
            int size = array.length;
            if (size < minForkSize)
            {
                BT procedure = procedureFactory.create();
                ArrayIterate.forEach(array, procedure);
                ParallelArrayIterate.<T, BT>combineSingleProcedure(combiner, procedure);
            }
            else
            {
                int threadCount = Math.min(size, taskCount);
                new ArrayProcedureFJTaskRunner<T, BT>(combiner, threadCount).executeAndCombine(executor, procedureFactory, array);
            }
        }
    }

    private static <T, BT extends Procedure<? super T>> void combineSingleProcedure(Combiner<BT> combiner, BT procedure)
    {
        if (combiner.useCombineOne())
        {
            combiner.combineOne(procedure);
        }
        else
        {
            combiner.combineAll(iList(procedure));
        }
    }
}
