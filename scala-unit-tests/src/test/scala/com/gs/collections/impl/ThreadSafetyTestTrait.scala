/*
 * Copyright 2013 Goldman Sachs.
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

import java.util.concurrent.TimeUnit
import org.junit.Assert

trait ThreadSafetyTestTrait
{
    def createReadLockHolderThread(gate: Gate): Thread

    def createWriteLockHolderThread(gate: Gate): Thread

    class Gate
    {
        val latch = new java.util.concurrent.CountDownLatch(1)

        def open()
        {
            this.latch.countDown()
        }

        def await()
        {
            this.latch.await()
        }
    }

    def sleep(gate: Gate)
    {
        gate.open()

        try
        {
            Thread.sleep(java.lang.Long.MAX_VALUE)
        }
        catch
        {
            case ignore: InterruptedException => Thread.currentThread.interrupt
        }
    }

    def time(code: => Unit) =
    {
        val before = System.currentTimeMillis
        code
        val after = System.currentTimeMillis
        after - before
    }

    def spawn(code: => Unit) =
    {
        val result = new Thread
        {
            override def run = code
        }
        result.start()
        result
    }

    def assertReadersBlocked(code: => Unit)
    {
        this.assertReadSafety(threadSafe = true, 10L, TimeUnit.MILLISECONDS, code)
    }

    def assertReadersNotBlocked(code: => Unit)
    {
        this.assertReadSafety(threadSafe = false, 60L, TimeUnit.SECONDS, code)
    }

    def assertWritersBlocked(code: => Unit)
    {
        this.assertWriteSafety(threadSafe = true, 10L, TimeUnit.MILLISECONDS, code)
    }

    def assertWritersNotBlocked(code: => Unit)
    {
        this.assertWriteSafety(threadSafe = false, 60L, TimeUnit.SECONDS, code)
    }

    def assertReadSafety(threadSafe: Boolean, timeout: Long, timeUnit: TimeUnit, code: => Unit)
    {
        val gate = new Gate
        assertThreadSafety(timeout, timeUnit, gate, code, threadSafe, createReadLockHolderThread(gate))
    }

    def assertWriteSafety(threadSafe: Boolean, timeout: Long, timeUnit: TimeUnit, code: => Unit)
    {
        val gate = new Gate
        assertThreadSafety(timeout, timeUnit, gate, code, threadSafe, createWriteLockHolderThread(gate))
    }

    def assertThreadSafety(timeout: Long, timeUnit: TimeUnit, gate: ThreadSafetyTestTrait.this.type#Gate, code: => Unit, threadSafe: Boolean, lockHolderThread: Thread)
    {
        val millisTimeout = TimeUnit.MILLISECONDS.convert(timeout, timeUnit)
        val measuredTime = time
        {
            // Don't start until the other thread is synchronized on classUnderTest
            gate.await()
            spawn(code).join(millisTimeout, 0)
        }

        Assert.assertEquals(
            "Measured " + measuredTime + " ms but timeout was " + millisTimeout + " ms.",
            threadSafe,
            measuredTime >= millisTimeout)

        lockHolderThread.interrupt()
        lockHolderThread.join()
    }
}
