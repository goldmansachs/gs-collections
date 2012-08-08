/*
 * Copyright 2011 Goldman Sachs.
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

package com.webguys.ponzu.impl.set.mutable;

import java.util.TreeSet;

import com.webguys.ponzu.api.collection.MutableCollection;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.collection.mutable.AbstractSynchronizedCollectionTestCase;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedMutableSet}.
 */
public class SynchronizedMutableSetTest extends AbstractSynchronizedCollectionTestCase
{
    @Override
    protected <T> MutableSet<T> classUnderTest()
    {
        return new SynchronizedMutableSet<T>(SetAdapter.adapt(new TreeSet<T>()));
    }

    @Override
    @Test
    public void newEmpty()
    {
        super.newEmpty();

        Verify.assertInstanceOf(UnifiedSet.class, this.classUnderTest().newEmpty());
    }

    @Override
    @Test
    public void remove()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3, 4);
        objects.removeIf(Predicates.equal(2));
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 3, 4);
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableSet.class, this.classUnderTest().asUnmodifiable());
    }
}
