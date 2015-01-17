/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.lazy;

import java.util.Collection;

import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.LazyByteIterable;
import com.gs.collections.api.LazyCharIterable;
import com.gs.collections.api.LazyDoubleIterable;
import com.gs.collections.api.LazyFloatIterable;
import com.gs.collections.api.LazyIntIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.LazyLongIterable;
import com.gs.collections.api.LazyShortIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.AbstractRichIterable;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.PartitionProcedure;
import com.gs.collections.impl.lazy.primitive.CollectBooleanIterable;
import com.gs.collections.impl.lazy.primitive.CollectByteIterable;
import com.gs.collections.impl.lazy.primitive.CollectCharIterable;
import com.gs.collections.impl.lazy.primitive.CollectDoubleIterable;
import com.gs.collections.impl.lazy.primitive.CollectFloatIterable;
import com.gs.collections.impl.lazy.primitive.CollectIntIterable;
import com.gs.collections.impl.lazy.primitive.CollectLongIterable;
import com.gs.collections.impl.lazy.primitive.CollectShortIterable;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.utility.LazyIterate;
import net.jcip.annotations.Immutable;

/**
 * AbstractLazyIterable provides a base from which deferred iterables such as SelectIterable,
 * RejectIterable and CollectIterable can be derived.
 */
@Immutable
public abstract class AbstractLazyIterable<T>
        extends AbstractRichIterable<T>
        implements LazyIterable<T>
{
    @Override
    public LazyIterable<T> asLazy()
    {
        return this;
    }

    public <R extends Collection<T>> R into(R target)
    {
        this.forEachWith(Procedures2.<T>addToCollection(), target);
        return target;
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        return this.toList().toArray(array);
    }

    public int size()
    {
        return this.count(Predicates.alwaysTrue());
    }

    @Override
    public boolean isEmpty()
    {
        return !this.anySatisfy(Predicates.alwaysTrue());
    }

    public T getFirst()
    {
        return this.detect(Predicates.alwaysTrue());
    }

    public T getLast()
    {
        final T[] result = (T[]) new Object[1];
        this.forEach(new Procedure<T>()
        {
            public void value(T each)
            {
                result[0] = each;
            }
        });
        return result[0];
    }

    public LazyIterable<T> select(Predicate<? super T> predicate)
    {
        return LazyIterate.select(this, predicate);
    }

    public <P> LazyIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return LazyIterate.select(this, Predicates.bind(predicate, parameter));
    }

    public LazyIterable<T> reject(Predicate<? super T> predicate)
    {
        return LazyIterate.reject(this, predicate);
    }

    public <P> LazyIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return LazyIterate.reject(this, Predicates.bind(predicate, parameter));
    }

    public PartitionMutableList<T> partition(Predicate<? super T> predicate)
    {
        PartitionMutableList<T> partitionMutableList = new PartitionFastList<T>();
        this.forEach(new PartitionProcedure<T>(predicate, partitionMutableList));
        return partitionMutableList;
    }

    public <P> PartitionMutableList<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.partition(Predicates.bind(predicate, parameter));
    }

    public <S> LazyIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return LazyIterate.selectInstancesOf(this, clazz);
    }

    public <V> LazyIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return LazyIterate.collect(this, function);
    }

    public LazyBooleanIterable collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return new CollectBooleanIterable<T>(this, booleanFunction);
    }

    public LazyByteIterable collectByte(ByteFunction<? super T> byteFunction)
    {
        return new CollectByteIterable<T>(this, byteFunction);
    }

    public LazyCharIterable collectChar(CharFunction<? super T> charFunction)
    {
        return new CollectCharIterable<T>(this, charFunction);
    }

    public LazyDoubleIterable collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return new CollectDoubleIterable<T>(this, doubleFunction);
    }

    public LazyFloatIterable collectFloat(FloatFunction<? super T> floatFunction)
    {
        return new CollectFloatIterable<T>(this, floatFunction);
    }

    public LazyIntIterable collectInt(IntFunction<? super T> intFunction)
    {
        return new CollectIntIterable<T>(this, intFunction);
    }

    public LazyLongIterable collectLong(LongFunction<? super T> longFunction)
    {
        return new CollectLongIterable<T>(this, longFunction);
    }

    public LazyShortIterable collectShort(ShortFunction<? super T> shortFunction)
    {
        return new CollectShortIterable<T>(this, shortFunction);
    }

    public <P, V> LazyIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return LazyIterate.collect(this, Functions.bind(function, parameter));
    }

    public <V> LazyIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return LazyIterate.flatCollect(this, function);
    }

    public LazyIterable<T> concatenate(Iterable<T> iterable)
    {
        return LazyIterate.concatenate(this, iterable);
    }

    public <V> LazyIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return LazyIterate.collectIf(this, predicate, function);
    }

    public LazyIterable<T> take(int count)
    {
        return LazyIterate.take(this, count);
    }

    public LazyIterable<T> drop(int count)
    {
        return LazyIterate.drop(this, count);
    }

    public LazyIterable<T> distinct()
    {
        return LazyIterate.distinct(this);
    }

    public MutableStack<T> toStack()
    {
        return ArrayStack.newStack(this);
    }

    public <V> Multimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, FastListMultimap.<V, T>newMultimap());
    }

    public <V> Multimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, FastListMultimap.<V, T>newMultimap());
    }

    public <V> MapIterable<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return this.groupByUniqueKey(function, UnifiedMap.<V, T>newMap());
    }

    public <S> LazyIterable<Pair<T, S>> zip(Iterable<S> that)
    {
        return LazyIterate.zip(this, that);
    }

    public LazyIterable<Pair<T, Integer>> zipWithIndex()
    {
        return LazyIterate.zipWithIndex(this);
    }

    public LazyIterable<RichIterable<T>> chunk(int size)
    {
        return LazyIterate.chunk(this, size);
    }

    public LazyIterable<T> tap(Procedure<? super T> procedure)
    {
        return LazyIterate.tap(this, procedure);
    }

    public <K, V> MapIterable<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public <K, V> MapIterable<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
    }
}
