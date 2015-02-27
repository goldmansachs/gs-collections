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

package com.gs.collections.impl.jmh.set.sorted

import scala.collection.{immutable, mutable}

object SortedSetContainsScalaTest
{
    private val SIZE = 2000000

    val scalaMutable: mutable.TreeSet[Int] = new mutable.TreeSet[Int]() ++ (0 to SIZE by 2)
    val scalaImmutable: immutable.TreeSet[Int] = immutable.TreeSet.empty[Int] ++ (0 to SIZE by 2)

    def contains_mutable_scala(): Unit =
    {
        val size = SIZE
        val localScalaMutable = this.scalaMutable

        var i = 0
        while (i < size)
        {
            if (!localScalaMutable.contains(i))
            {
                throw new AssertionError
            }

            i += 2
        }


        i = 1
        while (i < size)
        {
            if (localScalaMutable.contains(i))
            {
                throw new AssertionError
            }
            i += 2
        }
    }

    def contains_immutable_scala(): Unit =
    {
        val size = SIZE
        val localScalaImmutable = this.scalaImmutable

        var i = 0
        while (i < size)
        {
            if (!localScalaImmutable.contains(i))
            {
                throw new AssertionError
            }

            i += 2
        }

        i = 1
        while (i < size)
        {
            if (localScalaImmutable.contains(i))
            {
                throw new AssertionError
            }
            i += 2
        }
    }
}
