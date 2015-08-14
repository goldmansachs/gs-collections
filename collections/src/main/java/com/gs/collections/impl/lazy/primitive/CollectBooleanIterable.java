/*
 * Copyright 2015 Goldman Sachs.
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

import java.util.Iterator;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectBooleanIterable<T>
        extends AbstractLazyBooleanIterable
{
    private final LazyIterable<T> iterable;
    private final BooleanFunction<? super T> function;
    private final BooleanFunctionToProcedure<T> booleanFunctionToProcedure;

    public CollectBooleanIterable(LazyIterable<T> adapted, BooleanFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
        this.booleanFunctionToProcedure = new BooleanFunctionToProcedure<T>(function);
    }

    public BooleanIterator booleanIterator()
    {
        return new BooleanIterator()
        {
            private final Iterator<T> iterator = CollectBooleanIterable.this.iterable.iterator();

            public boolean next()
            {
                return CollectBooleanIterable.this.function.booleanValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(BooleanProcedure procedure)
    {
        this.each(procedure);
    }

    /**
     * @since 7.0.
     */
    public void each(BooleanProcedure procedure)
    {
        this.iterable.forEachWith(this.booleanFunctionToProcedure, procedure);
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
    public int count(final BooleanPredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectBooleanIterable.this.function.booleanValueOf(each));
            }
        });
    }

    @Override
    public boolean anySatisfy(final BooleanPredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectBooleanIterable.this.function.booleanValueOf(each));
            }
        });
    }

    @Override
    public boolean allSatisfy(final BooleanPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectBooleanIterable.this.function.booleanValueOf(each));
            }
        });
    }

    @Override
    public boolean noneSatisfy(final BooleanPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectBooleanIterable.this.function.booleanValueOf(each));
            }
        });
    }

    @Override
    public boolean[] toArray()
    {
        final boolean[] array = new boolean[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectBooleanIterable.this.function.booleanValueOf(each);
            }
        });
        return array;
    }

    @Override
    public MutableBooleanList toList()
    {
        return BooleanArrayList.newList(this);
    }

    @Override
    public MutableBooleanSet toSet()
    {
        return BooleanHashSet.newSet(this);
    }

    @Override
    public MutableBooleanBag toBag()
    {
        return BooleanHashBag.newBag(this);
    }

    @Override
    public boolean containsAll(boolean... source)
    {
        for (boolean value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(BooleanIterable source)
    {
        for (BooleanIterator iterator = source.booleanIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private static final class BooleanFunctionToProcedure<T> implements Procedure2<T, BooleanProcedure>
    {
        private static final long serialVersionUID = 1L;
        private final BooleanFunction<? super T> function;

        private BooleanFunctionToProcedure(BooleanFunction<? super T> function)
        {
            this.function = function;
        }

        public void value(T each, BooleanProcedure procedure)
        {
            procedure.value(this.function.booleanValueOf(each));
        }
    }
}
