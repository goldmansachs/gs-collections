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

package com.gs.collections.impl.lazy.parallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gs.collections.api.ParallelIterable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractUnsupportedParallelIterableTestCase
{
    protected ExecutorService executorService;

    @Before
    public void setUp()
    {
        this.executorService = Executors.newFixedThreadPool(10);
        Assert.assertFalse(Thread.interrupted());
    }

    @After
    public void tearDown()
    {
        this.executorService.shutdownNow();
        Thread.interrupted();
    }

    protected abstract ParallelIterable<Integer> classUnderTest();

    @Test(expected = UnsupportedOperationException.class)
    public void forEach()
    {
        this.classUnderTest().forEach(x -> { });
    }
}
