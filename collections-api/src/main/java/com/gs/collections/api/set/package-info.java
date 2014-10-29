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
 * This package contains interfaces for set API which enhance the performance and functionality of {@link java.util.Set}.
 * <p>
 *      This package contains 6 interfaces:
 * <ul>
 *   <li>
 *       {@link com.gs.collections.api.set.FixedSizeSet} - a set that may be mutated, but cannot grow or shrink in size.
 *   </li>
 *   <li>
 *       {@link com.gs.collections.api.set.ImmutableSet} - the non-modifiable equivalent interface to {@link com.gs.collections.api.set.MutableSet}.
 *   </li>
 *   <li>
 *       {@link com.gs.collections.api.set.MutableSet} - an implementation of a JCF Set which provides internal iterator methods matching the Smalltalk Collection protocol.
 *   </li>
 *   <li>
 *       {@link com.gs.collections.api.set.Pool} - locates an object in the pool which is equal to {@code key}.
 *   </li>
 *   <li>
 *       {@link com.gs.collections.api.set.SetIterable} - a read-only Set API, with the minor exception inherited from {@link java.lang.Iterable} (iterable.iterator().remove()).
 *   </li>
 *   <li>
 *       {@link com.gs.collections.api.set.UnsortedSetIterable} - an iterable whose items are unique.
 *   </li>
 * </ul>
 */
package com.gs.collections.api.set;
