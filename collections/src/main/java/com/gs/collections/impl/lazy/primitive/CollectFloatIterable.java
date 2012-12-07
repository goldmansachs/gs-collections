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

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import com.gs.collections.api.FloatIterable;
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
import com.gs.collections.impl.block.factory.primitive.FloatPredicates;
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

    public boolean isEmpty()
    {
        return this.iterable.isEmpty();
    }

    public boolean notEmpty()
    {
        return this.iterable.notEmpty();
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

    public FloatIterable select(FloatPredicate predicate)
    {
        return new SelectFloatIterable(this, predicate);
    }

    public FloatIterable reject(FloatPredicate predicate)
    {
        return new SelectFloatIterable(this, FloatPredicates.not(predicate));
    }

    public float detectIfNone(FloatPredicate predicate, float ifNone)
    {
        FloatIterator iterator = this.floatIterator();
        while (iterator.hasNext())
        {
            float next = iterator.next();
            if (predicate.accept(next))
            {
                return next;
            }
        }
        return ifNone;
    }

    public <V> RichIterable<V> collect(FloatToObjectFunction<? extends V> function)
    {
        return new CollectFloatToObjectIterable<V>(this, function);
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

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public String makeString()
    {
        return this.makeString(", ");
    }

    public String makeString(String separator)
    {
        return this.makeString("", separator, "");
    }

    public String makeString(String start, String separator, String end)
    {
        Appendable stringBuilder = new StringBuilder();
        this.appendString(stringBuilder, start, separator, end);
        return stringBuilder.toString();
    }

    public void appendString(Appendable appendable)
    {
        this.appendString(appendable, ", ");
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.appendString(appendable, "", separator, "");
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);

            FloatIterator iterator = this.floatIterator();
            if (iterator.hasNext())
            {
                appendable.append(String.valueOf(iterator.next()));
                while (iterator.hasNext())
                {
                    appendable.append(separator);
                    appendable.append(String.valueOf(iterator.next()));
                }
            }

            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean contains(float value)
    {
        return this.anySatisfy(FloatPredicates.equal(value));
    }

    public boolean containsAll(float... source)
    {
        for (float value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
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
