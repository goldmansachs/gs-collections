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
 * This package contains implementations of the {@link com.gs.collections.api.stack.MutableStack} interface.
 * <p>
 *     Mutable Stack is backed by a FastList and iterates from top to bottom (LIFO order). It behaves like FastList in terms of runtime complexity.
 * <p>
 *     This package contains 3 stack implementations:
 * <ul>
 *     <li>
 *          {@link com.gs.collections.impl.stack.mutable.ArrayStack} - a MutableStack backed by a FastList.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.stack.mutable.SynchronizedStack} - a synchronized view of a stack.
 *     </li>
 *     <li>
 *          {@link com.gs.collections.impl.stack.mutable.UnmodifiableStack} - an unmodifiable view of a stack.
 *     </li>
 * </ul>
 * <p>
 *     This package contains one factory implementation:
 * <ul>
 *     <li>
 *         {@link com.gs.collections.impl.stack.mutable.MutableStackFactoryImpl} - a factory which creates instances of type {@link com.gs.collections.api.stack.MutableStack}.
 *     </li>
 * </ul>
 */
package com.gs.collections.impl.stack.mutable;
