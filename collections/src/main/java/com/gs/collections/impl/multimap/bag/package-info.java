/*
 * Copyright 2013 Goldman Sachs.
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
 * This package contains implementations of the {@link com.gs.collections.api.multimap.bag.BagMultimap} interface.
 * <p>
 *     A {@link com.gs.collections.api.multimap.bag.BagMultimap} is a type of {@code MultiMap} that uses a Bag as its underlying store for the multiple values of a given key.
 * </p>
 * <p>
 *     This package contains the following implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.bag.HashBagMultimap} - a {@code MutableBagMultiMap} which uses a {@link com.gs.collections.impl.bag.mutable.HashBag} as its underlying store for the multiple values of a given key.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.bag.ImmutableBagMultimapImpl} - the default {@code ImmutableBagMultiMap} implementation.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.bag.SynchronizedPutHashBagMultimap} - a {@code MutableBagMultiMap} that is optimized for parallel writes, but is not protected for concurrent reads.
 *     </li>
 * </p>
 */
package com.gs.collections.impl.multimap.bag;
