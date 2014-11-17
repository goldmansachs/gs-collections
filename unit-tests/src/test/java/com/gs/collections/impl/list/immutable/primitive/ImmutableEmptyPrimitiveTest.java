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

package com.gs.collections.impl.list.immutable.primitive;

import com.gs.collections.impl.factory.primitive.BooleanLists;
import com.gs.collections.impl.factory.primitive.ByteLists;
import com.gs.collections.impl.factory.primitive.CharLists;
import com.gs.collections.impl.factory.primitive.DoubleLists;
import com.gs.collections.impl.factory.primitive.FloatLists;
import com.gs.collections.impl.factory.primitive.IntLists;
import com.gs.collections.impl.factory.primitive.LongLists;
import com.gs.collections.impl.factory.primitive.ShortLists;
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
        Verify.assertEmpty(BooleanLists.immutable.empty());
        Verify.assertEmpty(BooleanLists.immutable.of());
        Verify.assertEmpty(BooleanLists.immutable.with());

        Verify.assertEmpty(ByteLists.immutable.empty());
        Verify.assertEmpty(ByteLists.immutable.of());
        Verify.assertEmpty(ByteLists.immutable.with());

        Verify.assertEmpty(CharLists.immutable.empty());
        Verify.assertEmpty(CharLists.immutable.of());
        Verify.assertEmpty(CharLists.immutable.with());

        Verify.assertEmpty(DoubleLists.immutable.empty());
        Verify.assertEmpty(DoubleLists.immutable.of());
        Verify.assertEmpty(DoubleLists.immutable.with());

        Verify.assertEmpty(FloatLists.immutable.empty());
        Verify.assertEmpty(FloatLists.immutable.of());
        Verify.assertEmpty(FloatLists.immutable.with());

        Verify.assertEmpty(IntLists.immutable.empty());
        Verify.assertEmpty(IntLists.immutable.of());
        Verify.assertEmpty(IntLists.immutable.with());

        Verify.assertEmpty(LongLists.immutable.empty());
        Verify.assertEmpty(LongLists.immutable.of());
        Verify.assertEmpty(LongLists.immutable.with());

        Verify.assertEmpty(ShortLists.immutable.empty());
        Verify.assertEmpty(ShortLists.immutable.of());
        Verify.assertEmpty(ShortLists.immutable.with());
    }
}
