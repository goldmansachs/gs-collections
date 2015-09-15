/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.bag.sorted.immutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;

import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.procedure.checked.CheckedObjectIntProcedure;

class ImmutableSortedBagSerializationProxy<T> implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private ImmutableSortedBag<T> bag;

    @SuppressWarnings("UnusedDeclaration")
    public ImmutableSortedBagSerializationProxy()
    {
        // Empty constructor for Externalizable class
    }

    ImmutableSortedBagSerializationProxy(ImmutableSortedBag<T> bag)
    {
        this.bag = bag;
    }

    public void writeExternal(final ObjectOutput out) throws IOException
    {
        out.writeInt(this.bag.sizeDistinct());
        out.writeObject(this.bag.comparator());
        try
        {
            this.bag.forEachWithOccurrences(new CheckedObjectIntProcedure<T>()
            {
                public void safeValue(T object, int index) throws IOException
                {
                    out.writeObject(object);
                    out.writeInt(index);
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
        Comparator<T> comparator = (Comparator<T>) in.readObject();
        MutableSortedBag<T> deserializedBag = new TreeBag<T>(comparator);

        for (int i = 0; i < size; i++)
        {
            deserializedBag.addOccurrences((T) in.readObject(), in.readInt());
        }

        this.bag = deserializedBag.toImmutable();
    }

    protected Object readResolve()
    {
        return this.bag.toImmutable();
    }
}
