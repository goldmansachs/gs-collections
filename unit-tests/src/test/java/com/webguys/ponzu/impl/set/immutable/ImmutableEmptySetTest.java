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

package com.webguys.ponzu.impl.set.immutable;

import com.webguys.ponzu.api.set.ImmutableSet;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.impl.factory.Sets;
import com.webguys.ponzu.impl.list.Interval;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.set.mutable.UnifiedSet;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableEmptySetTest extends AbstractImmutableEmptySetTestCase
{
    @Override
    protected ImmutableSet<Integer> classUnderTest()
    {
        return Sets.immutable.of();
    }

    @Override
    @Test
    public void testNewWithout()
    {
        Assert.assertSame(Sets.immutable.of(), Sets.immutable.of().newWithout(1));
        Assert.assertSame(Sets.immutable.of(), Sets.immutable.of().newWithoutAll(Interval.oneTo(3)));
    }

    @Override
    @Test
    public void testEqualsAndHashCode()
    {
        ImmutableSet<Integer> immutable = this.classUnderTest();
        MutableSet<Integer> mutable = UnifiedSet.newSet(immutable);
        Verify.assertEqualsAndHashCode(mutable, immutable);
        Verify.assertPostSerializedIdentity(immutable);
        Verify.assertNotEquals(FastList.newList(mutable), immutable);
    }
}
