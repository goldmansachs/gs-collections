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
 * This package contains interfaces for SortedBag API.
 * <p>
 *     A sorted bag is a {@link java.util.Collection} which contains elements that are sorted, and may contain duplicate entries. It adds a protocol for
 * adding, removing, and determining the number of occurrences for an item.
 * <p>
 *     This package contains 3 interfaces:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.api.bag.sorted.SortedBag} - contains the common API for Mutable and Immutable SortedBag.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.api.bag.sorted.MutableSortedBag} - a SortedBag whose contents can be altered after initialization.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.api.bag.sorted.ImmutableSortedBag} - a SortedBag whose contents cannot be altered after initialization.
 *     </li>
 * </ul>
 */
package com.gs.collections.api.bag.sorted;
