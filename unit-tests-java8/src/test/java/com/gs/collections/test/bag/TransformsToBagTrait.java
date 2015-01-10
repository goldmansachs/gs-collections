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

package com.gs.collections.test.bag;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.UnsortedBag;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.bag.primitive.MutableDoubleBag;
import com.gs.collections.api.bag.primitive.MutableFloatBag;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.bag.primitive.MutableLongBag;
import com.gs.collections.api.bag.primitive.MutableShortBag;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.test.RichIterableTestCase;

public interface TransformsToBagTrait extends RichIterableTestCase
{
    @Override
    default <T> UnsortedBag<T> getExpectedTransformed(T... elements)
    {
        return Bags.immutable.with(elements);
    }

    @Override
    default <T> MutableBag<T> newMutableForTransform(T... elements)
    {
        return Bags.mutable.with(elements);
    }

    @Override
    default MutableBooleanBag newBooleanForTransform(boolean... elements)
    {
        return new BooleanHashBag(elements);
    }

    @Override
    default MutableByteBag newByteForTransform(byte... elements)
    {
        return new ByteHashBag(elements);
    }

    @Override
    default MutableCharBag newCharForTransform(char... elements)
    {
        return new CharHashBag(elements);
    }

    @Override
    default MutableDoubleBag newDoubleForTransform(double... elements)
    {
        return new DoubleHashBag(elements);
    }

    @Override
    default MutableFloatBag newFloatForTransform(float... elements)
    {
        return new FloatHashBag(elements);
    }

    @Override
    default MutableIntBag newIntForTransform(int... elements)
    {
        return new IntHashBag(elements);
    }

    @Override
    default MutableLongBag newLongForTransform(long... elements)
    {
        return new LongHashBag(elements);
    }

    @Override
    default MutableShortBag newShortForTransform(short... elements)
    {
        return new ShortHashBag(elements);
    }
}
