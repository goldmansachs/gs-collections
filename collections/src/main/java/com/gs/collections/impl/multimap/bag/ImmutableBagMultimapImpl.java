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

package com.gs.collections.impl.multimap.bag;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.procedure.checked.CheckedObjectIntProcedure;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure2;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.multimap.AbstractImmutableMultimap;
import com.gs.collections.impl.utility.Iterate;

/**
 * The default ImmutableBagMultimap implementation.
 *
 * @since 1.0
 */
public final class ImmutableBagMultimapImpl<K, V>
        extends AbstractImmutableMultimap<K, V, ImmutableBag<V>>
        implements ImmutableBagMultimap<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;

    public ImmutableBagMultimapImpl(MutableMap<K, ImmutableBag<V>> map)
    {
        super(map);
    }

    public ImmutableBagMultimapImpl(ImmutableMap<K, ImmutableBag<V>> map)
    {
        super(map);
    }

    @Override
    protected ImmutableBag<V> createCollection()
    {
        return Bags.immutable.empty();
    }

    public ImmutableBagMultimap<K, V> newEmpty()
    {
        return new ImmutableBagMultimapImpl<K, V>(Maps.immutable.<K, ImmutableBag<V>>of());
    }

    public MutableBagMultimap<K, V> toMutable()
    {
        return new HashBagMultimap<K, V>(this);
    }

    @Override
    public ImmutableBagMultimap<K, V> toImmutable()
    {
        return this;
    }

    private Object writeReplace()
    {
        return new ImmutableBagMultimapSerializationProxy<K, V>(this.map);
    }

    private static class ImmutableBagMultimapSerializationProxy<K, V>
            implements Externalizable
    {
        private static final long serialVersionUID = 1L;

        private ImmutableMap<K, ImmutableBag<V>> map;
        private MutableMultimap<K, V> multimap;

        @SuppressWarnings("UnusedDeclaration")
        public ImmutableBagMultimapSerializationProxy()
        {
            // For Externalizable use only
        }

        private ImmutableBagMultimapSerializationProxy(ImmutableMap<K, ImmutableBag<V>> map)
        {
            this.map = map;
        }

        protected Object readResolve()
        {
            return this.multimap.toImmutable();
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
        {
            this.multimap = new HashBagMultimap<K, V>();
            int keyCount = in.readInt();
            for (int i = 0; i < keyCount; i++)
            {
                K key = (K) in.readObject();
                int valuesSize = in.readInt();
                MutableBag<V> bag = Bags.mutable.empty();
                for (int j = 0; j < valuesSize; j++)
                {
                    V value = (V) in.readObject();
                    int count = in.readInt();

                    bag.addOccurrences(value, count);
                }
                this.multimap.putAll(key, bag);
            }
        }

        public void writeExternal(final ObjectOutput out) throws IOException
        {
            int keysCount = this.map.size();
            out.writeInt(keysCount);
            this.map.forEachKeyValue(new CheckedProcedure2<K, ImmutableBag<V>>()
            {
                public void safeValue(K key, ImmutableBag<V> bag) throws IOException
                {
                    out.writeObject(key);
                    out.writeInt(bag.sizeDistinct());
                    bag.forEachWithOccurrences(new CheckedObjectIntProcedure<V>()
                    {
                        public void safeValue(V value, int count) throws IOException
                        {
                            out.writeObject(value);
                            out.writeInt(count);
                        }
                    });
                }
            });
        }
    }

    @Override
    public ImmutableBagMultimap<K, V> newWith(K key, V value)
    {
        return (ImmutableBagMultimap<K, V>) super.newWith(key, value);
    }

    @Override
    public ImmutableBagMultimap<K, V> newWithout(Object key, Object value)
    {
        return (ImmutableBagMultimap<K, V>) super.newWithout(key, value);
    }

    @Override
    public ImmutableBagMultimap<K, V> newWithAll(K key, Iterable<? extends V> values)
    {
        return (ImmutableBagMultimap<K, V>) super.newWithAll(key, values);
    }

    @Override
    public ImmutableBagMultimap<K, V> newWithoutAll(Object key)
    {
        return (ImmutableBagMultimap<K, V>) super.newWithoutAll(key);
    }

    public ImmutableBagMultimap<V, K> flip()
    {
        return Iterate.flip(this).toImmutable();
    }

    public ImmutableBagMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, HashBagMultimap.<K, V>newMultimap()).toImmutable();
    }

    public ImmutableBagMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, HashBagMultimap.<K, V>newMultimap()).toImmutable();
    }

    public ImmutableBagMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, HashBagMultimap.<K, V>newMultimap()).toImmutable();
    }

    public ImmutableBagMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, HashBagMultimap.<K, V>newMultimap()).toImmutable();
    }

    public <K2, V2> ImmutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.<K2, V2>newMultimap()).toImmutable();
    }

    public <V2> ImmutableBagMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, HashBagMultimap.<K, V2>newMultimap()).toImmutable();
    }
}
