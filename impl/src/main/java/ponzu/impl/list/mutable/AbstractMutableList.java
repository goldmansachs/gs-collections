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

package ponzu.impl.list.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function0;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Function3;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.partition.list.PartitionMutableList;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Pair;
import ponzu.api.tuple.Twin;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Predicates2;
import ponzu.impl.collection.mutable.AbstractMutableCollection;
import ponzu.impl.factory.Lists;
import ponzu.impl.multimap.list.FastListMultimap;
import ponzu.impl.partition.list.PartitionFastList;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.utility.Iterate;
import ponzu.impl.utility.ListIterate;

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
        return that instanceof RandomAccess ? randomAccessEquals(that) : nonRandomAccessEquals(that);
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
    public MutableList<T> filter(Predicate<? super T> predicate)
    {
        return this.filter(predicate, this.newEmpty());
    }

    @Override
    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        return ListIterate.filter(this, predicate, target);
    }

    @Override
    public <P> MutableList<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.filterWith(predicate, parameter, this.newEmpty());
    }

    @Override
    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ListIterate.filterWith(this, predicate, parameter, targetCollection);
    }

    @Override
    public MutableList<T> filterNot(Predicate<? super T> predicate)
    {
        return this.filterNot(predicate, this.newEmpty());
    }

    @Override
    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        return ListIterate.filterNot(this, predicate, target);
    }

    @Override
    public <P> MutableList<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.filterNotWith(predicate, parameter, this.newEmpty());
    }

    @Override
    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ListIterate.filterNotWith(this, predicate, parameter, targetCollection);
    }

    @Override
    public <P> Twin<MutableList<T>> partitionWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return ListIterate.partitionWith(this, predicate, parameter);
    }

    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        return PartitionFastList.of(this, predicate);
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
    public <V> MutableList<V> transform(Function<? super T, ? extends V> function)
    {
        return this.transform(function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R transform(Function<? super T, ? extends V> function, R target)
    {
        return ListIterate.transform(this, function, target);
    }

    @Override
    public <V> MutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatTransform(function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R flatTransform(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return ListIterate.flatTransform(this, function, target);
    }

    @Override
    public <P, V> MutableList<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.transformWith(function, parameter, FastList.<V>newList());
    }

    @Override
    public <P, A, R extends Collection<A>> R transformWith(
            Function2<? super T, ? super P, ? extends A> function, P parameter, R targetCollection)
    {
        return ListIterate.transformWith(this, function, parameter, targetCollection);
    }

    @Override
    public <V> MutableList<V> transformIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.transformIf(predicate, function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        return ListIterate.transformIf(this, predicate, function, target);
    }

    @Override
    public T find(Predicate<? super T> predicate)
    {
        return ListIterate.find(this, predicate);
    }

    @Override
    public T findIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = ListIterate.find(this, predicate);
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
    public <P> T findWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ListIterate.findWith(this, predicate, parameter);
    }

    @Override
    public <P> T findWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        T result = ListIterate.findWith(this, predicate, parameter);
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
    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ListIterate.foldLeft(initialValue, this, function);
    }

    @Override
    public int foldLeft(int initialValue, IntObjectToIntFunction<? super T> function)
    {
        return ListIterate.foldLeft(initialValue, this, function);
    }

    @Override
    public long foldLeft(long initialValue, LongObjectToLongFunction<? super T> function)
    {
        return ListIterate.foldLeft(initialValue, this, function);
    }

    @Override
    public <IV, P> IV foldLeftWith(
            IV initialValue, Function3<? super IV, ? super T, ? super P, ? extends IV> function, P parameter)
    {
        return ListIterate.foldLeftWith(initialValue, this, function, parameter);
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
}
