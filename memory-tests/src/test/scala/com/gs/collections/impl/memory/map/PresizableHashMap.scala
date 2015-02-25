/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.memory.map

class PresizableHashMap[K, V](override val initialSize: Int, loadFactor: Int) extends scala.collection.mutable.HashMap[K, V]
{
    _loadFactor = loadFactor
    threshold = ((initialCapacity.toLong * loadFactor) / 1000).toInt

    private def initialCapacity = if (initialSize == 0) 1 else smallestPowerOfTwoGreaterThan(initialSize)

    private def smallestPowerOfTwoGreaterThan(n: Int): Int = if (n > 1) Integer.highestOneBit(n - 1) << 1 else 1
}
