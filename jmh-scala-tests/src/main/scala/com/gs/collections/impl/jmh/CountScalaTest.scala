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

import scala.collection.mutable.ArrayBuffer
import org.junit.Assert

object CountScalaTest
{
    private val SIZE = 1000000
    private val integers = new ArrayBuffer[Int](SIZE) ++ (1 to SIZE)

    def megamorphic(megamorphicWarmupLevel: Int): Unit =
    {
        if (megamorphicWarmupLevel > 0)
        {
            // serial, lazy
            {
                val evens = this.integers.view.count(_ % 2 == 0)
                Assert.assertEquals(SIZE / 2, evens)
                val odds = this.integers.view.count(_ % 2 == 1)
                Assert.assertEquals(SIZE / 2, odds)
                val evens2 = this.integers.view.count(each => (each & 1) == 0)
                Assert.assertEquals(SIZE / 2, evens2)
            }

            // parallel, lazy
            {
                val evens = this.integers.par.count(_ % 2 == 0)
                Assert.assertEquals(SIZE / 2, evens)
                val odds = this.integers.par.count(_ % 2 == 1)
                Assert.assertEquals(SIZE / 2, odds)
                val evens2 = this.integers.par.count(each => (each & 1) == 0)
                Assert.assertEquals(SIZE / 2, evens2)
            }

            // serial, eager
            {
                val evens = this.integers.count(_ % 2 == 0)
                Assert.assertEquals(SIZE / 2, evens)
                val odds = this.integers.count(_ % 2 == 1)
                Assert.assertEquals(SIZE / 2, odds)
                val evens2 = this.integers.count(each => (each & 1) == 0)
                Assert.assertEquals(SIZE / 2, evens2)
            }
        }

        // deoptimize scala.collection.mutable.ResizableArray.foreach()
        // deoptimize scala.collection.IndexedSeqOptimized.foreach()
        if (megamorphicWarmupLevel > 1)
        {
            this.integers.view.foreach(Assert.assertNotNull)
            this.integers.view.foreach(each => Assert.assertEquals(each, each))
            this.integers.view.foreach(each => Assert.assertNotEquals(null, each))
            this.integers.par.foreach(Assert.assertNotNull)
            this.integers.par.foreach(each => Assert.assertEquals(each, each))
            this.integers.par.foreach(each => Assert.assertNotEquals(null, each))
        }

        // deoptimize scala.collection.mutable.ResizableArray.foreach()
        // deoptimize scala.collection.IndexedSeqOptimized.foreach()
        if (megamorphicWarmupLevel > 2)
        {
            this.integers.foreach(Assert.assertNotNull)
            this.integers.foreach(each => Assert.assertEquals(each, each))
            this.integers.foreach(each => Assert.assertNotEquals(null, each))
        }
    }

    def serial_eager_scala(): Unit =
    {
        val evens = this.integers.count(_ % 2 == 0)
        Assert.assertEquals(SIZE / 2, evens)
    }

    def serial_lazy_scala(): Unit =
    {
        val evens = this.integers.view.count(_ % 2 == 0)
        Assert.assertEquals(SIZE / 2, evens)
    }

    def parallel_lazy_scala(): Unit =
    {
        val evens = this.integers.par.count(_ % 2 == 0)
        Assert.assertEquals(SIZE / 2, evens)
    }
}
