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

import com.gs.collections.api.ByteIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.BytePredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ByteProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.ByteIterator;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.set.primitive.MutableByteSet;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.set.mutable.primitive.ByteHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectByteIterable<T>
        extends AbstractLazyByteIterable
{
    private final LazyIterable<T> iterable;
    private final ByteFunction<? super T> function;
    private final ByteFunctionToProcedure byteFunctionToProcedure = new ByteFunctionToProcedure();

    public CollectByteIterable(LazyIterable<T> adapted, ByteFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public ByteIterator byteIterator()
    {
        return new ByteIterator()
        {
            private final Iterator<T> iterator = CollectByteIterable.this.iterable.iterator();

            public byte next()
            {
                return CollectByteIterable.this.function.byteValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(ByteProcedure procedure)
    {
        this.iterable.forEachWith(this.byteFunctionToProcedure, procedure);
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
    public int count(final BytePredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectByteIterable.this.function.byteValueOf(each));
            }
        });
    }

    @Override
    public boolean anySatisfy(final BytePredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectByteIterable.this.function.byteValueOf(each));
            }
        });
    }

    @Override
    public boolean allSatisfy(final BytePredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectByteIterable.this.function.byteValueOf(each));
            }
        });
    }

    @Override
    public boolean noneSatisfy(final BytePredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectByteIterable.this.function.byteValueOf(each));
            }
        });
    }

    @Override
    public byte[] toArray()
    {
        final byte[] array = new byte[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectByteIterable.this.function.byteValueOf(each);
            }
        });
        return array;
    }

    @Override
    public byte[] toSortedArray()
    {
        byte[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    @Override
    public MutableByteList toList()
    {
        return ByteArrayList.newList(this);
    }

    @Override
    public MutableByteList toSortedList()
    {
        return ByteArrayList.newList(this).sortThis();
    }

    @Override
    public MutableByteSet toSet()
    {
        return ByteHashSet.newSet(this);
    }

    @Override
    public MutableByteBag toBag()
    {
        return ByteHashBag.newBag(this);
    }

    @Override
    public boolean containsAll(byte... source)
    {
        for (byte value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(ByteIterable source)
    {
        for (ByteIterator iterator = source.byteIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private final class ByteFunctionToProcedure implements Procedure2<T, ByteProcedure>
    {
        private static final long serialVersionUID = -4133872659735979655L;

        public void value(T each, ByteProcedure parm)
        {
            parm.value(CollectByteIterable.this.function.byteValueOf(each));
        }
    }
}
