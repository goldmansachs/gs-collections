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

public final class BatchIterableProcedureFJTaskRunner<T, BT extends Procedure<? super T>>
{
    private final Function<BatchIterableProcedureFJTask<T, BT>, BT> procedureFunction = new ProcedureExtractor();
    private BatchIterableProcedureFJTask<T, BT>[] procedures;
    private Throwable error;
    private final Combiner<BT> combiner;
    private final int taskCount;
    private final BlockingQueue<BT> outputQueue;
    private final CountDownLatch latch;

    public BatchIterableProcedureFJTaskRunner(Combiner<BT> newCombiner, int taskCount)
    {
        this.combiner = newCombiner;
        this.taskCount = taskCount;
        if (this.combiner.useCombineOne())
        {
            this.outputQueue = new ArrayBlockingQueue<BT>(taskCount);
            this.latch = null;
        }
        else
        {
            this.latch = new CountDownLatch(taskCount);
            this.outputQueue = null;
        }
    }

    private void createAndExecuteTasks(Executor executor, ProcedureFactory<BT> procedureFactory, BatchIterable<T> batchIterable)
    {
        this.procedures = new BatchIterableProcedureFJTask[this.taskCount];
        int size = this.taskCount;
        for (int batchNumber = 0; batchNumber < size; batchNumber++)
        {
            BatchIterableProcedureFJTask<T, BT> procedureFJTask =
                    new BatchIterableProcedureFJTask<T, BT>(this, procedureFactory, batchIterable, batchNumber, this.taskCount);
            this.procedures[batchNumber] = procedureFJTask;
            executor.execute(procedureFJTask);
        }
    }

    public void setFailed(Throwable newError)
    {
        this.error = newError;
    }

    public void taskCompleted(BatchIterableProcedureFJTask<T, BT> task)
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

    public void executeAndCombine(Executor executor, ProcedureFactory<BT> procedureFactory, BatchIterable<T> set)
    {
        this.createAndExecuteTasks(executor, procedureFactory, set);
        this.join();
        if (this.error != null)
        {
            throw new RuntimeException("One or more parallel tasks failed", this.error);
        }
        //don't combine until the lock is notified
        this.combineTasks();
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

    private void combineTasks()
    {
        if (!this.combiner.useCombineOne())
        {
            this.combiner.combineAll(ArrayIterate.collect(this.procedures, this.procedureFunction));
        }
    }

    private final class ProcedureExtractor implements Function<BatchIterableProcedureFJTask<T, BT>, BT>
    {
        private static final long serialVersionUID = 1L;

        public BT valueOf(BatchIterableProcedureFJTask<T, BT> object)
        {
            return object.getProcedure();
        }
    }
}
