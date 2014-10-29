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
 * This package contains implementations of the {@link com.gs.collections.api.map.MutableMap} interface.
 * <p>
 *     A MutableMap is an implementation of a {@link java.util.Map} which provides internal iterator methods matching the Smalltalk Collection protocol.
 * <p>
 *     This package contains the following implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.map.mutable.MapAdapter} - a MutableMap wrapper around a {@link java.util.Map} interface instance.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.map.mutable.UnifiedMap} - a map which uses a hashtable as its underlying data store and stores key/value pairs in consecutive locations in a single array.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.map.mutable.SynchronizedMutableMap} - a synchronized view of a map.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.map.mutable.UnmodifiableMutableMap} - an unmodifiable view of a map.
 *     </li>
 * </ul>
 * <p>
 *     This package contains one factory implementation:
 * <ul>
 *     <li>
 *         {@link com.gs.collections.impl.map.mutable.MutableMapFactoryImpl} - a factory which creates instances of type {@link com.gs.collections.api.map.MutableMap}.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.map.mutable;
