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

package com.gs.collections.impl.list.mutable.primitive;

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
public class MutableEmptyPrimitiveTest
{
    @Test
    public void isEmptyMutable()
    {
        Verify.assertEmpty(BooleanLists.mutable.empty());
        Verify.assertEmpty(BooleanLists.mutable.of());
        Verify.assertEmpty(BooleanLists.mutable.with());

        Verify.assertEmpty(ByteLists.mutable.empty());
        Verify.assertEmpty(ByteLists.mutable.of());
        Verify.assertEmpty(ByteLists.mutable.with());

        Verify.assertEmpty(CharLists.mutable.empty());
        Verify.assertEmpty(CharLists.mutable.of());
        Verify.assertEmpty(CharLists.mutable.with());

        Verify.assertEmpty(DoubleLists.mutable.empty());
        Verify.assertEmpty(DoubleLists.mutable.of());
        Verify.assertEmpty(DoubleLists.mutable.with());

        Verify.assertEmpty(FloatLists.mutable.empty());
        Verify.assertEmpty(FloatLists.mutable.of());
        Verify.assertEmpty(FloatLists.mutable.with());

        Verify.assertEmpty(IntLists.mutable.empty());
        Verify.assertEmpty(IntLists.mutable.of());
        Verify.assertEmpty(IntLists.mutable.with());

        Verify.assertEmpty(LongLists.mutable.empty());
        Verify.assertEmpty(LongLists.mutable.of());
        Verify.assertEmpty(LongLists.mutable.with());

        Verify.assertEmpty(ShortLists.mutable.empty());
        Verify.assertEmpty(ShortLists.mutable.of());
        Verify.assertEmpty(ShortLists.mutable.with());
    }
}
