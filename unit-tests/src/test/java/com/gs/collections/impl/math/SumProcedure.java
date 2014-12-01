/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.math;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.parallel.ProcedureFactory;

/**
 * A multi-purpose code block factory which can be used to summarize the elements of a collection
 * either via a forEach() or injectInto() call.  SumProcedure returns optimized iterator blocks
 * for specialized iterator subclasses of Function which result in less garbage created for
 * summing iterator attributes of collections.
 *
 * @deprecated Don't use in new tests
 */
@Deprecated
public class SumProcedure<T>
        implements Procedure<T>, Function2<Sum, T, Sum>, ProcedureFactory<SumProcedure<T>>
{
    private static final long serialVersionUID = 1L;
    private static final SumProcedure<?> NUMBER = new SumProcedure<>();

    protected final Sum sum;
    protected final Function<? super T, ? extends Number> function;

    public SumProcedure(Sum newSum)
    {
        this(newSum, null);
    }

    public SumProcedure()
    {
        this(null, null);
    }

    public SumProcedure(Sum newSum, Function<? super T, ? extends Number> function)
    {
        this.sum = newSum;
        this.function = function;
    }

    public static <T extends Number> SumProcedure<T> number()
    {
        return (SumProcedure<T>) NUMBER;
    }

    @Override
    public SumProcedure<T> create()
    {
        return new SumProcedure<>(this.sum.speciesNew(), this.function);
    }

    @Override
    public Sum value(Sum argument1, T argument2)
    {
        return argument1.add(argument2);
    }

    @Override
    public void value(T object)
    {
        this.sum.add(object);
    }

    public Sum getSum()
    {
        return this.sum;
    }
}
