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

package com.gs.collections.impl

import org.junit.{Assert, Test}

import com.gs.collections.api.list.ListIterable
import block.procedure.CollectionAddProcedure
import list.mutable.FastList
import Prelude._

trait ListIterableTestTrait extends InternalIterableTestTrait
{
    val classUnderTest: ListIterable[String]

    @Test
    abstract override def forEach
    {
        super.forEach

        val result = FastList.newList[String]
        classUnderTest.forEach(CollectionAddProcedure.on(result))
        Assert.assertEquals(FastList.newListWith("1", "2", "3"), result)
    }

    @Test
    abstract override def forEachWithIndex
    {
        super.forEachWithIndex

        var count = 0
        classUnderTest.forEachWithIndex
        {
            (each: String, index: Int) =>
                Assert.assertEquals(index, count)
                count += 1
                Assert.assertEquals(String.valueOf(count), each)
        }

        Assert.assertEquals(3, count)
    }

    @Test
    abstract override def forEachWith
    {
        super.forEachWith

        val unique = new AnyRef
        var count = 0

        classUnderTest.forEachWith(
            (each: String, parameter: AnyRef) =>
            {
                count += 1
                Assert.assertEquals(String.valueOf(count), each)
                Assert.assertSame(unique, parameter)
                ()
            },
            unique)

        Assert.assertEquals(3, count)
    }
}
