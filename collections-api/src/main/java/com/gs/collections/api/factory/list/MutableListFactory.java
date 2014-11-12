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

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.list.MutableList;

public interface MutableListFactory
{
    /**
     * @since 6.0
     */
    <T> MutableList<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableList<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableList<T> with();

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> MutableList<T> of(T... items);

    <T> MutableList<T> with(T... items);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> MutableList<T> ofAll(Iterable<? extends T> iterable);

    <T> MutableList<T> withAll(Iterable<? extends T> iterable);

    <T> MutableList<T> withNValues(int size, Function0<T> factory);
}
