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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.collection.mutable.AbstractMutableCollection;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.impl.lazy.ReverseIterable;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;

public abstract class AbstractMutableList<T>
        extends AbstractMutableCollection<T>
        implements MutableList<T>
{
    @Override
    public MutableList<T> clone()
    {
        try
        {
            return (MutableList<T>) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof List))
        {
            return false;
        }

        List<?> that = (List<?>) o;
        return that instanceof RandomAccess ? this.randomAccessEquals(that) : this.nonRandomAccessEquals(that);
    }

    private boolean randomAccessEquals(List<?> that)
    {
        if (this.size() != that.size())
        {
            return false;
        }
        for (int i = 0; i < this.size(); i++)
        {
            if (!Comparators.nullSafeEquals(this.get(i), that.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    private boolean nonRandomAccessEquals(List<?> that)
    {
        Iterator<?> iterator = that.iterator();
        for (int i = 0; i < this.size(); i++)
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            if (!Comparators.nullSafeEquals(this.get(i), iterator.next()))
            {
                return false;
            }
        }
        return !iterator.hasNext();
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        for (int i = 0; i < this.size(); i++)
        {
            Object obj = this.get(i);
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
        ListIterate.forEach(this, procedure);
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        if (this.notEmpty())
        {
            this.forEach(this.size() - 1, 0, procedure);
        }
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.forEachWithIndex(this, objectIntProcedure);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        ListIterate.forEachWith(this, procedure, parameter);
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.forEachWithIndex(this, fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public MutableList<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, this.newEmpty());
    }

    @Override
    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return ListIterate.select(this, predicate, target);
    }

    @Override
    public <P> MutableList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.selectWith(predicate, parameter, this.newEmpty());
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ListIterate.selectWith(this, predicate, parameter, targetCollection);
    }

    @Override
    public MutableList<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, this.newEmpty());
    }

    @Override
    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return ListIterate.reject(this, predicate, target);
    }

    @Override
    public <P> MutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.rejectWith(predicate, parameter, this.newEmpty());
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ListIterate.rejectWith(this, predicate, parameter, targetCollection);
    }

    @Override
    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return ListIterate.selectAndRejectWith(this, predicate, parameter);
    }

    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return PartitionFastList.of(this, predicate);
    }

    public <S> MutableList<S> selectInstancesOf(Class<S> clazz)
    {
        return ListIterate.selectInstancesOf(this, clazz);
    }

    @Override
    public void removeIf(Predicate<? super T> predicate)
    {
        ListIterate.removeIf(this, predicate);
    }

    @Override
    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        ListIterate.removeIfWith(this, predicate, parameter);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.collect(function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return ListIterate.collect(this, function, target);
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return ListIterate.flatCollect(this, function, target);
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collectWith(function, parameter, FastList.<V>newList());
    }

    @Override
    public <P, A, R extends Collection<A>> R collectWith(
            Function2<? super T, ? super P, ? extends A> function, P parameter, R targetCollection)
    {
        return ListIterate.collectWith(this, function, parameter, targetCollection);
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.collectIf(predicate, function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        return ListIterate.collectIf(this, predicate, function, target);
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return ListIterate.detect(this, predicate);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = ListIterate.detect(this, predicate);
        return result == null ? function.value() : result;
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        return ListIterate.min(this, comparator);
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        return ListIterate.max(this, comparator);
    }

    @Override
    public T min()
    {
        return ListIterate.min(this);
    }

    @Override
    public T max()
    {
        return ListIterate.max(this);
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return ListIterate.min(this, Comparators.byFunction(function));
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return ListIterate.max(this, Comparators.byFunction(function));
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.detectWith(this, predicate, parameter);
    }

    @Override
    public <P> T detectWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        T result = ListIterate.detectWith(this, predicate, parameter);
        return result == null ? function.value() : result;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return ListIterate.count(this, predicate);
    }

    @Override
    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.countWith(this, predicate, parameter);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return ListIterate.anySatisfy(this, predicate);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.anySatisfyWith(this, predicate, parameter);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return ListIterate.allSatisfy(this, predicate);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.allSatisfyWith(this, predicate, parameter);
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ListIterate.injectInto(injectedValue, this, function);
    }

    @Override
    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        return ListIterate.injectInto(injectedValue, this, function);
    }

    @Override
    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        return ListIterate.injectInto(injectedValue, this, function);
    }

    @Override
    public long sumOfInt(IntFunction<? super T> function)
    {
        return ListIterate.sumOfInt(this, function);
    }

    @Override
    public long sumOfLong(LongFunction<? super T> function)
    {
        return ListIterate.sumOfLong(this, function);
    }

    @Override
    public double sumOfFloat(FloatFunction<? super T> function)
    {
        return ListIterate.sumOfFloat(this, function);
    }

    @Override
    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        return ListIterate.sumOfDouble(this, function);
    }

    @Override
    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        return ListIterate.injectInto(injectedValue, this, function);
    }

    @Override
    public <IV, P> IV injectIntoWith(
            IV injectValue, Function3<? super IV, ? super T, ? super P, ? extends IV> function, P parameter)
    {
        return ListIterate.injectIntoWith(injectValue, this, function, parameter);
    }

    @Override
    public MutableList<T> toList()
    {
        return FastList.newList(this);
    }

    @Override
    public MutableList<T> toSortedList()
    {
        return this.toSortedList(Comparators.naturalOrder());
    }

    @Override
    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return this.toList().sortThis(comparator);
    }

    @Override
    public MutableSet<T> toSet()
    {
        return UnifiedSet.newSet(this);
    }

    public MutableStack<T> toStack()
    {
        return Stacks.mutable.ofAll(this);
    }

    @Override
    public MutableList<T> asUnmodifiable()
    {
        return UnmodifiableMutableList.of(this);
    }

    public ImmutableList<T> toImmutable()
    {
        return Lists.immutable.ofAll(this);
    }

    @Override
    public MutableList<T> asSynchronized()
    {
        return SynchronizedMutableList.of(this);
    }

    public MutableList<T> sortThis(Comparator<? super T> comparator)
    {
        if (this.size() < 10)
        {
            if (comparator == null)
            {
                this.insertionSort();
            }
            else
            {
                this.insertionSort(comparator);
            }
        }
        else
        {
            this.defaultSort(comparator);
        }
        return this;
    }

    /**
     * Override in subclasses where it can be optimized.
     */
    protected void defaultSort(Comparator<? super T> comparator)
    {
        Collections.sort(this, comparator);
    }

    private void insertionSort(Comparator<? super T> comparator)
    {
        for (int i = 0; i < this.size(); i++)
        {
            for (int j = i; j > 0 && comparator.compare(this.get(j - 1), this.get(j)) > 0; j--)
            {
                Collections.swap(this, j, j - 1);
            }
        }
    }

    private void insertionSort()
    {
        for (int i = 0; i < this.size(); i++)
        {
            for (int j = i; j > 0 && ((Comparable<T>) this.get(j - 1)).compareTo(this.get(j)) > 0; j--)
            {
                Collections.swap(this, j, j - 1);
            }
        }
    }

    public MutableList<T> sortThis()
    {
        return this.sortThis(Comparators.naturalOrder());
    }

    public <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function)
    {
        return this.sortThis(Comparators.byFunction(function));
    }

    @Override
    public MutableList<T> newEmpty()
    {
        return Lists.mutable.of();
    }

    public void forEach(int from, int to, Procedure<? super T> procedure)
    {
        ListIterate.forEach(this, from, to, procedure);
    }

    public int indexOf(Object o)
    {
        if (o == null)
        {
            for (int i = 0; i < this.size(); i++)
            {
                if (this.get(i) == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = 0; i < this.size(); i++)
            {
                if (o.equals(this.get(i)))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o)
    {
        if (o == null)
        {
            for (int i = this.size(); i-- > 0; )
            {
                if (this.get(i) == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = this.size(); i-- > 0; )
            {
                if (o.equals(this.get(i)))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new MutableIterator(this);
    }

    public ListIterator<T> listIterator()
    {
        return this.listIterator(0);
    }

    public ListIterator<T> listIterator(int index)
    {
        if (index < 0 || index > this.size())
        {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        return new MutableListIterator(this, index);
    }

    public MutableList<T> toReversed()
    {
        return FastList.newList(this).reverseThis();
    }

    public MutableList<T> reverseThis()
    {
        Collections.reverse(this);
        return this;
    }

    public MutableList<T> subList(int fromIndex, int toIndex)
    {
        return new SubList<T>(this, fromIndex, toIndex);
    }

    protected static class SubList<T>
            extends AbstractMutableList<T>
            implements Serializable, RandomAccess
    {
        // Not important since it uses writeReplace()
        private static final long serialVersionUID = 1L;

        private final MutableList<T> original;
        private final int offset;
        private int size;

        protected SubList(AbstractMutableList<T> list, int fromIndex, int toIndex)
        {
            if (fromIndex < 0)
            {
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            }
            if (toIndex > list.size())
            {
                throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            }
            if (fromIndex > toIndex)
            {
                throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ')');
            }
            this.original = list;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
        }

        @Override
        public MutableList<T> toReversed()
        {
            return FastList.newList(this).reverseThis();
        }

        protected Object writeReplace()
        {
            return FastList.newList(this);
        }

        @Override
        public boolean add(T o)
        {
            this.original.add(this.offset + this.size, o);
            this.size++;
            return true;
        }

        public T set(int index, T element)
        {
            this.checkIfOutOfBounds(index);
            return this.original.set(index + this.offset, element);
        }

        public T get(int index)
        {
            this.checkIfOutOfBounds(index);
            return this.original.get(index + this.offset);
        }

        @Override
        public int size()
        {
            return this.size;
        }

        public void add(int index, T element)
        {
            this.checkIfOutOfBounds(index);
            this.original.add(index + this.offset, element);
            this.size++;
        }

        public T remove(int index)
        {
            this.checkIfOutOfBounds(index);
            T result = this.original.remove(index + this.offset);
            this.size--;
            return result;
        }

        @Override
        public void clear()
        {
            for (Iterator<T> iterator = this.iterator(); iterator.hasNext(); )
            {
                iterator.next();
                iterator.remove();
            }
        }

        @Override
        public boolean addAll(Collection<? extends T> collection)
        {
            return this.addAll(this.size, collection);
        }

        public boolean addAll(int index, Collection<? extends T> collection)
        {
            if (index < 0 || index > this.size)
            {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
            }
            int cSize = collection.size();
            if (cSize == 0)
            {
                return false;
            }
            this.original.addAll(this.offset + index, collection);
            this.size += cSize;
            return true;
        }

        @Override
        public Iterator<T> iterator()
        {
            return this.listIterator();
        }

        @Override
        public ListIterator<T> listIterator(final int index)
        {
            if (index < 0 || index > this.size)
            {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
            }

            return new ListIterator<T>()
            {
                private final ListIterator<T> listIterator = SubList.this.original.listIterator(index + SubList.this.offset);

                public boolean hasNext()
                {
                    return this.nextIndex() < SubList.this.size;
                }

                public T next()
                {
                    if (this.hasNext())
                    {
                        return this.listIterator.next();
                    }
                    throw new NoSuchElementException();
                }

                public boolean hasPrevious()
                {
                    return this.previousIndex() >= 0;
                }

                public T previous()
                {
                    if (this.hasPrevious())
                    {
                        return this.listIterator.previous();
                    }
                    throw new NoSuchElementException();
                }

                public int nextIndex()
                {
                    return this.listIterator.nextIndex() - SubList.this.offset;
                }

                public int previousIndex()
                {
                    return this.listIterator.previousIndex() - SubList.this.offset;
                }

                public void remove()
                {
                    this.listIterator.remove();
                    SubList.this.size--;
                }

                public void set(T o)
                {
                    this.listIterator.set(o);
                }

                public void add(T o)
                {
                    this.listIterator.add(o);
                    SubList.this.size++;
                }
            };
        }

        @Override
        public MutableList<T> subList(int fromIndex, int toIndex)
        {
            return new SubList<T>(this, fromIndex, toIndex);
        }

        private void checkIfOutOfBounds(int index)
        {
            if (index >= this.size || index < 0)
            {
                throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size);
            }
        }

        // Weird implementation of clone() is ok on final classes

        @Override
        public MutableList<T> clone()
        {
            return new FastList<T>(this);
        }

        @Override
        public T getFirst()
        {
            return this.isEmpty() ? null : this.original.get(this.offset);
        }

        @Override
        public T getLast()
        {
            return this.isEmpty() ? null : this.original.get(this.offset + this.size - 1);
        }

        @Override
        public MutableStack<T> toStack()
        {
            return ArrayStack.newStack(this);
        }

        @Override
        public void forEach(Procedure<? super T> procedure)
        {
            ListIterate.forEach(this, procedure);
        }

        @Override
        public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
        {
            ListIterate.forEachWithIndex(this, objectIntProcedure);
        }

        @Override
        public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
        {
            ListIterate.forEachWith(this, procedure, parameter);
        }
    }

    @Override
    public boolean contains(Object object)
    {
        return this.indexOf(object) > -1;
    }

    @Override
    public boolean containsAll(Collection<?> source)
    {
        return Iterate.allSatisfyWith(source, Predicates2.in(), this);
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        int currentSize = this.size();
        this.removeIfWith(Predicates2.in(), collection);
        return currentSize != this.size();
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        int currentSize = this.size();
        this.removeIfWith(Predicates2.notIn(), collection);
        return currentSize != this.size();
    }

    @Override
    public T getFirst()
    {
        return ListIterate.getFirst(this);
    }

    @Override
    public T getLast()
    {
        return ListIterate.getLast(this);
    }

    @Override
    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        ListIterate.appendString(this, appendable, start, separator, end);
    }

    public <V> FastListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return ListIterate.groupBy(this, function);
    }

    public <V> FastListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return ListIterate.groupByEach(this, function);
    }

    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return ListIterate.zip(this, that);
    }

    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        return ListIterate.zipWithIndex(this);
    }

    public MutableList<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public MutableList<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public MutableList<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public MutableList<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    public ReverseIterable<T> asReversed()
    {
        return ReverseIterable.adapt(this);
    }
}
