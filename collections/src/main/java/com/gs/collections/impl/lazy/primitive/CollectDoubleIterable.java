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

package com.gs.collections.impl.lazy.primitive;

import java.util.Arrays;
import java.util.Iterator;

import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableDoubleBag;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.DoublePredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.DoubleProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.DoubleIterator;
import com.gs.collections.api.list.primitive.MutableDoubleList;
import com.gs.collections.api.set.primitive.MutableDoubleSet;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.set.mutable.primitive.DoubleHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectDoubleIterable is an iterable that transforms a source iterable using a DoubleFunction as it iterates.
 */
@Immutable
public class CollectDoubleIterable<T>
        extends AbstractLazyDoubleIterable
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

    @Override
    public int size()
    {
        return this.iterable.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.iterable.isEmpty();
    }

    @Override
    public boolean notEmpty()
    {
        return this.iterable.notEmpty();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean noneSatisfy(final DoublePredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectDoubleIterable.this.function.doubleValueOf(each));
            }
        });
    }

    @Override
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

    @Override
    public double[] toSortedArray()
    {
        double[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    @Override
    public MutableDoubleList toList()
    {
        return DoubleArrayList.newList(this);
    }

    @Override
    public MutableDoubleList toSortedList()
    {
        return DoubleArrayList.newList(this).sortThis();
    }

    @Override
    public MutableDoubleSet toSet()
    {
        return DoubleHashSet.newSet(this);
    }

    @Override
    public MutableDoubleBag toBag()
    {
        return DoubleHashBag.newBag(this);
    }

    @Override
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

    @Override
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
