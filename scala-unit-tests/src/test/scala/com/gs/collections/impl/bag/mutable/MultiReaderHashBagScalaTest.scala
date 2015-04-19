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

package com.gs.collections.impl.bag.mutable

import com.gs.collections.api.bag.MutableBag
import com.gs.collections.impl.Prelude._
import com.gs.collections.impl.block.factory.primitive.IntPredicates
import com.gs.collections.impl.list.mutable.FastList
import org.junit.Test

class MultiReaderHashBagScalaTest extends MultiReaderHashBagTestTrait
{
    val classUnderTest = MultiReaderHashBag.newBagWith(1, 1, 2)

    @Test
    def newBag_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = false)
        {
            MultiReaderHashBag.newBag
        }

    @Test
    def newBagCapacity_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = false)
        {
            MultiReaderHashBag.newBag(5)
        }

    @Test
    def newBagIterable_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = false)
        {
            MultiReaderHashBag.newBag(FastList.newListWith(1, 2))
        }

    @Test
    def newBagWith_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = false)
        {
            MultiReaderHashBag.newBagWith(1, 2)
        }

    @Test
    def addOccurrences_safe(): Unit =
        this.assert(readersBlocked = true, writersBlocked = true)
        {
            this.classUnderTest.addOccurrences(1, 2)
        }

    @Test
    def removeOccurrences_safe(): Unit =
        this.assert(readersBlocked = true, writersBlocked = true)
        {
            this.classUnderTest.removeOccurrences(1, 1)
        }

    @Test
    def occurrencesOf_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.occurrencesOf(1)
        }

    @Test
    def sizeDistinct_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.sizeDistinct
        }

    @Test
    def toMapOfItemToCount_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.toMapOfItemToCount
        }

    @Test
    def toStringOfItemToCount_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.toStringOfItemToCount
        }

    @Test
    def iteratorWithReadLock_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.withReadLockAndDelegate((each: MutableBag[Int]) =>
            {
                each.iterator
                ()
            })
        }

    @Test
    def iteratorWithWriteLock_safe(): Unit =
        this.assert(readersBlocked = true, writersBlocked = true)
        {
            this.classUnderTest.withWriteLockAndDelegate((each: MutableBag[Int]) =>
            {
                each.iterator
                ()
            })
        }

    @Test
    def forEachWithOccurrences_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.forEachWithOccurrences((_: Int, _: Int) => ())
        }

    @Test
    def selectByOccurrences_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.selectByOccurrences(IntPredicates.isOdd)
        }
}
