/*
 * Copyright 2013 Goldman Sachs.
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

import org.junit.Test
import org.slf4j.LoggerFactory

object ScalaAnagramTest
{
    val LOGGER = LoggerFactory.getLogger(classOf[ScalaAnagramTest])
}

class ScalaAnagramTest
{
    val SIZE_THRESHOLD = 10
    val WORDS = Stream("alerts", "alters", "artels", "estral", "laster", "ratels", "salter", "slater", "staler", "stelar", "talers", "least", "setal", "slate", "stale", "steal", "stela", "taels", "tales", "teals", "tesla")

    def log(string: String)
    {
        ScalaAnagramTest.LOGGER.info(string)
    }

    @Test
    def testAnagrams
    {
        WORDS.groupBy(_.sorted)
                .values
                .filter(_.size > SIZE_THRESHOLD)
                .toList
                .sortWith(_.size > _.size)
                .map(list => list.size + ": " + list)
                .foreach(log)
    }

    @Test
    def testAnagramsLonghand
    {
        WORDS.groupBy(word => word.sorted)
                .values
                .filter(list => list.size > SIZE_THRESHOLD)
                .toList
                .sortWith((list1, list2) => list1.size > list2.size)
                .map(list => list.size + ": " + list)
                .foreach(listString => log(listString))
    }
}
