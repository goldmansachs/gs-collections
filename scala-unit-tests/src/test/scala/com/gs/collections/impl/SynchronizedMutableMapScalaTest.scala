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
import map.mutable.UnifiedMap
import org.junit.Test

import com.gs.collections.api.map.MutableMap
import Prelude._
import tuple.Tuples

class SynchronizedMutableMapScalaTest extends SynchronizedMapIterableTestTrait
{
    val classUnderTest: MutableMap[String, String] = UnifiedMap.newWithKeysValues("One", "1", "Two", "2", "Three", "3").asSynchronized()

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
    def withKeyValue_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.withKeyValue("foo", "bar")
        }
    }

    @Test
    def withAllKeyValues_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.withAllKeyValues(FastList.newListWith(Tuples.pair("foo", "bar")));
        }
    }

    @Test
    def withAllKeyValueArguments_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.withAllKeyValueArguments(Tuples.pair("foo", "bar"))
        }
    }

    @Test
    def withoutKey_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.withoutKey("foo")
        }
    }

    @Test
    def withoutAllKeys_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.withoutAllKeys(FastList.newListWith("foo"))
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
}
