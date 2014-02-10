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

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.utility.ArrayIterate;

public final class ObjectIntProcedureFJTaskRunner<T, BT extends ObjectIntProcedure<? super T>>
{
    private final Function<ObjectIntProcedureFJTask<T, BT>, BT> procedureFunction = new ObjectIntProcedureExtractor();
    private ObjectIntProcedureFJTask<T, BT>[] procedures;
    private Throwable error;
    private final Combiner<BT> combiner;
    private final int taskCount;
    private final BlockingQueue<BT> outputQueue;
    private final CountDownLatch latch;

    public ObjectIntProcedureFJTaskRunner(Combiner<BT> newCombiner, int taskCount)
    {
        this(newCombiner, taskCount,
                ObjectIntProcedureFJTaskRunner.buildQueue(newCombiner, taskCount),
                ObjectIntProcedureFJTaskRunner.buildCountDownLatch(newCombiner, taskCount));
    }

    ObjectIntProcedureFJTaskRunner(Combiner<BT> newCombiner, int taskCount, BlockingQueue<BT> queue, CountDownLatch latch)
    {
        this.combiner = newCombiner;
        this.taskCount = taskCount;
        this.outputQueue = queue;
        this.latch = latch;
    }

    private static <BT> CountDownLatch buildCountDownLatch(Combiner<BT> newCombiner, int taskCount)
    {
        return newCombiner.useCombineOne() ? null : new CountDownLatch(taskCount);
    }

    private static <BT> ArrayBlockingQueue<BT> buildQueue(Combiner<BT> newCombiner, int taskCount)
    {
        return newCombiner.useCombineOne() ? new ArrayBlockingQueue<BT>(taskCount) : null;
    }

    private void createAndExecuteTasks(Executor executor, ObjectIntProcedureFactory<BT> procedureFactory, List<T> list)
    {
        this.procedures = new ObjectIntProcedureFJTask[this.taskCount];

        int sectionSize = list.size() / this.taskCount;
        int size = this.taskCount;
        for (int index = 0; index < size; index++)
        {
            ObjectIntProcedureFJTask<T, BT> procedureFJTask =
                    new ObjectIntProcedureFJTask<T, BT>(this, procedureFactory, list, index, sectionSize, index == this.taskCount - 1);
            this.procedures[index] = procedureFJTask;
            executor.execute(procedureFJTask);
        }
    }

    public void setFailed(Throwable newError)
    {
        this.error = newError;
    }

    public void taskCompleted(ObjectIntProcedureFJTask<T, BT> task)
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

    public void executeAndCombine(Executor executor, ObjectIntProcedureFactory<BT> procedureFactory, List<T> list)
    {
        this.createAndExecuteTasks(executor, procedureFactory, list);
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

    private final class ObjectIntProcedureExtractor implements Function<ObjectIntProcedureFJTask<T, BT>, BT>
    {
        private static final long serialVersionUID = 1L;

        public BT valueOf(ObjectIntProcedureFJTask<T, BT> object)
        {
            return object.getProcedure();
        }
    }
}
