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

package com.gs.collections.impl.collection.immutable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.AbstractRichIterable;
import com.gs.collections.impl.block.procedure.PartitionPredicate2Procedure;
import com.gs.collections.impl.block.procedure.PartitionProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.partition.bag.PartitionHashBag;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public abstract class AbstractImmutableCollection<T> extends AbstractRichIterable<T> implements ImmutableCollection<T>, Collection<T>
{
    protected abstract MutableCollection<T> newMutable(int size);

    @Override
    public abstract <S> ImmutableCollection<Pair<T, S>> zip(Iterable<S> that);

    @Override
    public abstract ImmutableCollection<Pair<T, Integer>> zipWithIndex();

    @Override
    public PartitionImmutableCollection<T> partition(Predicate<? super T> predicate)
    {
        PartitionMutableBag<T> partitionHashBag = new PartitionHashBag<T>();
        this.forEach(new PartitionProcedure<T>(predicate, partitionHashBag));
        return partitionHashBag.toImmutable();
    }

    @Override
    public <P> PartitionImmutableCollection<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        PartitionMutableBag<T> partitionHashBag = new PartitionHashBag<T>();
        this.forEach(new PartitionPredicate2Procedure<T, P>(predicate, parameter, partitionHashBag));
        return partitionHashBag.toImmutable();
    }

    protected void removeAllFrom(Iterable<? extends T> elements, MutableCollection<T> result)
    {
        if (elements instanceof Set)
        {
            result.removeAll((Set<?>) elements);
        }
        else if (elements instanceof List)
        {
            List<T> toBeRemoved = (List<T>) elements;
            if (this.size() * toBeRemoved.size() > 10000)
            {
                result.removeAll(UnifiedSet.newSet(elements));
            }
            else
            {
                result.removeAll(toBeRemoved);
            }
        }
        else
        {
            result.removeAll(UnifiedSet.newSet(elements));
        }
    }

    public boolean add(T t)
    {
        throw new UnsupportedOperationException("Cannot add to an Immutable Collection");
    }

    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot add to an Immutable Collection");
    }

    public boolean addAll(Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException();
    }

    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        Iterator<T> iterator = this.iterator();
        MutableList<RichIterable<T>> result = Lists.mutable.of();
        while (iterator.hasNext())
        {
            MutableCollection<T> batch = this.newMutable(size);
            for (int i = 0; i < size && iterator.hasNext(); i++)
            {
                batch.add(iterator.next());
            }
            result.add(batch.toImmutable());
        }
        return result.toImmutable();
    }
}
