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

package ponzu.impl.set.mutable;

import java.util.Collection;
import java.util.Set;

import ponzu.api.LazyIterable;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.multimap.set.MutableSetMultimap;
import ponzu.api.partition.set.PartitionMutableSet;
import ponzu.api.set.ImmutableSet;
import ponzu.api.set.MutableSet;
import ponzu.api.set.SetIterable;
import ponzu.api.set.UnsortedSetIterable;
import ponzu.api.tuple.Pair;
import ponzu.impl.collection.mutable.SynchronizedMutableCollection;
import ponzu.impl.factory.Sets;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * A synchronized view of a set.
 *
 * @see MutableSet#asSynchronized()
 */
@ThreadSafe
public class SynchronizedMutableSet<E>
        extends SynchronizedMutableCollection<E>
        implements MutableSet<E>
{
    private static final long serialVersionUID = 1L;

    public SynchronizedMutableSet(MutableSet<E> set)
    {
        super(set);
    }

    public SynchronizedMutableSet(MutableSet<E> set, Object newLock)
    {
        super(set, newLock);
    }

    /**
     * This method will take a MutableSet and wrap it directly in a SynchronizedMutableSet.  It will
     * take any other non-GS-collection and first adapt it will a SetAdapter, and then return a
     * SynchronizedMutableSet that wraps the adapter.
     */
    public static <E, S extends Set<E>> SynchronizedMutableSet<E> of(S set)
    {
        return new SynchronizedMutableSet<E>(SetAdapter.adapt(set));
    }

    /**
     * This method will take a MutableSet and wrap it directly in a SynchronizedMutableSet.  It will
     * take any other non-GS-collection and first adapt it will a SetAdapter, and then return a
     * SynchronizedMutableSet that wraps the adapter.  Additionally, a developer specifies which lock to use
     * with the collection.
     */
    public static <E, S extends Set<E>> SynchronizedMutableSet<E> of(S set, Object lock)
    {
        return new SynchronizedMutableSet<E>(SetAdapter.adapt(set), lock);
    }

    @GuardedBy("getLock()")
    private MutableSet<E> getMutableSet()
    {
        return (MutableSet<E>) this.getCollection();
    }

    @Override
    public MutableSet<E> asUnmodifiable()
    {
        synchronized (this.getLock())
        {
            return UnmodifiableMutableSet.of(this);
        }
    }

    @Override
    public ImmutableSet<E> toImmutable()
    {
        synchronized (this.getLock())
        {
            return Sets.immutable.ofAll(this.getMutableSet());
        }
    }

    @Override
    public MutableSet<E> asSynchronized()
    {
        return this;
    }

    @Override
    public MutableSet<E> clone()
    {
        synchronized (this.getLock())
        {
            return of(this.getMutableSet().clone());
        }
    }

    @Override
    public <V> MutableSet<V> transform(Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().transform(function);
        }
    }

    @Override
    public <V> MutableSet<V> flatTransform(Function<? super E, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().flatTransform(function);
        }
    }

    @Override
    public <V> MutableSet<V> transformIf(
            Predicate<? super E> predicate,
            Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().transformIf(predicate, function);
        }
    }

    @Override
    public <P, V> MutableSet<V> transformWith(Function2<? super E, ? super P, ? extends V> function, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().transformWith(function, parameter);
        }
    }

    @Override
    public <V> MutableSetMultimap<V, E> groupBy(Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().groupBy(function);
        }
    }

    @Override
    public <V> MutableSetMultimap<V, E> groupByEach(Function<? super E, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().groupByEach(function);
        }
    }

    @Override
    public MutableSet<E> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().newEmpty();
        }
    }

    @Override
    public MutableSet<E> filterNot(Predicate<? super E> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().filterNot(predicate);
        }
    }

    @Override
    public <P> MutableSet<E> filterNotWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().filterNotWith(predicate, parameter);
        }
    }

    @Override
    public MutableSet<E> filter(Predicate<? super E> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().filter(predicate);
        }
    }

    @Override
    public <P> MutableSet<E> filterWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().filterWith(predicate, parameter);
        }
    }

    @Override
    public PartitionMutableSet<E> partition(Predicate<? super E> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().partition(predicate);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().equals(obj);
        }
    }

    @Override
    public int hashCode()
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().hashCode();
        }
    }

    @Override
    public <S> MutableSet<Pair<E, S>> zip(Iterable<S> that)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().zip(that);
        }
    }

    @Override
    public <S, R extends Collection<Pair<E, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().zip(that, target);
        }
    }

    @Override
    public MutableSet<Pair<E, Integer>> zipWithIndex()
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().zipWithIndex();
        }
    }

    @Override
    public <R extends Collection<Pair<E, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().zipWithIndex(target);
        }
    }

    public MutableSet<E> union(SetIterable<? extends E> set)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().union(set);
        }
    }

    public <R extends Set<E>> R unionInto(SetIterable<? extends E> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().unionInto(set, targetSet);
        }
    }

    public MutableSet<E> intersect(SetIterable<? extends E> set)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().intersect(set);
        }
    }

    public <R extends Set<E>> R intersectInto(SetIterable<? extends E> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().intersectInto(set, targetSet);
        }
    }

    public MutableSet<E> difference(SetIterable<? extends E> subtrahendSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().difference(subtrahendSet);
        }
    }

    public <R extends Set<E>> R differenceInto(SetIterable<? extends E> subtrahendSet, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().differenceInto(subtrahendSet, targetSet);
        }
    }

    public MutableSet<E> symmetricDifference(SetIterable<? extends E> setB)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().symmetricDifference(setB);
        }
    }

    public <R extends Set<E>> R symmetricDifferenceInto(SetIterable<? extends E> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().symmetricDifferenceInto(set, targetSet);
        }
    }

    public boolean isSubsetOf(SetIterable<? extends E> candidateSuperset)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().isSubsetOf(candidateSuperset);
        }
    }

    public boolean isProperSubsetOf(SetIterable<? extends E> candidateSuperset)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().isProperSubsetOf(candidateSuperset);
        }
    }

    public MutableSet<UnsortedSetIterable<E>> powerSet()
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().powerSet();
        }
    }

    public <B> LazyIterable<Pair<E, B>> cartesianProduct(SetIterable<B> set)
    {
        synchronized (this.getLock())
        {
            return this.getMutableSet().cartesianProduct(set);
        }
    }

    @Override
    public MutableSet<E> with(E element)
    {
        this.add(element);
        return this;
    }

    @Override
    public MutableSet<E> without(E element)
    {
        this.remove(element);
        return this;
    }

    @Override
    public MutableSet<E> withAll(Iterable<? extends E> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    public MutableSet<E> withoutAll(Iterable<? extends E> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }
}
