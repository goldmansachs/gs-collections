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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.impl.parallel.ProcedureFactory;
import com.gs.collections.impl.utility.ArrayListIterate;
import com.gs.collections.impl.utility.ListIterate;

public class FJListProcedureTask<T, PT extends Procedure<? super T>> extends ForkJoinTask<PT>
{
    private static final long serialVersionUID = 1L;

    private final ProcedureFactory<PT> procedureFactory;
    private PT procedure;
    private final List<T> list;
    private final int start;
    private final int end;
    private final FJListProcedureRunner<T, PT> taskRunner;

    /**
     * Creates an array of ProcedureFJTasks wrapping Procedures created by the specified ProcedureFactory.
     */
    public FJListProcedureTask(
            FJListProcedureRunner<T, PT> newFJTaskRunner, ProcedureFactory<PT> newProcedureFactory,
            List<T> list, int index, int sectionSize, boolean isLast)
    {
        this.taskRunner = newFJTaskRunner;
        this.procedureFactory = newProcedureFactory;
        this.list = list;
        this.start = index * sectionSize;
        this.end = isLast ? this.list.size() - 1 : this.start + sectionSize - 1;
    }

    @Override
    protected boolean exec()
    {
        try
        {
            this.procedure = this.procedureFactory.create();
            if (this.list instanceof ListIterable)
            {
                ((ListIterable<T>) this.list).forEach(this.start, this.end, this.procedure);
            }
            else if (this.list instanceof ArrayList)
            {
                ArrayListIterate.forEach((ArrayList<T>) this.list, this.start, this.end, this.procedure);
            }
            else
            {
                ListIterate.forEach(this.list, this.start, this.end, this.procedure);
            }
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
