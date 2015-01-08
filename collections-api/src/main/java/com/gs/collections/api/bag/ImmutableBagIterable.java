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

package com.gs.collections.api.bag;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.map.MutableMapIterable;
import com.gs.collections.api.multimap.bag.ImmutableBagIterableMultimap;
import com.gs.collections.api.partition.bag.PartitionImmutableBagIterable;
import com.gs.collections.api.set.ImmutableSetIterable;
import com.gs.collections.api.tuple.Pair;

public interface ImmutableBagIterable<T> extends Bag<T>, ImmutableCollection<T>
{
    ImmutableBagIterable<T> tap(Procedure<? super T> procedure);

    ImmutableBagIterable<T> select(Predicate<? super T> predicate);

    <P> ImmutableBagIterable<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableBagIterable<T> reject(Predicate<? super T> predicate);

    <P> ImmutableBagIterable<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    PartitionImmutableBagIterable<T> partition(Predicate<? super T> predicate);

    <P> PartitionImmutableBagIterable<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <S> ImmutableBagIterable<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableBagIterableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableBagIterableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    ImmutableSetIterable<Pair<T, Integer>> zipWithIndex();

    ImmutableBagIterable<T> selectByOccurrences(IntPredicate predicate);

    MutableMapIterable<T, Integer> toMapOfItemToCount();
}
