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
 * This package contains static utilities that provide internal iteration pattern implementations which work with JCF collections.
 * <p>
 *     All the iteration patterns in this package are internal. It is used by iterators specialized for various collections.
 * <p>
 *     This package contains 10 Iteration implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.DefaultSpeciesNewStrategy} - creates a new instance of a collection based on the class type of collection.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.InternalArrayIterate} -  a final class with static iterator methods.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.IterableIterate} - provides a few of the methods from the Smalltalk Collection Protocol.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.IteratorIterate} - provides various iteration patterns for use with {@link java.util.Iterator}.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.MutableCollectionIterate} - a final class used to chunk {@link com.gs.collections.api.collection.MutableCollection}.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.RandomAccessListIterate} - provides methods from the Smalltalk Collection Protocol for use with {@link java.util.ArrayList}.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.ReflectionHelper} - a utility/helper class for working with Classes and Reflection.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.SetIterables} - a class provides for set algebra operations.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.SetIterate} - a final class used for internal purposes to iterate over Set.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.internal.SortedSetIterables} - a class provides for sortedSet algebra operations.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.utility.internal;
