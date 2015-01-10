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

package com.gs.collections.test.stack;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.list.primitive.MutableDoubleList;
import com.gs.collections.api.list.primitive.MutableFloatList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.stack.StackIterable;
import com.gs.collections.api.stack.primitive.BooleanStack;
import com.gs.collections.api.stack.primitive.ByteStack;
import com.gs.collections.api.stack.primitive.CharStack;
import com.gs.collections.api.stack.primitive.DoubleStack;
import com.gs.collections.api.stack.primitive.FloatStack;
import com.gs.collections.api.stack.primitive.IntStack;
import com.gs.collections.api.stack.primitive.LongStack;
import com.gs.collections.api.stack.primitive.ShortStack;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.impl.factory.primitive.BooleanLists;
import com.gs.collections.impl.factory.primitive.BooleanStacks;
import com.gs.collections.impl.factory.primitive.ByteLists;
import com.gs.collections.impl.factory.primitive.ByteStacks;
import com.gs.collections.impl.factory.primitive.CharLists;
import com.gs.collections.impl.factory.primitive.CharStacks;
import com.gs.collections.impl.factory.primitive.DoubleLists;
import com.gs.collections.impl.factory.primitive.DoubleStacks;
import com.gs.collections.impl.factory.primitive.FloatLists;
import com.gs.collections.impl.factory.primitive.FloatStacks;
import com.gs.collections.impl.factory.primitive.IntLists;
import com.gs.collections.impl.factory.primitive.IntStacks;
import com.gs.collections.impl.factory.primitive.LongLists;
import com.gs.collections.impl.factory.primitive.LongStacks;
import com.gs.collections.impl.factory.primitive.ShortLists;
import com.gs.collections.impl.factory.primitive.ShortStacks;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.test.RichIterableTestCase;

public interface TransformsToStackTrait extends RichIterableTestCase
{
    @Override
    default <T> StackIterable<T> getExpectedTransformed(T... elements)
    {
        return Stacks.immutable.withReversed(elements);
    }

    @Override
    default <T> MutableList<T> newMutableForTransform(T... elements)
    {
        return Lists.mutable.with(elements);
    }

    @Override
    default MutableBooleanList newBooleanForTransform(boolean... elements)
    {
        return new BooleanArrayList(elements);
    }

    @Override
    default MutableByteList newByteForTransform(byte... elements)
    {
        return new ByteArrayList(elements);
    }

    @Override
    default MutableCharList newCharForTransform(char... elements)
    {
        return new CharArrayList(elements);
    }

    @Override
    default MutableDoubleList newDoubleForTransform(double... elements)
    {
        return new DoubleArrayList(elements);
    }

    @Override
    default MutableFloatList newFloatForTransform(float... elements)
    {
        return new FloatArrayList(elements);
    }

    @Override
    default MutableIntList newIntForTransform(int... elements)
    {
        return new IntArrayList(elements);
    }

    @Override
    default MutableLongList newLongForTransform(long... elements)
    {
        return new LongArrayList(elements);
    }

    @Override
    default MutableShortList newShortForTransform(short... elements)
    {
        return new ShortArrayList(elements);
    }

    @Override
    default BooleanStack getExpectedBoolean(boolean... elements)
    {
        return BooleanStacks.immutable.withAllReversed(BooleanLists.immutable.with(elements));
    }

    @Override
    default ByteStack getExpectedByte(byte... elements)
    {
        return ByteStacks.immutable.withAllReversed(ByteLists.immutable.with(elements));
    }

    @Override
    default CharStack getExpectedChar(char... elements)
    {
        return CharStacks.immutable.withAllReversed(CharLists.immutable.with(elements));
    }

    @Override
    default DoubleStack getExpectedDouble(double... elements)
    {
        return DoubleStacks.immutable.withAllReversed(DoubleLists.immutable.with(elements));
    }

    @Override
    default FloatStack getExpectedFloat(float... elements)
    {
        return FloatStacks.immutable.withAllReversed(FloatLists.immutable.with(elements));
    }

    @Override
    default IntStack getExpectedInt(int... elements)
    {
        return IntStacks.immutable.withAllReversed(IntLists.immutable.with(elements));
    }

    @Override
    default LongStack getExpectedLong(long... elements)
    {
        return LongStacks.immutable.withAllReversed(LongLists.immutable.with(elements));
    }

    @Override
    default ShortStack getExpectedShort(short... elements)
    {
        return ShortStacks.immutable.withAllReversed(ShortLists.immutable.with(elements));
    }
}
