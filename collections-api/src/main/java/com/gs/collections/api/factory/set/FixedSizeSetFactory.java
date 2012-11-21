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

package com.gs.collections.api.factory.set;

import com.gs.collections.api.set.FixedSizeSet;
import com.gs.collections.api.set.MutableSet;

public interface FixedSizeSetFactory
{
    /**
     * Same as {@link #with()}.
     */
    <T> FixedSizeSet<T> of();

    <T> FixedSizeSet<T> with();

    /**
     * Same as {@link #with(T)}.
     */
    <T> FixedSizeSet<T> of(T one);

    <T> FixedSizeSet<T> with(T one);

    /**
     * Same as {@link #with(T, T)}.
     */
    <T> FixedSizeSet<T> of(T one, T two);

    <T> FixedSizeSet<T> with(T one, T two);

    /**
     * Same as {@link #with(T, T, T)}.
     */
    <T> FixedSizeSet<T> of(T one, T two, T three);

    <T> FixedSizeSet<T> with(T one, T two, T three);

    /**
     * Same as {@link #with(T, T, T, T)}.
     */
    <T> FixedSizeSet<T> of(T one, T two, T three, T four);

    <T> FixedSizeSet<T> with(T one, T two, T three, T four);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> MutableSet<T> ofAll(Iterable<? extends T> items);

    <T> MutableSet<T> withAll(Iterable<? extends T> items);
}
