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

package com.gs.collections.impl.set.fixed;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.set.FixedSizeSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.set.mutable.AbstractMutableSet;
import com.gs.collections.impl.utility.Iterate;

abstract class AbstractMemoryEfficientMutableSet<T>
        extends AbstractMutableSet<T>
        implements FixedSizeSet<T>
{
    protected int nullSafeHashCode(Object element)
    {
        return element == null ? 0 : element.hashCode();
    }

    @Override
    public boolean addAll(Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException("Cannot add to a fixed size set: " + this.getClass());
    }

    @Override
    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        throw new UnsupportedOperationException("Cannot add to a fixed size set: " + this.getClass());
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size set: " + this.getClass());
    }

    @Override
    public boolean retainAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size set: " + this.getClass());
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size set: " + this.getClass());
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size set: " + this.getClass());
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size set: " + this.getClass());
    }

    @Override
    public boolean removeIf(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException("Cannot remove from a fixed size set: " + this.getClass());
    }

    @Override
    public <P> boolean removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException("Cannot removeIfWith from a fixed size set: " + this.getClass());
    }

    public void clear()
    {
        throw new UnsupportedOperationException("Cannot clear a fixed size set: " + this.getClass());
    }

    public MutableSet<T> withAll(Iterable<? extends T> elements)
    {
        if (Iterate.isEmpty(elements))
        {
            return this;
        }
        return Sets.fixedSize.ofAll(this.toList().withAll(elements));
    }

    public MutableSet<T> withoutAll(Iterable<? extends T> elements)
    {
        if (Iterate.isEmpty(elements))
        {
            return this;
        }
        return Sets.fixedSize.ofAll(this.toList().withoutAll(elements));
    }

    protected abstract class MemoryEfficientSetIterator
            implements Iterator<T>
    {
        private int next;    // next entry to return, defaults to 0

        protected abstract T getElement(int i);

        public boolean hasNext()
        {
            return this.next < AbstractMemoryEfficientMutableSet.this.size();
        }

        public T next()
        {
            return this.getElement(this.next++);
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove from a fixed size set: " + this.getClass());
        }
    }

    public ParallelUnsortedSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        return this.toSet().asParallel(executorService, batchSize);
    }
}
