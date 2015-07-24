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

package com.gs.collections.api.factory.set;

import com.gs.collections.api.set.MutableSet;

public interface MutableSetFactory
{
    /**
     * @since 6.0
     */
    <T> MutableSet<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableSet<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableSet<T> with();

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> MutableSet<T> of(T... items);

    <T> MutableSet<T> with(T... items);

    /**
     * Same as {@link #empty()}. but takes in initial capacity.
     */
    <T> MutableSet<T> ofInitialCapacity(int capacity);

    /**
     * Same as {@link #empty()}. but takes in initial capacity.
     */
    <T> MutableSet<T> withInitialCapacity(int capacity);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> MutableSet<T> ofAll(Iterable<? extends T> items);

    <T> MutableSet<T> withAll(Iterable<? extends T> items);
}
