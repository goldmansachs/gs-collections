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

package com.gs.collections.impl.block.procedure.checked;

import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Iterator;

import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import org.junit.Assert;
import org.junit.Test;

public class MultimapKeyValuesSerializingProcedureTest
{
    @Test
    public void testSerialization()
    {
        FastListMultimap<String, String> map = new FastListMultimap<>();
        map.put("A", "alpha");
        map.put("A", "beta");
        FastList<? extends Serializable> expectedWrites = FastList.newListWith("A", 2, "alpha", "beta");
        MultimapKeyValuesSerializingProcedure<String, String> procedure =
                new MultimapKeyValuesSerializingProcedure<>(new MockObjectOutput(expectedWrites));
        map.toMap().forEachKeyValue(procedure);
    }

    public static final class MockObjectOutput implements ObjectOutput
    {
        private final Iterator<? extends Serializable> iterator;

        public MockObjectOutput(Iterable<? extends Serializable> expectedWrites)
        {
            this.iterator = expectedWrites.iterator();
        }

        @Override
        public void writeObject(Object obj)
        {
            Assert.assertEquals(this.iterator.next(), obj);
        }

        @Override
        public void writeInt(int v)
        {
            Assert.assertEquals(this.iterator.next(), v);
        }

        @Override
        public void write(int i)
        {
            throw new RuntimeException("write not implemented");
        }

        @Override
        public void write(byte[] bs)
        {
            throw new RuntimeException("write not implemented");
        }

        @Override
        public void write(byte[] bs, int off, int len)
        {
            throw new RuntimeException("write not implemented");
        }

        @Override
        public void writeBoolean(boolean v)
        {
            throw new RuntimeException("writeBoolean not implemented");
        }

        @Override
        public void writeByte(int v)
        {
            throw new RuntimeException("writeByte not implemented");
        }

        @Override
        public void writeShort(int v)
        {
            throw new RuntimeException("writeShort not implemented");
        }

        @Override
        public void writeChar(int v)
        {
            throw new RuntimeException("writeChar not implemented");
        }

        @Override
        public void writeLong(long v)
        {
            throw new RuntimeException("writeLong not implemented");
        }

        @Override
        public void writeFloat(float v)
        {
            throw new RuntimeException("writeFloat not implemented");
        }

        @Override
        public void writeDouble(double v)
        {
            throw new RuntimeException("writeDouble not implemented");
        }

        @Override
        public void writeBytes(String s)
        {
            throw new RuntimeException("writeBytes not implemented");
        }

        @Override
        public void writeChars(String s)
        {
            throw new RuntimeException("writeChars not implemented");
        }

        @Override
        public void writeUTF(String s)
        {
            throw new RuntimeException("writeUTF not implemented");
        }

        @Override
        public void flush()
        {
            throw new RuntimeException("flush not implemented");
        }

        @Override
        public void close()
        {
            throw new RuntimeException("close not implemented");
        }
    }
}
