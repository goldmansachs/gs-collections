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

package com.gs.collections.impl.set.immutable.primitive;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.set.primitive.BooleanSet;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.block.procedure.checked.primitive.CheckedBooleanProcedure;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;

/**
 * @since 4.0.
 */
public final class ImmutableBooleanSetSerializationProxy implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private BooleanSet set;

    @SuppressWarnings("UnusedDeclaration")
    public ImmutableBooleanSetSerializationProxy()
    {
        // Empty constructor for Externalizable class
    }

    public ImmutableBooleanSetSerializationProxy(BooleanSet set)
    {
        this.set = set;
    }

    public void writeExternal(final ObjectOutput out) throws IOException
    {
        out.writeInt(this.set.size());
        try
        {
            this.set.forEach(new CheckedBooleanProcedure()
            {
                public void safeValue(boolean item) throws Exception
                {
                    out.writeBoolean(item);
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

    public void readExternal(ObjectInput in) throws IOException
    {
        int size = in.readInt();
        MutableBooleanSet deserializedSet = new BooleanHashSet();

        for (int i = 0; i < size; i++)
        {
            deserializedSet.add(in.readBoolean());
        }

        this.set = deserializedSet;
    }

    private Object readResolve()
    {
        return this.set.toImmutable();
    }
}
