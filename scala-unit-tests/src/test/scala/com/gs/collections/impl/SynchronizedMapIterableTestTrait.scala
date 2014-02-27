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

import org.junit.Test

import Prelude._
import com.gs.collections.api.map.MapIterable
import tuple.Tuples

trait SynchronizedMapIterableTestTrait extends SynchronizedRichIterableTestTrait
{
    val classUnderTest: MapIterable[String, String]

    @Test
    def equals_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.equals(null)
        }
    }

    @Test
    def hashCode_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.hashCode
        }
    }

    @Test
    def get_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.get("1")
        }
    }

    @Test
    def containsKey_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.containsKey("One")
        }
    }

    @Test
    def containsValue_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.containsValue("2")
        }
    }

    @Test
    def forEachKey_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.forEachKey
            {
                _: String => ()
            }
        }
    }

    @Test
    def forEachValue_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.forEachValue
            {
                _: String => ()
            }
        }
    }

    @Test
    def forEachKeyValue_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.forEachKeyValue
            {
                (_: String, _: String) => ()
            }
        }
    }

    @Test
    def getIfAbsent_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.getIfAbsent("Nine", () => "foo")
        }
    }

    @Test
    def getIfAbsentWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.getIfAbsentWith("Nine", (_: String) => "", "foo")
        }
    }

    @Test
    def ifPresentApply_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.ifPresentApply("1", (_: String) => "foo")
        }
    }

    @Test
    def keysView_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.keysView()
        }
    }

    @Test
    def valuesView_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.valuesView()
        }
    }

    @Test
    def mapSelect_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.select
            {
                (_: String, _: String) => false
            }
        }
    }

    @Test
    def mapCollectValues_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.collectValues
            {
                (_: String, _: String) => "foo"
            }
        }
    }

    @Test
    def mapCollect_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.collect
            {
                (_: String, _: String) => Tuples.pair("foo", "bar")
            }
        }
    }

    @Test
    def mapReject_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.reject
            {
                (_: String, _: String) => false
            }
        }
    }

    @Test
    def mapDetect_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.detect
            {
                (_: String, _: String) => false
            }
        }
    }
}
