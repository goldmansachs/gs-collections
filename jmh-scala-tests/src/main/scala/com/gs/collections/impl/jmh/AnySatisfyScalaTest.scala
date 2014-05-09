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

object AnySatisfyScalaTest
{
    private val SIZE = 1000000
    private val integers = new ArrayBuffer[Int](SIZE) ++ (1 to SIZE)

    def short_circuit_middle_serial_eager_scala
    {
        Assert.assertTrue(this.integers.exists(_ > SIZE / 2))
    }

    def process_all_serial_eager_scala
    {
        Assert.assertFalse(this.integers.exists(_ < 0))
    }

    def short_circuit_middle_serial_lazy_scala
    {
        Assert.assertTrue(this.integers.view.exists(_ > SIZE / 2))
    }

    def process_all_serial_lazy_scala
    {
        Assert.assertFalse(this.integers.view.exists(_ < 0))
    }

    def short_circuit_middle_parallel_lazy_scala
    {
        Assert.assertTrue(this.integers.par.exists(_ == SIZE / 2 - 1))
    }

    def process_all_parallel_lazy_scala
    {
        Assert.assertFalse(this.integers.par.exists(_ < 0))
    }
}
