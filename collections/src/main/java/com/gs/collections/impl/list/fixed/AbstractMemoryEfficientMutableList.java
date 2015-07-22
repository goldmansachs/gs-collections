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

package com.gs.collections.impl.list.fixed;

import java.util.Collection;
import java.util.ListIterator;
import java.util.RandomAccess;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.FixedSizeList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.AbstractMutableList;
import com.gs.collections.impl.utility.Iterate;

public abstract class AbstractMemoryEfficientMutableList<T>
        extends AbstractMutableList<T>
        implements FixedSizeList<T>, RandomAccess
{
    @Override
    public FixedSizeList<T> clone()
    {
        return (FixedSizeList<T>) super.clone();
    }

    @Override
    public boolean add(T o)
    {
        throw new UnsupportedOperationException("Cannot add to a fixed size list: " + this.getClass());
    }

    public void add(int index, T element)
    {
        throw new UnsupportedOperationException("Cannot add to a fixed size list: " + this.getClass());
    }

    @Override
    public boolean addAll(Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException("Cannot add to a fixed size list: " + this.getClass());
    }

    public boolean addAll(int index, Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException("Cannot add to a fixed size list: " + this.getClass());
    }

    @Override
    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        throw new UnsupportedOperationException("Cannot add to a fixed size list: " + this.getClass());
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
    }

    public T remove(int index)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
    }

    @Override
    public boolean removeIf(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
    }

    @Override
    public <P> boolean removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException("Cannot removeIfWith from a fixed size list: " + this.getClass());
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
    }

    @Override
    public boolean retainAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
    }

    public void clear()
    {
        throw new UnsupportedOperationException("Cannot clear a fixed size list: " + this.getClass());
    }

    @Override
    public FixedSizeList<T> toReversed()
    {
        FixedSizeList<T> result = Lists.fixedSize.withAll(this);
        result.reverseThis();
        return result;
    }

    @Override
    public MutableList<T> subList(int fromIndex, int toIndex)
    {
        return new SubList<T>(this, fromIndex, toIndex);
    }

    @Override
    public MutableList<T> without(T element)
    {
        if (this.contains(element))
        {
            return Lists.fixedSize.ofAll(this.toList().without(element));
        }
        return this;
    }

    @Override
    public MutableList<T> withAll(Iterable<? extends T> elements)
    {
        if (Iterate.isEmpty(elements))
        {
            return this;
        }
        return Lists.fixedSize.ofAll(this.toList().withAll(elements));
    }

    @Override
    public MutableList<T> withoutAll(Iterable<? extends T> elements)
    {
        if (Iterate.isEmpty(elements))
        {
            return this;
        }
        return Lists.fixedSize.ofAll(this.toList().withoutAll(elements));
    }

    private static class SubList<T>
            extends AbstractMutableList.SubList<T>
    {
        // Not important since it uses writeReplace()
        private static final long serialVersionUID = 1L;

        protected SubList(AbstractMutableList<T> list, int fromIndex, int toIndex)
        {
            super(list, fromIndex, toIndex);
        }

        @Override
        public boolean remove(Object o)
        {
            throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
        }

        @Override
        public boolean removeAll(Collection<?> collection)
        {
            throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
        }

        @Override
        public boolean removeAllIterable(Iterable<?> iterable)
        {
            throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
        }

        @Override
        public boolean retainAll(Collection<?> collection)
        {
            throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
        }

        @Override
        public boolean retainAllIterable(Iterable<?> iterable)
        {
            throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
        }

        @Override
        public boolean removeIf(Predicate<? super T> predicate)
        {
            throw new UnsupportedOperationException("Cannot remove from a fixed size list: " + this.getClass());
        }

        @Override
        public <P> boolean removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
        {
            throw new UnsupportedOperationException("Cannot removeIfWith from a fixed size list: " + this.getClass());
        }

        @Override
        public boolean addAll(Collection<? extends T> collection)
        {
            throw new UnsupportedOperationException("Cannot add to a fixed size list: " + this.getClass());
        }

        @Override
        public boolean addAllIterable(Iterable<? extends T> iterable)
        {
            throw new UnsupportedOperationException("Cannot add to a fixed size list: " + this.getClass());
        }
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        return new FixedSizeListIteratorAdapter<T>(super.listIterator(index));
    }

    @Override
    public ListIterator<T> listIterator()
    {
        return new FixedSizeListIteratorAdapter<T>(super.listIterator());
    }
}
