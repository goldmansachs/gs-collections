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

package com.gs.collections.impl.set.fixed;

import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollectionTestCase;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class QuadrupletonSetAsUnmodifiableTest extends UnmodifiableMutableCollectionTestCase<String>
{
    @Override
    protected MutableCollection<String> getCollection()
    {
        return Sets.fixedSize.of("1", "2", "3", "4").asUnmodifiable();
    }

    @Override
    @Test
    public void collectBoolean()
    {
        Verify.assertSize(1, this.getCollection().collectBoolean(Boolean::parseBoolean));
    }

    @Override
    @Test
    public void collectByte()
    {
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectByte(Byte::parseByte));
    }

    @Override
    @Test
    public void collectChar()
    {
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectChar((CharFunction<String>) string -> string.charAt(0)));
    }

    @Override
    @Test
    public void collectDouble()
    {
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectDouble(Double::parseDouble));
    }

    @Override
    @Test
    public void collectFloat()
    {
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectFloat(Float::parseFloat));
    }

    @Override
    @Test
    public void collectInt()
    {
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectInt(Integer::parseInt));
    }

    @Override
    @Test
    public void collectLong()
    {
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectLong(Long::parseLong));
    }

    @Override
    @Test
    public void collectShort()
    {
        Verify.assertSize(this.getCollection().size(), this.getCollection().collectShort(Short::parseShort));
    }
}
