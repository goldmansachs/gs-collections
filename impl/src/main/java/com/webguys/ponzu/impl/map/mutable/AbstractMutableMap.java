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

package com.webguys.ponzu.impl.map.mutable;

import java.util.Collection;
import java.util.Iterator;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.ImmutableMap;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.MutableMultimap;
import com.webguys.ponzu.api.partition.list.PartitionMutableList;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.factory.Maps;
import com.webguys.ponzu.impl.list.fixed.ArrayAdapter;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.AbstractMapIterable;
import com.webguys.ponzu.impl.multimap.list.FastListMultimap;
import com.webguys.ponzu.impl.partition.list.PartitionFastList;
import com.webguys.ponzu.impl.tuple.AbstractImmutableEntry;
import com.webguys.ponzu.impl.utility.LazyIterate;
import com.webguys.ponzu.impl.utility.MapIterate;

public abstract class AbstractMutableMap<K, V> extends AbstractMapIterable<K, V>
        implements MutableMap<K, V>
{
    /**
     * Returns a string representation of this map.  The string representation
     * consists of a list of key-value mappings in the order returned by the
     * map's <tt>entrySet</tt> view's iterator, enclosed in braces
     * (<tt>"{}"</tt>).  Adjacent mappings are separated by the characters
     * <tt>", "</tt> (comma and space).  Each key-value mapping is rendered as
     * the key followed by an equals sign (<tt>"="</tt>) followed by the
     * associated value.  Keys and values are converted to strings as by
     * <tt>String.valueOf(Object)</tt>.<p>
     * <p/>
     * This implementation creates an empty string buffer, appends a left
     * brace, and iterates over the map's <tt>entrySet</tt> view, appending
     * the string representation of each <tt>map.entry</tt> in turn.  After
     * appending each entry except the last, the string <tt>", "</tt> is
     * appended.  Finally a right brace is appended.  A string is obtained
     * from the stringbuffer, and returned.
     *
     * @return a String representation of this map.
     */
    @Override
    public abstract MutableMap<K, V> clone();

    /**
     * Creates a new instance of the same type, using the given capacity and the default growth parameters.
     */
    public abstract <K, V> MutableMap<K, V> newEmpty(int capacity);

    public MutableMap<K, V> asUnmodifiable()
    {
        return UnmodifiableMutableMap.of(this);
    }

    public ImmutableMap<K, V> toImmutable()
    {
        return Maps.immutable.ofMap(this);
    }

    public MutableMap<K, V> asSynchronized()
    {
        return SynchronizedMutableMap.of(this);
    }

    public RichIterable<K> keysView()
    {
        return LazyIterate.adapt(this.keySet());
    }

    public RichIterable<V> valuesView()
    {
        return LazyIterate.adapt(this.values());
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return LazyIterate.adapt(this.entrySet()).transform(AbstractImmutableEntry.<K, V>getPairFunction());
    }

    public V getIfAbsentPut(K key, Generator<? extends V> function)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            result = function.value();
            this.put(key, result);
        }
        return result;
    }

    public <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        V result = this.get(key);
        if (this.isAbsent(result, key))
        {
            result = function.valueOf(parameter);
            this.put(key, result);
        }
        return result;
    }

    public Iterator<V> iterator()
    {
        return this.values().iterator();
    }

    public <K2, V2> MutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return MapIterate.transform(this, function, UnifiedMap.<K2, V2>newMap(this.size()));
    }

    public <R> MutableMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return MapIterate.transformValues(this, function, this.<K, R>newEmpty(this.size()));
    }

    public MutableMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.filterMapOnEntry(this, predicate, this.newEmpty());
    }

    public MutableMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.filterNotMapOnEntry(this, predicate, this.newEmpty());
    }

    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.find(this, predicate);
    }

    @Override
    public <R> MutableList<R> transform(Function<? super V, ? extends R> function)
    {
        return this.transform(function, FastList.<R>newList(this.size()));
    }

    @Override
    public <R> MutableList<R> transformIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.transformIf(predicate, function, FastList.<R>newList(this.size()));
    }

    @Override
    public <R> MutableList<R> flatTransform(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.flatTransform(function, FastList.<R>newList(this.size()));
    }

    @Override
    public MutableList<V> filterNot(Predicate<? super V> predicate)
    {
        return this.filterNot(predicate, FastList.<V>newList(this.size()));
    }

    @Override
    public MutableList<V> filter(Predicate<? super V> predicate)
    {
        return this.filter(predicate, FastList.<V>newList(this.size()));
    }

    public PartitionMutableList<V> partition(Predicate<? super V> predicate)
    {
        return PartitionFastList.of(this, predicate);
    }

    @Override
    public <S> MutableList<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<V, S>>newList(this.size()));
    }

    @Override
    public MutableList<Pair<V, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(FastList.<Pair<V, Integer>>newList(this.size()));
    }

    public MutableMap<K, V> withKeyValue(K key, V value)
    {
        this.put(key, value);
        return this;
    }

    public MutableMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        for (Pair<? extends K, ? extends V> keyVal : keyValues)
        {
            this.put(keyVal.getOne(), keyVal.getTwo());
        }
        return this;
    }

    public MutableMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValues)
    {
        return this.withAllKeyValues(ArrayAdapter.adapt(keyValues));
    }

    public MutableMap<K, V> withoutKey(K key)
    {
        this.removeKey(key);
        return this;
    }

    public MutableMap<K, V> withoutAllKeys(Iterable<? extends K> keys)
    {
        for (K key : keys)
        {
            this.removeKey(key);
        }
        return this;
    }

    public void chainedForEachValue(Object[] chain, Procedure<? super V> procedure)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object key = chain[i];
            if (key == null)
            {
                return;
            }
            procedure.value((V) chain[i + 1]);
        }
    }

    protected void mapBatchForEach(Object[] map, Procedure<? super V> procedure, int sectionIndex, int sectionCount)
    {
        int sectionSize = map.length / sectionCount;
        int start = sectionIndex * sectionSize;
        int end = sectionIndex == sectionCount - 1 ? map.length : start + sectionSize;
        if (0 == start % 2)
        {
            start++;
        }
        for (int i = start; i < end; i += 2)
        {
            Object value = map[i];
            if (value instanceof Object[])
            {
                this.chainedForEachValue((Object[]) value, procedure);
            }
            else if (value == null && map[i - 1] != null || value != null)
            {
                procedure.value((V) value);
            }
        }
    }

    public int chainedForEachValueWithIndex(Object[] chain, ObjectIntProcedure<? super V> objectIntProcedure, int index)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object key = chain[i];
            if (key == null)
            {
                return index;
            }
            objectIntProcedure.value((V) chain[i + 1], index++);
        }
        return index;
    }

    public <P> void chainedForEachValueWith(
            Object[] chain,
            Procedure2<? super V, ? super P> procedure,
            P parameter)
    {
        for (int i = 0; i < chain.length; i += 2)
        {
            Object key = chain[i];
            if (key == null)
            {
                return;
            }
            procedure.value((V) chain[i + 1], parameter);
        }
    }

    /**
     * Trait-style class that is used to capture commonalities between ValuesCollection class implementations in order to
     * avoid code duplication.
     */
    protected abstract static class ValuesCollectionCommon<V> implements Collection<V>
    {
        public boolean add(V v)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends V> collection)
        {
            throw new UnsupportedOperationException();
        }

        protected void copyValuesFromTable(Object[] table, Object[] result, Object chainedKey)
        {
            int count = 0;
            for (int i = 0; i < table.length; i += 2)
            {
                Object x = table[i];
                if (x != null)
                {
                    if (x == chainedKey)
                    {
                        Object[] chain = (Object[]) table[i + 1];
                        for (int j = 0; j < chain.length; j += 2)
                        {
                            Object key = chain[j];
                            if (key == null)
                            {
                                break;
                            }
                            result[count++] = chain[j + 1];
                        }
                    }
                    else
                    {
                        result[count++] = table[i + 1];
                    }
                }
            }
        }
    }

    public <VV> MutableMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function)
    {
        return this.groupBy(function, FastListMultimap.<VV, V>newMultimap());
    }

    public <VV> MutableMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.groupByEach(function, FastListMultimap.<VV, V>newMultimap());
    }
}
