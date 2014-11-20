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

package com.gs.collections.api.factory.set.strategy;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.set.MutableSet;

public interface MutableHashingStrategySetFactory
{
    /**
     * Same as {@link #with(HashingStrategy)}.
     */
    <T> MutableSet<T> of(HashingStrategy<? super T> hashingStrategy);

    <T> MutableSet<T> with(HashingStrategy<? super T> hashingStrategy);

    /**
     * Same as {@link #with(HashingStrategy, Object[])}.
     */
    <T> MutableSet<T> of(HashingStrategy<? super T> hashingStrategy, T... items);

    <T> MutableSet<T> with(HashingStrategy<? super T> hashingStrategy, T... items);

    /**
     * Same as {@link #withAll(HashingStrategy, Iterable)}.
     */
    <T> MutableSet<T> ofAll(HashingStrategy<? super T> hashingStrategy, Iterable<? extends T> items);

    <T> MutableSet<T> withAll(HashingStrategy<? super T> hashingStrategy, Iterable<? extends T> items);
}
