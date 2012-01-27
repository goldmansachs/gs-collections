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

package ponzu.api.collection;

import ponzu.api.RichIterable;
import ponzu.api.block.function.Function;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.multimap.ImmutableMultimap;
import ponzu.api.partition.PartitionImmutableCollection;
import ponzu.api.tuple.Pair;
import net.jcip.annotations.Immutable;

/**
 * ImmutableCollection is the common interface between ImmutableList and ImmutableSet.
 */
@Immutable
public interface ImmutableCollection<T>
        extends RichIterable<T>
{
    ImmutableCollection<T> newWith(T element);

    ImmutableCollection<T> newWithout(T element);

    ImmutableCollection<T> newWithAll(Iterable<? extends T> elements);

    ImmutableCollection<T> newWithoutAll(Iterable<? extends T> elements);

    ImmutableCollection<T> filter(Predicate<? super T> predicate);

    ImmutableCollection<T> filterNot(Predicate<? super T> predicate);

    PartitionImmutableCollection<T> partition(Predicate<? super T> predicate);

    <V> ImmutableCollection<V> transform(Function<? super T, ? extends V> function);

    <V> ImmutableCollection<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ImmutableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ImmutableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ImmutableCollection<Pair<T, S>> zip(Iterable<S> that);

    ImmutableCollection<Pair<T, Integer>> zipWithIndex();
}
