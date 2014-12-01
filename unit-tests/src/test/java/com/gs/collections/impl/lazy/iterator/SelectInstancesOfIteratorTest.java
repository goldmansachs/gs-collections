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

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class SelectInstancesOfIteratorTest
{
    @Test
    public void iterator()
    {
        MutableList<Number> list = FastList.<Number>newListWith(null, 1, 2.0, null, 3, 4.0, 5, null);
        this.assertElements(new SelectInstancesOfIterator<>(list.iterator(), Integer.class));
        this.assertElements(new SelectInstancesOfIterator<>(list, Integer.class));
    }

    private void assertElements(Iterator<Integer> iterator)
    {
        MutableList<Integer> result = FastList.newList();
        while (iterator.hasNext())
        {
            result.add(iterator.next());
        }
        Assert.assertEquals(FastList.newListWith(1, 3, 5), result);
    }

    @Test
    public void noSuchElementException()
    {
        Verify.assertThrows(NoSuchElementException.class, () -> new SelectInstancesOfIterator<>(Lists.fixedSize.of(), Object.class).next());
    }

    @Test
    public void remove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> new SelectInstancesOfIterator<>(Lists.fixedSize.of(), Object.class).remove());
    }
}
