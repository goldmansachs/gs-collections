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
 * This package contains implementations of the {@link com.gs.collections.api.multimap.sortedset.SortedSetMultimap} interface.
 * <p>
 *     A {@link com.gs.collections.api.multimap.sortedset.SortedSetMultimap} is a type of {@code Multimap} that uses a sorted set as its underlying store for the multiple values of a given key.
 * <p>
 *     This package contains the following implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.set.sorted.TreeSortedSetMultimap} - a {@code MutableSortedSetMultimap} which uses a {@link com.gs.collections.impl.set.sorted.mutable.TreeSortedSet} as its underlying store for the multiple values of a given key.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.set.sorted.ImmutableSortedSetMultimapImpl} - the default {@code ImmutableSortedSetMultimap} implementation.
 *     </li>
 */
package com.gs.collections.impl.multimap.set.sorted;
