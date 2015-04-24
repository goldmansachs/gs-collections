/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.block.function.primitive;

import org.junit.Assert;
import org.junit.Test;

/**
 * Junit test for {@link CharFunction}.
 *
 * @deprecated in 6.2
 */
@Deprecated
public class CharFunctionTest
{
    @Test
    public void toUppercase()
    {
        Assert.assertEquals('A', CharFunction.TO_UPPERCASE.valueOf('a'));
        Assert.assertEquals('A', CharFunction.TO_UPPERCASE.valueOf('A'));
        Assert.assertEquals('1', CharFunction.TO_UPPERCASE.valueOf('1'));
    }

    @Test
    public void toLowercase()
    {
        Assert.assertEquals('a', CharFunction.TO_LOWERCASE.valueOf('a'));
        Assert.assertEquals('a', CharFunction.TO_LOWERCASE.valueOf('A'));
        Assert.assertEquals('1', CharFunction.TO_LOWERCASE.valueOf('1'));
    }
}
