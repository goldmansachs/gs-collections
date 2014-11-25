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

package com.gs.collections.impl.set.mutable.primitive;

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
public class MutableEmptyPrimitiveTest
{
    @Test
    public void isEmptyMutable()
    {
        Verify.assertEmpty(BooleanSets.mutable.empty());
        Verify.assertEmpty(BooleanSets.mutable.of());
        Verify.assertEmpty(BooleanSets.mutable.with());

        Verify.assertEmpty(ByteSets.mutable.empty());
        Verify.assertEmpty(ByteSets.mutable.of());
        Verify.assertEmpty(ByteSets.mutable.with());

        Verify.assertEmpty(CharSets.mutable.empty());
        Verify.assertEmpty(CharSets.mutable.of());
        Verify.assertEmpty(CharSets.mutable.with());

        Verify.assertEmpty(DoubleSets.mutable.empty());
        Verify.assertEmpty(DoubleSets.mutable.of());
        Verify.assertEmpty(DoubleSets.mutable.with());

        Verify.assertEmpty(FloatSets.mutable.empty());
        Verify.assertEmpty(FloatSets.mutable.of());
        Verify.assertEmpty(FloatSets.mutable.with());

        Verify.assertEmpty(IntSets.mutable.empty());
        Verify.assertEmpty(IntSets.mutable.of());
        Verify.assertEmpty(IntSets.mutable.with());

        Verify.assertEmpty(LongSets.mutable.empty());
        Verify.assertEmpty(LongSets.mutable.of());
        Verify.assertEmpty(LongSets.mutable.with());

        Verify.assertEmpty(ShortSets.mutable.empty());
        Verify.assertEmpty(ShortSets.mutable.of());
        Verify.assertEmpty(ShortSets.mutable.with());
    }
}
