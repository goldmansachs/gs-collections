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

package com.gs.collections.impl.collection.mutable;

import com.gs.collections.api.collection.MutableCollection;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractSynchronizedCollectionTestCase extends AbstractCollectionTestCase
{
    @Override
    @Test
    public void testToString()
    {
        MutableCollection<Object> collection = this.newWith(1, 2);
        String string = collection.toString();
        Assert.assertTrue("[1, 2]".equals(string) || "[2, 1]".equals(string));
    }

    @Override
    @Test
    public void makeString()
    {
        MutableCollection<Object> collection = this.newWith(1, 2, 3);
        Assert.assertEquals(collection.toString(), '[' + collection.makeString() + ']');
    }

    @Override
    @Test
    public void appendString()
    {
        MutableCollection<Object> collection = this.newWith(1, 2, 3);
        Appendable builder = new StringBuilder();
        collection.appendString(builder);
        Assert.assertEquals(collection.toString(), '[' + builder.toString() + ']');
    }

    @Override
    @Test
    public void asSynchronized()
    {
        MutableCollection<Object> collection = this.newWith();
        Assert.assertSame(collection, collection.asSynchronized());
    }
}
