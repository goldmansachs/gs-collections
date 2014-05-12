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
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable

object FunctionalInterfaceScalaTest
{
    private val SIZE: Int = 1000000
    private val integers: ArrayBuffer[Int] = new ArrayBuffer[Int]() ++ (1 to SIZE)

    def megamorphic(megamorphicWarmupLevel: Int)
    {
        val predicate1: (Int) => Boolean = each => (each + 2) % 10000 != 0
        val predicate2: (Int) => Boolean = each => (each + 3) % 10000 != 0
        val predicate3: (Int) => Boolean = each => (each + 4) % 10000 != 0
        val predicate4: (Int) => Boolean = each => (each + 5) % 10000 != 0

        val function1: (Int) => String = each =>
        {
            Assert.assertNotNull(each)
            String.valueOf(each)
        }

        val function2: (String) => Int = each =>
        {
            Assert.assertNotNull(each)
            Integer.valueOf(each)
        }

        val function3: (Int) => String = each =>
        {
            Assert.assertEquals(each, each)
            String.valueOf(each)
        }

        val function4: (String) => Int = each =>
        {
            Assert.assertEquals(each, each)
            Integer.valueOf(each)
        }

        if (megamorphicWarmupLevel > 0)
        {
            // serial, lazy, GSC
            {
                val set = this.integers.view.filter(predicate1).map(function1).map(function2).filter(predicate2).toSet
                Assert.assertEquals(999800, set.size)
                val buffer = this.integers.view.filter(predicate3).map(function3).map(function4).filter(predicate4).toBuffer
                Assert.assertEquals(999800, buffer.size)
            }

            // parallel, lazy, GSC
            {
                val set = this.integers.par.filter(predicate1).map(function1).map(function2).filter(predicate2).toSet
                Assert.assertEquals(999800, set.size)
                val buffer = this.integers.par.filter(predicate3).map(function3).map(function4).filter(predicate4).toBuffer
                Assert.assertEquals(999800, buffer.size)
            }

            // serial, eager, GSC
            {
                val set = this.integers.filter(predicate1).map(function1).map(function2).filter(predicate2).toSet
                Assert.assertEquals(999800, set.size)
                val buffer = this.integers.filter(predicate3).map(function3).map(function4).filter(predicate4).toBuffer
                Assert.assertEquals(999800, buffer.size)
            }
        }
    }

    def serial_eager_scala(): ArrayBuffer[Integer] =
    {
        val list = this.integers
                .filter(each => each % 10000 != 0)
                .map(String.valueOf)
                .map(Integer.valueOf)
                .filter(each => (each + 1) % 10000 != 0)
        Assert.assertEquals(999800, list.size)
        list
    }

    def test_serial_eager_scala()
    {
        Assert.assertEquals(1.to(1000000, 10000).flatMap(each => each.to(each + 9997)).toBuffer, this.serial_eager_scala())
    }

    def serial_lazy_scala(): mutable.Buffer[Integer] =
    {
        val list = this.integers
                .view
                .filter(each => each % 10000 != 0)
                .map(String.valueOf)
                .map(Integer.valueOf)
                .filter(each => (each + 1) % 10000 != 0)
                .toBuffer
        Assert.assertEquals(999800, list.size)
        list
    }

    def test_serial_lazy_scala()
    {
        Assert.assertEquals(1.to(1000000, 10000).flatMap(each => each.to(each + 9997)).toBuffer, this.serial_lazy_scala())
    }

    def parallel_lazy_scala(): mutable.Buffer[Integer] =
    {
        val list = this.integers
                .par
                .filter(each => each % 10000 != 0)
                .map(String.valueOf)
                .map(Integer.valueOf)
                .filter(each => (each + 1) % 10000 != 0)
                .toBuffer
        Assert.assertEquals(999800, list.size)
        list
    }

    def test_parallel_lazy_scala()
    {
        Assert.assertEquals(1.to(1000000, 10000).flatMap(each => each.to(each + 9997)).toBuffer, this.parallel_lazy_scala())
    }

    def parallel_lazy_scala_hand_coded(): mutable.Buffer[Int] =
    {
        val list = this.integers
                .par
                .filter(integer => integer % 10000 != 0 && (Integer.valueOf(String.valueOf(integer)) + 1) % 10000 != 0)
                .toBuffer
        Assert.assertEquals(999800, list.size)
        list
    }

    def test_parallel_lazy_scala_hand_coded()
    {
        Assert.assertEquals(1.to(1000000, 10000).flatMap(each => each.to(each + 9997)).toBuffer, this.parallel_lazy_scala_hand_coded())
    }
}
