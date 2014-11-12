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

package com.gs.collections.api.factory.set;

import com.gs.collections.api.set.ImmutableSet;

public interface ImmutableSetFactory
{
    /**
     * @since 6.0
     */
    <T> ImmutableSet<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableSet<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableSet<T> with();

    /**
     * Same as {@link #with(Object)}.
     */
    <T> ImmutableSet<T> of(T one);

    <T> ImmutableSet<T> with(T one);

    /**
     * Same as {@link #with(Object, Object)}.
     */
    <T> ImmutableSet<T> of(T one, T two);

    <T> ImmutableSet<T> with(T one, T two);

    /**
     * Same as {@link #with(Object, Object, Object)}.
     */
    <T> ImmutableSet<T> of(T one, T two, T three);

    <T> ImmutableSet<T> with(T one, T two, T three);

    /**
     * Same as {@link #with(Object, Object, Object, Object)}.
     */
    <T> ImmutableSet<T> of(T one, T two, T three, T four);

    <T> ImmutableSet<T> with(T one, T two, T three, T four);

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> ImmutableSet<T> of(T... items);

    <T> ImmutableSet<T> with(T... items);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> ImmutableSet<T> ofAll(Iterable<? extends T> items);

    <T> ImmutableSet<T> withAll(Iterable<? extends T> items);
}
