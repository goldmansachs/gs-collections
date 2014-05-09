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

package com.gs.collections.impl.jmh

import org.junit.Assert
import scala.collection.mutable.HashSet
import scala.collection.IterableView
import scala.collection.parallel.immutable.ParMap
import scala.collection.parallel.mutable.ParHashSet

object GroupBySetScalaTest
{
    private val SIZE = 1000000
    private val integers = new HashSet[Int]() ++ (0 to SIZE - 1)

    def groupBy_2_keys_serial_eager_scala(): Map[Boolean, HashSet[Int]] =
    {
        val multimap: Map[Boolean, HashSet[Int]] = this.integers.groupBy((each: Int) => each % 2 == 0)
        Assert.assertEquals(2, multimap.size)
        multimap
    }

    def test_groupBy_2_keys_serial_eager_scala(): Unit =
    {
        val multimap: Map[Boolean, HashSet[Int]] = groupBy_2_keys_serial_eager_scala()
        val odds: HashSet[Int] = multimap(false)
        val evens: HashSet[Int] = multimap(true)
        Assert.assertEquals(0.to(999999, 2).toSet, evens)
        Assert.assertEquals(1.to(999999, 2).toSet, odds)
    }

    def groupBy_100_keys_serial_eager_scala(): Map[Int, HashSet[Int]] =
    {
        val multimap: Map[Int, HashSet[Int]] = this.integers.groupBy((each: Int) => each % 100)
        Assert.assertEquals(100, multimap.size)
        multimap
    }

    def test_groupBy_100_keys_serial_eager_scala(): Unit =
    {
        val multimap: Map[Int, HashSet[Int]] = groupBy_100_keys_serial_eager_scala()
        for (i <- 0 to 99)
        {
            val integers: HashSet[Int] = multimap(i)
            Assert.assertEquals(i.to(999999, 100).toSet, integers)
        }
    }

    def groupBy_10000_keys_serial_eager_scala(): Map[Int, HashSet[Int]] =
    {
        val multimap: Map[Int, HashSet[Int]] = this.integers.groupBy((each: Int) => each % 10000)
        Assert.assertEquals(10000, multimap.size)
        multimap
    }

    def test_groupBy_10000_keys_serial_eager_scala(): Unit =
    {
        val multimap: Map[Int, HashSet[Int]] = groupBy_10000_keys_serial_eager_scala()
        for (i <- 0 to 9999)
        {
            val integers: HashSet[Int] = multimap(i)
            Assert.assertEquals(i.to(999999, 10000).toSet, integers)
        }
    }

    def groupBy_unordered_lists_2_keys_serial_lazy_scala(): Map[Boolean, IterableView[Int, HashSet[Int]]] =
    {
        val multimap: Map[Boolean, IterableView[Int, HashSet[Int]]] = this.integers.view.groupBy((each: Int) => each % 2 == 0)
        Assert.assertEquals(2, multimap.size)
        multimap
    }

    def test_groupBy_unordered_lists_2_keys_serial_lazy_scala(): Unit =
    {
        val multimap: Map[Boolean, IterableView[Int, HashSet[Int]]] = groupBy_unordered_lists_2_keys_serial_lazy_scala()
        val evens: IterableView[Int, HashSet[Int]] = multimap(true)
        val odds: IterableView[Int, HashSet[Int]] = multimap(false)
        Assert.assertEquals(0.to(999999, 2).toSet, evens.toSet)
        Assert.assertEquals(1.to(999999, 2).toSet, odds.toSet)
    }

    def groupBy_unordered_lists_100_keys_serial_lazy_scala(): Map[Int, IterableView[Int, HashSet[Int]]] =
    {
        val multimap: Map[Int, IterableView[Int, HashSet[Int]]] = this.integers.view.groupBy((each: Int) => each % 100)
        Assert.assertEquals(100, multimap.size)
        multimap
    }

    def test_groupBy_unordered_lists_100_keys_serial_lazy_scala(): Unit =
    {
        val multimap: Map[Int, IterableView[Int, HashSet[Int]]] = groupBy_unordered_lists_100_keys_serial_lazy_scala()
        for (i <- 0 to 99)
        {
            val integers: IterableView[Int, HashSet[Int]] = multimap(i)
            Assert.assertEquals(i.to(999999, 100).toSet, integers.toSet)
        }
    }

    def groupBy_unordered_lists_10000_keys_serial_lazy_scala(): Map[Int, IterableView[Int, HashSet[Int]]] =
    {
        val multimap: Map[Int, IterableView[Int, HashSet[Int]]] = this.integers.view.groupBy((each: Int) => each % 10000)
        Assert.assertEquals(10000, multimap.size)
        multimap
    }

    def test_groupBy_unordered_lists_10000_keys_serial_lazy_scala(): Unit =
    {
        val multimap: Map[Int, IterableView[Int, HashSet[Int]]] = groupBy_unordered_lists_10000_keys_serial_lazy_scala()
        for (i <- 0 to 9999)
        {
            val integers: IterableView[Int, HashSet[Int]] = multimap(i)
            Assert.assertEquals(i.to(999999, 10000).toSet, integers.toSet)
        }
    }

    def groupBy_2_keys_parallel_lazy_scala(): ParMap[Boolean, ParHashSet[Int]] =
    {
        val multimap: ParMap[Boolean, ParHashSet[Int]] = this.integers.par.groupBy((each: Int) => each % 2 == 0)
        Assert.assertEquals(2, multimap.size)
        multimap
    }

    def test_groupBy_2_keys_parallel_lazy_scala(): Unit =
    {
        val multimap: ParMap[Boolean, ParHashSet[Int]] = groupBy_2_keys_parallel_lazy_scala()
        val evens: ParHashSet[Int] = multimap(true)
        val odds: ParHashSet[Int] = multimap(false)
        Assert.assertEquals(0.to(999999, 2).toSet, evens)
        Assert.assertEquals(1.to(999999, 2).toSet, odds)
    }

    def groupBy_100_keys_parallel_lazy_scala(): ParMap[Int, ParHashSet[Int]] =
    {
        val multimap: ParMap[Int, ParHashSet[Int]] = this.integers.par.groupBy((each: Int) => each % 100)
        Assert.assertEquals(100, multimap.size)
        multimap
    }

    def test_groupBy_100_keys_parallel_lazy_scala(): Unit =
    {
        val multimap: ParMap[Int, ParHashSet[Int]] = groupBy_100_keys_parallel_lazy_scala()
        for (i <- 0 to 99)
        {
            val integers: ParHashSet[Int] = multimap(i)
            Assert.assertEquals(i.to(999999, 100).toSet, integers)
        }
    }

    def groupBy_10000_keys_parallel_lazy_scala(): ParMap[Int, ParHashSet[Int]] =
    {
        val multimap: ParMap[Int, ParHashSet[Int]] = this.integers.par.groupBy((each: Int) => each % 10000)
        Assert.assertEquals(10000, multimap.size)
        multimap
    }

    def test_groupBy_10000_keys_parallel_lazy_scala(): Unit =
    {
        val multimap: ParMap[Int, ParHashSet[Int]] = groupBy_10000_keys_parallel_lazy_scala()
        for (i <- 0 to 9999)
        {
            val integers: ParHashSet[Int] = multimap(i)
            Assert.assertEquals(i.to(999999, 10000).toSet, integers)
        }
    }
}
