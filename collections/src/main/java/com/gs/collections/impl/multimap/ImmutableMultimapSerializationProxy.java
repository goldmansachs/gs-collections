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

package com.gs.collections.impl.multimap;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.impl.block.procedure.checked.MultimapKeyValuesSerializingProcedure;

public abstract class ImmutableMultimapSerializationProxy<K, V, R extends RichIterable<V>>
{
    private Multimap<K, V> multimapToReadInto;
    private ImmutableMap<K, R> mapToWrite;

    protected ImmutableMultimapSerializationProxy()
    {
    }

    protected ImmutableMultimapSerializationProxy(ImmutableMap<K, R> immutableMap)
    {
        this.mapToWrite = immutableMap;
    }

    protected abstract AbstractMutableMultimap<K, V, ?> createEmptyMutableMultimap();

    protected Object readResolve()
    {
        return this.multimapToReadInto.toImmutable();
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        int keysCount = this.mapToWrite.size();
        out.writeInt(keysCount);
        this.mapToWrite.forEachKeyValue(new MultimapKeyValuesSerializingProcedure<K, V>(out));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        AbstractMutableMultimap<K, V, ?> toReadInto = this.createEmptyMutableMultimap();
        toReadInto.readValuesFrom(in);
        this.multimapToReadInto = toReadInto;
    }
}
