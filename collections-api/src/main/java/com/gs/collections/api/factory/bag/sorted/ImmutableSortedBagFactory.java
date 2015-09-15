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

package com.gs.collections.api.factory.bag.sorted;

import java.util.Comparator;

import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.SortedBag;

/**
 * A factory which creates instances of type {@link ImmutableSortedBag}.
 */
public interface ImmutableSortedBagFactory
{
    <T> ImmutableSortedBag<T> empty();

    <T> ImmutableSortedBag<T> empty(Comparator<? super T> comparator);

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableSortedBag<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableSortedBag<T> with();

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> ImmutableSortedBag<T> of(T... items);

    <T> ImmutableSortedBag<T> with(T... items);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> ImmutableSortedBag<T> ofAll(Iterable<? extends T> items);

    <T> ImmutableSortedBag<T> withAll(Iterable<? extends T> items);

    /**
     * Same as {@link #with(Comparator, Object[])}.
     */
    <T> ImmutableSortedBag<T> of(Comparator<? super T> comparator, T... items);

    <T> ImmutableSortedBag<T> with(Comparator<? super T> comparator, T... items);

    /**
     * Same as {@link #with(Comparator)}.
     */
    <T> ImmutableSortedBag<T> of(Comparator<? super T> comparator);

    <T> ImmutableSortedBag<T> with(Comparator<? super T> comparator);

    /**
     * Same as {@link #withAll(Comparator, Iterable)}.
     */
    <T> ImmutableSortedBag<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> items);

    <T> ImmutableSortedBag<T> withAll(Comparator<? super T> comparator, Iterable<? extends T> items);

    /**
     * Same as {@link #withSortedBag(SortedBag)}.
     */
    <T> ImmutableSortedBag<T> ofSortedBag(SortedBag<T> bag);

    <T> ImmutableSortedBag<T> withSortedBag(SortedBag<T> bag);
}
