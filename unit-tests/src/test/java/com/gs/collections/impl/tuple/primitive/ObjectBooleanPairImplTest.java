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

package com.gs.collections.impl.tuple.primitive;

import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ObjectBooleanPairImpl}.
 */
public class ObjectBooleanPairImplTest
{
    @Test
    public void testEqualsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(PrimitiveTuples.pair("true", false), PrimitiveTuples.pair("true", false));
        Assert.assertNotEquals(PrimitiveTuples.pair("false", true), PrimitiveTuples.pair("true", false));
        Assert.assertEquals(Tuples.pair("true", false).hashCode(), PrimitiveTuples.pair("true", false).hashCode());
    }

    @Test
    public void getOne()
    {
        Assert.assertEquals("true", PrimitiveTuples.pair("true", false).getOne());
        Assert.assertEquals("false", PrimitiveTuples.pair("false", true).getOne());
    }

    @Test
    public void getTwo()
    {
        Assert.assertTrue(PrimitiveTuples.pair("false", true).getTwo());
        Assert.assertFalse(PrimitiveTuples.pair("true", false).getTwo());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("true:false", PrimitiveTuples.pair("true", false).toString());
        Assert.assertEquals("true:true", PrimitiveTuples.pair("true", true).toString());
    }

    @Test
    public void compareTo()
    {
        Assert.assertEquals(1, PrimitiveTuples.pair("true", true).compareTo(PrimitiveTuples.pair("true", false)));
        Assert.assertEquals(0, PrimitiveTuples.pair("true", false).compareTo(PrimitiveTuples.pair("true", false)));
        Assert.assertEquals(-1, PrimitiveTuples.pair("true", false).compareTo(PrimitiveTuples.pair("true", true)));
    }
}
