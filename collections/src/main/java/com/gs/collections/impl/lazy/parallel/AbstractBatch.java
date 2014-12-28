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

package com.gs.collections.impl.lazy.parallel;

import java.util.Comparator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.procedure.CountProcedure;
import com.gs.collections.impl.block.procedure.DoubleSumResultHolder;
import com.gs.collections.impl.block.procedure.MaxByProcedure;
import com.gs.collections.impl.block.procedure.MaxComparatorProcedure;
import com.gs.collections.impl.block.procedure.MinByProcedure;
import com.gs.collections.impl.block.procedure.MinComparatorProcedure;
import com.gs.collections.impl.block.procedure.SumOfDoubleProcedure;
import com.gs.collections.impl.block.procedure.SumOfFloatProcedure;
import com.gs.collections.impl.block.procedure.SumOfIntProcedure;
import com.gs.collections.impl.block.procedure.SumOfLongProcedure;

public abstract class AbstractBatch<T> implements Batch<T>
{
    public int count(Predicate<? super T> predicate)
    {
        CountProcedure<T> procedure = new CountProcedure<T>(predicate);
        this.forEach(procedure);
        return procedure.getCount();
    }

    public String makeString(final String separator)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        this.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                if (stringBuilder.length() != 0)
                {
                    stringBuilder.append(separator);
                }
                stringBuilder.append(each);
            }
        });
        return stringBuilder.toString();
    }

    public T min(Comparator<? super T> comparator)
    {
        MinComparatorProcedure<T> procedure = new MinComparatorProcedure<T>(comparator);
        this.forEach(procedure);
        return procedure.isVisitedAtLeastOnce() ? procedure.getResult() : null;
    }

    public T max(Comparator<? super T> comparator)
    {
        MaxComparatorProcedure<T> procedure = new MaxComparatorProcedure<T>(comparator);
        this.forEach(procedure);
        return procedure.isVisitedAtLeastOnce() ? procedure.getResult() : null;
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        MinByProcedure<T, V> procedure = new MinByProcedure<T, V>(function);
        this.forEach(procedure);
        return procedure.isVisitedAtLeastOnce() ? procedure.getResult() : null;
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        MaxByProcedure<T, V> procedure = new MaxByProcedure<T, V>(function);
        this.forEach(procedure);
        return procedure.isVisitedAtLeastOnce() ? procedure.getResult() : null;
    }

    public long sumOfInt(IntFunction<? super T> function)
    {
        SumOfIntProcedure<T> procedure = new SumOfIntProcedure<T>(function);
        this.forEach(procedure);
        return procedure.getResult();
    }

    public DoubleSumResultHolder sumOfFloat(FloatFunction<? super T> function)
    {
        SumOfFloatProcedure<T> procedure = new SumOfFloatProcedure<T>(function);
        this.forEach(procedure);
        return procedure;
    }

    public long sumOfLong(LongFunction<? super T> function)
    {
        SumOfLongProcedure<T> procedure = new SumOfLongProcedure<T>(function);
        this.forEach(procedure);
        return procedure.getResult();
    }

    public DoubleSumResultHolder sumOfDouble(DoubleFunction<? super T> function)
    {
        SumOfDoubleProcedure<T> procedure = new SumOfDoubleProcedure<T>(function);
        this.forEach(procedure);
        return procedure;
    }
}
