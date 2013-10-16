/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.api.bag;

import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.bag.primitive.MutableDoubleBag;
import com.gs.collections.api.bag.primitive.MutableFloatBag;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.bag.primitive.MutableLongBag;
import com.gs.collections.api.bag.primitive.MutableShortBag;
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
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.tuple.Pair;

/**
 * A MutableBag is a Collection whose elements are unordered and may contain duplicate entries.  It varies from
 * MutableCollection in that it adds a protocol for determining, adding, and removing the number of occurrences for an
 * item.
 *
 * @since 1.0
 */
public interface MutableBag<T>
        extends Bag<T>, MutableCollection<T>
{
    MutableBag<T> selectByOccurrences(IntPredicate predicate);

    ImmutableBag<T> toImmutable();

    MutableBag<T> with(T element);

    MutableBag<T> without(T element);

    MutableBag<T> withAll(Iterable<? extends T> elements);

    MutableBag<T> withoutAll(Iterable<? extends T> elements);

    MutableBag<T> newEmpty();

    <P> MutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <P> MutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <P, V> MutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    MutableBag<T> asUnmodifiable();

    MutableBag<T> asSynchronized();

    PartitionMutableBag<T> partition(Predicate<? super T> predicate);

    <V> MutableBag<V> collect(Function<? super T, ? extends V> function);

    MutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction);

    MutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction);

    MutableFloatBag collectFloat(FloatFunction<? super T> floatFunction);

    MutableLongBag collectLong(LongFunction<? super T> longFunction);

    MutableShortBag collectShort(ShortFunction<? super T> shortFunction);

    <V> MutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> MutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> MutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> MutableBag<Pair<T, S>> zip(Iterable<S> that);

    MutableBag<Pair<T, Integer>> zipWithIndex();

    MutableBag<T> select(Predicate<? super T> predicate);

    MutableBag<T> reject(Predicate<? super T> predicate);

    <S> MutableBag<S> selectInstancesOf(Class<S> clazz);

    MutableByteBag collectByte(ByteFunction<? super T> byteFunction);

    MutableCharBag collectChar(CharFunction<? super T> charFunction);

    MutableIntBag collectInt(IntFunction<? super T> intFunction);

    <V> MutableBag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    void addOccurrences(T item, int occurrences);

    boolean removeOccurrences(Object item, int occurrences);
}
