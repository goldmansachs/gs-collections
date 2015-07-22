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

package com.gs.collections.test.set.mutable;

import com.gs.collections.test.UnmodifiableMutableCollectionTestCase;
import com.gs.collections.test.set.UnmodifiableSetTestCase;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;

public interface UnmodifiableMutableSetTestCase extends UnmodifiableMutableCollectionTestCase, UnmodifiableSetTestCase, MutableSetTestCase
{
    @Override
    @Test
    default void Iterable_remove()
    {
        UnmodifiableSetTestCase.super.Iterable_remove();
    }

    @Override
    @Test
    default void MutableCollection_removeIf()
    {
        UnmodifiableMutableCollectionTestCase.super.MutableCollection_removeIf();
    }

    @Override
    @Test
    default void MutableCollection_removeIfWith()
    {
        UnmodifiableMutableCollectionTestCase.super.MutableCollection_removeIfWith();
    }
}
