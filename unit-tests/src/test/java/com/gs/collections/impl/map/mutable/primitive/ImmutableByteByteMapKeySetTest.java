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

package com.gs.collections.impl.map.mutable.primitive;

import com.gs.collections.api.set.primitive.ByteSet;
import com.gs.collections.api.set.primitive.ImmutableByteSet;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.set.mutable.primitive.AbstractImmutableByteHashSetTestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableByteSet} created from the freeze() method.
 */
public class ImmutableByteByteMapKeySetTest extends AbstractImmutableByteHashSetTestCase
{
    protected static ByteArrayList generateCollisions()
    {
        ByteArrayList collisions = new ByteArrayList();
        ImmutableByteByteMapKeySet set = (ImmutableByteByteMapKeySet) new ByteByteHashMap().keySet().freeze();
        for (byte i = 2; collisions.size() <= 10; i++)
        {
            if (set.spreadAndMask(i) == set.spreadAndMask((byte) 2))
            {
                collisions.add(i);
            }
        }
        return collisions;
    }

    @Override
    protected ImmutableByteSet classUnderTest()
    {
        return (ImmutableByteSet) ByteByteHashMap.newWithKeysValues((byte) 1, (byte) -1, (byte) 2, (byte) 2, (byte) 3, (byte) 4).keySet().freeze();
    }

    @Override
    protected ImmutableByteSet newWith(byte... elements)
    {
        ByteByteHashMap byteByteHashMap = new ByteByteHashMap();
        for (byte element : elements)
        {
            byteByteHashMap.put(element, element);
        }
        return (ImmutableByteSet) byteByteHashMap.keySet().freeze();
    }

    @Test
    @Override
    public void contains()
    {
        super.contains();
        byte collision1 = ImmutableByteByteMapKeySetTest.generateCollisions().getFirst();
        byte collision2 = ImmutableByteByteMapKeySetTest.generateCollisions().get(1);
        ByteByteHashMap byteByteHashMap = ByteByteHashMap.newWithKeysValues(collision1, (byte) 0, collision2, (byte) 0);
        byteByteHashMap.removeKey(collision2);
        ByteSet byteSet = byteByteHashMap.keySet().freeze();
        Assert.assertTrue(byteSet.contains(collision1));
        Assert.assertFalse(byteSet.contains(collision2));
    }
}
