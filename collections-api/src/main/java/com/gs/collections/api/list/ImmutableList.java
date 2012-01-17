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

package com.gs.collections.api.list;

import java.util.List;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.partition.list.PartitionImmutableList;
import com.gs.collections.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableList is the non-modifiable equivalent interface to {@link MutableList}. {@link MutableList#toImmutable()}
 * will give you an appropriately trimmed implementation of ImmutableList.  All ImmutableList implementations must
 * implement the java.util.List interface so they can satisfy the equals() contract and be compared against other list
 * structures like FastList or ArrayList.
 */
@Immutable
public interface ImmutableList<T>
        extends ImmutableCollection<T>, ListIterable<T>
{
    ImmutableList<T> newWith(T element);

    ImmutableList<T> newWithout(T element);

    ImmutableList<T> newWithAll(Iterable<? extends T> elements);

    ImmutableList<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableList<T> filter(Predicate<? super T> predicate);

    ImmutableList<T> filterNot(Predicate<? super T> predicate);

    PartitionImmutableList<T> partition(Predicate<? super T> predicate);

    <V> ImmutableList<V> transform(Function<? super T, ? extends V> function);

    <V> ImmutableList<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    <V> ImmutableListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that);

    ImmutableList<Pair<T, Integer>> zipWithIndex();

    List<T> castToList();
}
