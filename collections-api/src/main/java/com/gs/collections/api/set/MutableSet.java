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
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.api.set.primitive.MutableByteSet;
import com.gs.collections.api.set.primitive.MutableCharSet;
import com.gs.collections.api.set.primitive.MutableDoubleSet;
import com.gs.collections.api.set.primitive.MutableFloatSet;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.api.set.primitive.MutableLongSet;
import com.gs.collections.api.set.primitive.MutableShortSet;
import com.gs.collections.api.tuple.Pair;

/**
 * A MutableSet is an implementation of a JCF Set which provides methods matching the Smalltalk Collection protocol.
 */
public interface MutableSet<T>
        extends UnsortedSetIterable<T>, MutableSetIterable<T>, Cloneable
{
    MutableSet<T> with(T element);

    MutableSet<T> without(T element);

    MutableSet<T> withAll(Iterable<? extends T> elements);

    MutableSet<T> withoutAll(Iterable<? extends T> elements);

    MutableSet<T> newEmpty();

    MutableSet<T> clone();

    MutableSet<T> tap(Procedure<? super T> procedure);

    MutableSet<T> select(Predicate<? super T> predicate);

    <P> MutableSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    MutableSet<T> reject(Predicate<? super T> predicate);

    <P> MutableSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionMutableSet<T> partition(Predicate<? super T> predicate);

    <P> PartitionMutableSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> MutableSet<S> selectInstancesOf(Class<S> clazz);

    <V> MutableSet<V> collect(Function<? super T, ? extends V> function);

    MutableBooleanSet collectBoolean(BooleanFunction<? super T> booleanFunction);

    MutableByteSet collectByte(ByteFunction<? super T> byteFunction);

    MutableCharSet collectChar(CharFunction<? super T> charFunction);

    MutableDoubleSet collectDouble(DoubleFunction<? super T> doubleFunction);

    MutableFloatSet collectFloat(FloatFunction<? super T> floatFunction);

    MutableIntSet collectInt(IntFunction<? super T> intFunction);

    MutableLongSet collectLong(LongFunction<? super T> longFunction);

    MutableShortSet collectShort(ShortFunction<? super T> shortFunction);

    <P, V> MutableSet<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    <V> MutableSet<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> MutableSet<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Returns an unmodifable view of the set.
     * The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     *
     * @return an unmodifiable view of this set
     */
    MutableSet<T> asUnmodifiable();

    MutableSet<T> asSynchronized();

    /**
     * Returns an immutable copy of this set. If the set is immutable, it returns itself.
     * <p>
     * The returned set will be <tt>Serializable</tt> if this set is <tt>Serializable</tt>.
     */
    ImmutableSet<T> toImmutable();

    <V> MutableSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    <S> MutableSet<Pair<T, S>> zip(Iterable<S> that);

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    MutableSet<Pair<T, Integer>> zipWithIndex();

    MutableSet<T> union(SetIterable<? extends T> set);

    MutableSet<T> intersect(SetIterable<? extends T> set);

    MutableSet<T> difference(SetIterable<? extends T> subtrahendSet);

    MutableSet<T> symmetricDifference(SetIterable<? extends T> setB);

    MutableSet<UnsortedSetIterable<T>> powerSet();
}
