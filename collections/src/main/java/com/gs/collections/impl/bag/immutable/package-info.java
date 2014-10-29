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

/**
 * This package contains implementations of the {@link com.gs.collections.api.bag.ImmutableBag} interface.
 * <p>
 *     An {@link com.gs.collections.api.bag.ImmutableBag} is an immutable collection which contains elements that are unordered, and may contain duplicate entries.
 * <p>
 *     This package contains 4 immutable bag implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.bag.immutable.ImmutableArrayBag} - an {@link com.gs.collections.api.bag.ImmutableBag} which uses an array as its underlying data store.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.bag.immutable.ImmutableEmptyBag} - an empty {@link com.gs.collections.api.bag.ImmutableBag}.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.bag.immutable.ImmutableHashBag} - an {@link com.gs.collections.api.bag.ImmutableBag} which uses a hashtable as its underlying data store.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.bag.immutable.ImmutableSingletonBag} - an {@link com.gs.collections.api.bag.ImmutableBag} which contains only one element.
 *     </li>
 * </ul>
 * <p>
 *     This package contains one factory implementation:
 * <ul>
 *     <li>
 *         {@link com.gs.collections.impl.bag.immutable.ImmutableBagFactoryImpl} - a factory which creates instances of type {@link com.gs.collections.api.bag.ImmutableBag}.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.bag.immutable;
