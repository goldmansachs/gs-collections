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
import com.gs.collections.impl.utility.ArrayIterate;

public final class ArrayProcedureFJTask<T, BT extends Procedure<? super T>> implements Runnable
{
    private final ProcedureFactory<BT> procedureFactory;
    private BT procedure;
    private final T[] array;
    private final int start;
    private final int end;
    private final ArrayProcedureFJTaskRunner<T, BT> taskRunner;

    public ArrayProcedureFJTask(
            ArrayProcedureFJTaskRunner<T, BT> newFJTaskRunner,
            ProcedureFactory<BT> procedureFactory,
            T[] newArray,
            int newIndex,
            int newSectionSize,
            boolean isLast)
    {
        this.taskRunner = newFJTaskRunner;
        this.procedureFactory = procedureFactory;
        this.array = newArray;
        this.start = newIndex * newSectionSize;
        this.end = isLast ? this.array.length : this.start + newSectionSize;
    }

    public void run()
    {
        try
        {
            this.procedure = this.procedureFactory.create();
            ArrayIterate.forEach(this.array, this.start, this.end - 1, this.procedure);
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
