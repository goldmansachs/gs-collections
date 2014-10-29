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
 * This package contains implementations of the {@link com.gs.collections.api.list.MutableList} interface.
 * <p>
 *     A MutableList is an implementation of a {@link java.util.List} which provides methods matching the Smalltalk Collection protocol.
 * <p>
 *     This package contains the following implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.list.mutable.FastList} - an array-backed list which provides optimized internal iterators.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.list.mutable.ArrayListAdapter} - a MutableList wrapper around an {@link java.util.ArrayList} instance.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.list.mutable.CompositeFastList} - behaves like a list, but is composed of one or more lists.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.list.mutable.ListAdapter} - a MutableList wrapper around a {@link java.util.List} interface instance.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.list.mutable.RandomAccessListAdapter} - a MutableList wrapper around a {@link java.util.List} interface instance.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.list.mutable.MultiReaderFastList} - provides a thread-safe wrapper around a FastList, using a {@link java.util.concurrent.locks.ReentrantReadWriteLock}.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.list.mutable.SynchronizedMutableList} - a synchronized view of a list.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.list.mutable.UnmodifiableMutableList} - an unmodifiable view of a list.
 *     </li>
 * </ul>
 * <p>
 *     This package contains one factory implementation:
 * <ul>
 *     <li>
 *         {@link com.gs.collections.impl.list.mutable.MutableListFactoryImpl} - a factory which creates instances of type {@link com.gs.collections.api.list.MutableList}.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.list.mutable;
