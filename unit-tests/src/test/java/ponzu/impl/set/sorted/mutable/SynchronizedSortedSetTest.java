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

package ponzu.impl.set.sorted.mutable;

import java.util.TreeSet;

import ponzu.api.collection.MutableCollection;
import ponzu.api.set.sorted.MutableSortedSet;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.collection.mutable.AbstractSynchronizedCollectionTestCase;
import ponzu.impl.test.Verify;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedSortedSet}.
 */
public class SynchronizedSortedSetTest extends AbstractSynchronizedCollectionTestCase
{
    @Override
    protected <T> MutableSortedSet<T> classUnderTest()
    {
        return new SynchronizedSortedSet<T>(SortedSetAdapter.adapt(new TreeSet<T>()));
    }

    @Override
    @Test
    public void newEmpty()
    {
        super.newEmpty();

        Verify.assertInstanceOf(TreeSortedSet.class, classUnderTest().newEmpty());
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
        Verify.assertInstanceOf(UnmodifiableSortedSet.class, this.classUnderTest().asUnmodifiable());
    }
}
