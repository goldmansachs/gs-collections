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

package com.gs.collections.impl.set.sorted.immutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;

import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;

class ImmutableSortedSetSerializationProxy<T> implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private ImmutableSortedSet<T> set;

    @SuppressWarnings("UnusedDeclaration")
    public ImmutableSortedSetSerializationProxy()
    {
        // Empty constructor for Externalizable class
    }

    ImmutableSortedSetSerializationProxy(ImmutableSortedSet<T> set)
    {
        this.set = set;
    }

    public void writeExternal(final ObjectOutput out) throws IOException
    {
        out.writeObject(this.set.comparator());
        out.writeInt(this.set.size());
        try
        {
            this.set.forEach(new CheckedProcedure<T>()
            {
                public void safeValue(T each) throws IOException
                {
                    out.writeObject(each);
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
        Comparator<T> comparator = (Comparator<T>) in.readObject();
        int size = in.readInt();
        MutableSortedSet<T> deserializedSet = new TreeSortedSet<T>(comparator);

        for (int i = 0; i < size; i++)
        {
            deserializedSet.add((T) in.readObject());
        }

        this.set = deserializedSet.toImmutable();
    }

    protected Object readResolve()
    {
        return this.set.toImmutable();
    }
}
