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

package com.gs.collections.api.set;

import java.util.Set;

import com.gs.collections.api.block.function.Function;
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
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.set.PartitionImmutableSet;
import com.gs.collections.api.set.primitive.ImmutableBooleanSet;
import com.gs.collections.api.set.primitive.ImmutableByteSet;
import com.gs.collections.api.set.primitive.ImmutableCharSet;
import com.gs.collections.api.set.primitive.ImmutableDoubleSet;
import com.gs.collections.api.set.primitive.ImmutableFloatSet;
import com.gs.collections.api.set.primitive.ImmutableIntSet;
import com.gs.collections.api.set.primitive.ImmutableLongSet;
import com.gs.collections.api.set.primitive.ImmutableShortSet;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableSet is the non-modifiable equivalent interface to {@link MutableSet}. {@link MutableSet#toImmutable()} will
 * give you an appropriately trimmed implementation of ImmutableSet.  All ImmutableSet implementations must implement
 * the java.util.Set interface so they can satisfy the equals() contract and be compared against other set structures
 * like UnifiedSet or HashSet.
 */
@Immutable
public interface ImmutableSet<T>
        extends UnsortedSetIterable<T>, ImmutableSetIterable<T>
{
    ImmutableSet<T> newWith(T element);

    ImmutableSet<T> newWithout(T element);

    ImmutableSet<T> newWithAll(Iterable<? extends T> elements);

    ImmutableSet<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableSet<T> tap(Procedure<? super T> procedure);

    ImmutableSet<T> select(Predicate<? super T> predicate);

    <P> ImmutableSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableSet<T> reject(Predicate<? super T> predicate);

    <P> ImmutableSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableSet<T> partition(Predicate<? super T> predicate);

    <P> PartitionImmutableSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ImmutableSet<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableSet<V> collect(Function<? super T, ? extends V> function);

    ImmutableBooleanSet collectBoolean(BooleanFunction<? super T> booleanFunction);

    ImmutableByteSet collectByte(ByteFunction<? super T> byteFunction);

    ImmutableCharSet collectChar(CharFunction<? super T> charFunction);

    ImmutableDoubleSet collectDouble(DoubleFunction<? super T> doubleFunction);

    ImmutableFloatSet collectFloat(FloatFunction<? super T> floatFunction);

    ImmutableIntSet collectInt(IntFunction<? super T> intFunction);

    ImmutableLongSet collectLong(LongFunction<? super T> longFunction);

    ImmutableShortSet collectShort(ShortFunction<? super T> shortFunction);

    <P, V> ImmutableSet<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> ImmutableSet<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableSet<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    <S> ImmutableSet<Pair<T, S>> zip(Iterable<S> that);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    ImmutableSet<Pair<T, Integer>> zipWithIndex();

    Set<T> castToSet();

    ImmutableSet<T> union(SetIterable<? extends T> set);

    ImmutableSet<T> intersect(SetIterable<? extends T> set);

    ImmutableSet<T> difference(SetIterable<? extends T> subtrahendSet);

    ImmutableSet<T> symmetricDifference(SetIterable<? extends T> setB);

    ImmutableSet<UnsortedSetIterable<T>> powerSet();
}
