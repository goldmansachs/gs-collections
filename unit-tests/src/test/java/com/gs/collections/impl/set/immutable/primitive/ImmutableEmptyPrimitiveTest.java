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

package com.gs.collections.impl.set.immutable.primitive;

import com.gs.collections.impl.factory.primitive.BooleanSets;
import com.gs.collections.impl.factory.primitive.ByteSets;
import com.gs.collections.impl.factory.primitive.CharSets;
import com.gs.collections.impl.factory.primitive.DoubleSets;
import com.gs.collections.impl.factory.primitive.FloatSets;
import com.gs.collections.impl.factory.primitive.IntSets;
import com.gs.collections.impl.factory.primitive.LongSets;
import com.gs.collections.impl.factory.primitive.ShortSets;
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
        Verify.assertEmpty(BooleanSets.immutable.empty());
        Verify.assertEmpty(BooleanSets.immutable.of());
        Verify.assertEmpty(BooleanSets.immutable.with());

        Verify.assertEmpty(ByteSets.immutable.empty());
        Verify.assertEmpty(ByteSets.immutable.of());
        Verify.assertEmpty(ByteSets.immutable.with());

        Verify.assertEmpty(CharSets.immutable.empty());
        Verify.assertEmpty(CharSets.immutable.of());
        Verify.assertEmpty(CharSets.immutable.with());

        Verify.assertEmpty(DoubleSets.immutable.empty());
        Verify.assertEmpty(DoubleSets.immutable.of());
        Verify.assertEmpty(DoubleSets.immutable.with());

        Verify.assertEmpty(FloatSets.immutable.empty());
        Verify.assertEmpty(FloatSets.immutable.of());
        Verify.assertEmpty(FloatSets.immutable.with());

        Verify.assertEmpty(IntSets.immutable.empty());
        Verify.assertEmpty(IntSets.immutable.of());
        Verify.assertEmpty(IntSets.immutable.with());

        Verify.assertEmpty(LongSets.immutable.empty());
        Verify.assertEmpty(LongSets.immutable.of());
        Verify.assertEmpty(LongSets.immutable.with());

        Verify.assertEmpty(ShortSets.immutable.empty());
        Verify.assertEmpty(ShortSets.immutable.of());
        Verify.assertEmpty(ShortSets.immutable.with());
    }
}
