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

package com.gs.collections.impl.lazy.parallel.bag;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.ParallelBag;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.lazy.parallel.ParallelIterableTestCase;
import org.junit.Assert;
import org.junit.Test;

public abstract class ParallelBagTestCase extends ParallelIterableTestCase
{
    @Override
    protected abstract ParallelBag<Integer> classUnderTest();

    @Override
    protected abstract ParallelBag<Integer> newWith(Integer... littleElements);

    @Override
    protected MutableBag<Integer> getExpected()
    {
        return HashBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    }

    @Override
    protected MutableBag<Integer> getExpectedWith(Integer... littleElements)
    {
        return HashBag.newBagWith(littleElements);
    }

    @Override
    protected boolean isOrdered()
    {
        return false;
    }

    @Override
    protected boolean isUnique()
    {
        return false;
    }

    @Test
    public void forEachWithOccurrences()
    {
        MutableBag<Integer> actual = HashBag.<Integer>newBag().asSynchronized();
        this.classUnderTest().forEachWithOccurrences(actual::addOccurrences);
        Assert.assertEquals(this.getExpected().toBag(), actual);
    }

    @Override
    @Test
    public void forEach_executionException()
    {
        // Not applicable in serial
    }

    @Override
    @Test
    public void collect_executionException()
    {
        // Not applicable in serial
    }

    @Override
    @Test
    public void anySatisfy_executionException()
    {
        // Not applicable in serial
    }

    @Override
    @Test
    public void allSatisfy_executionException()
    {
        // Not applicable
    }

    @Override
    @Test
    public void detect_executionException()
    {
        // Not applicable in serial
    }

    @Override
    @Test
    public void forEach_interruptedException()
    {
        // Not applicable in serial
    }

    @Override
    @Test
    public void anySatisfy_interruptedException()
    {
        // Not applicable in serial
    }

    @Override
    @Test
    public void allSatisfy_interruptedException()
    {
        // Not applicable in serial
    }

    @Override
    @Test
    public void detect_interruptedException()
    {
        // Not applicable in serial
    }

    @Override
    @Test
    public void toString_interruptedException()
    {
        // Not applicable in serial
    }
}
