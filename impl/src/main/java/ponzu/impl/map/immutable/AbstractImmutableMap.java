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

package ponzu.impl.map.immutable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.collection.ImmutableCollection;
import ponzu.api.map.ImmutableMap;
import ponzu.api.map.MutableMap;
import ponzu.api.multimap.ImmutableMultimap;
import ponzu.api.partition.PartitionImmutableCollection;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.map.AbstractMapIterable;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.multimap.list.FastListMultimap;
import ponzu.impl.partition.list.PartitionFastList;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.tuple.ImmutableEntry;
import ponzu.impl.utility.MapIterate;
import net.jcip.annotations.Immutable;

@Immutable
public abstract class AbstractImmutableMap<K, V>
        extends AbstractMapIterable<K, V>
        implements ImmutableMap<K, V>, Map<K, V>
{
    /**
     * Returns a string representation of this map.  The string representation consists of a list of key-value mappings
     * in the order returned by the map's <tt>entrySet</tt> view's iterator, enclosed in braces (<tt>"{}"</tt>).
     * Adjacent mappings are separated by the characters <tt>", "</tt> (comma and space).  Each key-value mapping is
     * rendered as the key followed by an equals sign (<tt>"="</tt>) followed by the associated value.  Keys and values
     * are converted to strings as by <tt>String.valueOf(Object)</tt>.<p>
     * <p/>
     * This implementation creates an empty string buffer, appends a left brace, and iterates over the map's
     * <tt>entrySet</tt> view, appending the string representation of each <tt>map.entry</tt> in turn.  After appending
     * each entry except the last, the string <tt>", "</tt> is appended.  Finally a right brace is appended.  A string
     * is obtained from the stringbuffer, and returned.
     *
     * @return a String representation of this map.
     */
    public Map<K, V> castToMap()
    {
        return this;
    }

    public MutableMap<K, V> toMap()
    {
        return UnifiedMap.newMap(this);
    }

    public Iterator<V> iterator()
    {
        return this.valuesView().iterator();
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        throw new UnsupportedOperationException("ImmutableMap");
    }

    public void clear()
    {
        throw new UnsupportedOperationException("ImmutableMap");
    }

    public Set<Entry<K, V>> entrySet()
    {
        final MutableSet<Entry<K, V>> set = UnifiedSet.newSet(this.size());
        this.forEachKeyValue(new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                set.add(ImmutableEntry.of(key, value));
            }
        });
        return set.toImmutable().castToSet();
    }

    public ImmutableMap<K, V> newWithKeyValue(K key, V value)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        map.put(key, value);
        return map.toImmutable();
    }

    public ImmutableMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        for (Pair<? extends K, ? extends V> keyValuePair : keyValues)
        {
            map.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
        return map.toImmutable();
    }

    public ImmutableMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        for (Pair<? extends K, ? extends V> keyValuePair : keyValuePairs)
        {
            map.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
        return map.toImmutable();
    }

    public ImmutableMap<K, V> newWithoutKey(K key)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        map.removeKey(key);
        return map.toImmutable();
    }

    public ImmutableMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        for (K key : keys)
        {
            map.removeKey(key);
        }
        return map.toImmutable();
    }

    public V put(K key, V value)
    {
        throw new UnsupportedOperationException("ImmutableMap");
    }

    public V remove(Object key)
    {
        throw new UnsupportedOperationException("ImmutableMap");
    }

    public <K2, V2> ImmutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        UnifiedMap<K2, V2> result = MapIterate.transform(this, function, UnifiedMap.<K2, V2>newMap());
        return result.toImmutable();
    }

    public <R> ImmutableMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        UnifiedMap<K, R> result = MapIterate.transformValues(this, function, UnifiedMap.<K, R>newMap(this.size()));
        return result.toImmutable();
    }

    public ImmutableMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        UnifiedMap<K, V> result = MapIterate.filterMapOnEntry(this, predicate, UnifiedMap.<K, V>newMap());
        return result.toImmutable();
    }

    public ImmutableMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        UnifiedMap<K, V> result = MapIterate.filterNotMapOnEntry(this, predicate, UnifiedMap.<K, V>newMap());
        return result.toImmutable();
    }

    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.find(this, predicate);
    }

    @Override
    public <R> ImmutableCollection<R> transform(Function<? super V, ? extends R> function)
    {
        return this.transform(function, FastList.<R>newList(this.size())).toImmutable();
    }

    @Override
    public <R> ImmutableCollection<R> transformIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.transformIf(predicate, function, FastList.<R>newList(this.size())).toImmutable();
    }

    @Override
    public <R> ImmutableCollection<R> flatTransform(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.flatTransform(function, FastList.<R>newList(this.size())).toImmutable();
    }

    @Override
    public ImmutableCollection<V> filterNot(Predicate<? super V> predicate)
    {
        return this.filterNot(predicate, FastList.<V>newList(this.size())).toImmutable();
    }

    @Override
    public ImmutableCollection<V> filter(Predicate<? super V> predicate)
    {
        return this.filter(predicate, FastList.<V>newList(this.size())).toImmutable();
    }

    public PartitionImmutableCollection<V> partition(Predicate<? super V> predicate)
    {
        return PartitionFastList.of(this, predicate).toImmutable();
    }

    @Override
    public <S> ImmutableCollection<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<V, S>>newList(this.size())).toImmutable();
    }

    @Override
    public ImmutableCollection<Pair<V, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(FastList.<Pair<V, Integer>>newList(this.size())).toImmutable();
    }

    public <VV> ImmutableMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function)
    {
        return this.groupBy(function, FastListMultimap.<VV, V>newMultimap()).toImmutable();
    }

    public <VV> ImmutableMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.groupByEach(function, FastListMultimap.<VV, V>newMultimap()).toImmutable();
    }
}
