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

package com.gs.collections.impl.stack.mutable

import com.gs.collections.impl.SynchronizedRichIterableTestTrait
import org.junit.Test

class SynchronizedStackScalaTest extends SynchronizedRichIterableTestTrait
{
    val classUnderTest = ArrayStack.newStackFromTopToBottom[String]("1", "2", "3").asSynchronized

    @Test
    def equals_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.equals(null)
        }
    }

    @Test
    def hashCode_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.hashCode
        }
    }

    @Test
    def push
    {
        this.assertSynchronized
        {
            this.classUnderTest.push("4")
        }
    }

    @Test
    def pop
    {
        this.assertSynchronized
        {
            this.classUnderTest.pop
        }
    }

    @Test
    def pop_int
    {
        this.assertSynchronized
        {
            this.classUnderTest.pop(2)
        }
    }

    @Test
    def peek
    {
        this.assertSynchronized
        {
            this.classUnderTest.peek()
        }
    }

    @Test
    def peek_int
    {
        this.assertSynchronized
        {
            this.classUnderTest.peek(2)
        }
    }

    @Test
    def clear
    {
        this.assertSynchronized
        {
            this.classUnderTest.clear
        }
    }

    @Test
    def to_immutable
    {
        this.assertSynchronized
        {
            this.classUnderTest.toImmutable
        }
    }
}
