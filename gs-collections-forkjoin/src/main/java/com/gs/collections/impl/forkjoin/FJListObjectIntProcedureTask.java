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

import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.impl.parallel.ObjectIntProcedureFactory;
import com.gs.collections.impl.utility.ArrayListIterate;
import com.gs.collections.impl.utility.ListIterate;

public class FJListObjectIntProcedureTask<T, PT extends ObjectIntProcedure<? super T>> extends ForkJoinTask<PT>
{
    private final ObjectIntProcedureFactory<PT> procedureFactory;
    private PT procedure;
    private final List<T> list;
    private final int start;
    private final int end;
    private final FJListObjectIntProcedureRunner<T, PT> taskRunner;

    /**
     * Creates an array of VoidBlockFJTasks wrapping VoidBlocks created by the specified VoidBlockFactory.
     */
    public FJListObjectIntProcedureTask(
            FJListObjectIntProcedureRunner<T, PT> newFJTaskRunner, ObjectIntProcedureFactory<PT> newBlockFactory,
            List<T> list, int index, int sectionSize, boolean isLast)
    {
        this.taskRunner = newFJTaskRunner;
        this.procedureFactory = newBlockFactory;
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
                ((ListIterable<T>) this.list).forEachWithIndex(this.start, this.end, this.procedure);
            }
            else if (this.list instanceof ArrayList)
            {
                ArrayListIterate.forEachWithIndex((ArrayList<T>) this.list, this.start, this.end, this.procedure);
            }
            else
            {
                ListIterate.forEachWithIndex(this.list, this.start, this.end, this.procedure);
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
