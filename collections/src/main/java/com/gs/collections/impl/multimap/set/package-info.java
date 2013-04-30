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
 * This package contains implementations of the {@link com.gs.collections.api.multimap.set.SetMultimap} interface.
 * <p>
 *     A {@link com.gs.collections.api.multimap.set.SetMultimap} is a type of {@code MultiMap} that uses a set as its underlying store for the multiple values of a given key.
 * </p>
 * <p>
 *     This package contains the following implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.set.UnifiedSetMultimap} - a {@code MutableSetMultiMap} which uses a {@link com.gs.collections.impl.set.mutable.UnifiedSet} as its underlying store for the multiple values of a given key.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.set.ImmutableSetMultimapImpl} - the default {@code ImmutableSetMultiMap} implementation.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.set.SynchronizedPutUnifiedSetMultimap} - a {@code MutableSetMultiMap} that is optimized for parallel writes, but is not protected for concurrent reads.
 *     </li>
 * </p>
 */
package com.gs.collections.impl.multimap.set;
