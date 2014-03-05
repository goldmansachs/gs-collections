/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl;

import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.primitive.CharToCharFunctions;
import com.gs.collections.impl.block.factory.primitive.IntToIntFunctions;
import com.gs.collections.impl.block.function.MaxFunction;
import com.gs.collections.impl.block.function.MaxSizeFunction;
import com.gs.collections.impl.block.function.MinFunction;
import com.gs.collections.impl.block.function.MinSizeFunction;
import com.gs.collections.impl.block.function.SubtractFunction;
import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.factory.primitive.BooleanSets;
import com.gs.collections.impl.factory.primitive.BooleanStacks;
import com.gs.collections.impl.factory.primitive.ByteBooleanMaps;
import com.gs.collections.impl.factory.primitive.CharBooleanMaps;
import com.gs.collections.impl.factory.primitive.DoubleBooleanMaps;
import com.gs.collections.impl.factory.primitive.FloatBooleanMaps;
import com.gs.collections.impl.factory.primitive.IntBooleanMaps;
import com.gs.collections.impl.factory.primitive.LongBooleanMaps;
import com.gs.collections.impl.factory.primitive.ObjectBooleanMaps;
import com.gs.collections.impl.factory.primitive.ShortBooleanMaps;
import com.gs.collections.impl.parallel.Combiners;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.primitive.PrimitiveTuples;
import com.gs.collections.impl.utility.internal.InternalArrayIterate;
import com.gs.collections.impl.utility.internal.IteratorIterate;
import com.gs.collections.impl.utility.internal.MutableCollectionIterate;
import com.gs.collections.impl.utility.internal.ReflectionHelper;
import com.gs.collections.impl.utility.internal.SetIterables;
import com.gs.collections.impl.utility.internal.SetIterate;
import com.gs.collections.impl.utility.internal.SortedSetIterables;
import com.gs.collections.impl.utility.internal.primitive.BooleanIterableIterate;
import com.gs.collections.impl.utility.internal.primitive.BooleanIteratorIterate;
import com.gs.collections.impl.utility.primitive.LazyBooleanIterate;
import org.junit.Test;

public class NonInstantiableTest
{
    // TODO: Move the test for each of these classes into appropriate generated test classes
    private static final Class<?>[] GENERATED_NON_INSTANTIABLE_CLASSES =
            {
                    BooleanStacks.class,
                    ByteBooleanMaps.class,
                    CharBooleanMaps.class,
                    DoubleBooleanMaps.class,
                    FloatBooleanMaps.class,
                    IntBooleanMaps.class,
                    LongBooleanMaps.class,
                    ShortBooleanMaps.class,
                    ObjectBooleanMaps.class,
                    BooleanIterableIterate.class,
                    BooleanIteratorIterate.class,
                    LazyBooleanIterate.class,
            };

    private static final Class<?>[] HAND_CODED_NON_INSTANTIABLE_CLASSES = // With no dedicated test class
            {
                    PrimitiveFunctions.class,

                    CharToCharFunctions.class,
                    IntToIntFunctions.class,

                    MaxFunction.class,
                    MaxSizeFunction.class,
                    MinFunction.class,
                    MinSizeFunction.class,
                    SubtractFunction.class,

                    BooleanSets.class,

                    Combiners.class,

                    PrimitiveTuples.class,

                    InternalArrayIterate.class,
                    IteratorIterate.class,
                    MutableCollectionIterate.class,
                    ReflectionHelper.class,
                    SetIterables.class,
                    SetIterate.class,
                    SortedMaps.class,
                    SortedSetIterables.class
            };

    @Test
    public void generatedNonInstantiableClassesThrow()
    {
        for (Class<?> aClass : GENERATED_NON_INSTANTIABLE_CLASSES)
        {
            Verify.assertClassNonInstantiable(aClass);
        }
    }

    @Test
    public void handCodedNonInstantiableClassesThrow()
    {
        for (Class<?> aClass : HAND_CODED_NON_INSTANTIABLE_CLASSES)
        {
            Verify.assertClassNonInstantiable(aClass);
        }
    }
}
