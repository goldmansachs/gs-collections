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

package com.gs.collections.api.set;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.multimap.set.SetMultimap;
import com.gs.collections.api.set.primitive.BooleanSet;
import com.gs.collections.api.set.primitive.ByteSet;
import com.gs.collections.api.set.primitive.CharSet;
import com.gs.collections.api.set.primitive.DoubleSet;
import com.gs.collections.api.set.primitive.FloatSet;
import com.gs.collections.api.set.primitive.IntSet;
import com.gs.collections.api.set.primitive.LongSet;
import com.gs.collections.api.set.primitive.ShortSet;
import com.gs.collections.api.tuple.Pair;

/**
 * An iterable whose items are unique.
 */
public interface UnsortedSetIterable<T>
        extends SetIterable<T>
{
    /**
     * Returns the set whose members are all possible subsets of {@code this}. For example, the powerset of [1, 2] is
     * [[], [1], [2], [1, 2]].
     */
    UnsortedSetIterable<UnsortedSetIterable<T>> powerSet();

    <V> UnsortedSetIterable<V> collect(Function<? super T, ? extends V> function);

    BooleanSet collectBoolean(BooleanFunction<? super T> booleanFunction);

    ByteSet collectByte(ByteFunction<? super T> byteFunction);

    CharSet collectChar(CharFunction<? super T> charFunction);

    DoubleSet collectDouble(DoubleFunction<? super T> doubleFunction);

    FloatSet collectFloat(FloatFunction<? super T> floatFunction);

    IntSet collectInt(IntFunction<? super T> intFunction);

    LongSet collectLong(LongFunction<? super T> longFunction);

    ShortSet collectShort(ShortFunction<? super T> shortFunction);

    <V> UnsortedSetIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> UnsortedSetIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> SetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> SetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    UnsortedSetIterable<T> union(SetIterable<? extends T> set);

    UnsortedSetIterable<T> intersect(SetIterable<? extends T> set);

    UnsortedSetIterable<T> difference(SetIterable<? extends T> subtrahendSet);

    UnsortedSetIterable<T> symmetricDifference(SetIterable<? extends T> setB);

    UnsortedSetIterable<T> select(Predicate<? super T> predicate);

    UnsortedSetIterable<T> reject(Predicate<? super T> predicate);

    <S> UnsortedSetIterable<S> selectInstancesOf(Class<S> clazz);

    <S> UnsortedSetIterable<Pair<T, S>> zip(Iterable<S> that);

    UnsortedSetIterable<Pair<T, Integer>> zipWithIndex();
}
