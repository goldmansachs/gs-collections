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

package com.gs.collections.impl

import com.gs.collections.impl.list.mutable.FastList
import org.junit.Test

trait SynchronizedCollectionTestTrait extends SynchronizedTestTrait with IterableTestTrait
{
    val classUnderTest: java.util.Collection[String]

    @Test
    def size_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.size
        }
    }

    @Test
    def isEmpty_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.isEmpty
        }
    }

    @Test
    def contains_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.contains(null)
        }
    }

    @Test
    def iterator_not_synchronized
    {
        this.assertNotSynchronized
        {
            this.classUnderTest.iterator
        }
    }

    @Test
    def toArray_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toArray
        }
    }

    @Test
    def toArray_with_target_synchronized
    {
        this.assertSynchronized
        {
            val array: Array[String] = null
            this.classUnderTest.toArray(array)
        }
    }

    @Test
    def add_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.add("")
        }
    }

    @Test
    def remove_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.remove("")
        }
    }

    @Test
    def containsAll_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.containsAll(FastList.newList[String])
        }
    }

    @Test
    def addAll_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.addAll(FastList.newList[String])
        }
    }

    @Test
    def removeAll_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.removeAll(FastList.newList[String])
        }
    }

    @Test
    def retainAll_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.retainAll(FastList.newList[String])
        }
    }

    @Test
    def clear_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.clear()
        }
    }

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
}
