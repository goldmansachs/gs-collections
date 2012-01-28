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

package ponzu.impl.block.function;

import org.junit.Assert;
import org.junit.Test;
import ponzu.impl.bag.mutable.HashBag;
import ponzu.impl.block.factory.Generators;
import ponzu.impl.factory.Bags;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.test.Verify;

public class Functions0Test
{
    @Test
    public void newFastList()
    {
        Assert.assertEquals(Lists.mutable.of(), Generators.newFastList().value());
        Verify.assertInstanceOf(FastList.class, Generators.newFastList().value());
    }

    @Test
    public void newUnifiedSet()
    {
        Assert.assertEquals(UnifiedSet.newSet(), Generators.newUnifiedSet().value());
        Verify.assertInstanceOf(UnifiedSet.class, Generators.newUnifiedSet().value());
    }

    @Test
    public void newHashBag()
    {
        Assert.assertEquals(Bags.mutable.of(), Generators.newHashBag().value());
        Verify.assertInstanceOf(HashBag.class, Generators.newHashBag().value());
    }

    @Test
    public void newUnifiedMap()
    {
        Assert.assertEquals(UnifiedMap.newMap(), Generators.newUnifiedMap().value());
        Verify.assertInstanceOf(UnifiedMap.class, Generators.newUnifiedMap().value());
    }
}
