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

package ponzu.impl.lazy.iterator;

import java.util.List;
import java.util.NoSuchElementException;

import ponzu.api.block.function.Function;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.factory.Lists;
import org.junit.Assert;
import org.junit.Test;

public class FlatTransformIteratorTest
{
    private static final Function<Object, Iterable<Object>> FUNCTION = new Function<Object, Iterable<Object>>()
    {
        public Iterable<Object> valueOf(Object object)
        {
            return null;
        }
    };

    @Test(expected = NoSuchElementException.class)
    public void nextIfDoesntHaveAnything()
    {
        new FlatTransformIterator<Object, Object>(Lists.immutable.of(), FUNCTION).next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIsUnsupported()
    {
        new FlatTransformIterator<Object, Object>(Lists.immutable.of().iterator(), FUNCTION).remove();
    }

    @Test
    public void nextAfterEmptyIterable()
    {
        Object expected = new Object();
        FlatTransformIterator<List<Object>, Object> flattenIterator = new FlatTransformIterator<List<Object>, Object>(
                Lists.fixedSize.<List<Object>>of(
                        Lists.fixedSize.of(),
                        Lists.fixedSize.of(expected)),
                Functions.<List<Object>>getPassThru());
        Assert.assertSame(expected, flattenIterator.next());
    }
}
