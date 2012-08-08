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

package com.gs.collections.impl.bag.immutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.procedure.checked.CheckedObjectIntProcedure;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.factory.Bags;

/**
 * @since 1.0
 */
public abstract class AbstractImmutableBag<T>
        extends AbstractImmutableCollection<T>
        implements ImmutableBag<T>
{
    @Override
    protected MutableCollection<T> newMutable(int size)
    {
        return Bags.mutable.of();
    }

    protected static class ImmutableBagSerializationProxy<T> implements Externalizable
    {
        private static final long serialVersionUID = 1L;

        private Bag<T> bag;

        @SuppressWarnings("UnusedDeclaration")
        public ImmutableBagSerializationProxy()
        {
            // Empty constructor for Externalizable class
        }

        protected ImmutableBagSerializationProxy(Bag<T> bag)
        {
            this.bag = bag;
        }

        public void writeExternal(final ObjectOutput out) throws IOException
        {
            out.writeInt(this.bag.sizeDistinct());
            try
            {
                this.bag.forEachWithOccurrences(new CheckedObjectIntProcedure<T>()
                {
                    @Override
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
            MutableBag<T> deserializedBag = new HashBag<T>(size);

            for (int i = 0; i < size; i++)
            {
                deserializedBag.addOccurrences((T) in.readObject(), in.readInt());
            }

            this.bag = deserializedBag;
        }

        protected Object readResolve()
        {
            return this.bag.toImmutable();
        }
    }
}
