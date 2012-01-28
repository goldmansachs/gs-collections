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

package ponzu.impl.collection.mutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;

import ponzu.api.LazyIterable;
import ponzu.api.RichIterable;
import ponzu.api.bag.MutableBag;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Function3;
import ponzu.api.block.function.Generator;
import ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.collection.MutableCollection;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.map.sorted.MutableSortedMap;
import ponzu.api.multimap.MutableMultimap;
import ponzu.api.set.MutableSet;
import ponzu.api.set.sorted.MutableSortedSet;
import ponzu.api.tuple.Pair;
import ponzu.api.tuple.Twin;

/**
 * AbstractMultiReaderMutableCollection is a common abstraction that provides thread-safe collection behaviors.
 * Subclasses of this class must provide implementations of getDelegate() and getLock().
 */
public abstract class AbstractMultiReaderMutableCollection<T> implements MutableCollection<T>
{
    protected abstract MutableCollection<T> getDelegate();

    protected abstract ReadWriteLock getLock();

    protected void acquireWriteLock()
    {
        this.getLock().writeLock().lock();
    }

    protected void unlockWriteLock()
    {
        this.getLock().writeLock().unlock();
    }

    protected void acquireReadLock()
    {
        this.getLock().readLock().lock();
    }

    protected void unlockReadLock()
    {
        this.getLock().readLock().unlock();
    }

    protected void withReadLockRun(Runnable block)
    {
        this.acquireReadLock();
        try
        {
            block.run();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean contains(Object item)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().contains(item);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean containsAll(Collection<?> collection)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().containsAll(collection);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().containsAllIterable(source);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean containsAllArguments(Object... elements)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().containsAllArguments(elements);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().allSatisfy(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> boolean allSatisfyWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().allSatisfyWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().anySatisfy(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> boolean anySatisfyWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().anySatisfyWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> toList()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toList();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toMap(keyFunction, valueFunction);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedMap(keyFunction, valueFunction);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedMap(comparator, keyFunction, valueFunction);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public LazyIterable<T> asLazy()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().asLazy();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableSet<T> toSet()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSet();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableBag<T> toBag()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toBag();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> toSortedList()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedList();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedList(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(
            Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedListBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableSortedSet<T> toSortedSet()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedSet();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedSet(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(
            Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toSortedSetBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public int count(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().count(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> int countWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().countWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T find(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().find(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T min(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().min(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T max(Comparator<? super T> comparator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().max(comparator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T min()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().min();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T max()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().max();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().minBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().maxBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T findIfNone(
            Predicate<? super T> predicate,
            Generator<? extends T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().findIfNone(predicate, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> T findWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().findWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> T findWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Generator<? extends T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().findWithIfNone(predicate, parameter, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T getFirst()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().getFirst();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T getLast()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().getLast();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public boolean notEmpty()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().notEmpty();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> Twin<MutableList<T>> partitionWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().partitionWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends Collection<V>> R transform(
            Function<? super T, ? extends V> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().transform(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends Collection<V>> R flatTransform(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().flatTransform(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends Collection<V>> R transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().transformIf(predicate, function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P, V, R extends Collection<V>> R transformWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter,
            R targetCollection)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().transformWith(function, parameter, targetCollection);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P, R extends Collection<T>> R filterWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().filterWith(predicate, parameter, targetCollection);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends Collection<T>> R filterNot(
            Predicate<? super T> predicate,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().filterNot(predicate, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P, R extends Collection<T>> R filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().filterNotWith(predicate, parameter, targetCollection);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().filter(predicate, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <IV> IV foldLeft(
            IV initialValue,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().foldLeft(initialValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public int foldLeft(int initialValue, IntObjectToIntFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().foldLeft(initialValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public long foldLeft(long initialValue, LongObjectToLongFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().foldLeft(initialValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().foldLeft(initialValue, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <IV, P> IV foldLeftWith(
            IV initialValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().foldLeftWith(initialValue, function, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void removeIf(Predicate<? super T> predicate)
    {
        this.acquireWriteLock();
        try
        {
            this.getDelegate().removeIf(predicate);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public <P> void removeIfWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireWriteLock();
        try
        {
            this.getDelegate().removeIfWith(predicate, parameter);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean add(T item)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().add(item);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean addAll(Collection<? extends T> collection)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().addAll(collection);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().addAllIterable(iterable);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public void clear()
    {
        this.acquireWriteLock();
        try
        {
            this.getDelegate().clear();
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean isEmpty()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().isEmpty();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    /**
     * This method is not supported directly on MultiReader collections because it is not thread-safe. If you would like
     * to use an iterator with a MultiReader collection, then you must do the following:
     * <p/>
     * <pre>
     * multiReaderList.withReadLockAndDelegate(new Procedure<MutableList<Person>>()
     * {
     *     public void value(MutableList<Person> people)
     *     {
     *         Iterator it = people.iterator();
     *         ....
     *     }
     * });
     * </pre>
     * <p/>
     * <pre>
     * final Collection jdkSet = new HashSet();
     * final boolean containsAll = new boolean[1];
     * multiReaderList.withReadLockAndDelegate(new Procedure<MutableList<Person>>()
     * {
     *     public void value(MutableList<Person> people)
     *     {
     *         set.addAll(people); // addAll uses iterator() in AbstractCollection
     *         containsAll[0] = set.containsAll(people); // containsAll uses iterator() in AbstractCollection
     *     }
     * });
     * </pre>
     */
    public Iterator<T> iterator()
    {
        throw new UnsupportedOperationException(
                "Iterator is not supported directly on MultiReader collections.  "
                        + "If you would like to use an iterator, you must either use withReadLockAndDelegate() or withWriteLockAndDelegate().");
    }

    public boolean remove(Object item)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().remove(item);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean removeAll(Collection<?> collection)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().removeAll(collection);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().removeAllIterable(iterable);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean retainAll(Collection<?> collection)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().retainAll(collection);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean retainAllIterable(Iterable<?> iterable)
    {
        this.acquireWriteLock();
        try
        {
            return this.getDelegate().retainAllIterable(iterable);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public int size()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().size();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public Object[] toArray()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toArray();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <E> E[] toArray(E[] a)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toArray(a);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void forEach(Procedure<? super T> procedure)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().forEach(procedure);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().forEachWith(procedure, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().forEachWithIndex(objectIntProcedure);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    @Override
    public String toString()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().toString();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public String makeString()
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().makeString();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public String makeString(String separator)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().makeString(separator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public String makeString(String start, String separator, String end)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().makeString(start, separator, end);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void appendString(Appendable appendable)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().appendString(appendable);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().appendString(appendable, separator);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        this.acquireReadLock();
        try
        {
            this.getDelegate().appendString(appendable, start, separator, end);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().groupBy(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().groupByEach(function, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().zip(that, target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        this.acquireReadLock();
        try
        {
            return this.getDelegate().zipWithIndex(target);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    protected abstract static class UntouchableMutableCollection<T>
            implements MutableCollection<T>
    {
        protected MutableCollection<T> delegate;

        public boolean allSatisfy(Predicate<? super T> predicate)
        {
            return this.delegate.allSatisfy(predicate);
        }

        public <P> boolean allSatisfyWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.delegate.allSatisfyWith(predicate, parameter);
        }

        public boolean anySatisfy(Predicate<? super T> predicate)
        {
            return this.delegate.anySatisfy(predicate);
        }

        public <P> boolean anySatisfyWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.delegate.anySatisfyWith(predicate, parameter);
        }

        public MutableList<T> toList()
        {
            return this.delegate.toList();
        }

        public <NK, NV> MutableMap<NK, NV> toMap(
                Function<? super T, ? extends NK> keyFunction,
                Function<? super T, ? extends NV> valueFunction)
        {
            return this.delegate.toMap(keyFunction, valueFunction);
        }

        public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
                Function<? super T, ? extends NK> keyFunction,
                Function<? super T, ? extends NV> valueFunction)
        {
            return this.delegate.toSortedMap(keyFunction, valueFunction);
        }

        public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
                Function<? super T, ? extends NK> keyFunction,
                Function<? super T, ? extends NV> valueFunction)
        {
            return this.delegate.toSortedMap(comparator, keyFunction, valueFunction);
        }

        public MutableSet<T> toSet()
        {
            return this.delegate.toSet();
        }

        public MutableBag<T> toBag()
        {
            return this.delegate.toBag();
        }

        public MutableList<T> toSortedList()
        {
            return this.delegate.toSortedList();
        }

        public MutableList<T> toSortedList(Comparator<? super T> comparator)
        {
            return this.delegate.toSortedList(comparator);
        }

        public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.toSortedListBy(function);
        }

        public MutableSortedSet<T> toSortedSet()
        {
            return this.delegate.toSortedSet();
        }

        public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
        {
            return this.delegate.toSortedSet(comparator);
        }

        public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.toSortedSetBy(function);
        }

        public <V, R extends Collection<V>> R transform(
                Function<? super T, ? extends V> function,
                R target)
        {
            return this.delegate.transform(function, target);
        }

        public <V, R extends Collection<V>> R flatTransform(
                Function<? super T, ? extends Iterable<V>> function,
                R target)
        {
            return this.delegate.flatTransform(function, target);
        }

        public <V, R extends Collection<V>> R transformIf(
                Predicate<? super T> predicate,
                Function<? super T, ? extends V> function,
                R target)
        {
            return this.delegate.transformIf(predicate, function, target);
        }

        public <P, V, R extends Collection<V>> R transformWith(
                Function2<? super T, ? super P, ? extends V> function,
                P parameter,
                R targetCollection)
        {
            return this.delegate.transformWith(function, parameter, targetCollection);
        }

        public <V, R extends MutableMultimap<V, T>> R groupBy(
                Function<? super T, ? extends V> function,
                R target)
        {
            return this.delegate.groupBy(function, target);
        }

        public <V, R extends MutableMultimap<V, T>> R groupByEach(
                Function<? super T, ? extends Iterable<V>> function,
                R target)
        {
            return this.delegate.groupByEach(function, target);
        }

        public int count(Predicate<? super T> predicate)
        {
            return this.delegate.count(predicate);
        }

        public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
        {
            return this.delegate.countWith(predicate, parameter);
        }

        public T find(Predicate<? super T> predicate)
        {
            return this.delegate.find(predicate);
        }

        public T min(Comparator<? super T> comparator)
        {
            return this.delegate.min(comparator);
        }

        public T max(Comparator<? super T> comparator)
        {
            return this.delegate.max(comparator);
        }

        public T min()
        {
            return this.delegate.min();
        }

        public T max()
        {
            return this.delegate.max();
        }

        public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.minBy(function);
        }

        public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
        {
            return this.delegate.maxBy(function);
        }

        public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
        {
            return this.delegate.findIfNone(predicate, function);
        }

        public <P> T findWith(Predicate2<? super T, ? super P> predicate, P parameter)
        {
            return this.delegate.findWith(predicate, parameter);
        }

        public <P> T findWithIfNone(
                Predicate2<? super T, ? super P> predicate,
                P parameter,
                Generator<? extends T> function)
        {
            return this.delegate.findWithIfNone(predicate, parameter, function);
        }

        public T getFirst()
        {
            return this.delegate.getFirst();
        }

        public T getLast()
        {
            return this.delegate.getLast();
        }

        public <IV> IV foldLeft(
                IV initialValue,
                Function2<? super IV, ? super T, ? extends IV> function)
        {
            return this.delegate.foldLeft(initialValue, function);
        }

        public int foldLeft(int initialValue, IntObjectToIntFunction<? super T> function)
        {
            return this.delegate.foldLeft(initialValue, function);
        }

        public long foldLeft(long initialValue, LongObjectToLongFunction<? super T> function)
        {
            return this.delegate.foldLeft(initialValue, function);
        }

        public double foldLeft(double initialValue, DoubleObjectToDoubleFunction<? super T> function)
        {
            return this.delegate.foldLeft(initialValue, function);
        }

        public <IV, P> IV foldLeftWith(
                IV initialValue,
                Function3<? super IV, ? super T, ? super P, ? extends IV> function,
                P parameter)
        {
            return this.delegate.foldLeftWith(initialValue, function, parameter);
        }

        public boolean notEmpty()
        {
            return this.delegate.notEmpty();
        }

        public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
        {
            return this.delegate.filterNot(predicate, target);
        }

        public <P, R extends Collection<T>> R filterNotWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter,
                R targetCollection)
        {
            return this.delegate.filterNotWith(predicate, parameter, targetCollection);
        }

        public void removeIf(Predicate<? super T> predicate)
        {
            this.delegate.removeIf(predicate);
        }

        public <P> void removeIfWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            this.delegate.removeIfWith(predicate, parameter);
        }

        public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
        {
            return this.delegate.filter(predicate, target);
        }

        public <P> Twin<MutableList<T>> partitionWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.delegate.partitionWith(predicate, parameter);
        }

        public <P, R extends Collection<T>> R filterWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter,
                R targetCollection)
        {
            return this.delegate.filterWith(predicate, parameter, targetCollection);
        }

        public boolean add(T o)
        {
            return this.delegate.add(o);
        }

        public boolean addAll(Collection<? extends T> collection)
        {
            return this.delegate.addAll(collection);
        }

        public boolean addAllIterable(Iterable<? extends T> iterable)
        {
            return this.delegate.addAllIterable(iterable);
        }

        public void clear()
        {
            this.delegate.clear();
        }

        public boolean contains(Object o)
        {
            return this.delegate.contains(o);
        }

        public boolean containsAll(Collection<?> collection)
        {
            return this.delegate.containsAll(collection);
        }

        public boolean containsAllIterable(Iterable<?> source)
        {
            return this.delegate.containsAllIterable(source);
        }

        public boolean containsAllArguments(Object... elements)
        {
            return this.delegate.containsAllArguments(elements);
        }

        @Override
        public boolean equals(Object o)
        {
            return this.delegate.equals(o);
        }

        @Override
        public int hashCode()
        {
            return this.delegate.hashCode();
        }

        public boolean isEmpty()
        {
            return this.delegate.isEmpty();
        }

        public boolean remove(Object o)
        {
            return this.delegate.remove(o);
        }

        public boolean removeAll(Collection<?> collection)
        {
            return this.delegate.removeAll(collection);
        }

        public boolean removeAllIterable(Iterable<?> iterable)
        {
            return this.delegate.removeAllIterable(iterable);
        }

        public boolean retainAll(Collection<?> collection)
        {
            return this.delegate.retainAll(collection);
        }

        public boolean retainAllIterable(Iterable<?> iterable)
        {
            return this.delegate.retainAllIterable(iterable);
        }

        public int size()
        {
            return this.delegate.size();
        }

        public Object[] toArray()
        {
            return this.delegate.toArray();
        }

        public <T> T[] toArray(T[] a)
        {
            return this.delegate.toArray(a);
        }

        public void forEach(Procedure<? super T> procedure)
        {
            this.delegate.forEach(procedure);
        }

        public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
        {
            this.delegate.forEachWith(procedure, parameter);
        }

        public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
        {
            this.delegate.forEachWithIndex(objectIntProcedure);
        }

        @Override
        public String toString()
        {
            return this.delegate.toString();
        }

        public String makeString()
        {
            return this.delegate.makeString();
        }

        public String makeString(String separator)
        {
            return this.delegate.makeString(separator);
        }

        public String makeString(String start, String separator, String end)
        {
            return this.delegate.makeString(start, separator, end);
        }

        public void appendString(Appendable appendable)
        {
            this.delegate.appendString(appendable);
        }

        public void appendString(Appendable appendable, String separator)
        {
            this.delegate.appendString(appendable, separator);
        }

        public void appendString(Appendable appendable, String start, String separator, String end)
        {
            this.delegate.appendString(appendable, start, separator, end);
        }

        public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
        {
            return this.delegate.zip(that, target);
        }

        public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
        {
            return this.delegate.zipWithIndex(target);
        }

        public RichIterable<RichIterable<T>> chunk(int size)
        {
            return this.delegate.chunk(size);
        }
    }
}
