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

package com.gs.collections.impl.test;

import org.junit.Assert;
import org.junit.Test;

public class SerializeTestHelperTest
{
    @Test
    public void serializeDeserializeSuccess()
    {
        String input = "Test";
        String output = SerializeTestHelper.serializeDeserialize(input);
        Assert.assertEquals(input, output);
        Assert.assertNotSame(input, output);
    }

    @Test
    public void serializeNotSerializable()
    {
        try
        {
            Object nonSerializable = new Object();
            SerializeTestHelper.serialize(nonSerializable);
        }
        catch (AssertionError ignored)
        {
            return;
        }

        Assert.fail();
    }

    @Test
    public void deserializeNonsense()
    {
        try
        {
            byte[] nonsenseByteArray = "Why is the man who invests all your money called a broker?".getBytes();
            SerializeTestHelper.deserialize(nonsenseByteArray);
        }
        catch (AssertionError ignored)
        {
            return;
        }

        Assert.fail();
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(SerializeTestHelper.class);
    }
}
