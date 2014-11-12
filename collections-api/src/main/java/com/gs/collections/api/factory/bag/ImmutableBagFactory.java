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

package com.gs.collections.api.factory.bag;

import com.gs.collections.api.bag.ImmutableBag;

/**
 * A factory which creates instances of type {@link ImmutableBag}.
 */
public interface ImmutableBagFactory
{
    /**
     * @since 6.0
     */
    <T> ImmutableBag<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableBag<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableBag<T> with();

    /**
     * Same as {@link #with(Object)}.
     */
    <T> ImmutableBag<T> of(T element);

    <T> ImmutableBag<T> with(T element);

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> ImmutableBag<T> of(T... elements);

    <T> ImmutableBag<T> with(T... elements);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> ImmutableBag<T> ofAll(Iterable<? extends T> items);

    <T> ImmutableBag<T> withAll(Iterable<? extends T> items);
}
