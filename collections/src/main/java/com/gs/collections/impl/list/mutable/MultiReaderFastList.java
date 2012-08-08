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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.AbstractMultiReaderMutableCollection;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.utility.LazyIterate;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * MultiReadFastList provides a thread-safe wrapper around a FastList, using a ReentrantReadWriteLock.  In order to
 * provide true thread-safety, MultiReaderFastList does not implement iterator(), listIterator(), listIterator(int), or
 * get(int), as all of these methods require an external lock to be taken to provide thread-safe iteration.  All of
 * these methods are available however, if you use the withReadLockAndDelegate() or withWriteLockAndDelegate() methods.
 * Both of these methods take a parameter of type Procedure<MutableList>, and a wrapped version of the underlying
 * FastList is returned.  This wrapper guarantees that no external pointer can ever reference the underlying FastList
 * outside of a locked procedure.  In the case of the read lock method, an Unmodifiable version of the collection is
 * offered, which will throw UnsupportedOperationExceptions on any write methods like add or remove.
 */
public final class MultiReaderFastList<T>
        extends AbstractMultiReaderMutableCollection<T>
        implements RandomAccess, Externalizable, MutableList<T>
{
    private static final long serialVersionUID = 1L;

    private transient ReadWriteLock lock;
    private MutableList<T> delegate;

    @SuppressWarnings("UnusedDeclaration")
    public MultiReaderFastList()
    {
        // For Externalizable use only
    }

    private MultiReaderFastList(MutableList<T> newDelegate)
    {
        this(newDelegate, new ReentrantReadWriteLock());
    }

    private MultiReaderFastList(MutableList<T> newDelegate, ReadWriteLock newLock)
    {
        this.lock = newLock;
        this.delegate = newDelegate;
    }

    public static <T> MultiReaderFastList<T> newList()
    {
        return new MultiReaderFastList<T>(FastList.<T>newList());
    }

    public static <T> MultiReaderFastList<T> newList(int capacity)
    {
        return new MultiReaderFastList<T>(FastList.<T>newList(capacity));
    }

    public static <T> MultiReaderFastList<T> newList(Iterable<T> iterable)
    {
        return new MultiReaderFastList<T>(FastList.<T>newList(iterable));
    }

    public static <T> MultiReaderFastList<T> newListWith(T... elements)
    {
        return new MultiReaderFastList<T>(FastList.<T>newListWith(elements));
    }

    @Override
    protected MutableList<T> getDelegate()
    {
        return this.delegate;
    }

    @Override
    protected ReadWriteLock getLock()
    {
        return this.lock;
    }

    UntouchableMutableList<T> asReadUntouchable()
    {
        return new UntouchableMutableList<T>(this.asUnmodifiable());
    }

    UntouchableMutableList<T> asWriteUntouchable()
    {
        return new UntouchableMutableList<T>(this.delegate);
    }

    public void withReadLockAndDelegate(Procedure<MutableList<T>> procedure)
    {
        this.acquireReadLock();
        try
        {
            UntouchableMutableList<T> list = this.asReadUntouchable();
            procedure.value(list);
            list.becomeUseless();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void withWriteLockAndDelegate(Procedure<MutableList<T>> procedure)
    {
        this.acquireWriteLock();
        try
        {
            UntouchableMutableList<T> list = this.asWriteUntouchable();
            procedure.value(list);
            list.becomeUseless();
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public MutableList<T> asSynchronized()
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.clone().asSynchronized();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> asUnmodifiable()
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.asUnmodifiable();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public ImmutableList<T> toImmutable()
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.toImmutable();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    @Override
    public MutableList<T> clone()
    {
        this.acquireReadLock();
        try
        {
            return new MultiReaderFastList<T>(this.delegate.clone());
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.collect(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V> MutableList<V> flatCollect(
            Function<? super T, ? extends Iterable<V>> function)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.flatCollect(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.collectIf(predicate, function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P, V> MutableList<V> collectWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.collectWith(function, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> newEmpty()
    {
        return MultiReaderFastList.newList();
    }

    public MutableList<T> reject(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.reject(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> MutableList<T> rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.rejectWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> select(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.select(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <P> MutableList<T> selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.selectWith(predicate, parameter);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.partition(predicate);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <S> MutableList<S> selectInstancesOf(Class<S> clazz)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.selectInstancesOf(clazz);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> sortThis()
    {
        this.acquireWriteLock();
        try
        {
            this.delegate.sortThis();
            return this;
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public MutableList<T> sortThis(Comparator<? super T> comparator)
    {
        this.acquireWriteLock();
        try
        {
            this.delegate.sortThis(comparator);
            return this;
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public <V extends Comparable<? super V>> MutableList<T> sortThisBy(
            Function<? super T, ? extends V> function)
    {
        this.acquireWriteLock();
        try
        {
            this.delegate.sortThisBy(function);
            return this;
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public MutableList<T> subList(int fromIndex, int toIndex)
    {
        this.acquireReadLock();
        try
        {
            return new MultiReaderFastList<T>(this.delegate.subList(fromIndex, toIndex), this.lock);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    @Override
    public boolean equals(Object o)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.equals(o);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    @Override
    public int hashCode()
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.hashCode();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public T get(int index)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.get(index);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public int indexOf(Object o)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.indexOf(o);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public int lastIndexOf(Object o)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.lastIndexOf(o);
        }
        finally
        {
            this.unlockReadLock();
        }
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

    /**
     * This method is not supported directly on a MultiReaderFastList.  If you would like to use a ListIterator with
     * MultiReaderFastList, then you must do the following:
     * <p/>
     * <pre>
     * multiReaderList.withReadLockAndDelegate(new Procedure<MutableList<Person>>()
     * {
     *     public void value(MutableList<Person> people)
     *     {
     *         Iterator it = people.listIterator();
     *         ....
     *     }
     * });
     * </pre>
     */
    public ListIterator<T> listIterator()
    {
        throw new UnsupportedOperationException(
                "ListIterator is not supported for MultiReaderFastList.  "
                        + "If you would like to use a ListIterator, you must either use withReadLockAndDelegate() or withWriteLockAndDelegate().");
    }

    /**
     * This method is not supported directly on a MultiReaderFastList.  If you would like to use a ListIterator with
     * MultiReaderFastList, then you must do the following:
     * <p/>
     * <pre>
     * multiReaderList.withReadLockAndDelegate(new Procedure<MutableList<Person>>()
     * {
     *     public void value(MutableList<Person> people)
     *     {
     *         Iterator it = people.listIterator(0);
     *         ....
     *     }
     * });
     * </pre>
     */
    public ListIterator<T> listIterator(int index)
    {
        throw new UnsupportedOperationException(
                "ListIterator is not supported for MultiReaderFastList.  "
                        + "If you would like to use a ListIterator, you must either use withReadLockAndDelegate() or withWriteLockAndDelegate().");
    }

    public T remove(int index)
    {
        this.acquireWriteLock();
        try
        {
            return this.delegate.remove(index);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public T set(int index, T element)
    {
        this.acquireWriteLock();
        try
        {
            return this.delegate.set(index, element);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public boolean addAll(int index, Collection<? extends T> collection)
    {
        this.acquireWriteLock();
        try
        {
            return this.delegate.addAll(index, collection);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public void add(int index, T element)
    {
        this.acquireWriteLock();
        try
        {
            this.delegate.add(index, element);
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public void forEach(int startIndex, int endIndex, Procedure<? super T> procedure)
    {
        this.acquireReadLock();
        try
        {
            this.delegate.forEach(startIndex, endIndex, procedure);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public void reverseForEach(final Procedure<? super T> procedure)
    {
        this.withReadLockRun(new Runnable()
        {
            public void run()
            {
                MultiReaderFastList.this.getDelegate().reverseForEach(procedure);
            }
        });
    }

    public void forEachWithIndex(final int fromIndex, final int toIndex, final ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.withReadLockRun(new Runnable()
        {
            public void run()
            {
                MultiReaderFastList.this.getDelegate().forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
            }
        });
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.delegate);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.delegate = (MutableList<T>) in.readObject();
        this.lock = new ReentrantReadWriteLock();
    }

    // Exposed for testing

    static final class UntouchableMutableList<T>
            extends UntouchableMutableCollection<T>
            implements MutableList<T>
    {
        private final MutableList<UntouchableListIterator<T>> requestedIterators = mList();
        private final MutableList<UntouchableMutableList<T>> requestedSubLists = mList();

        private UntouchableMutableList(MutableList<T> delegate)
        {
            this.delegate = delegate;
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

        public MutableList<T> asSynchronized()
        {
            throw new UnsupportedOperationException();
        }

        public MutableList<T> asUnmodifiable()
        {
            throw new UnsupportedOperationException();
        }

        public LazyIterable<T> asLazy()
        {
            return LazyIterate.adapt(this);
        }

        public ImmutableList<T> toImmutable()
        {
            return Lists.immutable.ofAll(this.getDelegate());
        }

        @Override
        public MutableList<T> clone()
        {
            return this.getDelegate().clone();
        }

        public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
        {
            return this.getDelegate().collect(function);
        }

        public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
        {
            return this.getDelegate().flatCollect(function);
        }

        public <V> MutableList<V> collectIf(
                Predicate<? super T> predicate,
                Function<? super T, ? extends V> function)
        {
            return this.getDelegate().collectIf(predicate, function);
        }

        public <P, V> MutableList<V> collectWith(
                Function2<? super T, ? super P, ? extends V> function,
                P parameter)
        {
            return this.getDelegate().collectWith(function, parameter);
        }

        public <V> MutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
        {
            return this.getDelegate().groupBy(function);
        }

        public <V> MutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
        {
            return this.getDelegate().groupByEach(function);
        }

        public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
        {
            this.getDelegate().forEach(fromIndex, toIndex, procedure);
        }

        public void reverseForEach(Procedure<? super T> procedure)
        {
            this.getDelegate().reverseForEach(procedure);
        }

        public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
        {
            this.getDelegate().forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
        }

        public MutableList<T> newEmpty()
        {
            return this.getDelegate().newEmpty();
        }

        public MutableList<T> reject(Predicate<? super T> predicate)
        {
            return this.getDelegate().reject(predicate);
        }

        public <P> MutableList<T> rejectWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.getDelegate().rejectWith(predicate, parameter);
        }

        public MutableList<T> select(Predicate<? super T> predicate)
        {
            return this.getDelegate().select(predicate);
        }

        public <P> MutableList<T> selectWith(
                Predicate2<? super T, ? super P> predicate,
                P parameter)
        {
            return this.getDelegate().selectWith(predicate, parameter);
        }

        public PartitionMutableList<T> partition(Predicate<? super T> predicate)
        {
            return this.getDelegate().partition(predicate);
        }

        public <S> MutableList<S> selectInstancesOf(Class<S> clazz)
        {
            return this.getDelegate().selectInstancesOf(clazz);
        }

        public MutableList<T> sortThis()
        {
            this.getDelegate().sortThis();
            return this;
        }

        public MutableList<T> sortThis(Comparator<? super T> comparator)
        {
            this.getDelegate().sortThis(comparator);
            return this;
        }

        public MutableList<T> toReversed()
        {
            return this.getDelegate().toReversed();
        }

        public MutableList<T> reverseThis()
        {
            this.getDelegate().reverseThis();
            return this;
        }

        public MutableStack<T> toStack()
        {
            return ArrayStack.newStack(this.delegate);
        }

        public <V extends Comparable<? super V>> MutableList<T> sortThisBy(Function<? super T, ? extends V> function)
        {
            this.getDelegate().sortThisBy(function);
            return this;
        }

        public MutableList<T> subList(int fromIndex, int toIndex)
        {
            UntouchableMutableList<T> subList = new UntouchableMutableList<T>(
                    this.getDelegate().subList(fromIndex, toIndex));
            this.requestedSubLists.add(subList);
            return subList;
        }

        public Iterator<T> iterator()
        {
            UntouchableListIterator<T> iterator = new UntouchableListIterator<T>(this.delegate.iterator());
            this.requestedIterators.add(iterator);
            return iterator;
        }

        public void add(int index, T element)
        {
            this.getDelegate().add(index, element);
        }

        public boolean addAll(int index, Collection<? extends T> collection)
        {
            return this.getDelegate().addAll(index, collection);
        }

        public T get(int index)
        {
            return this.getDelegate().get(index);
        }

        public int indexOf(Object o)
        {
            return this.getDelegate().indexOf(o);
        }

        public int lastIndexOf(Object o)
        {
            return this.getDelegate().lastIndexOf(o);
        }

        public ListIterator<T> listIterator()
        {
            UntouchableListIterator<T> iterator = new UntouchableListIterator<T>(this.getDelegate().listIterator());
            this.requestedIterators.add(iterator);
            return iterator;
        }

        public ListIterator<T> listIterator(int index)
        {
            UntouchableListIterator<T> iterator = new UntouchableListIterator<T>(this.getDelegate().listIterator(index));
            this.requestedIterators.add(iterator);
            return iterator;
        }

        public T remove(int index)
        {
            return this.getDelegate().remove(index);
        }

        public T set(int index, T element)
        {
            return this.getDelegate().set(index, element);
        }

        public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
        {
            return this.getDelegate().zip(that);
        }

        public MutableList<Pair<T, Integer>> zipWithIndex()
        {
            return this.getDelegate().zipWithIndex();
        }

        public void becomeUseless()
        {
            this.delegate = null;
            this.requestedSubLists.forEach(new Procedure<UntouchableMutableList<T>>()
            {
                public void value(UntouchableMutableList<T> each)
                {
                    each.becomeUseless();
                }
            });
            this.requestedIterators.forEach(new Procedure<UntouchableListIterator<T>>()
            {
                public void value(UntouchableListIterator<T> each)
                {
                    each.becomeUseless();
                }
            });
        }

        private MutableList<T> getDelegate()
        {
            return (MutableList<T>) this.delegate;
        }
    }

    private static final class UntouchableListIterator<T>
            implements ListIterator<T>
    {
        private Iterator<T> delegate;

        private UntouchableListIterator(Iterator<T> newDelegate)
        {
            this.delegate = newDelegate;
        }

        public void add(T o)
        {
            ((ListIterator<T>) this.delegate).add(o);
        }

        public boolean hasNext()
        {
            return this.delegate.hasNext();
        }

        public boolean hasPrevious()
        {
            return ((ListIterator<T>) this.delegate).hasPrevious();
        }

        public T next()
        {
            return this.delegate.next();
        }

        public int nextIndex()
        {
            return ((ListIterator<T>) this.delegate).nextIndex();
        }

        public T previous()
        {
            return ((ListIterator<T>) this.delegate).previous();
        }

        public int previousIndex()
        {
            return ((ListIterator<T>) this.delegate).previousIndex();
        }

        public void remove()
        {
            this.delegate.remove();
        }

        public void set(T o)
        {
            ((ListIterator<T>) this.delegate).set(o);
        }

        public void becomeUseless()
        {
            this.delegate = null;
        }
    }

    public <V> MutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.groupBy(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <V> MutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.groupByEach(function);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.zip(that);
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<Pair<T, Integer>> zipWithIndex()
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.zipWithIndex();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> toReversed()
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.toReversed();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public MutableList<T> reverseThis()
    {
        this.acquireWriteLock();
        try
        {
            this.delegate.reverseThis();
            return this;
        }
        finally
        {
            this.unlockWriteLock();
        }
    }

    public MutableStack<T> toStack()
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.toStack();
        }
        finally
        {
            this.unlockReadLock();
        }
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        this.acquireReadLock();
        try
        {
            return this.delegate.chunk(size);
        }
        finally
        {
            this.unlockReadLock();
        }
    }
}
