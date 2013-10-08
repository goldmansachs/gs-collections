package com.gs.collections.impl.set

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
import com.gs.collections.api.set.SetIterable
import com.gs.collections.impl.SynchronizedRichIterableTestTrait
import sorted.mutable.TreeSortedSet

trait SynchronizedSetIterableTestTrait extends SynchronizedRichIterableTestTrait
{
    val classUnderTest: SetIterable[String]

    @Test
    def union_synchronized
    {
        this.assertSynchronized(this.classUnderTest.union(TreeSortedSet.newSet[String]))
    }

    @Test
    def unionInto_synchronized
    {
        this.assertSynchronized(this.classUnderTest.unionInto(TreeSortedSet.newSet[String], TreeSortedSet.newSet[String]))
    }

    @Test
    def intersect_synchronized
    {
        this.assertSynchronized(this.classUnderTest.intersect(TreeSortedSet.newSet[String]))
    }

    @Test
    def intersectInto_synchronized
    {
        this.assertSynchronized(this.classUnderTest.intersectInto(TreeSortedSet.newSet[String], TreeSortedSet.newSet[String]))
    }

    @Test
    def difference_synchronized
    {
        this.assertSynchronized(this.classUnderTest.difference(TreeSortedSet.newSet[String]))
    }

    @Test
    def differenceInto_synchronized
    {
        this.assertSynchronized(this.classUnderTest.differenceInto(TreeSortedSet.newSet[String], TreeSortedSet.newSet[String]))
    }

    @Test
    def symmetricDifference_synchronized
    {
        this.assertSynchronized(this.classUnderTest.symmetricDifference(TreeSortedSet.newSet[String]))
    }

    @Test
    def symmetricDifferenceInto_synchronized
    {
        this.assertSynchronized(this.classUnderTest.symmetricDifferenceInto(TreeSortedSet.newSet[String], TreeSortedSet.newSet[String]))
    }

    @Test
    def isSubsetOf_synchronized
    {
        this.assertSynchronized(this.classUnderTest.isSubsetOf(TreeSortedSet.newSet[String]))
    }

    @Test
    def isProperSubsetOf_synchronized
    {
        this.assertSynchronized(this.classUnderTest.isProperSubsetOf(TreeSortedSet.newSet[String]))
    }

    @Test
    def cartesianProduct_synchronized
    {
        this.assertSynchronized(this.classUnderTest.cartesianProduct(TreeSortedSet.newSet[String]))
    }
}
