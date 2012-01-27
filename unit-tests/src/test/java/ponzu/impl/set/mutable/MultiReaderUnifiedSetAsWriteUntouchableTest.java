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

package ponzu.impl.set.mutable;

import ponzu.api.set.MutableSet;
import ponzu.impl.collection.mutable.AbstractCollectionTestCase;
import ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class MultiReaderUnifiedSetAsWriteUntouchableTest extends AbstractCollectionTestCase
{
    @Override
    protected <T> MutableSet<T> classUnderTest()
    {
        return MultiReaderUnifiedSet.<T>newSet().asWriteUntouchable();
    }

    @Override
    public void getLast()
    {
        Assert.assertNotNull(this.newWith(1, 2, 3).getLast());
        Assert.assertNull(this.classUnderTest().getLast());
    }

    @Override
    @Test
    public void makeString()
    {
        Assert.assertEquals("1, 2, 3", this.<Object>newWith(1, 2, 3).makeString());
    }

    @Override
    @Test
    public void appendString()
    {
        Appendable builder = new StringBuilder();
        this.<Object>newWith(1, 2, 3).appendString(builder);
        Assert.assertEquals("1, 2, 3", builder.toString());
    }

    @Override
    @Test
    public void testToString()
    {
        Assert.assertEquals("[1, 2, 3]", this.<Object>newWith(1, 2, 3).toString());
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                MultiReaderUnifiedSetAsWriteUntouchableTest.this.classUnderTest().asSynchronized();
            }
        });
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                MultiReaderUnifiedSetAsWriteUntouchableTest.this.classUnderTest().asUnmodifiable();
            }
        });
    }
}
