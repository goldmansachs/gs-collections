/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.set.sorted.immutable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.impl.UnmodifiableIteratorAdapter;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import net.jcip.annotations.Immutable;

@Immutable
final class ImmutableTreeSet<T>
        extends AbstractImmutableSortedSet<T>
        implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final TreeSortedSet<T> delegate;

    private ImmutableTreeSet(TreeSortedSet<T> delegate)
    {
        this.delegate = delegate;
    }

    public int size()
    {
        return this.delegate.size();
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.delegate.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    @Override
    public boolean contains(Object object)
    {
        return this.delegate.contains(object);
    }

    @Override
    public Iterator<T> iterator()
    {
        return new UnmodifiableIteratorAdapter<T>(this.delegate.iterator());
    }

    public static <T> ImmutableSortedSet<T> newSetWith(T... elements)
    {
        return new ImmutableTreeSet<T>(TreeSortedSet.newSetWith(elements));
    }

    public static <T> ImmutableSortedSet<T> newSetWith(Comparator<? super T> comparator, T... elements)
    {
        return new ImmutableTreeSet<T>(TreeSortedSet.newSetWith(comparator, elements));
    }

    public static <T> ImmutableSortedSet<T> newSet(SortedSet<T> set)
    {
        return new ImmutableTreeSet<T>(TreeSortedSet.newSet(set));
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.delegate.forEach(procedure);
    }

    public T first()
    {
        return this.delegate.first();
    }

    public T last()
    {
        return this.delegate.last();
    }

    public Comparator<? super T> comparator()
    {
        return this.delegate.comparator();
    }

    public int compareTo(SortedSetIterable<T> o)
    {
        return this.delegate.compareTo(o);
    }
}
