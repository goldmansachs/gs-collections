package com.gs.collections.impl.collection.mutable

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

import org.junit.Test
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet
import com.gs.collections.impl.set.sorted.SynchronizedSortedSetIterableTestTrait

class SynchronizedSortedSetScalaTest extends SynchronizedMutableCollectionTestTrait with SynchronizedSortedSetIterableTestTrait
{
    val classUnderTest = TreeSortedSet.newSetWith("1", "2", "3").asSynchronized

    @Test
    override def equals_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.equals(null)
        }
    }

    @Test
    override def hashCode_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.hashCode
        }
    }

    @Test
    def comparator_synchronized
    {
        this.assertSynchronized(this.classUnderTest.comparator())
    }

    @Test
    def headSet_synchronized
    {
        this.assertSynchronized(this.classUnderTest.headSet("2"))
        this.assertSynchronized(this.classUnderTest.headSet("2").add("1"))
    }

    @Test
    def tailSet_synchronized
    {
        this.assertSynchronized(this.classUnderTest.tailSet("2"))
        this.assertSynchronized(this.classUnderTest.tailSet("2").add("4"))
    }

    @Test
    def subSet_synchronized
    {
        this.assertSynchronized(this.classUnderTest.subSet("1", "3"))
        this.assertSynchronized(this.classUnderTest.subSet("1", "3").add("1"))
    }

    @Test
    def first_synchronized
    {
        this.assertSynchronized(this.classUnderTest.first())
    }

    @Test
    def last_synchronized
    {
        this.assertSynchronized(this.classUnderTest.last())
    }
}
