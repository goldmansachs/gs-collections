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
 * This package package contains implementations of {@link com.gs.collections.api.set.MutableSet}.
 * <p>
 *     This package contains the following mutable set implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.set.mutable.MultiReaderUnifiedSet} -  a thread safe wrapper around UnifiedSet.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.set.mutable.SetAdapter} - a MutableSet wrapper around a JDK Collections Set interface instance.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.set.mutable.SynchronizedMutableSet} - a synchronized view of a set.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.set.mutable.UnifiedSet} - an implementation of a JCF Set which provides methods matching the Smalltalk Collection protocol.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.set.mutable.UnmodifiableMutableSet} - an unmodifiable view of a set.
 *     </li>
 * </ul>
 * <p>
 *     This package contains 1 factory implementation:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.set.mutable.MutableSetFactoryImpl} - a factory which creates instances of type {@link com.gs.collections.api.set.MutableSet}.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.set.mutable;
