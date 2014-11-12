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

import com.gs.collections.api.bag.MutableBag;

/**
 * A factory which creates instances of type {@link MutableBag}.
 */
public interface MutableBagFactory
{
    /**
     * @since 6.0
     */
    <T> MutableBag<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableBag<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableBag<T> with();

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> MutableBag<T> of(T... elements);

    <T> MutableBag<T> with(T... elements);
}
