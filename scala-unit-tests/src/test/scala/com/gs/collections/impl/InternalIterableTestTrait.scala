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

import com.gs.collections.api.InternalIterable
import block.procedure.CollectionAddProcedure
import list.mutable.FastList
import test.Verify
import Prelude._

import org.junit.{Assert, Test}

trait InternalIterableTestTrait extends IterableTestTrait
{
    val classUnderTest: InternalIterable[String]

    @Test
    def forEach
    {
        val result = FastList.newList[String]

        this.classUnderTest.forEach(CollectionAddProcedure.on(result))

        Verify.assertSize(3, result)
        Verify.assertContainsAll(result, "1", "2", "3")
    }

    @Test
    def forEachWithIndex
    {
        val result = FastList.newList[String]
        val indices = FastList.newList[Integer]

        var count = 0
        this.classUnderTest.forEachWithIndex
        {
            (each: String, index: Int) =>
                Assert.assertEquals(index, count)
                count += 1
                result.add(each)
                indices.add(index)
                ()
        }

        Verify.assertSize(3, result)
        Verify.assertContainsAll(result, "1", "2", "3")

        Verify.assertSize(3, indices)
        Verify.assertContainsAll(indices, Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2))
    }

    @Test
    def forEachWith
    {
        val result = FastList.newList[String]

        this.classUnderTest.forEachWith(
            (each: String, parameter: String) =>
            {
                result.add(each + parameter)
                ()
            },
            "!")

        Verify.assertSize(3, result)
        Verify.assertContainsAll(result, "1!", "2!", "3!")
    }
}
