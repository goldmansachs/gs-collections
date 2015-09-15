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

import com.gs.collections.api.bag.sorted.MutableSortedBag;

/**
 * A factory which creates instances of type {@link MutableSortedBag}.
 *
 * @since 6.0
 */
public interface MutableSortedBagFactory
{
    <T> MutableSortedBag<T> empty();

    <T> MutableSortedBag<T> empty(Comparator<? super T> comparator);

    /**
     * Same as {@link #with()}.
     */
    <T> MutableSortedBag<T> of();

    <T> MutableSortedBag<T> with();

    /**
     * Same as {@link #with(Comparator)}.
     */
    <T> MutableSortedBag<T> of(Comparator<? super T> comparator);

    <T> MutableSortedBag<T> with(Comparator<? super T> comparator);

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> MutableSortedBag<T> of(T... elements);

    <T> MutableSortedBag<T> with(T... elements);

    /**
     * Same as {@link #with(Comparator, Object[])}.
     */
    <T> MutableSortedBag<T> of(Comparator<? super T> comparator, T... elements);

    <T> MutableSortedBag<T> with(Comparator<? super T> comparator, T... elements);

    /**
     * Same as {@link #withAll(Comparator, Iterable)}.
     */
    <T> MutableSortedBag<T> ofAll(Iterable<? extends T> items);

    <T> MutableSortedBag<T> withAll(Iterable<? extends T> items);

    /**
     * Same as {@link #withAll(Comparator, Iterable)}
     */
    <T> MutableSortedBag<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> items);

    <T> MutableSortedBag<T> withAll(Comparator<? super T> comparator, Iterable<? extends T> items);
}
