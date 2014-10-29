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
 * This package contains implementations of the {@link com.gs.collections.api.multimap.list.ListMultimap} interface.
 * <p>
 *     A {@link com.gs.collections.api.multimap.list.ListMultimap} is a type of {@code Multimap} that uses a list as its underlying store for the multiple values of a given key.
 * <p>
 *     This package contains the following implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.list.FastListMultimap} - a {@code MutableListMultimap} which uses a {@link com.gs.collections.impl.list.mutable.FastList} as its underlying store for the multiple values of a given key.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.list.ImmutableListMultimapImpl} - the default {@code ImmutableListMultimap} implementation.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.multimap.list.SynchronizedPutFastListMultimap} - a {@code MutableListMultimap} that is optimized for parallel writes, but is not protected for concurrent reads.
 *     </li>
 */
package com.gs.collections.impl.multimap.list;
