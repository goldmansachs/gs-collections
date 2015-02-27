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

object ScalaSortedSetIterationTest
{
    private val SIZE = 1000000

    val scalaMutable: mutable.TreeSet[Int] = new mutable.TreeSet[Int]() ++ (0 to SIZE)
    val scalaImmutable: immutable.TreeSet[Int] = immutable.TreeSet.empty[Int] ++ (0 to SIZE)

    def serial_mutable_scala(): Unit =
    {
        val count: Int = this.scalaMutable
                .view
                .filter(each => each % 10000 != 0)
                .map(String.valueOf)
                .map(Integer.valueOf)
                .count(each => (each + 1) % 10000 != 0)
        if (count != 999800)
        {
            throw new AssertionError
        }
    }

    def serial_immutable_scala(): Unit =
    {
        val count: Int = this.scalaImmutable
                .view
                .filter(each => each % 10000 != 0)
                .map(String.valueOf)
                .map(Integer.valueOf)
                .count(each => (each + 1) % 10000 != 0)
        if (count != 999800)
        {
            throw new AssertionError
        }
    }

    def parallel_mutable_scala(): Unit =
    {
        val count: Int = this.scalaMutable.par
                .filter(each => each % 10000 != 0)
                .map(String.valueOf)
                .map(Integer.valueOf)
                .count(each => (each + 1) % 10000 != 0)
        if (count != 999800)
        {
            throw new AssertionError
        }
    }

    def parallel_immutable_scala(): Unit =
    {
        val count: Int = this.scalaImmutable.par
                .filter(each => each % 10000 != 0)
                .map(String.valueOf)
                .map(Integer.valueOf)
                .count(each => (each + 1) % 10000 != 0)
        if (count != 999800)
        {
            throw new AssertionError
        }
    }
}
