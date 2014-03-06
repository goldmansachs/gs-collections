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

import com.gs.collections.api.FloatIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableFloatBag;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.FloatPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.FloatProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.FloatIterator;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.api.set.primitive.MutableFloatSet;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.set.mutable.primitive.FloatHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectFloatIterable is an iterable that transforms a source iterable using a FloatFunction as it iterates.
 */
@Immutable
public class CollectFloatIterable<T>
        extends AbstractLazyFloatIterable
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

    @Override
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

    @Override
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

    @Override
    public boolean noneSatisfy(final FloatPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectFloatIterable.this.function.floatValueOf(each));
            }
        });
    }

    @Override
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

    @Override
    public float[] toSortedArray()
    {
        float[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    @Override
    public MutableFloatList toList()
    {
        return FloatArrayList.newList(this);
    }

    @Override
    public MutableFloatList toSortedList()
    {
        return FloatArrayList.newList(this).sortThis();
    }

    @Override
    public MutableFloatSet toSet()
    {
        return FloatHashSet.newSet(this);
    }

    @Override
    public MutableFloatBag toBag()
    {
        return FloatHashBag.newBag(this);
    }

    @Override
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

    @Override
    public boolean containsAll(FloatIterable source)
    {
        for (FloatIterator iterator = source.floatIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
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
