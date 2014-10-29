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

import java.io.IOException;
import java.io.ObjectOutput;

import com.gs.collections.api.RichIterable;

public class MultimapKeyValuesSerializingProcedure<K, V>
        extends CheckedProcedure2<K, RichIterable<V>>
{
    private static final long serialVersionUID = 1L;
    private final ObjectOutput out;

    public MultimapKeyValuesSerializingProcedure(ObjectOutput out)
    {
        this.out = out;
    }

    public void safeValue(K key, RichIterable<V> iterable) throws IOException
    {
        this.out.writeObject(key);
        this.out.writeInt(iterable.size());
        iterable.forEach(new CheckedProcedure<V>()
        {
            public void safeValue(V object) throws IOException
            {
                MultimapKeyValuesSerializingProcedure.this.out.writeObject(object);
            }
        });
    }
}
