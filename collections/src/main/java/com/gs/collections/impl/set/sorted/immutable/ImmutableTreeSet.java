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

package com.gs.collections.impl.set.sorted.immutable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import net.jcip.annotations.Immutable;

@Immutable
final class ImmutableTreeSet<T>
        extends AbstractImmutableSortedSet<T>
        implements Serializable
{
    private static final long serialVersionUID = 2L;
    private final T[] delegate;
    private final Comparator<? super T> comparator;

    private ImmutableTreeSet(SortedSet<T> sortedSet)
    {
        this.delegate = (T[]) sortedSet.toArray();
        this.comparator = sortedSet.comparator();
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

    public int size()
    {
        return this.delegate.length;
    }

    private Object writeReplace()
    {
        return new ImmutableSortedSetSerializationProxy<T>(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (!(obj instanceof Set))
        {
            return false;
        }
        Set<?> otherSet = (Set<?>) obj;
        if (otherSet.size() != this.size())
        {
            return false;
        }
        try
        {
            return this.containsAll(otherSet);
        }
        catch (ClassCastException ignored)
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int result = 0;
        for (T each : this.delegate)
        {
            result += each.hashCode();
        }

        return result;
    }

    @Override
    public boolean contains(Object object)
    {
        return Arrays.binarySearch(this.delegate, (T) object, this.comparator) >= 0;
    }

    public Iterator<T> iterator()
    {
        return FastList.newListWith(this.delegate).asUnmodifiable().iterator();
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.each(procedure);
    }

    public void each(Procedure<? super T> procedure)
    {
        for (T t : this.delegate)
        {
            procedure.value(t);
        }
    }

    public T first()
    {
        return this.delegate[0];
    }

    public T last()
    {
        return this.delegate[this.delegate.length - 1];
    }

    public Comparator<? super T> comparator()
    {
        return this.comparator;
    }

    public int compareTo(SortedSetIterable<T> otherSet)
    {
        Iterator<T> iterator = otherSet.iterator();

        for (T eachInThis : this.delegate)
        {
            if (!iterator.hasNext())
            {
                return 1;
            }

            T eachInOther = iterator.next();

            int compare = this.compare(eachInThis, eachInOther);
            if (compare != 0)
            {
                return compare;
            }
        }

        return iterator.hasNext() ? -1 : 0;
    }

    private int compare(T o1, T o2)
    {
        return this.comparator == null
                ? ((Comparable<T>) o1).compareTo(o2)
                : this.comparator.compare(o1, o2);
    }
}
