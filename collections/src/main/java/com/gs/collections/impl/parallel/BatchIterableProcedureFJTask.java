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

import com.gs.collections.api.block.procedure.Procedure;

public final class BatchIterableProcedureFJTask<T, BT extends Procedure<? super T>> implements Runnable
{
    private final ProcedureFactory<BT> procedureFactory;
    private BT procedure;
    private final BatchIterable<T> iterable;
    private final int sectionIndex;
    private final int sectionCount;
    private final BatchIterableProcedureFJTaskRunner<T, BT> taskRunner;

    /**
     * Creates an array of ProcedureFJTasks wrapping Procedures created by the specified ProcedureFactory.
     */
    public BatchIterableProcedureFJTask(
            BatchIterableProcedureFJTaskRunner<T, BT> newFJTaskRunner,
            ProcedureFactory<BT> procedureFactory,
            BatchIterable<T> iterable,
            int index,
            int count)
    {
        this.taskRunner = newFJTaskRunner;
        this.procedureFactory = procedureFactory;
        this.iterable = iterable;
        this.sectionIndex = index;
        this.sectionCount = count;
    }

    public void run()
    {
        try
        {
            this.procedure = this.procedureFactory.create();
            this.iterable.batchForEach(this.procedure, this.sectionIndex, this.sectionCount);
        }
        catch (Throwable newError)
        {
            this.taskRunner.setFailed(newError);
        }
        finally
        {
            this.taskRunner.taskCompleted(this);
        }
    }

    public BT getProcedure()
    {
        return this.procedure;
    }
}
