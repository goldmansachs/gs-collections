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

    public int size()
    {
        return 0;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public boolean notEmpty()
    {
        return false;
    }

    public T getFirst()
    {
        return null;
    }

    public T getLast()
    {
        return null;
    }

    public boolean contains(Object object)
    {
        return false;
    }

    public boolean containsAllIterable(Iterable<?> source)
    {
        return false;
    }

    public boolean containsAll(Collection<?> source)
    {
        return false;
    }

    public boolean containsAllArguments(Object... elements)
    {
        return false;
    }

    public LazyIterable<T> select(Predicate<? super T> predicate)
    {
        return null;
    }

    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return null;
    }

    public <P> LazyIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    public <P, R extends Collection<T>> R selectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return null;
    }

    public <S> LazyIterable<S> selectInstancesOf(Class<S> clazz)
    {
        return null;
    }

    public LazyIterable<T> reject(Predicate<? super T> predicate)
    {
        return null;
    }

    public <P> LazyIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return null;
    }

    public <P, R extends Collection<T>> R rejectWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return null;
    }

    public PartitionIterable<T> partition(Predicate<? super T> predicate)
    {
        return null;
    }

    public <P> PartitionIterable<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    public <V> LazyIterable<V> collect(Function<? super T, ? extends V> function)
    {
        return null;
    }

    public <P, V> LazyIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return null;
    }

    public <P, V, R extends Collection<V>> R collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return null;
    }

    public <V> LazyIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return null;
    }

    public <V, R extends Collection<V>> R collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return null;
    }

    public LazyIterable<T> take(int count)
    {
        return null;
    }

    public LazyIterable<T> drop(int count)
    {
        return null;
    }

    public LazyIterable<T> distinct()
    {
        return null;
    }

    public <V> LazyIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return null;
    }

    public <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return null;
    }

    public T detect(Predicate<? super T> predicate)
    {
        return null;
    }

    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return null;
    }

    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        return null;
    }

    public <P> T detectWithIfNone(Predicate2<? super T, ? super P> predicate, P parameter, Function0<? extends T> function)
    {
        return null;
    }

    public int count(Predicate<? super T> predicate)
    {
        return 0;
    }

    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return 0;
    }

    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return false;
    }

    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return false;
    }

    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return false;
    }

    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return null;
    }

    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        return 0;
    }

    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        return 0;
    }

    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        return 0;
    }

    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        return 0;
    }

    public MutableList<T> toList()
    {
        return null;
    }

    public MutableList<T> toSortedList()
    {
        return null;
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        return null;
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    public MutableSet<T> toSet()
    {
        return null;
    }

    public MutableSortedSet<T> toSortedSet()
    {
        return null;
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return null;
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    public MutableBag<T> toBag()
    {
        return null;
    }

    public <NK, NV> MutableMap<NK, NV> toMap(Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        return null;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        return null;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator, Function<? super T, ? extends NK> keyFunction, Function<? super T, ? extends NV> valueFunction)
    {
        return null;
    }

    public LazyIterable<T> asLazy()
    {
        return null;
    }

    public Object[] toArray()
    {
        return new Object[0];
    }

    public <T1> T1[] toArray(T1[] target)
    {
        return null;
    }

    public T min(Comparator<? super T> comparator)
    {
        return null;
    }

    public T max(Comparator<? super T> comparator)
    {
        return null;
    }

    public T min()
    {
        return null;
    }

    public T max()
    {
        return null;
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    public long sumOfInt(IntFunction<? super T> function)
    {
        return 0;
    }

    public double sumOfFloat(FloatFunction<? super T> function)
    {
        return 0;
    }

    public long sumOfLong(LongFunction<? super T> function)
    {
        return 0;
    }

    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        return 0;
    }

    public <V> ObjectLongMap<V> sumByInt(Function<T, V> groupBy, IntFunction<? super T> function)
    {
        return null;
    }

    public <V> ObjectDoubleMap<V> sumByFloat(Function<T, V> groupBy, FloatFunction<? super T> function)
    {
        return null;
    }

    public <V> ObjectLongMap<V> sumByLong(Function<T, V> groupBy, LongFunction<? super T> function)
    {
        return null;
    }

    public <V> ObjectDoubleMap<V> sumByDouble(Function<T, V> groupBy, DoubleFunction<? super T> function)
    {
        return null;
    }

    public String makeString()
    {
        return null;
    }

    public String makeString(String separator)
    {
        return null;
    }

    public String makeString(String start, String separator, String end)
    {
        return null;
    }

    public void appendString(Appendable appendable)
    {
    }

    public void appendString(Appendable appendable, String separator)
    {
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
    }

    public <V> Multimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return null;
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(Function<? super T, ? extends V> function, R target)
    {
        return null;
    }

    public <V> Multimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return null;
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return null;
    }

    public <V> MapIterable<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return null;
    }

    public LazyIterable<T> concatenate(Iterable<T> iterable)
    {
        return null;
    }

    public <S> LazyIterable<Pair<T, S>> zip(Iterable<S> that)
    {
        return null;
    }

    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return null;
    }

    public LazyIterable<Pair<T, Integer>> zipWithIndex()
    {
        return null;
    }

    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return null;
    }

    public LazyIterable<RichIterable<T>> chunk(int size)
    {
        return null;
    }

    public LazyIterable<T> tap(Procedure<? super T> procedure)
    {
        return null;
    }

    public <K, V> MapIterable<K, V> aggregateInPlaceBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Procedure2<? super V, ? super T> mutatingAggregator)
    {
        return null;
    }

    public <K, V> MapIterable<K, V> aggregateBy(Function<? super T, ? extends K> groupBy, Function0<? extends V> zeroValueFactory, Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        return null;
    }

    public <R extends Collection<T>> R into(R target)
    {
        return null;
    }

    public LazyBooleanIterable collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return null;
    }

    public <R extends MutableBooleanCollection> R collectBoolean(BooleanFunction<? super T> booleanFunction, R target)
    {
        return null;
    }

    public LazyByteIterable collectByte(ByteFunction<? super T> byteFunction)
    {
        return null;
    }

    public <R extends MutableByteCollection> R collectByte(ByteFunction<? super T> byteFunction, R target)
    {
        return null;
    }

    public LazyCharIterable collectChar(CharFunction<? super T> charFunction)
    {
        return null;
    }

    public <R extends MutableCharCollection> R collectChar(CharFunction<? super T> charFunction, R target)
    {
        return null;
    }

    public LazyDoubleIterable collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return null;
    }

    public <R extends MutableDoubleCollection> R collectDouble(DoubleFunction<? super T> doubleFunction, R target)
    {
        return null;
    }

    public LazyFloatIterable collectFloat(FloatFunction<? super T> floatFunction)
    {
        return null;
    }

    public <R extends MutableFloatCollection> R collectFloat(FloatFunction<? super T> floatFunction, R target)
    {
        return null;
    }

    public LazyIntIterable collectInt(IntFunction<? super T> intFunction)
    {
        return null;
    }

    public <R extends MutableIntCollection> R collectInt(IntFunction<? super T> intFunction, R target)
    {
        return null;
    }

    public LazyLongIterable collectLong(LongFunction<? super T> longFunction)
    {
        return null;
    }

    public <R extends MutableLongCollection> R collectLong(LongFunction<? super T> longFunction, R target)
    {
        return null;
    }

    public LazyShortIterable collectShort(ShortFunction<? super T> shortFunction)
    {
        return null;
    }

    public <R extends MutableShortCollection> R collectShort(ShortFunction<? super T> shortFunction, R target)
    {
        return null;
    }

    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return null;
    }

    public void forEach(Procedure<? super T> procedure)
    {
    }

    public void each(Procedure<? super T> procedure)
    {
    }

    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        Verify.assertSerializedForm(1L, this.serializedForm, procedure);
    }

    public Iterator<T> iterator()
    {
        return null;
    }
}
