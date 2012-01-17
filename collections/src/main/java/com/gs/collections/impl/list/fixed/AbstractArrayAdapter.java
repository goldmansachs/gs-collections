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

package com.gs.collections.impl.list.fixed;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.list.mutable.AbstractMutableList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.ListAdapter;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.internal.IterableIterate;

public abstract class AbstractArrayAdapter<T>
        extends AbstractMutableList<T>
        implements RandomAccess
{
    protected T[] items;

    /**
     * This method must be here so subclasses can be serializable.
     */
    protected AbstractArrayAdapter()
    {
    }

    protected AbstractArrayAdapter(T[] newElements)
    {
        this.items = newElements;
    }

    @Override
    public boolean notEmpty()
    {
        return ArrayIterate.notEmpty(this.items);
    }

    @Override
    public T getFirst()
    {
        return ArrayIterate.getFirst(this.items);
    }

    @Override
    public T getLast()
    {
        return ArrayIterate.getLast(this.items);
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            procedure.value(this.items[i]);
        }
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int size = this.items.length;
        for (int i = 0; i < size; i++)
        {
            objectIntProcedure.value(this.items[i], i);
        }
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ArrayIterate.forEachWithIndex(this.items, fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public void removeIf(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException("Cannot remove from an array");
    }

    @Override
    public <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException("Cannot remove from an array");
    }

    @Override
    public T find(Predicate<? super T> predicate)
    {
        return ArrayIterate.find(this.items, predicate);
    }

    @Override
    public T findIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.find(predicate);
        if (result == null)
        {
            result = function.value();
        }
        return result;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return ArrayIterate.count(this.items, predicate);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.anySatisfy(this.items, predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.allSatisfy(this.items, predicate);
    }

    @Override
    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ArrayIterate.foldLeft(initialValue, this.items, function);
    }

    @Override
    public MutableList<T> filter(Predicate<? super T> predicate)
    {
        return this.filter(predicate, FastList.<T>newList());
    }

    @Override
    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        return ArrayIterate.filter(this.items, predicate, target);
    }

    @Override
    public MutableList<T> filterNot(Predicate<? super T> predicate)
    {
        return this.filterNot(predicate, FastList.<T>newList());
    }

    @Override
    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        return ArrayIterate.filterNot(this.items, predicate, target);
    }

    @Override
    public <V> MutableList<V> transform(Function<? super T, ? extends V> function)
    {
        return this.transform(function, FastList.<V>newList(this.size()));
    }

    @Override
    public <V, R extends Collection<V>> R transform(Function<? super T, ? extends V> function, R target)
    {
        return ArrayIterate.transform(this.items, function, target);
    }

    @Override
    public <V> MutableList<V> transformIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.transformIf(predicate, function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return ArrayIterate.<T, V, R>transformIf(this.items, predicate, function, target);
    }

    @Override
    public <P> Twin<MutableList<T>> partitionWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return ArrayIterate.partitionWith(this.items, predicate, parameter);
    }

    @Override
    public int size()
    {
        return this.items.length;
    }

    @Override
    public boolean isEmpty()
    {
        return ArrayIterate.isEmpty(this.items);
    }

    @Override
    public boolean contains(Object o)
    {
        return this.anySatisfy(Predicates.equal((T) o));
    }

    @Override
    public Iterator<T> iterator()
    {
        return Arrays.asList(this.items).iterator();
    }

    @Override
    public Object[] toArray()
    {
        return this.items.clone();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        int size = this.size();
        E[] result = array.length < size
                ? (E[]) Array.newInstance(array.getClass().getComponentType(), size)
                : array;

        System.arraycopy(this.items, 0, result, 0, size);
        if (result.length > size)
        {
            result[size] = null;
        }
        return result;
    }

    @Override
    public boolean add(T item)
    {
        throw new UnsupportedOperationException("cannot add to an ArrayAdapter");
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot remove from an ArrayAdapter");
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return Iterate.allSatisfy(collection, Predicates.in(this.items));
    }

    @Override
    public boolean addAll(Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException("cannot addAll to an ArrayAdapter");
    }

    @Override
    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        throw new UnsupportedOperationException("cannot addAll to an ArrayAdapter");
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot removeAll from an ArrayAdapter");
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException("Cannot removeAll from an ArrayAdapter");
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot remove from an ArrayAdapter");
    }

    @Override
    public boolean retainAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException("Cannot remove from an ArrayAdapter");
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("Cannot clear an ArrayAdapter");
    }

    public boolean addAll(int index, Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException("Cannot add all to an array from a specific index");
    }

    public T get(int index)
    {
        if (index >= this.size())
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
        }

        return this.items[index];
    }

    public void add(int index, T element)
    {
        throw new UnsupportedOperationException("Cannot add to an ArrayAdapter at a specific index");
    }

    public T remove(int index)
    {
        throw new UnsupportedOperationException("Cannot remove from an ArrayAdapter");
    }

    @Override
    public int indexOf(Object item)
    {
        return ArrayIterate.indexOf(this.items, item);
    }

    @Override
    public int lastIndexOf(Object item)
    {
        return Arrays.asList(this.items).lastIndexOf(item);
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        return Arrays.asList(this.items).listIterator(index);
    }

    @Override
    public MutableList<T> subList(int fromIndex, int toIndex)
    {
        return ListAdapter.adapt(Arrays.asList(this.items).subList(fromIndex, toIndex));
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
        if (otherList instanceof AbstractArrayAdapter)
        {
            return this.sameTypeEquals((AbstractArrayAdapter<T>) otherList);
        }
        if (otherList instanceof RandomAccess)
        {
            return this.randomAccessListEquals(list);
        }
        return this.regularListEquals(list);
    }

    private boolean sameTypeEquals(AbstractArrayAdapter<?> abstractArrayAdapter)
    {
        return Arrays.equals(this.items, abstractArrayAdapter.items);
    }

    private boolean regularListEquals(List<T> otherList)
    {
        T[] localItems = this.items;
        Iterator<T> iterator = otherList.iterator();
        int size = this.items.length;
        for (int i = 0; i < size; i++)
        {
            T one = localItems[i];
            T two = iterator.next();
            if (!Comparators.nullSafeEquals(one, two))
            {
                return false;
            }
        }
        return true;
    }

    private boolean randomAccessListEquals(List<T> otherList)
    {
        T[] localItems = this.items;
        int n = this.items.length;
        for (int i = 0; i < n; i++)
        {
            T one = localItems[i];
            T two = otherList.get(i);
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
        return Arrays.hashCode(this.items);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        ArrayIterate.forEachWith(this.items, procedure, parameter);
    }

    @Override
    public <P> MutableList<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.filterWith(predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ArrayIterate.filterWith(this.items, predicate, parameter, targetCollection);
    }

    @Override
    public <P> MutableList<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.filterNotWith(predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ArrayIterate.filterNotWith(this.items, predicate, parameter, targetCollection);
    }

    @Override
    public <P, A> MutableList<A> transformWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.transformWith(function, parameter, FastList.<A>newList());
    }

    @Override
    public <P, A, R extends Collection<A>> R transformWith(
            Function2<? super T, ? super P, ? extends A> function, P parameter, R targetCollection)
    {
        return ArrayIterate.transformWith(this.items, function, parameter, targetCollection);
    }

    @Override
    public <IV, P> IV foldLeftWith(
            IV initialValue, Function3<? super IV, ? super T, ? super P, ? extends IV> function, P parameter)
    {
        return ArrayIterate.foldLeftWith(initialValue, this.items, function, parameter);
    }

    @Override
    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        ArrayIterate.forEach(this.items, fromIndex, toIndex, procedure);
    }

    @Override
    public <P> T findWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.findWith(this.items, predicate, parameter);
    }

    @Override
    public <P> T findWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        T result = this.findWith(predicate, parameter);
        if (result == null)
        {
            result = function.value();
        }
        return result;
    }

    @Override
    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.countWith(this.items, predicate, parameter);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.anySatisfyWith(this.items, predicate, parameter);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.allSatisfyWith(this.items, predicate, parameter);
    }

    @Override
    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);

            if (this.notEmpty())
            {
                appendable.append(IterableIterate.stringValueOfItem(this, this.items[0]));
                int size = this.size();
                for (int i = 1; i < size; i++)
                {
                    appendable.append(separator);
                    appendable.append(IterableIterate.stringValueOfItem(this, this.items[i]));
                }
            }

            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
