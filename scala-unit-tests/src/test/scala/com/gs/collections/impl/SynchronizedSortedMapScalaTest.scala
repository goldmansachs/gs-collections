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

package com.gs.collections.impl

import block.factory.Functions
import list.mutable.FastList
import map.sorted.mutable.TreeSortedMap
import Prelude._
import org.junit.Test
import com.gs.collections.api.map.sorted.MutableSortedMap
import tuple.Tuples

class SynchronizedSortedMapScalaTest extends SynchronizedMapIterableTestTrait
{
    val classUnderTest: MutableSortedMap[String, String] = TreeSortedMap.newMapWith("A", "1", "B", "2", "C", "3").asSynchronized()

    @Test
    def newEmpty_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.newEmpty
        }
    }

    @Test
    def removeKey_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.remove("1")
        }
    }

    @Test
    def getIfAbsentPut_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.getIfAbsentPut("Nine", () => "foo")
        }
    }

    @Test
    def getIfAbsentPutWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.getIfAbsentPutWith("Nine", Functions.getPassThru[String], "foo")
        }
    }

    @Test
    def asUnmodifiable_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.asUnmodifiable
        }
    }

    @Test
    def toImmutable_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toImmutable
        }
    }

    @Test
    def collectKeysAndValues_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.collectKeysAndValues(FastList.newListWith[java.lang.Integer](4, 5, 6),
            {
                _: java.lang.Integer => ""
            },
            {
                _: java.lang.Integer => ""
            })
        }
    }

    @Test
    def comparator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.comparator
        }
    }

    @Test
    def values_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.values
        }
    }

    @Test
    def keySet_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.keySet
        }
    }

    @Test
    def entrySet_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.entrySet
        }
    }

    @Test
    def headMap_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.headMap("B")
        }
    }

    @Test
    def tailMap_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.tailMap("B")
        }
    }

    @Test
    def subMap_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.subMap("A", "C")
        }
    }

    @Test
    def firstKey_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.firstKey
        }
    }

    @Test
    def lastKey_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.lastKey
        }
    }

    @Test
    def with_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.`with`(Tuples.pair("D", "4"))
        }
    }
}
