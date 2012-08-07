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

package ponzu.impl.list.immutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import net.jcip.annotations.Immutable;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.collection.MutableCollection;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.multimap.MutableMultimap;
import ponzu.api.multimap.list.ImmutableListMultimap;
import ponzu.api.partition.list.PartitionImmutableList;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.procedure.FilterNotProcedure;
import ponzu.impl.block.procedure.FilterProcedure;
import ponzu.impl.block.procedure.FlatTransformProcedure;
import ponzu.impl.block.procedure.TransformIfProcedure;
import ponzu.impl.block.procedure.TransformProcedure;
import ponzu.impl.collection.immutable.AbstractImmutableCollection;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.multimap.list.FastListMultimap;
import ponzu.impl.partition.list.PartitionFastList;
import ponzu.impl.utility.Iterate;
import ponzu.impl.utility.ListIterate;

/**
 * This class is the parent class for all ImmutableLists.  All implementations of ImmutableList must implement the List
 * interface so anArrayList.equals(anImmutableList) can return true when the contents and order are the same.
 */
@Immutable
abstract class AbstractImmutableList<T> extends AbstractImmutableCollection<T>
        implements ImmutableList<T>, List<T>
{
    public List<T> castToList()
    {
        return this;
    }

    @Override
    public boolean equals(Object otherList)
    {
        if (otherList == this)
        {
            return true;
        }
        if (!(otherList instanceof List))
        {
            return false;
        }
        List<T> list = (List<T>) otherList;
        if (this.size() != list.size())
        {
            return false;
        }
        if (list instanceof RandomAccess)
        {
            return this.randomAccessListEquals(list);
        }
        return this.regularListEquals(list);
    }

    private boolean randomAccessListEquals(List<T> otherList)
    {
        int localSize = this.size();
        for (int i = 0; i < localSize; i++)
        {
            T one = this.get(i);
            Object two = otherList.get(i);
            if (!Comparators.nullSafeEquals(one, two))
            {
                return false;
            }
        }
        return true;
    }

    private boolean regularListEquals(List<T> otherList)
    {
        Iterator<T> iterator = otherList.iterator();
        int localSize = this.size();
        for (int i = 0; i < localSize; i++)
        {
            T one = this.get(i);
            Object two = iterator.next();
            if (!Comparators.nullSafeEquals(one, two))
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
        int localSize = this.size();
        for (int i = 0; i < localSize; i++)
        {
            T item = this.get(i);
            hashCode = 31 * hashCode + (item == null ? 0 : item.hashCode());
        }
        return hashCode;
    }

    public ImmutableList<T> newWithout(T element)
    {
        int indexToRemove = this.indexOf(element);
        if (indexToRemove > 0)
        {
            T[] results = (T[]) new Object[this.size() - 1];
            int currentIndex = 0;
            for (int i = 0; i < this.size(); i++)
            {
                T item = this.get(i);
                if (i != indexToRemove)
                {
                    results[currentIndex++] = item;
                }
            }
            return Lists.immutable.of(results);
        }
        return this;
    }

    public ImmutableList<T> newWithAll(Iterable<? extends T> elements)
    {
        final int oldSize = this.size();
        int newSize = Iterate.sizeOf(elements);
        final T[] array = (T[]) new Object[oldSize + newSize];
        this.toArray(array);
        Iterate.forEachWithIndex(elements, new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[oldSize + index] = each;
            }
        });
        return Lists.immutable.of(array);
    }

    public ImmutableList<T> newWithoutAll(Iterable<? extends T> elements)
    {
        FastList<T> result = FastList.newListWith((T[]) this.toArray());
        this.removeAllFrom(elements, result);
        return result.toImmutable();
    }

    public T getFirst()
    {
        return this.isEmpty() ? null : this.get(0);
    }

    public T getLast()
    {
        return this.isEmpty() ? null : this.get(this.size() - 1);
    }

    public ImmutableList<T> filter(Predicate<? super T> predicate)
    {
        MutableList<T> result = Lists.mutable.of();
        this.forEach(new FilterProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    @Override
    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ListIterate.filterWith(this.castToList(), predicate, parameter, targetCollection);
    }

    public ImmutableList<T> filterNot(Predicate<? super T> predicate)
    {
        MutableList<T> result = Lists.mutable.of();
        this.forEach(new FilterNotProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    @Override
    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ListIterate.filterNotWith(this.castToList(), predicate, parameter, targetCollection);
    }

    public PartitionImmutableList<T> partition(Predicate<? super T> predicate)
    {
        return PartitionFastList.of(this, predicate).toImmutable();
    }

    public <V> ImmutableList<V> transform(Function<? super T, ? extends V> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new TransformProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public <V> ImmutableList<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new TransformIfProcedure<T, V>(result, function, predicate));
        return result.toImmutable();
    }

    @Override
    public <P, V, R extends Collection<V>> R transformWith(
            Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return ListIterate.transformWith(this.castToList(), function, parameter, targetCollection);
    }

    public <V> ImmutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new FlatTransformProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    @Override
    public <V, R extends Collection<V>> R flatTransform(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return ListIterate.flatTransform(this, function, target);
    }

    @Override
    public T find(Predicate<? super T> predicate)
    {
        for (int i = 0, size = this.size(); i < size; i++)
        {
            T item = this.get(i);
            if (predicate.accept(item))
            {
                return item;
            }
        }
        return null;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        int count = 0;
        for (int i = 0, size = this.size(); i < size; i++)
        {
            if (predicate.accept(this.get(i)))
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        for (int i = 0, size = this.size(); i < size; i++)
        {
            if (predicate.accept(this.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        for (int i = 0, size = this.size(); i < size; i++)
        {
            if (!predicate.accept(this.get(i)))
            {
                return false;
            }
        }
        return true;
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
    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return ListIterate.foldLeft(initialValue, this, function);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int localSize = this.size();
        for (int i = 0; i < localSize; i++)
        {
            T each = this.get(i);
            objectIntProcedure.value(each, i);
        }
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        int localSize = this.size();
        for (int i = 0; i < localSize; i++)
        {
            T each = this.get(i);
            procedure.value(each, parameter);
        }
    }

    public void forEach(
            int from, int to, Procedure<? super T> procedure)
    {
        if (from < 0 || to < 0)
        {
            throw new IllegalArgumentException("Neither from nor to may be negative.");
        }
        if (from <= to)
        {
            for (int i = from; i <= to; i++)
            {
                procedure.value(this.get(i));
            }
        }
        else
        {
            for (int i = from; i >= to; i--)
            {
                procedure.value(this.get(i));
            }
        }
    }

    public void forEachWithIndex(
            int from, int to, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (from < 0 || to < 0)
        {
            throw new IllegalArgumentException("Neither from nor to may be negative.");
        }
        if (from <= to)
        {
            for (int i = from; i <= to; i++)
            {
                objectIntProcedure.value(this.get(i), i);
            }
        }
        else
        {
            for (int i = from; i >= to; i--)
            {
                objectIntProcedure.value(this.get(i), i);
            }
        }
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        if (this.notEmpty())
        {
            this.forEach(this.size() - 1, 0, procedure);
        }
    }

    public abstract T get(int index);

    public int indexOf(Object object)
    {
        int n = this.size();
        if (object == null)
        {
            for (int i = 0; i < n; i++)
            {
                if (this.get(i) == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = 0; i < n; i++)
            {
                if (object.equals(this.get(i)))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(Object object)
    {
        int n = this.size() - 1;
        if (object == null)
        {
            for (int i = n; i >= 0; i--)
            {
                if (this.get(i) == null)
                {
                    return i;
                }
            }
        }
        else
        {
            for (int i = n; i >= 0; i--)
            {
                if (object.equals(this.get(i)))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    public Iterator<T> iterator()
    {
        return new ImmutableListIterator();
    }

    private class ImmutableListIterator implements ListIterator<T>
    {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        protected int currentIndex;

        public boolean hasNext()
        {
            return this.currentIndex != AbstractImmutableList.this.size();
        }

        public T next()
        {
            try
            {
                T next = AbstractImmutableList.this.get(this.currentIndex);
                this.currentIndex++;
                return next;
            }
            catch (IndexOutOfBoundsException ignored)
            {
                throw new NoSuchElementException();
            }
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove from an Immutable List");
        }

        public boolean hasPrevious()
        {
            throw new UnsupportedOperationException();
        }

        public T previous()
        {
            throw new UnsupportedOperationException();
        }

        public int nextIndex()
        {
            throw new UnsupportedOperationException();
        }

        public int previousIndex()
        {
            throw new UnsupportedOperationException();
        }

        public void set(T t)
        {
            throw new UnsupportedOperationException("Cannot set into an Immutable List");
        }

        public void add(T t)
        {
            throw new UnsupportedOperationException("Cannot add to an Immutable List");
        }
    }

    public boolean addAll(int index, Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException();
    }

    public T set(int index, T element)
    {
        throw new UnsupportedOperationException();
    }

    public void add(int index, T element)
    {
        throw new UnsupportedOperationException();
    }

    public T remove(int index)
    {
        throw new UnsupportedOperationException();
    }

    public ListIterator<T> listIterator()
    {
        return new ImmutableListIterator();
    }

    public ListIterator<T> listIterator(int index)
    {
        throw new UnsupportedOperationException();
    }

    public List<T> subList(int fromIndex, int toIndex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        ListIterate.appendString(this, appendable, start, separator, end);
    }

    public <V> ImmutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, FastListMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        return ListIterate.groupBy(this, function, target);
    }

    public <V> ImmutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, FastListMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return ListIterate.groupByEach(this, function, target);
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

    public <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<T, S>>newList()).toImmutable();
    }

    public ImmutableList<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(FastList.<Pair<T, Integer>>newList()).toImmutable();
    }

    @Override
    protected MutableCollection<T> newMutable(int size)
    {
        return FastList.newList(size);
    }
}
