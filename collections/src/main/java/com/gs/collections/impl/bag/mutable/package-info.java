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
 * This package contains implementations of the {@link com.gs.collections.api.bag.MutableBag} interface.
 * <p>
 *     A MutableBag is a {@link java.util.Collection} which contains elements that are unordered and may contain duplicate entries. It adds a protocol for
 * adding, removing, and determining the number of occurrences for an item.
 * <p>
 *     This package contains 3 bag implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.bag.mutable.HashBag} - a {@link com.gs.collections.api.bag.MutableBag} which uses a hashtable as its underlying data store.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.bag.mutable.SynchronizedBag} - a synchronized view of a bag.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.bag.mutable.UnmodifiableBag} - an unmodifiable view of a bag.
 *     </li>
 * </ul>
 * <p>
 *     This package contains one factory implementation:
 * <ul>
 *     <li>
 *         {@link com.gs.collections.impl.bag.mutable.MutableBagFactoryImpl} - a factory which creates instances of type {@link com.gs.collections.api.bag.MutableBag}.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.bag.mutable;
