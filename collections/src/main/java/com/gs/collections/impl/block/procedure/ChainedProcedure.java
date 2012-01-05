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

package com.gs.collections.impl.block.procedure;

import java.util.List;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.list.mutable.FastList;

/**
 * ChainedProcedure allows a developer to chain together procedure to be executed in sequence.
 */
public final class ChainedProcedure<T> implements Procedure<T>
{
    private static final long serialVersionUID = 1L;

    private final List<Procedure<? super T>> procedures = FastList.newList(3);

    public static <E> ChainedProcedure<E> with(Procedure<? super E> procedure1, Procedure<? super E> procedure2)
    {
        ChainedProcedure<E> chainedProcedure = new ChainedProcedure<E>();
        chainedProcedure.addProcedure(procedure1);
        chainedProcedure.addProcedure(procedure2);
        return chainedProcedure;
    }

    public void addProcedure(Procedure<? super T> procedure)
    {
        this.procedures.add(procedure);
    }

    public void value(T object)
    {
        int size = this.procedures.size();
        for (int i = 0; i < size; i++)
        {
            this.procedures.get(i).value(object);
        }
    }

    @Override
    public String toString()
    {
        return "ChainedProcedure.with(" + this.procedures + ')';
    }
}
