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

package com.gs.collections.impl.bimap.immutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.bimap.ImmutableBiMap;
import com.gs.collections.api.bimap.MutableBiMap;
import com.gs.collections.impl.bimap.mutable.HashBiMap;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure2;

class ImmutableBiMapSerializationProxy<K, V> implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private ImmutableBiMap<K, V> biMap;

    @SuppressWarnings("UnusedDeclaration")
    public ImmutableBiMapSerializationProxy()
    {
        // Empty constructor for Externalizable class
    }

    ImmutableBiMapSerializationProxy(ImmutableBiMap<K, V> biMap)
    {
        this.biMap = biMap;
    }

    public void writeExternal(final ObjectOutput out) throws IOException
    {
        out.writeInt(this.biMap.size());
        try
        {
            this.biMap.forEachKeyValue(new CheckedProcedure2<K, V>()
            {
                public void safeValue(K key, V value) throws IOException
                {
                    out.writeObject(key);
                    out.writeObject(value);
                }
            });
        }
        catch (RuntimeException e)
        {
            if (e.getCause() instanceof IOException)
            {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int size = in.readInt();
        MutableBiMap<K, V> deserializedBiMap = new HashBiMap<K, V>(size);

        for (int i = 0; i < size; i++)
        {
            if (deserializedBiMap.put((K) in.readObject(), (V) in.readObject()) != null)
            {
                throw new IllegalStateException();
            }
        }

        this.biMap = deserializedBiMap.toImmutable();
    }

    protected Object readResolve()
    {
        return this.biMap;
    }
}
