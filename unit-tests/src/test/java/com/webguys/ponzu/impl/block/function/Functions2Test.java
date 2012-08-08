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

package com.webguys.ponzu.impl.block.function;

import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.block.factory.Functions2;
import org.junit.Assert;
import org.junit.Test;

public class Functions2Test
{
    @Test
    public void asFunction2Function()
    {
        Function2<Integer, Object, String> block = Functions2.fromFunction(Functions.getToString());
        Assert.assertEquals("1", block.value(1, null));
    }
}
