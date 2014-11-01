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

package com.gs.collections.impl.block.function;

import java.io.IOException;

import com.gs.collections.api.block.function.Function2;
import com.gs.collections.impl.block.factory.Functions2;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class Functions2Test
{
    @Test
    public void throwing()
    {
        Verify.assertThrowsWithCause(
                RuntimeException.class,
                IOException.class,
                () -> Functions2.throwing((a, b) -> { throw new IOException(); }).value(null, null));
    }

    @Test
    public void asFunction2Function()
    {
        Function2<Integer, Object, String> block = Functions2.fromFunction(String::valueOf);
        Assert.assertEquals("1", block.value(1, null));
    }

    @Test
    public void plusInteger()
    {
        Function2<Integer, Integer, Integer> plusInteger = Functions2.integerAddition();
        Assert.assertEquals(Integer.valueOf(5), plusInteger.value(2, 3));
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(Functions2.class);
    }
}
