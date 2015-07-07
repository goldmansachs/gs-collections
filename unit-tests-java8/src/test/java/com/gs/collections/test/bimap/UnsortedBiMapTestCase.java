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

package com.gs.collections.test.bimap;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gs.collections.api.bimap.BiMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.test.bag.TransformsToBagTrait;
import com.gs.collections.test.set.UnsortedSetLikeTestTrait;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;

public interface UnsortedBiMapTestCase extends BiMapTestCase, TransformsToBagTrait, UnsortedSetLikeTestTrait
{
    @Override
    <T> BiMap<Object, T> newWith(T... elements);

    @Test
    @Override
    default void Iterable_remove()
    {
        BiMap<Object, Integer> iterable = this.newWith(3, 2, 1);
        Iterator<Integer> iterator = iterable.iterator();
        iterator.next();
        iterator.remove();
        assertEquals(2, iterable.size());
        MutableSet<Integer> valuesSet = iterable.inverse().keysView().toSet();
        assertThat(
                valuesSet,
                isOneOf(
                        Sets.immutable.with(3, 2),
                        Sets.immutable.with(3, 1),
                        Sets.immutable.with(2, 1)));
    }

    @Override
    @Test
    default void RichIterable_toString()
    {
        String string = this.newWith(3, 2, 1).toString();
        Pattern pattern = Pattern.compile("^\\{\\d\\.\\d+(E-\\d)?=(\\d),"
                + " \\d\\.\\d+(E-\\d)?=(\\d),"
                + " \\d\\.\\d+(E-\\d)?=(\\d)\\}$");
        Matcher matcher = pattern.matcher(string);
        Assert.assertTrue(string, matcher.matches());

        assertEquals(
                Bags.immutable.with("1", "2", "3"),
                Bags.immutable.with(
                        matcher.group(2),
                        matcher.group(4),
                        matcher.group(6)));
    }
}
