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
    public T detect(Predicate<? super T> predicate)
    {
        return ArrayIterate.detect(this.items, predicate);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.detect(predicate);
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
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ArrayIterate.injectInto(injectedValue, this.items, function);
    }

    @Override
    public MutableList<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, FastList.<T>newList());
    }

    @Override
    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return ArrayIterate.select(this.items, predicate, target);
    }

    @Override
    public MutableList<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, FastList.<T>newList());
    }

    @Override
    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return ArrayIterate.reject(this.items, predicate, target);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.collect(function, FastList.<V>newList(this.size()));
    }

    @Override
    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return ArrayIterate.collect(this.items, function, target);
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.collectIf(predicate, function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return ArrayIterate.collectIf(this.items, predicate, function, target);
    }

    @Override
    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return ArrayIterate.selectAndRejectWith(this.items, predicate, parameter);
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
        return this.anySatisfy(Predicates.equal(o));
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
        List<?> list = (List<?>) otherList;
        if (otherList instanceof AbstractArrayAdapter)
        {
            return this.abstractArrayAdapterEquals((AbstractArrayAdapter<?>) otherList);
        }
        if (otherList instanceof RandomAccess)
        {
            return this.randomAccessListEquals(list);
        }
        return this.regularListEquals(list);
    }

    public boolean abstractArrayAdapterEquals(AbstractArrayAdapter<?> otherList)
    {
        return Arrays.equals(this.items, otherList.items);
    }

    private boolean regularListEquals(List<?> otherList)
    {
        Iterator<?> iterator = otherList.iterator();
        for (int i = 0; i < this.size(); i++)
        {
            T one = this.items[i];
            if (!iterator.hasNext())
            {
                return false;
            }
            Object two = iterator.next();
            if (!Comparators.nullSafeEquals(one, two))
            {
                return false;
            }
        }
        return !iterator.hasNext();
    }

    private boolean randomAccessListEquals(List<?> otherList)
    {
        if (this.size() != otherList.size())
        {
            return false;
        }
        int n = this.items.length;
        for (int i = 0; i < n; i++)
        {
            Object one = this.items[i];
            Object two = otherList.get(i);
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
    public <P> MutableList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.selectWith(predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ArrayIterate.selectWith(this.items, predicate, parameter, targetCollection);
    }

    @Override
    public <P> MutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.rejectWith(predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return ArrayIterate.rejectWith(this.items, predicate, parameter, targetCollection);
    }

    @Override
    public <P, A> MutableList<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.collectWith(function, parameter, FastList.<A>newList());
    }

    @Override
    public <P, A, R extends Collection<A>> R collectWith(
            Function2<? super T, ? super P, ? extends A> function, P parameter, R targetCollection)
    {
        return ArrayIterate.collectWith(this.items, function, parameter, targetCollection);
    }

    @Override
    public <IV, P> IV injectIntoWith(
            IV injectValue, Function3<? super IV, ? super T, ? super P, ? extends IV> function, P parameter)
    {
        return ArrayIterate.injectIntoWith(injectValue, this.items, function, parameter);
    }

    @Override
    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        ArrayIterate.forEach(this.items, fromIndex, toIndex, procedure);
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.detectWith(this.items, predicate, parameter);
    }

    @Override
    public <P> T detectWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        T result = this.detectWith(predicate, parameter);
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
