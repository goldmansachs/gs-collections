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

package com.gs.collections.impl.bag.mutable.primitive;

import com.gs.collections.impl.factory.primitive.BooleanBags;
import com.gs.collections.impl.factory.primitive.ByteBags;
import com.gs.collections.impl.factory.primitive.CharBags;
import com.gs.collections.impl.factory.primitive.DoubleBags;
import com.gs.collections.impl.factory.primitive.FloatBags;
import com.gs.collections.impl.factory.primitive.IntBags;
import com.gs.collections.impl.factory.primitive.LongBags;
import com.gs.collections.impl.factory.primitive.ShortBags;
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
        Verify.assertEmpty(BooleanBags.mutable.empty());
        Verify.assertEmpty(BooleanBags.mutable.of());
        Verify.assertEmpty(BooleanBags.mutable.with());

        Verify.assertEmpty(ByteBags.mutable.empty());
        Verify.assertEmpty(ByteBags.mutable.of());
        Verify.assertEmpty(ByteBags.mutable.with());

        Verify.assertEmpty(CharBags.mutable.empty());
        Verify.assertEmpty(CharBags.mutable.of());
        Verify.assertEmpty(CharBags.mutable.with());

        Verify.assertEmpty(DoubleBags.mutable.empty());
        Verify.assertEmpty(DoubleBags.mutable.of());
        Verify.assertEmpty(DoubleBags.mutable.with());

        Verify.assertEmpty(FloatBags.mutable.empty());
        Verify.assertEmpty(FloatBags.mutable.of());
        Verify.assertEmpty(FloatBags.mutable.with());

        Verify.assertEmpty(IntBags.mutable.empty());
        Verify.assertEmpty(IntBags.mutable.of());
        Verify.assertEmpty(IntBags.mutable.with());

        Verify.assertEmpty(LongBags.mutable.empty());
        Verify.assertEmpty(LongBags.mutable.of());
        Verify.assertEmpty(LongBags.mutable.with());

        Verify.assertEmpty(ShortBags.mutable.empty());
        Verify.assertEmpty(ShortBags.mutable.of());
        Verify.assertEmpty(ShortBags.mutable.with());
    }
}
