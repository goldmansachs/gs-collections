package com.gs.collections.impl.set.mutable

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
import com.gs.collections.impl.collection.mutable.SynchronizedMutableCollectionTestTrait
import com.gs.collections.impl.list.mutable.FastList

class SynchronizedMutableSetScalaTest extends SynchronizedMutableCollectionTestTrait with SynchronizedUnsortedSetIterableTestTrait
{
    val treeSet = new java.util.TreeSet[String](FastList.newListWith[String]("1", "2", "3"))
    val mutableSet = SetAdapter.adapt(treeSet)
    val classUnderTest = mutableSet.asSynchronized

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
}
