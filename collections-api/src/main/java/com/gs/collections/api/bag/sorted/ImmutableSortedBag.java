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

package com.gs.collections.api.bag.sorted;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.partition.bag.sorted.PartitionImmutableSortedBag;
import net.jcip.annotations.Immutable;

/**
 * ImmutableSortedBag is the non-modifiable equivalent interface to {@link MutableSortedBag}.
 *
 * @since 4.2
 */
@Immutable
public interface ImmutableSortedBag<T>
        extends ImmutableBag<T>, SortedBag<T>
{
    ImmutableSortedBag<T> newWith(T element);

    ImmutableSortedBag<T> newWithout(T element);

    ImmutableSortedBag<T> newWithAll(Iterable<? extends T> elements);

    ImmutableSortedBag<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableSortedBag<T> selectByOccurrences(IntPredicate predicate);

    ImmutableSortedBag<T> select(Predicate<? super T> predicate);

    <P> ImmutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    ImmutableSortedBag<T> reject(Predicate<? super T> predicate);

    PartitionImmutableSortedBag<T> partition(Predicate<? super T> predicate);

    <S> ImmutableSortedBag<S> selectInstancesOf(Class<S> clazz);

    <V> ImmutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    //Not yet supported

    //<S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that);

    //ImmutableBag<Pair<T, Integer>> zipWithIndex();

    // <K, V> SortedMapIterable<K, V> aggregateInPlaceBy(
    //            Function<? super T, ? extends K> groupBy,
    //            Function0<? extends V> zeroValueFactory,
    //            Procedure2<? super V, ? super T> mutatingAggregator);
    //
    // <K, V> SortedMapIterable<K, V> aggregateBy(
    //            Function<? super T, ? extends K> groupBy,
    //            Function0<? extends V> zeroValueFactory,
    //            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator);
}
