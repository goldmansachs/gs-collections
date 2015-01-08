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

package com.gs.collections.impl.map.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.map.MutableMapIterable;

public class SynchronizedMapSerializationProxy<K, V> implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private MutableMapIterable<K, V> map;

    @SuppressWarnings("UnusedDeclaration")
    public SynchronizedMapSerializationProxy()
    {
        // Empty constructor for Externalizable class
    }

    public SynchronizedMapSerializationProxy(MutableMapIterable<K, V> map)
    {
        this.map = map;
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(this.map);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.map = (MutableMapIterable<K, V>) in.readObject();
    }

    protected Object readResolve()
    {
        return this.map.asSynchronized();
    }
}
