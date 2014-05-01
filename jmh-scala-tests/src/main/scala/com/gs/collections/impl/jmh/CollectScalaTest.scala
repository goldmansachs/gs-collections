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
import java.util.concurrent.TimeUnit
import org.junit.Assert

object CollectScalaTest
{
    private val SIZE = 1000000
    private val integers = new ArrayBuffer[Int](SIZE) ++ (1 to SIZE)

    def serial_eager_scala()
    {
        val strings = this.integers.map(_.toString)
        Assert.assertEquals(SIZE, strings.size)
    }

    def serial_lazy_scala()
    {
        val strings = this.integers.view.map(_.toString).toBuffer
        Assert.assertEquals(SIZE, strings.size)
    }

    def parallel_lazy_scala()
    {
        val strings = this.integers.par.map(_.toString)
        Assert.assertEquals(SIZE, strings.size)
    }

}
