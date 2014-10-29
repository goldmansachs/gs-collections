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
 * This package contains interfaces for list API which enhance the performance and functionality of {@link java.util.List}.
 * <p>
 *      This package contains 4 interfaces:
 * <ul>
 *     <li>
 *         {@link com.gs.collections.api.list.ListIterable} - an {@link java.lang.Iterable} which contains items that are ordered and may be accessed directly by index.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.list.MutableList} - an implementation of a JCF List which provides internal iterator methods matching the Smalltalk Collection protocol.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.list.ImmutableList} - the non-modifiable equivalent interface to {@link com.gs.collections.api.list.MutableList}.
 *     </li>
 *     <li>
 *         {@link com.gs.collections.api.list.FixedSizeList} - a list that may be mutated, but cannot grow or shrink in size.
 *     </li>
 * </ul>
 */
package com.gs.collections.api.list;
