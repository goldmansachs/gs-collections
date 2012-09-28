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

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.FloatToObjectFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.FloatPredicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.FloatProcedure;
import com.gs.collections.api.iterator.FloatIterator;
import com.gs.collections.api.primitive.FloatIterable;
import net.jcip.annotations.Immutable;

/**
 * A CollectFloatIterable is an iterable that transforms a source iterable using a FloatFunction as it iterates.
 */
@Immutable
public class CollectFloatIterable<T>
        implements FloatIterable
{
    private final LazyIterable<T> iterable;
    private final FloatFunction<? super T> function;
    private final FloatFunctionToProcedure floatFunctionToProcedure = new FloatFunctionToProcedure();

    public CollectFloatIterable(LazyIterable<T> adapted, FloatFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public FloatIterator floatIterator()
    {
        return new FloatIterator()
        {
            private final Iterator<T> iterator = CollectFloatIterable.this.iterable.iterator();

            public float next()
            {
                return CollectFloatIterable.this.function.floatValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(FloatProcedure procedure)
    {
        this.iterable.forEachWith(this.floatFunctionToProcedure, procedure);
    }

    public int size()
    {
        return this.iterable.size();
    }

    public int count(final FloatPredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectFloatIterable.this.function.floatValueOf(each));
            }
        });
    }

    public boolean anySatisfy(final FloatPredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectFloatIterable.this.function.floatValueOf(each));
            }
        });
    }

    public boolean allSatisfy(final FloatPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectFloatIterable.this.function.floatValueOf(each));
            }
        });
    }

    public <V> RichIterable<V> collect(FloatToObjectFunction<? extends V> function)
    {
        return new CollectFloatToObjectIterable<T, V>(this, function);
    }

    public double sum()
    {
        return this.iterable.injectInto(0.0d, new DoubleObjectToDoubleFunction<T>()
        {
            public double doubleValueOf(double doubleValue, T each)
            {
                return doubleValue + (double) CollectFloatIterable.this.function.floatValueOf(each);
            }
        });
    }

    public float max()
    {
        return this.iterable.injectInto(Float.MIN_VALUE, new FloatObjectToFloatFunction<T>()
        {
            public float floatValueOf(float floatValue, T each)
            {
                return Math.max(floatValue, CollectFloatIterable.this.function.floatValueOf(each));
            }
        });
    }

    public float min()
    {
        return this.iterable.injectInto(Float.MAX_VALUE, new FloatObjectToFloatFunction<T>()
        {
            public float floatValueOf(float floatValue, T each)
            {
                return Math.min(floatValue, CollectFloatIterable.this.function.floatValueOf(each));
            }
        });
    }

    public double average()
    {
        return this.sum() / (double) this.size();
    }

    public double median()
    {
        float[] sortedArray = this.toSortedArray();
        int i = sortedArray.length >> 1;
        if (sortedArray.length > 1 && (sortedArray.length & 1) == 0)
        {
            float first = sortedArray[i];
            float second = sortedArray[i - 1];
            return ((double) first + (double) second) / 2.0d;
        }
        return (double) sortedArray[i];
    }

    public float[] toArray()
    {
        final float[] array = new float[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectFloatIterable.this.function.floatValueOf(each);
            }
        });
        return array;
    }

    public float[] toSortedArray()
    {
        float[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    private final class FloatFunctionToProcedure implements Procedure2<T, FloatProcedure>
    {
        private static final long serialVersionUID = 5812943420002956844L;

        public void value(T each, FloatProcedure parm)
        {
            parm.value(CollectFloatIterable.this.function.floatValueOf(each));
        }
    }
}
