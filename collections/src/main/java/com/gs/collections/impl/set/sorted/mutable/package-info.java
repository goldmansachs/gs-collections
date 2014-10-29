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
 * This package contains implementations of {@link com.gs.collections.api.set.sorted.MutableSortedSet}.
 * <p>
 *     This package contains 4 sorted mutable set implementation:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.set.sorted.mutable.SortedSetAdapter} -  a class which provides a MutableSortedSet wrapper around a JDK Collections SortedSet interface instance.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.set.sorted.mutable.SynchronizedSortedSet} -  a synchronized view of a SortedSet.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.set.sorted.mutable.TreeSortedSet} - a sorted set backed by Tree data structure..
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.set.sorted.mutable.UnmodifiableSortedSet} -  an unmodifiable view of a SortedSet.
 *     </li>
 * </ul>
 * <p>
 *     This package contains 1 factory implementation:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.set.sorted.mutable.MutableSortedSetFactoryImpl} - a factory which creates instances of type {@link com.gs.collections.api.set.sorted.MutableSortedSet}.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.set.sorted.mutable;
