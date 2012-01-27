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

package ponzu.impl.bag.mutable;

import ponzu.api.bag.ImmutableBag;
import ponzu.api.bag.MutableBag;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.map.MutableMap;
import ponzu.api.multimap.bag.MutableBagMultimap;
import ponzu.api.partition.bag.PartitionMutableBag;
import ponzu.api.tuple.Pair;
import ponzu.impl.collection.mutable.UnmodifiableMutableCollection;
import ponzu.impl.factory.Bags;

/**
 * An unmodifiable view of a list.
 *
 * @see MutableBag#asUnmodifiable()
 * @since 1.0
 */
public class UnmodifiableBag<T>
        extends UnmodifiableMutableCollection<T>
        implements MutableBag<T>
{
    private static final long serialVersionUID = 1L;

    protected UnmodifiableBag(MutableBag<? extends T> mutableBag)
    {
        super(mutableBag);
    }

    /**
     * This method will take a MutableBag and wrap it directly in a UnmodifiableBag.
     */
    public static <E, B extends MutableBag<E>> UnmodifiableBag<E> of(B bag)
    {
        if (bag == null)
        {
            throw new IllegalArgumentException("cannot create an UnmodifiableBag for null");
        }
        return new UnmodifiableBag<E>(bag);
    }

    protected MutableBag<T> getMutableBag()
    {
        return (MutableBag<T>) this.getMutableCollection();
    }

    @Override
    public MutableBag<T> asUnmodifiable()
    {
        return this;
    }

    @Override
    public MutableBag<T> asSynchronized()
    {
        return SynchronizedBag.of(this);
    }

    @Override
    public ImmutableBag<T> toImmutable()
    {
        return Bags.immutable.ofAll(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.getMutableBag().equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.getMutableBag().hashCode();
    }

    @Override
    public MutableBag<T> newEmpty()
    {
        return this.getMutableBag().newEmpty();
    }

    @Override
    public MutableBag<T> filter(Predicate<? super T> predicate)
    {
        return this.getMutableBag().filter(predicate);
    }

    @Override
    public <P> MutableBag<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableBag().filterWith(predicate, parameter);
    }

    @Override
    public MutableBag<T> filterNot(Predicate<? super T> predicate)
    {
        return this.getMutableBag().filterNot(predicate);
    }

    @Override
    public <P> MutableBag<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableBag().filterNotWith(predicate, parameter);
    }

    @Override
    public PartitionMutableBag<T> partition(Predicate<? super T> predicate)
    {
        return this.getMutableBag().partition(predicate);
    }

    @Override
    public <V> MutableBag<V> transform(Function<? super T, ? extends V> function)
    {
        return this.getMutableBag().transform(function);
    }

    @Override
    public <V> MutableBag<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableBag().flatTransform(function);
    }

    @Override
    public <P, A> MutableBag<A> transformWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.getMutableBag().transformWith(function, parameter);
    }

    @Override
    public <V> MutableBag<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.getMutableBag().transformIf(predicate, function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.getMutableBag().groupBy(function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableBag().groupByEach(function);
    }

    public void addOccurrences(T item, int occurrences)
    {
        throw new UnsupportedOperationException();
    }

    public boolean removeOccurrences(Object item, int occurrences)
    {
        throw new UnsupportedOperationException();
    }

    public int sizeDistinct()
    {
        return this.getMutableBag().sizeDistinct();
    }

    public int occurrencesOf(Object item)
    {
        return this.getMutableBag().occurrencesOf(item);
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.getMutableBag().forEachWithOccurrences(objectIntProcedure);
    }

    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        return this.getMutableBag().toMapOfItemToCount();
    }

    @Override
    public <S> MutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.getMutableBag().zip(that);
    }

    @Override
    public MutableBag<Pair<T, Integer>> zipWithIndex()
    {
        return this.getMutableBag().zipWithIndex();
    }

    @Override
    public MutableBag<T> with(T element)
    {
        throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableBag<T> without(T element)
    {
        throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableBag<T> withAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableBag<T> withoutAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
    }
}
