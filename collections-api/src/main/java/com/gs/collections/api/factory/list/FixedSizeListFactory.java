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

package com.gs.collections.api.factory.list;

import com.gs.collections.api.list.FixedSizeList;

public interface FixedSizeListFactory
{
    /**
     * @since 6.0
     */
    <T> FixedSizeList<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> FixedSizeList<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> FixedSizeList<T> with();

    /**
     * Same as {@link #with(Object)}.
     */
    <T> FixedSizeList<T> of(T one);

    <T> FixedSizeList<T> with(T one);

    /**
     * Same as {@link #with(Object, Object)}.
     */
    <T> FixedSizeList<T> of(T one, T two);

    <T> FixedSizeList<T> with(T one, T two);

    /**
     * Same as {@link #with(Object, Object, Object)}.
     */
    <T> FixedSizeList<T> of(T one, T two, T three);

    <T> FixedSizeList<T> with(T one, T two, T three);

    /**
     * Same as {@link #with(Object, Object, Object, Object)}.
     */
    <T> FixedSizeList<T> of(T one, T two, T three, T four);

    <T> FixedSizeList<T> with(T one, T two, T three, T four);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object)}.
     */
    <T> FixedSizeList<T> of(T one, T two, T three, T four, T five);

    <T> FixedSizeList<T> with(T one, T two, T three, T four, T five);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object)}.
     */
    <T> FixedSizeList<T> of(T one, T two, T three, T four, T five, T six);

    <T> FixedSizeList<T> with(T one, T two, T three, T four, T five, T six);

    /**
     * Same as {@link #with(Object[])}
     */
    <T> FixedSizeList<T> of(T... items);

    <T> FixedSizeList<T> with(T... items);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> FixedSizeList<T> ofAll(Iterable<? extends T> items);

    <T> FixedSizeList<T> withAll(Iterable<? extends T> items);
}
