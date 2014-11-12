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

package com.gs.collections.api.factory.set.sorted;

import java.util.Comparator;

import com.gs.collections.api.set.sorted.MutableSortedSet;

public interface MutableSortedSetFactory
{
    /**
     * @since 6.0
     */
    <T> MutableSortedSet<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableSortedSet<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableSortedSet<T> with();

    <T> MutableSortedSet<T> of(T... items);

    <T> MutableSortedSet<T> with(T... items);

    <T> MutableSortedSet<T> ofAll(Iterable<? extends T> items);

    <T> MutableSortedSet<T> withAll(Iterable<? extends T> items);

    <T> MutableSortedSet<T> of(Comparator<? super T> comparator);

    <T> MutableSortedSet<T> with(Comparator<? super T> comparator);

    <T> MutableSortedSet<T> of(Comparator<? super T> comparator, T... items);

    <T> MutableSortedSet<T> with(Comparator<? super T> comparator, T... items);

    <T> MutableSortedSet<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> items);

    <T> MutableSortedSet<T> withAll(Comparator<? super T> comparator, Iterable<? extends T> items);
}
