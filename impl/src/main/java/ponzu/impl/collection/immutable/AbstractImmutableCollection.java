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

package ponzu.impl.collection.immutable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ponzu.api.LazyIterable;
import ponzu.api.RichIterable;
import ponzu.api.bag.MutableBag;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Generator;
import ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.collection.ImmutableCollection;
import ponzu.api.collection.MutableCollection;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.map.sorted.MutableSortedMap;
import ponzu.api.set.MutableSet;
import ponzu.api.set.sorted.MutableSortedSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.bag.mutable.HashBag;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.procedure.FilterNotProcedure;
import ponzu.impl.block.procedure.FilterProcedure;
import ponzu.impl.block.procedure.TransformIfProcedure;
import ponzu.impl.block.procedure.TransformProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.map.sorted.mutable.TreeSortedMap;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.set.sorted.mutable.TreeSortedSet;
import ponzu.impl.utility.ArrayIterate;
import ponzu.impl.utility.Iterate;
import ponzu.impl.utility.LazyIterate;
import ponzu.impl.utility.internal.IterableIterate;

public abstract class AbstractImmutableCollection<T> implements ImmutableCollection<T>, Collection<T>
{
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

    public boolean contains(Object object)
    {
        return this.anySatisfy(Predicates.equal(object));
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return Iterate.allSatisfy(source, Predicates.in(this));
    }

    public boolean containsAllArguments(Object... elements)
    {
        return ArrayIterate.allSatisfy(elements, Predicates.in(this));
    }

    public Object[] toArray()
    {
        final Object[] result = new Object[this.size()];
        this.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                result[index] = each;
            }
        });
        return result;
    }

    public <E> E[] toArray(E[] array)
    {
        final E[] result = array.length < this.size()
                ? (E[]) Array.newInstance(array.getClass().getComponentType(), this.size())
                : array;

        this.forEachWithIndex(new ObjectIntProcedure<Object>()
        {
            public void value(Object each, int index)
            {
                result[index] = (E) each;
            }
        });
        if (result.length > this.size())
        {
            result[this.size()] = null;
        }
        return result;
    }

    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    public boolean notEmpty()
    {
        return this.size() > 0;
    }

    public MutableList<T> toList()
    {
        return Lists.mutable.ofAll(this);
    }

    public MutableList<T> toSortedList()
    {
        return this.toList().sortThis();
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return this.toList().sortThis(comparator);
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedList(Comparators.byFunction(function));
    }

    public MutableSortedSet<T> toSortedSet()
    {
        return TreeSortedSet.newSet(null, this);
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return TreeSortedSet.newSet(comparator, this);
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedSet(Comparators.byFunction(function));
    }

    public MutableSet<T> toSet()
    {
        return UnifiedSet.newSet(this);
    }

    public MutableBag<T> toBag()
    {
        return HashBag.newBag(this);
    }

    public <K, V> MutableMap<K, V> toMap(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return UnifiedMap.<K, V>newMap(this.size()).transformKeysAndValues(this, keyFunction, valueFunction);
    }

    public <K, V> MutableSortedMap<K, V> toSortedMap(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return TreeSortedMap.<K, V>newMap().transformKeysAndValues(this, keyFunction, valueFunction);
    }

    public <K, V> MutableSortedMap<K, V> toSortedMap(Comparator<? super K> comparator,
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return TreeSortedMap.<K, V>newMap(comparator).transformKeysAndValues(this, keyFunction, valueFunction);
    }

    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        this.forEach(new FilterProcedure<T>(predicate, target));
        return target;
    }

    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return IterableIterate.filterWith(this, predicate, parameter, targetCollection);
    }

    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        this.forEach(new FilterNotProcedure<T>(predicate, target));
        return target;
    }

    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return IterableIterate.filterNotWith(this, predicate, parameter, targetCollection);
    }

    public <V, R extends Collection<V>> R transform(Function<? super T, ? extends V> function, R target)
    {
        this.forEach(new TransformProcedure<T, V>(function, target));
        return target;
    }

    public <P, V, R extends Collection<V>> R transformWith(
            Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return IterableIterate.transformWith(this, function, parameter, targetCollection);
    }

    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        this.forEach(new TransformIfProcedure<T, V>(target, function, predicate));
        return target;
    }

    public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
    {
        T result = this.find(predicate);
        return result == null ? function.value() : result;
    }

    public T min(Comparator<? super T> comparator)
    {
        return Iterate.min(this, comparator);
    }

    public T max(Comparator<? super T> comparator)
    {
        return Iterate.max(this, comparator);
    }

    public T min()
    {
        return Iterate.min(this);
    }

    public T max()
    {
        return Iterate.max(this);
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return Iterate.min(this, Comparators.byFunction(function));
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return Iterate.max(this, Comparators.byFunction(function));
    }

    public LazyIterable<T> asLazy()
    {
        return LazyIterate.adapt(this);
    }

    public <V, R extends Collection<V>> R flatTransform(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return IterableIterate.flatTransform(this, function, target);
    }

    public T find(Predicate<? super T> predicate)
    {
        return IterableIterate.find(this, predicate);
    }

    public int count(Predicate<? super T> predicate)
    {
        return IterableIterate.count(this, predicate);
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return IterableIterate.anySatisfy(this, predicate);
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return IterableIterate.allSatisfy(this, predicate);
    }

    public <IV> IV foldLeft(IV initialValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public int foldLeft(int initialValue, IntObjectToIntFunction<? super T> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public long foldLeft(long initialValue, LongObjectToLongFunction<? super T> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return IterableIterate.foldLeft(initialValue, this, function);
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        IterableIterate.forEachWithIndex(this, objectIntProcedure);
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        IterableIterate.forEachWith(this, procedure, parameter);
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return IterableIterate.zip(this, that, target);
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return IterableIterate.zipWithIndex(this, target);
    }

    /**
     * Returns a string representation of this collection.  The string representation consists of a list of the
     * collection's elements in the order they are returned by its iterator, enclosed in square brackets
     * (<tt>"[]"</tt>).  Adjacent elements are separated by the characters <tt>", "</tt> (comma and space).  Elements
     * are converted to strings as by <tt>String.valueOf(Object)</tt>.<p>
     * <p/>
     * This implementation creates an empty string buffer, appends a left square bracket, and iterates over the
     * collection appending the string representation of each element in turn.  After appending each element except the
     * last, the string <tt>", "</tt> is appended.  Finally a right bracket is appended.  A string is obtained from the
     * string buffer, and returned.
     *
     * @return a string representation of this collection.
     */
    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public String makeString()
    {
        return this.makeString(", ");
    }

    public String makeString(String separator)
    {
        return this.makeString("", separator, "");
    }

    public String makeString(String start, String separator, String end)
    {
        Appendable stringBuilder = new StringBuilder();
        this.appendString(stringBuilder, start, separator, end);
        return stringBuilder.toString();
    }

    public void appendString(Appendable appendable)
    {
        this.appendString(appendable, ", ");
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.appendString(appendable, "", separator, "");
    }

    public void appendString(
            Appendable appendable,
            String start,
            String separator,
            String end)
    {
        IterableIterate.appendString(this, appendable, start, separator, end);
    }

    public boolean add(T t)
    {
        throw new UnsupportedOperationException("Cannot add to an Immutable Collection");
    }

    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot add to an Immutable Collection");
    }

    public boolean containsAll(Collection<?> collection)
    {
        return this.containsAllIterable(collection);
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

    protected abstract MutableCollection<T> newMutable(int size);
}
