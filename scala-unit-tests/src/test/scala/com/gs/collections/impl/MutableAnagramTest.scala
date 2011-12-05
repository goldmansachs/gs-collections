/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl

import java.util.Collections

import org.junit.Test
import org.slf4j.LoggerFactory

import com.gs.collections.api._
import com.gs.collections.api.block.function.Function
import block.factory.Comparators
import list.mutable.FastList
import utility.internal.IteratorIterate
import Prelude._

object MutableAnagramTest
{
    val LOGGER = LoggerFactory.getLogger(classOf[MutableAnagramTest])
}

class MutableAnagramTest
{
    val SIZE_THRESHOLD = 10
    val WORDS = FastList.newListWith("alerts", "alters", "artels", "estral", "laster", "ratels", "salter", "slater", "staler", "stelar", "talers", "least", "setal", "slate", "stale", "steal", "stela", "taels", "tales", "teals", "tesla").iterator

    @Test
    def testAnagrams
    {
        val sizeOfIterable: Function[RichIterable[String], Integer] = (iterable: RichIterable[String]) => Integer.valueOf(iterable.size)
        IteratorIterate.groupBy(WORDS, (string: String) => string.sortWith(_ > _))
                .multiValuesView
                .select((iterable: RichIterable[String]) => iterable.size > SIZE_THRESHOLD)
                .toSortedList(Collections.reverseOrder(Comparators.byFunction[RichIterable[String], Integer](sizeOfIterable)))
                .asLazy
                .collect[String]((iterable: RichIterable[String]) => iterable.size + ": " + iterable)
                .forEach((string: String) => MutableAnagramTest.LOGGER.info(string))
    }
}
