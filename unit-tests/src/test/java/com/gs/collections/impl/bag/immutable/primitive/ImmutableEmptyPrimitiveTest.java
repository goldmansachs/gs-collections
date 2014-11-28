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

package com.gs.collections.impl.bag.immutable.primitive;

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
public class ImmutableEmptyPrimitiveTest
{
    @Test
    public void isEmptyImmutable()
    {
        Verify.assertEmpty(BooleanBags.immutable.empty());
        Verify.assertEmpty(BooleanBags.immutable.of());
        Verify.assertEmpty(BooleanBags.immutable.with());

        Verify.assertEmpty(ByteBags.immutable.empty());
        Verify.assertEmpty(ByteBags.immutable.of());
        Verify.assertEmpty(ByteBags.immutable.with());

        Verify.assertEmpty(CharBags.immutable.empty());
        Verify.assertEmpty(CharBags.immutable.of());
        Verify.assertEmpty(CharBags.immutable.with());

        Verify.assertEmpty(DoubleBags.immutable.empty());
        Verify.assertEmpty(DoubleBags.immutable.of());
        Verify.assertEmpty(DoubleBags.immutable.with());

        Verify.assertEmpty(FloatBags.immutable.empty());
        Verify.assertEmpty(FloatBags.immutable.of());
        Verify.assertEmpty(FloatBags.immutable.with());

        Verify.assertEmpty(IntBags.immutable.empty());
        Verify.assertEmpty(IntBags.immutable.of());
        Verify.assertEmpty(IntBags.immutable.with());

        Verify.assertEmpty(LongBags.immutable.empty());
        Verify.assertEmpty(LongBags.immutable.of());
        Verify.assertEmpty(LongBags.immutable.with());

        Verify.assertEmpty(ShortBags.immutable.empty());
        Verify.assertEmpty(ShortBags.immutable.of());
        Verify.assertEmpty(ShortBags.immutable.with());
    }
}
