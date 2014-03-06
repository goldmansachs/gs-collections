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

import com.gs.collections.api.CharIterable;
import com.gs.collections.api.LazyCharIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.CharPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.CharProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.CharIterator;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.set.primitive.MutableCharSet;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.set.mutable.primitive.CharHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectCharIterable<T>
        extends AbstractLazyCharIterable
{
    private final LazyIterable<T> iterable;
    private final CharFunction<? super T> function;
    private final CharFunctionToProcedure charFunctionToProcedure = new CharFunctionToProcedure();

    public CollectCharIterable(LazyIterable<T> adapted, CharFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public CharIterator charIterator()
    {
        return new CharIterator()
        {
            private final Iterator<T> iterator = CollectCharIterable.this.iterable.iterator();

            public char next()
            {
                return CollectCharIterable.this.function.charValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(CharProcedure procedure)
    {
        this.iterable.forEachWith(this.charFunctionToProcedure, procedure);
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
    public int count(final CharPredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectCharIterable.this.function.charValueOf(each));
            }
        });
    }

    @Override
    public boolean anySatisfy(final CharPredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectCharIterable.this.function.charValueOf(each));
            }
        });
    }

    @Override
    public boolean allSatisfy(final CharPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectCharIterable.this.function.charValueOf(each));
            }
        });
    }

    @Override
    public boolean noneSatisfy(final CharPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectCharIterable.this.function.charValueOf(each));
            }
        });
    }

    @Override
    public char[] toArray()
    {
        final char[] array = new char[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectCharIterable.this.function.charValueOf(each);
            }
        });
        return array;
    }

    @Override
    public char[] toSortedArray()
    {
        char[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    @Override
    public MutableCharList toList()
    {
        return CharArrayList.newList(this);
    }

    @Override
    public MutableCharList toSortedList()
    {
        return CharArrayList.newList(this).sortThis();
    }

    @Override
    public MutableCharSet toSet()
    {
        return CharHashSet.newSet(this);
    }

    @Override
    public MutableCharBag toBag()
    {
        return CharHashBag.newBag(this);
    }

    @Override
    public LazyCharIterable asLazy()
    {
        return this;
    }

    @Override
    public boolean containsAll(char... source)
    {
        for (char value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(CharIterable source)
    {
        for (CharIterator iterator = source.charIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private final class CharFunctionToProcedure implements Procedure2<T, CharProcedure>
    {
        private static final long serialVersionUID = -4133872659735979655L;

        public void value(T each, CharProcedure parm)
        {
            parm.value(CollectCharIterable.this.function.charValueOf(each));
        }
    }
}
