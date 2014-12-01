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

package com.gs.collections.impl.lazy.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.impl.factory.Lists;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public class CollectIteratorTest
{
    @Test
    public void iterator()
    {
        Iterator<String> iterator = new CollectIterator<>(iList(Boolean.TRUE), String::valueOf);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("true", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void iteratorWithFunctionName()
    {
        Iterator<String> iterator = new CollectIterator<>(iList(Boolean.TRUE), String::valueOf);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("true", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void iteratorWithFunctionNameAndIterator()
    {
        Iterator<String> iterator = new CollectIterator<>(iList(Boolean.TRUE).iterator(), String::valueOf);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("true", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void noSuchElementException()
    {
        new CollectIterator<>(Lists.mutable.<Boolean>of(), String::valueOf).next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove()
    {
        new CollectIterator<>(Lists.mutable.<Boolean>of(), String::valueOf).remove();
    }
}
