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

package com.gs.collections.impl.stack.mutable.primitive;

import com.gs.collections.impl.factory.primitive.BooleanStacks;
import com.gs.collections.impl.factory.primitive.ByteStacks;
import com.gs.collections.impl.factory.primitive.CharStacks;
import com.gs.collections.impl.factory.primitive.DoubleStacks;
import com.gs.collections.impl.factory.primitive.FloatStacks;
import com.gs.collections.impl.factory.primitive.IntStacks;
import com.gs.collections.impl.factory.primitive.LongStacks;
import com.gs.collections.impl.factory.primitive.ShortStacks;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

/**
 * JUnit test for empty() methods of primitive classes
 */
public class MutableEmptyPrimitiveTest
{
    @Test
    public void isEmptyMutable()
    {
        Verify.assertEmpty(BooleanStacks.mutable.empty());
        Verify.assertEmpty(BooleanStacks.mutable.of());
        Verify.assertEmpty(BooleanStacks.mutable.with());

        Verify.assertEmpty(ByteStacks.mutable.empty());
        Verify.assertEmpty(ByteStacks.mutable.of());
        Verify.assertEmpty(ByteStacks.mutable.with());

        Verify.assertEmpty(CharStacks.mutable.empty());
        Verify.assertEmpty(CharStacks.mutable.of());
        Verify.assertEmpty(CharStacks.mutable.with());

        Verify.assertEmpty(DoubleStacks.mutable.empty());
        Verify.assertEmpty(DoubleStacks.mutable.of());
        Verify.assertEmpty(DoubleStacks.mutable.with());

        Verify.assertEmpty(FloatStacks.mutable.empty());
        Verify.assertEmpty(FloatStacks.mutable.of());
        Verify.assertEmpty(FloatStacks.mutable.with());

        Verify.assertEmpty(IntStacks.mutable.empty());
        Verify.assertEmpty(IntStacks.mutable.of());
        Verify.assertEmpty(IntStacks.mutable.with());

        Verify.assertEmpty(LongStacks.mutable.empty());
        Verify.assertEmpty(LongStacks.mutable.of());
        Verify.assertEmpty(LongStacks.mutable.with());

        Verify.assertEmpty(ShortStacks.mutable.empty());
        Verify.assertEmpty(ShortStacks.mutable.of());
        Verify.assertEmpty(ShortStacks.mutable.with());
    }
}
