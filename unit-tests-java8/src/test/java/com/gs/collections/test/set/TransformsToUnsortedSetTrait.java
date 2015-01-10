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

package com.gs.collections.test.set;

import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.api.set.primitive.MutableByteSet;
import com.gs.collections.api.set.primitive.MutableCharSet;
import com.gs.collections.api.set.primitive.MutableDoubleSet;
import com.gs.collections.api.set.primitive.MutableFloatSet;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.api.set.primitive.MutableLongSet;
import com.gs.collections.api.set.primitive.MutableShortSet;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.set.mutable.primitive.ByteHashSet;
import com.gs.collections.impl.set.mutable.primitive.CharHashSet;
import com.gs.collections.impl.set.mutable.primitive.DoubleHashSet;
import com.gs.collections.impl.set.mutable.primitive.FloatHashSet;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import com.gs.collections.impl.set.mutable.primitive.LongHashSet;
import com.gs.collections.impl.set.mutable.primitive.ShortHashSet;
import com.gs.collections.test.RichIterableTestCase;

// TODO Collected sets should return bags
public interface TransformsToUnsortedSetTrait extends RichIterableTestCase
{
    @Override
    default <T> UnsortedSetIterable<T> getExpectedTransformed(T... elements)
    {
        return Sets.immutable.with(elements);
    }

    @Override
    default <T> MutableSet<T> newMutableForTransform(T... elements)
    {
        return Sets.mutable.with(elements);
    }

    @Override
    default MutableBooleanSet newBooleanForTransform(boolean... elements)
    {
        return new BooleanHashSet(elements);
    }

    @Override
    default MutableByteSet newByteForTransform(byte... elements)
    {
        return new ByteHashSet(elements);
    }

    @Override
    default MutableCharSet newCharForTransform(char... elements)
    {
        return new CharHashSet(elements);
    }

    @Override
    default MutableDoubleSet newDoubleForTransform(double... elements)
    {
        return new DoubleHashSet(elements);
    }

    @Override
    default MutableFloatSet newFloatForTransform(float... elements)
    {
        return new FloatHashSet(elements);
    }

    @Override
    default MutableIntSet newIntForTransform(int... elements)
    {
        return new IntHashSet(elements);
    }

    @Override
    default MutableLongSet newLongForTransform(long... elements)
    {
        return new LongHashSet(elements);
    }

    @Override
    default MutableShortSet newShortForTransform(short... elements)
    {
        return new ShortHashSet(elements);
    }
}
