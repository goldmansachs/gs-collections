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

package com.gs.collections.test.map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.UnsortedBag;
import com.gs.collections.api.map.UnsortedMapIterable;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.test.UnorderedIterableTestCase;
import com.gs.collections.test.bag.TransformsToBagTrait;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;

public interface UnsortedMapIterableTestCase extends MapIterableTestCase, UnorderedIterableTestCase, TransformsToBagTrait
{
    @Override
    <T> UnsortedMapIterable<Object, T> newWith(T... elements);

    @Override
    default <T> UnsortedBag<T> getExpectedFiltered(T... elements)
    {
        return Bags.immutable.with(elements);
    }

    @Override
    default <T> MutableBag<T> newMutableForFilter(T... elements)
    {
        return Bags.mutable.with(elements);
    }

    @Override
    @Test
    default void RichIterable_makeString_appendString()
    {
        RichIterable<Integer> iterable = this.newWith(2, 2, 1);
        assertThat(iterable.makeString(), isOneOf("2, 2, 1", "1, 2, 2", "2, 1, 2"));
        assertThat(iterable.makeString("/"), isOneOf("2/2/1", "1/2/2", "2/1/2"));
        assertThat(iterable.makeString("[", "/", "]"), isOneOf("[2/2/1]", "[1/2/2]", "[2/1/2]"));

        StringBuilder builder1 = new StringBuilder();
        iterable.appendString(builder1);
        assertThat(builder1.toString(), isOneOf("2, 2, 1", "1, 2, 2", "2, 1, 2"));

        StringBuilder builder2 = new StringBuilder();
        iterable.appendString(builder2, "/");
        assertThat(builder2.toString(), isOneOf("2/2/1", "1/2/2", "2/1/2"));

        StringBuilder builder3 = new StringBuilder();
        iterable.appendString(builder3, "[", "/", "]");
        assertThat(builder3.toString(), isOneOf("[2/2/1]", "[1/2/2]", "[2/1/2]"));
    }

    @Override
    @Test
    default void RichIterable_toString()
    {
        String string = this.newWith(2, 2, 1).toString();
        Pattern pattern = Pattern.compile("^\\{\\d\\.\\d+(E-\\d)?=(\\d),"
                + " \\d\\.\\d+(E-\\d)?=(\\d),"
                + " \\d\\.\\d+(E-\\d)?=(\\d)\\}$");
        Matcher matcher = pattern.matcher(string);
        Assert.assertTrue(string, matcher.matches());

        assertEquals(
                Bags.immutable.with("1", "2", "2"),
                Bags.immutable.with(
                        matcher.group(2),
                        matcher.group(4),
                        matcher.group(6)));
    }

    @Override
    @Test
    default void RichIterable_toList()
    {
        assertThat(
                this.newWith(2, 2, 1).toList(),
                isOneOf(
                        Lists.immutable.with(2, 2, 1),
                        Lists.immutable.with(1, 2, 2),
                        Lists.immutable.with(2, 1, 2)));
    }
}
