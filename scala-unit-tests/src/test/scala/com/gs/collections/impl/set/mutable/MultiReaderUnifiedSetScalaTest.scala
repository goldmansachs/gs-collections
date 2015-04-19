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

package com.gs.collections.impl.set.mutable

import com.gs.collections.api.set.MutableSet
import com.gs.collections.impl.Prelude._
import com.gs.collections.impl.list.mutable.FastList
import org.junit.Test

class MultiReaderUnifiedSetScalaTest extends MultiReaderUnifiedSetTestTrait
{
    val classUnderTest = MultiReaderUnifiedSet.newSetWith(1, 2, 3)

    @Test
    def newSet_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = false)
        {
            MultiReaderUnifiedSet.newSet
        }

    @Test
    def newBagCapacity_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = false)
        {
            MultiReaderUnifiedSet.newSet(5)
        }

    @Test
    def newSetIterable_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = false)
        {
            MultiReaderUnifiedSet.newSet(new FastList[Int])
        }

    @Test
    def newSetWith_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = false)
        {
            MultiReaderUnifiedSet.newSetWith(1, 2)
        }

    @Test
    def union_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.union(new UnifiedSet[Int])
        }

    @Test
    def unionInto_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.unionInto(new UnifiedSet[Int], new UnifiedSet[Int])
        }

    @Test
    def intersect_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.intersect(new UnifiedSet[Int])
        }

    @Test
    def intersectInto_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.intersectInto(new UnifiedSet[Int], new UnifiedSet[Int])
        }

    @Test
    def difference_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.difference(new UnifiedSet[Int])
        }

    @Test
    def differenceInto_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.differenceInto(new UnifiedSet[Int], new UnifiedSet[Int])
        }

    @Test
    def symmetricDifference_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.symmetricDifference(new UnifiedSet[Int])
        }

    @Test
    def symmetricDifferenceInto_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.symmetricDifferenceInto(new UnifiedSet[Int], new UnifiedSet[Int])
        }

    @Test
    def isSubsetOf_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.isSubsetOf(new UnifiedSet[Int])
        }

    @Test
    def isProperSubsetOf_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.isProperSubsetOf(new UnifiedSet[Int])
        }

    @Test
    def powerSet_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.powerSet
        }

    @Test
    def cartesianProduct_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.cartesianProduct(new UnifiedSet[Int])
        }

    @Test
    def clone_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.clone
        }

    @Test
    def iteratorWithReadLock_safe(): Unit =
        this.assert(readersBlocked = false, writersBlocked = true)
        {
            this.classUnderTest.withReadLockAndDelegate((each: MutableSet[Int]) =>
            {
                each.iterator
                ()
            })
        }

    @Test
    def iteratorWithWriteLock_safe(): Unit =
        this.assert(readersBlocked = true, writersBlocked = true)
        {
            this.classUnderTest.withWriteLockAndDelegate((each: MutableSet[Int]) =>
            {
                each.iterator
                ()
            })
        }
}
