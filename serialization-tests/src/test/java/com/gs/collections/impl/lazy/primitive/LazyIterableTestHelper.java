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

package com.gs.collections.impl.lazy.primitive;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

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
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
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
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableByteCollection;
import com.gs.collections.api.collection.primitive.MutableCharCollection;
import com.gs.collections.api.collection.primitive.MutableDoubleCollection;
import com.gs.collections.api.collection.primitive.MutableFloatCollection;
import com.gs.collections.api.collection.primitive.MutableIntCollection;
import com.gs.collections.api.collection.primitive.MutableLongCollection;
import com.gs.collections.api.collection.primitive.MutableShortCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.primitive.ObjectDoubleMap;
import com.gs.collections.api.map.primitive.ObjectLongMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.test.Verify;

public class LazyIterableTestHelper<T> implements LazyIterable<T>
{
    private final String serializedForm;

    public LazyIterableTestHelper(String serializedForm)
    {
        this.serializedForm = serializedForm;
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean notEmpty()
    {
        return false;
    }

    @Override
    public T getFirst()
    {
        return null;
    }

    @Override
    public T getLast()
    {
        return null;
    }

    @Override
    public boolean contains(Object object)
    {
        return false;
    }

    @Override
    public boolean containsAllIterable(Iterable<?> source)
    {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> source)
    {
        return false;
    }

    @Override
    public boolean containsAllArguments(Object... elements)
    {
        return false;
    }

    @Override
    public LazyIterable<T> select(Predicate<? super T> predicate)
    {
        return null;
    }

    @Override
    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return null;
    }

    @Override
    public <P> LazyIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return null;
    }

    @Override
    public <S> LazyIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return null;
    }

    @Override
    public LazyIterable<T> reject(Predicate<? super T> predicate)
    {
        return null;
    }

    @Override
    public <P> LazyIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    @Override
    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return null;
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return null;
    }

    @Override
    public PartitionIterable<T> partition(Predicate<? super T> predicate)
    {
        return null;
    }

    @Override
    public <P> PartitionIterable<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    @Override
    public <V> LazyIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public <P, V> LazyIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return null;
    }

    @Override
    public <P, V, R extends Collection<V>> R collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return null;
    }

    @Override
    public <V> LazyIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return null;
    }

    @Override
    public LazyIterable<T> take(int count)
    {
        return null;
    }

    @Override
    public LazyIterable<T> drop(int count)
    {
        return null;
    }

    @Override
    public LazyIterable<T> distinct()
    {
        return null;
    }

    @Override
    public <V> LazyIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return null;
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return null;
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return null;
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return null;
    }

    @Override
    public <P> T detectWithIfNone(Predicate2<? super T, ? super P> predicate, P parameter, Function0<? extends T> function)
    {
        return null;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return 0;
    }

    @Override
    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return 0;
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return false;
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return false;
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return false;
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return null;
    }

    @Override
    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        return 0;
    }

    @Override
    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        return 0;
    }

    @Override
    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        return 0;
    }

    @Override
    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return 0;
    }

    @Override
    public MutableList<T> toList()
    {
        return null;
    }

    @Override
    public MutableList<T> toSortedList()
    {
        return null;
    }

    @Override
    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return null;
    }

    @Override
    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public MutableSet<T> toSet()
    {
        return null;
    }

    @Override
    public MutableSortedSet<T> toSortedSet()
    {
        return null;
    }

    @Override
    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return null;
    }

    @Override
    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public MutableBag<T> toBag()
    {
        return null;
    }

    @Override
    public MutableSortedBag<T> toSortedBag()
    {
        return null;
    }

    @Override
    public MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator)
    {
        return null;
    }

    @Override
    public <V extends Comparable<? super V>> MutableSortedBag<T> toSortedBagBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public <NK, NV> MutableMap<NK, NV> toMap(Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        return null;
    }

    @Override
    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        return null;
    }

    @Override
    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator, Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        return null;
    }

    @Override
    public LazyIterable<T> asLazy()
    {
        return null;
    }

    @Override
    public Object[] toArray()
    {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] target)
    {
        return null;
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        return null;
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        return null;
    }

    @Override
    public T min()
    {
        return null;
    }

    @Override
    public T max()
    {
        return null;
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public long sumOfInt(IntFunction<? super T> function)
    {
        return 0;
    }

    @Override
    public double sumOfFloat(FloatFunction<? super T> function)
    {
        return 0;
    }

    @Override
    public long sumOfLong(LongFunction<? super T> function)
    {
        return 0;
    }

    @Override
    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        return 0;
    }

    @Override
    public <V> ObjectLongMap<V> sumByInt(Function<T, V> groupBy, IntFunction<? super T> function)
    {
        return null;
    }

    @Override
    public <V> ObjectDoubleMap<V> sumByFloat(Function<T, V> groupBy, FloatFunction<? super T> function)
    {
        return null;
    }

    @Override
    public <V> ObjectLongMap<V> sumByLong(Function<T, V> groupBy, LongFunction<? super T> function)
    {
        return null;
    }

    @Override
    public <V> ObjectDoubleMap<V> sumByDouble(Function<T, V> groupBy, DoubleFunction<? super T> function)
    {
        return null;
    }

    @Override
    public String makeString()
    {
        return null;
    }

    @Override
    public String makeString(String separator)
    {
        return null;
    }

    @Override
    public String makeString(String start, String separator, String end)
    {
        return null;
    }

    @Override
    public void appendString(Appendable appendable)
    {
    }

    @Override
    public void appendString(Appendable appendable, String separator)
    {
    }

    @Override
    public void appendString(Appendable appendable, String start, String separator, String end)
    {
    }

    @Override
    public <V> Multimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupBy(Function<? super T, ? extends V> function, R target)
    {
        return null;
    }

    @Override
    public <V> Multimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return null;
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupByEach(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return null;
    }

    @Override
    public <V> MapIterable<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return null;
    }

    @Override
    public <V, R extends MutableMap<V, T>> R groupByUniqueKey(Function<? super T, ? extends V> function, R target)
    {
        return null;
    }

    @Override
    public LazyIterable<T> concatenate(Iterable<T> iterable)
    {
        return null;
    }

    @Override
    public <S> LazyIterable<Pair<T, S>> zip(Iterable<S> that)
    {
        return null;
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return null;
    }

    @Override
    public LazyIterable<Pair<T, Integer>> zipWithIndex()
    {
        return null;
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return null;
    }

    @Override
    public LazyIterable<RichIterable<T>> chunk(int size)
    {
        return null;
    }

    @Override
    public LazyIterable<T> tap(Procedure<? super T> procedure)
    {
        return null;
    }

    @Override
    public <K, V> MapIterable<K, V> aggregateInPlaceBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Procedure2<? super V, ? super T> mutatingAggregator)
    {
        return null;
    }

    @Override
    public <K, V> MapIterable<K, V> aggregateBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        return null;
    }

    @Override
    public <R extends Collection<T>> R into(R target)
    {
        return null;
    }

    @Override
    public LazyBooleanIterable collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return null;
    }

    @Override
    public <R extends MutableBooleanCollection> R collectBoolean(BooleanFunction<? super T> booleanFunction, R target)
    {
        return null;
    }

    @Override
    public LazyByteIterable collectByte(ByteFunction<? super T> byteFunction)
    {
        return null;
    }

    @Override
    public <R extends MutableByteCollection> R collectByte(ByteFunction<? super T> byteFunction, R target)
    {
        return null;
    }

    @Override
    public LazyCharIterable collectChar(CharFunction<? super T> charFunction)
    {
        return null;
    }

    @Override
    public <R extends MutableCharCollection> R collectChar(CharFunction<? super T> charFunction, R target)
    {
        return null;
    }

    @Override
    public LazyDoubleIterable collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return null;
    }

    @Override
    public <R extends MutableDoubleCollection> R collectDouble(DoubleFunction<? super T> doubleFunction, R target)
    {
        return null;
    }

    @Override
    public LazyFloatIterable collectFloat(FloatFunction<? super T> floatFunction)
    {
        return null;
    }

    @Override
    public <R extends MutableFloatCollection> R collectFloat(FloatFunction<? super T> floatFunction, R target)
    {
        return null;
    }

    @Override
    public LazyIntIterable collectInt(IntFunction<? super T> intFunction)
    {
        return null;
    }

    @Override
    public <R extends MutableIntCollection> R collectInt(IntFunction<? super T> intFunction, R target)
    {
        return null;
    }

    @Override
    public LazyLongIterable collectLong(LongFunction<? super T> longFunction)
    {
        return null;
    }

    @Override
    public <R extends MutableLongCollection> R collectLong(LongFunction<? super T> longFunction, R target)
    {
        return null;
    }

    @Override
    public LazyShortIterable collectShort(ShortFunction<? super T> shortFunction)
    {
        return null;
    }

    @Override
    public <R extends MutableShortCollection> R collectShort(ShortFunction<? super T> shortFunction, R target)
    {
        return null;
    }

    @Override
    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return null;
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
    }

    @Override
    public void each(Procedure<? super T> procedure)
    {
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Verify.assertSerializedForm(1L, this.serializedForm, procedure);
    }

    @Override
    public Iterator<T> iterator()
    {
        return null;
    }
}
