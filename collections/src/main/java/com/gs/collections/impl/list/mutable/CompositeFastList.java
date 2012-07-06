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

package com.gs.collections.impl.list.mutable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.parallel.ParallelIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;

/**
 * CompositeFastList behaves like a list, but is composed of at least one list.
 * It is useful where you don't want the additional expense of appending several lists or allocating memory
 * for a super list to add multiple sublists to.<p>
 * <b>Note:</b> mutation operations (e.g. add and remove, sorting) will change the underlying
 * lists - so be sure to only use a composite list where it will be the only reference to the sublists
 * (for example, a composite list which contains multiple query results is OK as long
 * as it is the only thing that references the lists)
 */
public final class CompositeFastList<E>
        extends AbstractMutableList<E>
        implements Serializable
{
    private static final Predicate2<FastList<?>, Object> REMOVE_PREDICATE = new Predicate2<FastList<?>, Object>()
    {
        public boolean accept(FastList<?> list, Object toRemove)
        {
            return list.remove(toRemove);
        }
    };
    private static final Procedure<FastList<?>> REVERSE_LIST_PROCEDURE = new Procedure<FastList<?>>()
    {
        public void value(FastList<?> each)
        {
            each.reverseThis();
        }
    };

    private static final long serialVersionUID = 1L;
    private final FastList<FastList<E>> lists = FastList.newList();

    @Override
    public MutableList<E> clone()
    {
        // TODO: need to implement this at some point
        throw new UnsupportedOperationException();
    }

    @Override
    public int size()
    {
        int size = 0;
        for (int i = this.lists.size() - 1; i >= 0; i--)
        {
            size += this.lists.get(i).size();
        }
        return size;
    }

    @Override
    public CompositeFastList<E> reverseThis()
    {
        ParallelIterate.forEach(this.lists, REVERSE_LIST_PROCEDURE);
        this.lists.reverseThis();
        return this;
    }

    @Override
    public void forEach(final Procedure<? super E> procedure)
    {
        this.lists.forEach(new Procedure<FastList<E>>()
        {
            public void value(FastList<E> list)
            {
                list.forEach(procedure);
            }
        });
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super E> objectIntProcedure)
    {
        this.lists.forEach(new ProcedureToInnerListObjectIntProcedure<E>(objectIntProcedure));
    }

    @Override
    public void reverseForEach(final Procedure<? super E> procedure)
    {
        this.lists.reverseForEach(new Procedure<FastList<E>>()
        {
            public void value(FastList<E> each)
            {
                each.reverseForEach(procedure);
            }
        });
    }

    @Override
    public void forEach(int fromIndex, int toIndex, Procedure<? super E> procedure)
    {
        ListIterate.rangeCheck(fromIndex, toIndex, this.size());

        if (fromIndex > toIndex)
        {
            super.forEach(fromIndex, toIndex, procedure);
        }
        else
        {
            this.optimizedForwardForEach(procedure, fromIndex, toIndex);
        }
    }

    private void optimizedForwardForEach(Procedure<? super E> procedure, int fromIndex, int toIndex)
    {
        toIndex++;
        int outerIndex = 0;
        int size;
        // Find first list in the range
        FastList<FastList<E>> fastList = this.lists;
        Object[] items = fastList.items;
        while ((size = ((FastList<E>) items[outerIndex]).size()) <= fromIndex)
        {
            fromIndex -= size;
            toIndex -= size;
            outerIndex++;
        }
        while (true)
        {
            FastList<E> list = (FastList<E>) items[outerIndex++];
            int end = list.size();
            if (toIndex < end)
            {
                end = toIndex;
            }
            for (int j = fromIndex; j < end; j++)
            {
                procedure.value(list.items[j]);
            }
            toIndex -= end;
            if (toIndex == 0)
            {
                return;
            }
            fromIndex = 0;
        }
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super E> objectIntProcedure)
    {
        ListIterate.rangeCheck(fromIndex, toIndex, this.size());
        if (fromIndex > toIndex)
        {
            super.forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
        }
        else
        {
            this.optimizedForwardForEach(Procedures.fromObjectIntProcedure(objectIntProcedure), fromIndex, toIndex);
        }
    }

    @Override
    public <P> void forEachWith(
            final Procedure2<? super E, ? super P> procedure2,
            final P parameter)
    {
        this.lists.forEach(new Procedure<FastList<E>>()
        {
            public void value(FastList<E> list)
            {
                list.forEachWith(procedure2, parameter);
            }
        });
    }

    @Override
    public boolean isEmpty()
    {
        return this.lists.allSatisfy(new Predicate<FastList<E>>()
        {
            public boolean accept(FastList<E> list)
            {
                return list.isEmpty();
            }
        });
    }

    @Override
    public boolean contains(final Object object)
    {
        return this.lists.anySatisfy(new Predicate<FastList<E>>()
        {
            public boolean accept(FastList<E> list)
            {
                return list.contains(object);
            }
        });
    }

    @Override
    public Iterator<E> iterator()
    {
        if (this.lists.isEmpty())
        {
            return Collections.<E>emptyList().iterator();
        }
        return new CompositeIterator(this.lists);
    }

    @Override
    public Object[] toArray()
    {
        final Object[] result = new Object[this.size()];
        this.forEachWithIndex(new ObjectIntProcedure<E>()
        {
            public void value(E each, int index)
            {
                result[index] = each;
            }
        });
        return result;
    }

    @Override
    public boolean add(E object)
    {
        if (this.lists.isEmpty())
        {
            this.addComposited(FastList.<E>newList());
        }
        Collection<E> list = this.lists.getLast();
        return list.add(object);
    }

    @Override
    public boolean remove(Object object)
    {
        return this.lists.anySatisfyWith(REMOVE_PREDICATE, object);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection)
    {
        Collection<? extends E> collectionToAdd = collection instanceof FastList ? collection : new FastList<E>(collection);
        this.addComposited(collectionToAdd);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return Iterate.allSatisfy(collection, Predicates.in(this));
    }

    @Override
    public Object[] toArray(Object[] array)
    {
        int size = this.size();
        final Object[] result = array.length >= size
                ? array
                : (Object[]) Array.newInstance(array.getClass().getComponentType(), size);

        this.forEachWithIndex(new ObjectIntProcedure<E>()
        {
            public void value(E each, int index)
            {
                result[index] = each;
            }
        });

        if (result.length > size)
        {
            result[size] = null;
        }
        return result;
    }

    public void addComposited(Collection<? extends E> collection)
    {
        if (!(collection instanceof FastList))
        {
            throw new IllegalArgumentException("CompositeFastList can only add FastLists");
        }
        this.lists.add((FastList<E>) collection);
    }

    public boolean addAll(int index, Collection<? extends E> collection)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        this.lists.forEach(new Procedure<FastList<E>>()
        {
            public void value(FastList<E> object)
            {
                object.clear();
            }
        });
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        boolean changed = false;
        for (int i = this.lists.size() - 1; i >= 0; i--)
        {
            changed = this.lists.get(i).retainAll(collection) || changed;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        if (collection.isEmpty())
        {
            return false;
        }
        boolean changed = false;
        for (int i = this.lists.size() - 1; i >= 0; i--)
        {
            changed = this.lists.get(i).removeAll(collection) || changed;
        }
        return changed;
    }

    public E get(int index)
    {
        this.rangeCheck(index);
        int p = 0;
        int currentSize = this.lists.getFirst().size();
        while (index >= currentSize)
        {
            index -= currentSize;
            currentSize = this.lists.get(++p).size();
        }
        return this.lists.get(p).items[index];
    }

    private void rangeCheck(int index)
    {
        if (index >= this.size())
        {
            throw new IndexOutOfBoundsException("No such element " + index + " size: " + this.size());
        }
    }

    public E set(int index, E element)
    {
        this.rangeCheck(index);
        int p = 0;
        int currentSize = this.lists.getFirst().size();
        while (index >= currentSize)
        {
            index -= currentSize;
            currentSize = this.lists.get(++p).size();
        }
        return this.lists.get(p).set(index, element);
    }

    public void add(int index, E element)
    {
        int localSize = this.size();
        if (index > localSize || index < 0)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + localSize);
        }
        int max = 0;
        for (int i = 0; i < this.lists.size(); i++)
        {
            List<E> list = this.lists.get(i);
            int previousMax = max;
            max += list.size();
            if (index <= max)
            {
                list.add(index - previousMax, element);
                return;
            }
        }
    }

    public E remove(int index)
    {
        this.rangeCheck(index);
        int p = 0;
        int currentSize = this.lists.getFirst().size();
        while (index >= currentSize)
        {
            index -= currentSize;
            currentSize = this.lists.get(++p).size();
        }
        return this.lists.get(p).remove(index);
    }

    @Override
    public int indexOf(Object o)
    {
        int offset = 0;
        int listsSize = this.lists.size();
        for (int i = 0; i < listsSize; i++)
        {
            MutableList<E> list = this.lists.get(i);
            int index = list.indexOf(o);
            if (index > -1)
            {
                return index + offset;
            }
            offset += list.size();
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o)
    {
        int offset = this.size();
        for (int i = this.lists.size() - 1; i >= 0; i--)
        {
            MutableList<E> list = this.lists.get(i);
            offset -= list.size();
            int index = list.lastIndexOf(o);
            if (index > -1)
            {
                return index + offset;
            }
        }
        return -1;
    }

    /**
     * a list iterator is a problem for a composite list as going back in the order of the list is an issue,
     * as are the other methods like set() and add() (and especially, remove).
     * Convert the internal lists to one list (if not already just one list)
     * and return that list's list iterator.
     * <p/>
     * AFAIK list iterator is only commonly used in sorting.
     *
     * @return a ListIterator for this, with internal state convertedto one list if needed.
     */
    @Override
    public ListIterator<E> listIterator()
    {
        return this.listIterator(0);
    }

    /**
     * a llst iterator is a problem for a composite list as going back in the order of the list is an issue,
     * as are the other methods like set() and add() (and especially, remove).
     * Convert the internal lists to one list (if not already just one list)
     * and return that list's list iterator.
     * <p/>
     * AFAIK list iterator is only commonly used in sorting.
     *
     * @return a ListIterator for this, with internal state convertedto one list if needed.
     */
    @Override
    public ListIterator<E> listIterator(int index)
    {
        if (this.lists.size() == 1)
        {
            return this.lists.getFirst().listIterator(index);
        }
        if (this.lists.isEmpty())
        {
            return Lists.immutable.<E>of().listIterator(index);
        }
        this.flattenLists();
        return super.listIterator(index);
    }

    /**
     * convert multiple contained lists into one list and replace the contained lists with that list.
     * Synchronize to prevent changes to this list whilst this process is happening
     */
    private void flattenLists()
    {
        FastList<E> list = (FastList<E>) this.toList();
        this.lists.clear();
        this.lists.add(list);
    }

    /**
     * Override in subclasses where it can be optimized.
     */
    @Override
    protected void defaultSort(Comparator<? super E> comparator)
    {
        FastList<E> list = comparator == null
                ? (FastList<E>) this.toSortedList()
                : (FastList<E>) this.toSortedList(comparator);
        this.lists.clear();
        this.lists.add(list);
    }

    private final class CompositeIterator
            implements Iterator<E>
    {
        private final Iterator<E>[] iterators;
        private Iterator<E> currentIterator;
        private int currentIndex = 0;

        private CompositeIterator(FastList<FastList<E>> newLists)
        {
            this.iterators = new Iterator[newLists.size()];
            for (int i = 0; i < newLists.size(); ++i)
            {
                this.iterators[i] = newLists.get(i).iterator();
            }
            this.currentIterator = this.iterators[0];
            this.currentIndex = 0;
        }

        public boolean hasNext()
        {
            if (this.currentIterator.hasNext())
            {
                return true;
            }
            if (this.currentIndex < this.iterators.length - 1)
            {
                this.currentIterator = this.iterators[++this.currentIndex];
                return this.hasNext();
            }
            return false;
        }

        public E next()
        {
            if (this.currentIterator.hasNext())
            {
                return this.currentIterator.next();
            }
            if (this.currentIndex < this.iterators.length - 1)
            {
                this.currentIterator = this.iterators[++this.currentIndex];
                return this.next();
            }
            throw new NoSuchElementException();
        }

        public void remove()
        {
            this.currentIterator.remove();
        }
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof List))
        {
            return false;
        }
        List<?> otherList = (List<?>) other;
        if (this.size() != otherList.size())
        {
            return false;
        }

        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = otherList.iterator();
        while (thisIterator.hasNext())
        {
            E thisObject = thisIterator.next();
            Object otherObject = otherIterator.next();
            if (!Comparators.nullSafeEquals(thisObject, otherObject))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        Iterator<E> iterator = this.iterator();
        while (iterator.hasNext())
        {
            E item = iterator.next();
            hashCode = 31 * hashCode + (item == null ? 0 : item.hashCode());
        }
        return hashCode;
    }

    private static final class ProcedureToInnerListObjectIntProcedure<E> implements Procedure<FastList<E>>
    {
        private static final long serialVersionUID = 1L;

        private int index;
        private final ObjectIntProcedure<? super E> objectIntProcedure;

        private ProcedureToInnerListObjectIntProcedure(ObjectIntProcedure<? super E> objectIntProcedure)
        {
            this.objectIntProcedure = objectIntProcedure;
        }

        public void value(FastList<E> list)
        {
            list.forEach(new Procedure<E>()
            {
                public void value(E object)
                {
                    ProcedureToInnerListObjectIntProcedure.this.objectIntProcedure.value(
                            object,
                            ProcedureToInnerListObjectIntProcedure.this.index);
                    ProcedureToInnerListObjectIntProcedure.this.index++;
                }
            });
        }
    }
}
