/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.lazy.parallel;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.ByteIterable;
import com.gs.collections.api.CharIterable;
import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.FloatIterable;
import com.gs.collections.api.IntIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.LongIterable;
import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.ShortIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.AbstractRichIterable;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;

@Beta
public abstract class AbstractParallelIterable<T> extends AbstractRichIterable<T> implements ParallelIterable<T>
{
    public Iterator<T> iterator()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public LazyIterable<T> asLazy()
    {
        throw new UnsupportedOperationException();
    }

    public int size()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString()
    {
        throw new UnsupportedOperationException();
    }

    public T getFirst()
    {
        throw new UnsupportedOperationException();
    }

    public T getLast()
    {
        throw new UnsupportedOperationException();
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <P, V, R extends Collection<V>> R collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        throw new UnsupportedOperationException();
    }

    public BooleanIterable collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        throw new UnsupportedOperationException();
    }

    public ByteIterable collectByte(ByteFunction<? super T> byteFunction)
    {
        throw new UnsupportedOperationException();
    }

    public CharIterable collectChar(CharFunction<? super T> charFunction)
    {
        throw new UnsupportedOperationException();
    }

    public DoubleIterable collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        throw new UnsupportedOperationException();
    }

    public FloatIterable collectFloat(FloatFunction<? super T> floatFunction)
    {
        throw new UnsupportedOperationException();
    }

    public IntIterable collectInt(IntFunction<? super T> intFunction)
    {
        throw new UnsupportedOperationException();
    }

    public LongIterable collectLong(LongFunction<? super T> longFunction)
    {
        throw new UnsupportedOperationException();
    }

    public ShortIterable collectShort(ShortFunction<? super T> shortFunction)
    {
        throw new UnsupportedOperationException();
    }

    public PartitionIterable<T> partition(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException();
    }

    public <P> PartitionIterable<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException();
    }

    public <S> RichIterable<Pair<T, S>> zip(Iterable<S> that)
    {
        throw new UnsupportedOperationException();
    }

    public RichIterable<Pair<T, Integer>> zipWithIndex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.anySatisfy(Predicates.bind(predicate, parameter));
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.allSatisfy(Predicates.bind(predicate, parameter));
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.noneSatisfy(Predicates.bind(predicate, parameter));
    }

    @Override
    public MutableList<T> toList()
    {
        MutableList<T> result = FastList.<T>newList().asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    @Override
    public MutableSet<T> toSet()
    {
        MutableSet<T> result = UnifiedSet.<T>newSet().asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    @Override
    public MutableBag<T> toBag()
    {
        MutableBag<T> result = HashBag.<T>newBag().asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    @Override
    public MutableSortedSet<T> toSortedSet()
    {
        MutableSortedSet<T> result = TreeSortedSet.<T>newSet().asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    @Override
    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        MutableSortedSet<T> result = TreeSortedSet.newSet(comparator).asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupBy(Function<? super T, ? extends V> function, R target)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupByEach(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        throw new UnsupportedOperationException();
    }

    public <K, V> MapIterable<K, V> aggregateInPlaceBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Procedure2<? super V, ? super T> mutatingAggregator)
    {
        throw new UnsupportedOperationException();
    }

    public <K, V> MapIterable<K, V> aggregateBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        throw new UnsupportedOperationException();
    }
}
