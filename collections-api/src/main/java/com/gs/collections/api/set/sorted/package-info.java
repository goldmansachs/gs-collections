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
 * This package contains interfaces for sorted set API.
 * <p>
 *     A sorted set is an {@link java.lang.Iterable} which contains elements in sorted order. It allows for faster retrievals.
 * <p>
 *   This package contains 3 interfaces:
 * <ul>
 *   <li>
 *      {@link com.gs.collections.api.set.sorted.ImmutableSortedSet} - the non-modifiable equivalent interface to {@link com.gs.collections.api.set.sorted.MutableSortedSet}.
 *   </li>
 *   <li>
 *      {@link com.gs.collections.api.set.sorted.MutableSortedSet} - an implementation of a JCF SortedSet which provides internal iterator methods matching the Smalltalk Collection protocol.
 *   </li>
 *   <li>
 *      {@link com.gs.collections.api.set.sorted.SortedSetIterable} - an iterable whose items are unique and sorted.
 *   </li>
 * </ul>
 */
package com.gs.collections.api.set.sorted;
