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

package com.gs.collections.impl.string.immutable;

import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.list.primitive.ImmutableCharList;
import com.gs.collections.impl.factory.primitive.CharBags;
import com.gs.collections.impl.list.immutable.primitive.AbstractImmutableCharListTestCase;
import org.junit.Assert;

public class CharAdapterTest extends AbstractImmutableCharListTestCase
{
    @Override
    protected ImmutableCharList classUnderTest()
    {
        return CharAdapter.build((char) 1, (char) 2, (char) 3);
    }

    @Override
    protected ImmutableCharList newWith(char... elements)
    {
        return CharAdapter.build(elements);
    }

    @Override
    public void toBag()
    {
        super.toBag();
        MutableCharBag expected = CharBags.mutable.empty();
        expected.addOccurrences('a', 3);
        expected.addOccurrences('b', 3);
        expected.addOccurrences('c', 3);
        Assert.assertEquals(expected, CharAdapter.adapt("aaabbbccc").toBag());
    }
}
