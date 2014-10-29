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
 * This package contains static utilities that provide iteration pattern implementations which work with JCF collections.
 * <p>
 *     This package contains 7 Iteration implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.utility.ArrayIterate} - provides iteration pattern implementations that work with {@link java.util.Arrays}.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.ArrayListIterate} - provides optimized iteration pattern implementations that work with {@link java.util.ArrayList}.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.Iterate} - a router to other utility classes to provide optimized iteration pattern.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.LazyIterate} - a factory class which creates "deferred" iterables around the specified iterables.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.ListIterate} - used for iterating over lists.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.MapIterate} - used for iterating over maps.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.utility.StringIterate} - implements the methods available on the collection protocol that make sense for Strings.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.utility;
