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

package com.gs.collections.impl.list.mutable

import com.gs.collections.impl.Prelude._
import com.gs.collections.impl.collection.mutable.SynchronizedMutableCollectionTestTrait

class SynchronizedMutableListScalaTest extends SynchronizedMutableCollectionTestTrait
{
    import org.junit.Test

    val classUnderTest = FastList.newListWith[String]("1", "2", "3").asSynchronized

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
    def asReversed_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.asReversed()
        }
        val reverseIterable = this.classUnderTest.asReversed()
        this.assertSynchronized
        {
            reverseIterable.forEach{_: String => ()}
        }
    }
}
