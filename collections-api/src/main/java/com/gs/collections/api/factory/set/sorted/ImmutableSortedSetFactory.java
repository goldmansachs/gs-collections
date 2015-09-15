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

package com.gs.collections.api.factory.set.sorted;

import java.util.Comparator;
import java.util.SortedSet;

import com.gs.collections.api.set.sorted.ImmutableSortedSet;

public interface ImmutableSortedSetFactory
{
    /**
     * @since 6.0
     */
    <T> ImmutableSortedSet<T> empty();

    /**
     * @since 7.0
     */
    <T> ImmutableSortedSet<T> empty(Comparator<? super T> comparator);

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableSortedSet<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableSortedSet<T> with();

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> ImmutableSortedSet<T> of(T... items);

    <T> ImmutableSortedSet<T> with(T... items);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> ImmutableSortedSet<T> ofAll(Iterable<? extends T> items);

    <T> ImmutableSortedSet<T> withAll(Iterable<? extends T> items);

    /**
     * Same as {@link #with(Comparator)}.
     */
    <T> ImmutableSortedSet<T> of(Comparator<? super T> comparator);

    <T> ImmutableSortedSet<T> with(Comparator<? super T> comparator);

    /**
     * Same as {@link #with(Comparator, Object[])}.
     */
    <T> ImmutableSortedSet<T> of(Comparator<? super T> comparator, T... items);

    <T> ImmutableSortedSet<T> with(Comparator<? super T> comparator, T... items);

    /**
     * Same as {@link #withAll(Comparator, Iterable)}.
     */
    <T> ImmutableSortedSet<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> items);

    <T> ImmutableSortedSet<T> withAll(Comparator<? super T> comparator, Iterable<? extends T> items);

    /**
     * Same as {@link #withSortedSet(SortedSet)}.
     */
    <T> ImmutableSortedSet<T> ofSortedSet(SortedSet<T> set);

    <T> ImmutableSortedSet<T> withSortedSet(SortedSet<T> set);
}
