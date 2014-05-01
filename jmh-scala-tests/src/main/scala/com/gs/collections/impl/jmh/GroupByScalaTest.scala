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

object GroupByScalaTest
{
    private val SIZE = 1000000
    private val integers = new ArrayBuffer[Int](SIZE) ++ (1 to SIZE)

    def groupBy_2_keys_serial_eager_scala()
    {
        Assert.assertEquals(2, this.integers.groupBy((each: Int) => each % 2 == 0).size)
    }

    def groupBy_100_keys_serial_eager_scala()
    {
        Assert.assertEquals(100, this.integers.groupBy((each: Int) => each % 100).size)
    }

    def groupBy_10000_keys_serial_eager_scala()
    {
        Assert.assertEquals(10000, this.integers.groupBy((each: Int) => each % 10000).size)
    }

    def groupBy_2_keys_serial_lazy_scala()
    {
        Assert.assertEquals(2, this.integers.view.groupBy((each: Int) => each % 2 == 0).size)
    }

    def groupBy_100_keys_serial_lazy_scala()
    {
        Assert.assertEquals(100, this.integers.view.groupBy((each: Int) => each % 100).size)
    }

    def groupBy_10000_keys_serial_lazy_scala()
    {
        Assert.assertEquals(10000, this.integers.view.groupBy((each: Int) => each % 10000).size)
    }
}
