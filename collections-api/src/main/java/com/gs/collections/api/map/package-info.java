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
 * This package contains interfaces for map API which enhance the performance and functionality of {@link java.util.Map}
 * <p>
 * This package contains the following interfaces:
 * <ul>
 *     <li>
 *         {@link com.gs.collections.api.map.MapIterable} - a Read-only Map API, with the minor exception inherited from {@link java.lang.Iterable}.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.map.MutableMap} - an implementation of a JCF Map which provides methods matching the Smalltalk Collection protocol.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.map.ImmutableMap} - the non-modifiable equivalent interface to {@link com.gs.collections.api.map.MutableMap}.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.map.FixedSizeMap} - a map that may be mutated, but cannot grow or shrink in size.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.map.ConcurrentMutableMap} - provides an API which combines and supports both MutableMap and ConcurrentMap.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.map.UnsortedMapIterable} - a map whose elements are unsorted.
 *     </li>
 * </ul>
 */
package com.gs.collections.api.map;
