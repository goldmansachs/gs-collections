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

package com.gs.collections.impl.set.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class PrimitiveProbeSequenceTest
{
    @Test
    public void intProbeSequence()
    {
        int noOfCollisions = 30000;
        IntHashSet hashSet = new IntHashSet(noOfCollisions);

        int count = 0;
        for (int i = 32; count < noOfCollisions; i++)
        {
            if (hashSet.index(32) == hashSet.index(i))
            {
                Assert.assertTrue(hashSet.add(i));
                count++;
            }
        }
        Verify.assertSize(noOfCollisions, hashSet);
    }

    @Test
    public void longProbeSequence()
    {
        int noOfCollisions = 50000;
        LongHashSet hashSet = new LongHashSet(noOfCollisions);

        int count = 0;

        for (long l = 32L; count < noOfCollisions; l++)
        {
            if (hashSet.index(32L) == hashSet.index(l))
            {
                Assert.assertTrue(hashSet.add(l));
                count++;
            }
        }
        Verify.assertSize(noOfCollisions, hashSet);
    }

    @Test
    public void doubleProbeSequence()
    {
        int noOfCollisions = 25000;
        DoubleHashSet hashSet = new DoubleHashSet(noOfCollisions);

        int count = 0;
        for (double d = 32.0; count < noOfCollisions; d = Math.nextUp(d))
        {
            if (hashSet.index(50.0) == hashSet.index(d))
            {
                Assert.assertTrue(hashSet.add(d));
                count++;
            }
        }
        Verify.assertSize(noOfCollisions, hashSet);
    }

    @Test
    public void floatProbeSequence()
    {
        int noOfCollisions = 15000;
        FloatHashSet hashSet = new FloatHashSet(noOfCollisions);

        int count = 0;
        for (float f = 32.0f; count < noOfCollisions; f = Math.nextUp(f))
        {
            if (hashSet.index(32.0f) == hashSet.index(f))
            {
                Assert.assertTrue(hashSet.add(f));
                count++;
            }
        }
        Verify.assertSize(noOfCollisions, hashSet);
    }

    @Test
    public void charProbeSequence()
    {
        int noOfCollisions = 132;
        CharHashSet hashSet = new CharHashSet(noOfCollisions);

        int count = 0;
        for (char c = (char) 32; count < noOfCollisions; c++)
        {
            if (hashSet.index((char) 32) == hashSet.index(c))
            {
                Assert.assertTrue(hashSet.add(c));
                count++;
            }
        }
        Verify.assertSize(noOfCollisions, hashSet);
    }
}
