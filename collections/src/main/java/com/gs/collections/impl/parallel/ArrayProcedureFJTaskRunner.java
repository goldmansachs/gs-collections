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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.utility.ArrayIterate;

public final class ArrayProcedureFJTaskRunner<T, BT extends Procedure<? super T>>
{
    private final Function<ArrayProcedureFJTask<T, BT>, BT> procedureFunction = new ProcedureExtractor();
    private ArrayProcedureFJTask<T, BT>[] procedures;
    private Throwable error;
    private final CountDownLatch latch;
    private final Combiner<BT> combiner;
    private final BlockingQueue<BT> outputQueue;
    private final int taskCount;

    public ArrayProcedureFJTaskRunner(Combiner<BT> newCombiner, int newTaskCount)
    {
        this.combiner = newCombiner;
        this.taskCount = newTaskCount;
        if (this.combiner.useCombineOne())
        {
            this.outputQueue = new ArrayBlockingQueue<BT>(newTaskCount);
            this.latch = null;
        }
        else
        {
            this.latch = new CountDownLatch(newTaskCount);
            this.outputQueue = null;
        }
    }

    /**
     * Creates an array of ProcedureFJTasks wrapping Procedures created by the specified ProcedureFactory.
     */
    private void createAndExecuteTasks(Executor executor, ProcedureFactory<BT> procedureFactory, T[] array)
    {
        this.procedures = new ArrayProcedureFJTask[this.taskCount];
        int sectionSize = array.length / this.taskCount;
        int size = this.taskCount;
        for (int index = 0; index < size; index++)
        {
            ArrayProcedureFJTask<T, BT> procedureFJTask = new ArrayProcedureFJTask<T, BT>(this, procedureFactory, array, index,
                    sectionSize, index == this.taskCount - 1);
            this.procedures[index] = procedureFJTask;
            executor.execute(procedureFJTask);
        }
    }

    public void taskCompleted(ArrayProcedureFJTask<T, BT> task)
    {
        if (this.combiner.useCombineOne())
        {
            this.outputQueue.add(task.getProcedure());
        }
        else
        {
            this.latch.countDown();
        }
    }

    private void join()
    {
        try
        {
            if (this.combiner.useCombineOne())
            {
                int remaingTaskCount = this.taskCount;
                while (remaingTaskCount > 0)
                {
                    this.combiner.combineOne(this.outputQueue.take());
                    remaingTaskCount--;
                }
            }
            else
            {
                this.latch.await();
            }
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException("Combine failed", e);
        }
    }

    public void executeAndCombine(Executor executor, ProcedureFactory<BT> procedureFactory, T[] array)
    {
        this.createAndExecuteTasks(executor, procedureFactory, array);
        this.join();
        if (this.error != null)
        {
            throw new RuntimeException("One or more parallel tasks failed", this.error);
        }
        //don't combine until the lock is notified
        this.combineTasks();
    }

    public void setFailed(Throwable newError)
    {
        this.error = newError;
    }

    private void combineTasks()
    {
        if (!this.combiner.useCombineOne())
        {
            this.combiner.combineAll(ArrayIterate.collect(this.procedures, this.procedureFunction));
        }
    }

    private final class ProcedureExtractor implements Function<ArrayProcedureFJTask<T, BT>, BT>
    {
        private static final long serialVersionUID = 1L;

        public BT valueOf(ArrayProcedureFJTask<T, BT> object)
        {
            return object.getProcedure();
        }
    }
}
