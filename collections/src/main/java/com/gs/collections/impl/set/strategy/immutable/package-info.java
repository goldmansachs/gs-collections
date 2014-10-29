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
 * This package contains implementations of immutable sets with user defined {@link com.gs.collections.api.block.HashingStrategy}s.
 * <p>
 *     This package contains the following implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.set.strategy.immutable.ImmutableEmptySetWithHashingStrategy} - an {@link com.gs.collections.impl.set.immutable.ImmutableEmptySet} with user defined hashing strategy.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.set.strategy.immutable.ImmutableUnifiedSetWithHashingStrategy} - an {@link com.gs.collections.impl.set.immutable.ImmutableUnifiedSet} with user defined hashing strategy.
 *     </li>
 * </ul>
 * <p>
 *     This package contains one factory implementation:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.set.strategy.immutable.ImmutableHashingStrategySetFactoryImpl} - a factory which creates instances of type {@link com.gs.collections.api.set.ImmutableSet} with user defined hashing strategy.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.set.strategy.immutable;
