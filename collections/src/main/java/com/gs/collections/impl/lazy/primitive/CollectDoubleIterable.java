/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.lazy.primitive;

import java.util.Arrays;
import java.util.Iterator;

import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.DoubleProcedure;
import com.gs.collections.api.iterator.DoubleIterator;
import net.jcip.annotations.Immutable;

/**
 * A CollectDoubleIterable is an iterable that transforms a source iterable using a DoubleFunction as it iterates.
 */
@Immutable
public class CollectDoubleIterable<T>
        implements DoubleIterable
{
    private final LazyIterable<T> iterable;
    private final DoubleFunction<? super T> function;
    private final DoubleFunctionToProcedure floatFunctionToProcedure = new DoubleFunctionToProcedure();

    public CollectDoubleIterable(LazyIterable<T> adapted, DoubleFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public DoubleIterator iterator()
    {
        return new DoubleIterator()
        {
            private final Iterator<T> iterator = CollectDoubleIterable.this.iterable.iterator();

            public double next()
            {
                return CollectDoubleIterable.this.function.doubleValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(DoubleProcedure procedure)
    {
        this.iterable.forEachWith(this.floatFunctionToProcedure, procedure);
    }

    public int size()
    {
        return this.iterable.size();
    }

    public double sum()
    {
        return this.iterable.injectInto(0.0d, new DoubleObjectToDoubleFunction<T>()
        {
            public double doubleValueOf(double doubleValue, T each)
            {
                return doubleValue + (double) CollectDoubleIterable.this.function.doubleValueOf(each);
            }
        });
    }

    public double max()
    {
        return this.iterable.injectInto(Double.MIN_VALUE, new DoubleObjectToDoubleFunction<T>()
        {
            public double doubleValueOf(double doubleValue, T each)
            {
                return Math.max(doubleValue, CollectDoubleIterable.this.function.doubleValueOf(each));
            }
        });
    }

    public double min()
    {
        return this.iterable.injectInto(Double.MAX_VALUE, new DoubleObjectToDoubleFunction<T>()
        {
            public double doubleValueOf(double doubleValue, T each)
            {
                return Math.min(doubleValue, CollectDoubleIterable.this.function.doubleValueOf(each));
            }
        });
    }

    public double average()
    {
        return this.sum() / (double) this.size();
    }

    public double median()
    {
        double[] sortedArray = this.toSortedArray();
        int i = sortedArray.length >> 1;
        if (sortedArray.length > 1 && (sortedArray.length & 1) == 0)
        {
            double first = sortedArray[i];
            double second = sortedArray[i - 1];
            return (first + second) / 2.0d;
        }
        return (double) sortedArray[i];
    }

    public double[] toArray()
    {
        final double[] array = new double[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectDoubleIterable.this.function.doubleValueOf(each);
            }
        });
        return array;
    }

    public double[] toSortedArray()
    {
        double[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    private final class DoubleFunctionToProcedure implements Procedure2<T, DoubleProcedure>
    {
        private static final long serialVersionUID = 8449781737918512474L;

        public void value(T each, DoubleProcedure parm)
        {
            parm.value(CollectDoubleIterable.this.function.doubleValueOf(each));
        }
    }
}
