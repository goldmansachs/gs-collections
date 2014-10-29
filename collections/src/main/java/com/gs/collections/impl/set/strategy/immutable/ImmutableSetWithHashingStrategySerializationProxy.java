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

package com.gs.collections.impl.set.strategy.immutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;

class ImmutableSetWithHashingStrategySerializationProxy<T> implements Externalizable
{
    private static final long serialVersionUID = 1L;
    private ImmutableSet<T> set;
    private HashingStrategy<? super T> hashingStrategy;

    @SuppressWarnings("UnusedDeclaration")
    public ImmutableSetWithHashingStrategySerializationProxy()
    {
        // Empty constructor for Externalizable class
    }

    ImmutableSetWithHashingStrategySerializationProxy(ImmutableSet<T> set, HashingStrategy<? super T> hashingStrategy)
    {
        this.set = set;
        this.hashingStrategy = hashingStrategy;
    }

    public void writeExternal(final ObjectOutput out) throws IOException
    {
        out.writeObject(this.hashingStrategy);
        out.writeInt(this.set.size());
        try
        {
            this.set.forEach(new CheckedProcedure<T>()
            {
                public void safeValue(T value) throws IOException
                {
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
        HashingStrategy<? super T> strategy = (HashingStrategy<? super T>) in.readObject();
        int size = in.readInt();
        UnifiedSetWithHashingStrategy<T> deserializedSet = UnifiedSetWithHashingStrategy.newSet(strategy);

        for (int i = 0; i < size; i++)
        {
            deserializedSet.put((T) in.readObject());
        }

        this.set = deserializedSet.toImmutable();
    }

    protected Object readResolve()
    {
        return this.set;
    }
}
