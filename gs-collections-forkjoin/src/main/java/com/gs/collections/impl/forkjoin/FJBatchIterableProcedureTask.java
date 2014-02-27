/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.forkjoin;

import java.util.concurrent.ForkJoinTask;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.parallel.ProcedureFactory;

public class FJBatchIterableProcedureTask<T, PT extends Procedure<? super T>> extends ForkJoinTask<PT>
{
    private static final long serialVersionUID = 1L;

    private final ProcedureFactory<PT> procedureFactory;
    private PT procedure;
    private final BatchIterable<T> iterable;
    private final int sectionIndex;
    private final int sectionCount;
    private final FJBatchIterableProcedureRunner<T, PT> taskRunner;

    /**
     * Creates an array of ProcedureFJTasks wrapping Procedures created by the specified ProcedureFactory.
     */
    public FJBatchIterableProcedureTask(
            FJBatchIterableProcedureRunner<T, PT> newFJTaskRunner,
            ProcedureFactory<PT> newProcedureFactory, BatchIterable<T> iterable, int index, int count)
    {
        this.taskRunner = newFJTaskRunner;
        this.procedureFactory = newProcedureFactory;
        this.iterable = iterable;
        this.sectionIndex = index;
        this.sectionCount = count;
    }

    @Override
    protected boolean exec()
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
        return true;
    }

    @Override
    public PT getRawResult()
    {
        return this.procedure;
    }

    @Override
    protected void setRawResult(PT value)
    {
        throw new UnsupportedOperationException();
    }
}
