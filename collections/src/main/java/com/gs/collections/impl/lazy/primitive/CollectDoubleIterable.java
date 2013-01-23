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

import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleToObjectFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.DoublePredicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.DoubleProcedure;
import com.gs.collections.api.iterator.DoubleIterator;
import com.gs.collections.impl.block.factory.primitive.DoublePredicates;
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
    private final DoubleFunctionToProcedure doubleFunctionToProcedure = new DoubleFunctionToProcedure();

    public CollectDoubleIterable(LazyIterable<T> adapted, DoubleFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public DoubleIterator doubleIterator()
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
        this.iterable.forEachWith(this.doubleFunctionToProcedure, procedure);
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

    public int count(final DoublePredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectDoubleIterable.this.function.doubleValueOf(each));
            }
        });
    }

    public boolean anySatisfy(final DoublePredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectDoubleIterable.this.function.doubleValueOf(each));
            }
        });
    }

    public double detectIfNone(DoublePredicate predicate, double ifNone)
    {
        DoubleIterator iterator = this.doubleIterator();
        while (iterator.hasNext())
        {
            double next = iterator.next();
            if (predicate.accept(next))
            {
                return next;
            }
        }
        return ifNone;
    }

    public boolean allSatisfy(final DoublePredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectDoubleIterable.this.function.doubleValueOf(each));
            }
        });
    }

    public DoubleIterable select(DoublePredicate predicate)
    {
        return new SelectDoubleIterable(this, predicate);
    }

    public DoubleIterable reject(DoublePredicate predicate)
    {
        return new SelectDoubleIterable(this, DoublePredicates.not(predicate));
    }

    public <V> RichIterable<V> collect(DoubleToObjectFunction<? extends V> function)
    {
        return new CollectDoubleToObjectIterable<V>(this, function);
    }

    public double sum()
    {
        return this.iterable.injectInto(0.0d, new DoubleObjectToDoubleFunction<T>()
        {
            public double doubleValueOf(double doubleValue, T each)
            {
                return doubleValue + CollectDoubleIterable.this.function.doubleValueOf(each);
            }
        });
    }

    public double max()
    {
        DoubleIterator iterator = this.doubleIterator();
        double max = iterator.next();
        while (iterator.hasNext())
        {
            max = Math.max(max, iterator.next());
        }
        return max;
    }

    public double min()
    {
        DoubleIterator iterator = this.doubleIterator();
        double min = iterator.next();
        while (iterator.hasNext())
        {
            min = Math.min(min, iterator.next());
        }
        return min;
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
        return sortedArray[i];
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

            DoubleIterator iterator = this.doubleIterator();
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

    public boolean contains(double value)
    {
        return this.anySatisfy(DoublePredicates.equal(value));
    }

    public boolean containsAll(double... source)
    {
        for (double value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(DoubleIterable source)
    {
        for (DoubleIterator iterator = source.doubleIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
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
