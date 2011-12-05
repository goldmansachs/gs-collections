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

package com.gs.collections.impl.set.mutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class UnifiedSetAsPoolTest
{
    private final UnifiedSet<Integer> staticPool = UnifiedSet.newSet();

    @Test
    public void getReturnsNullIfObjectIsNotPooled()
    {
        Assert.assertNull(this.staticPool.get(1));
    }

    @Test
    public void getReturnsOriginalObjectForIdenticalObject()
    {
        Integer firstPooledObject = 1;
        this.staticPool.put(firstPooledObject);
        Assert.assertSame(firstPooledObject, this.staticPool.get(firstPooledObject));
    }

    @Test
    public void getReturnsPooledObjectForEqualObject()
    {
        UnifiedSet<AlwaysEqual> pool = UnifiedSet.newSet();
        AlwaysEqual firstObject = new AlwaysEqual();
        pool.put(firstObject);
        AlwaysEqual equalObject = new AlwaysEqual();  // deliberate new instance
        Assert.assertSame(firstObject, pool.get(equalObject));
    }

    private static final class AlwaysEqual
    {
        @Override
        public boolean equals(Object obj)
        {
            return obj != null;
        }

        @Override
        public int hashCode()
        {
            return 0;
        }
    }

    @Test
    public void putReturnsPassedInObject()
    {
        Integer firstObject = 1;
        Object returnedObject = this.staticPool.put(firstObject);
        Assert.assertSame(returnedObject, firstObject);
    }

    @Test
    public void putAndGetReturnOriginalPooledObjectForEqualObject()
    {
        AlwaysEqual firstObject = new AlwaysEqual();
        UnifiedSet<AlwaysEqual> pool = UnifiedSet.newSet();
        pool.put(firstObject);
        AlwaysEqual secondObject = new AlwaysEqual();  // deliberate new instance
        Object returnedObject = pool.put(secondObject);

        Assert.assertSame(returnedObject, firstObject);
        Assert.assertSame(firstObject, pool.get(secondObject));
    }

    @Test
    public void removeFromPool()
    {
        Integer firstObject = 1;

        this.staticPool.put(firstObject);
        Integer returnedObject = this.staticPool.removeFromPool(firstObject);

        Assert.assertSame(returnedObject, firstObject);
        Verify.assertEmpty(this.staticPool);
    }
}
