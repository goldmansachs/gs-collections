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

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.apache.commons.lang.RandomStringUtils
import java.util
import org.junit.Assert
import scala.collection.parallel.immutable.ParSet
import scala.collection.mutable

object AnagramSetScalaTest
{
    private val SIZE = 1000000
    private val CORES = Runtime.getRuntime.availableProcessors
    private val SIZE_THRESHOLD = 10
    private val WORDS: Set[String] = Set(Seq.fill(SIZE)(RandomStringUtils.randomAlphabetic(5).toUpperCase()): _*)

    private var executorService: ExecutorService = null

    def setUp(): Unit =
    {
        this.executorService = Executors.newFixedThreadPool(CORES)
    }

    def tearDown(): Unit =
    {
        this.executorService.shutdownNow
        this.executorService.awaitTermination(1L, TimeUnit.MILLISECONDS)
    }

    def serial_eager_scala(): Unit =
    {
        WORDS
                .groupBy(Alphagram.apply)
                .values
                .filter(_.size >= SIZE_THRESHOLD)
                .toSeq.sortBy(_.size)
                .reverseIterator
                .map(iterable => iterable.size + ": " + iterable.toString())
                .foreach(string => Assert.assertFalse(string.isEmpty))
    }

    def serial_lazy_scala(): Unit =
    {
        WORDS.view
                .groupBy(Alphagram.apply)
                .values
                .filter(_.size >= SIZE_THRESHOLD)
                .toSeq.sortBy(_.size)
                .reverseIterator
                .map(iterable => iterable.size + ": " + iterable.toString())
                .foreach(string => Assert.assertFalse(string.isEmpty))
    }

    def parallel_lazy_scala(): Unit =
    {
        val toBuffer: mutable.Buffer[ParSet[String]] = WORDS.par
                .groupBy(Alphagram.apply)
                .values
                .filter(_.size >= SIZE_THRESHOLD)
                .toBuffer
        toBuffer.sortBy(_.size)
                .par
                .reverseMap(iterable => iterable.size + ": " + iterable.toString())
                .foreach(string => Assert.assertFalse(string.isEmpty))
    }

    object Alphagram
    {
        def apply(string: String): Alphagram =
        {
            val key = string.toCharArray
            util.Arrays.sort(key)
            new Alphagram(key)
        }
    }

    class Alphagram(private val key: Array[Char])
    {
        override def equals(o: Any): Boolean =
        {
            if (null == o || this.getClass != o.getClass)
            {
                return false
            }
            val other = o.asInstanceOf[Alphagram]
            if (this eq other)
            {
                return true
            }
            util.Arrays.equals(this.key, other.key)
        }

        override def hashCode = util.Arrays.hashCode(this.key)

        override def toString = new String(this.key)
    }
}
