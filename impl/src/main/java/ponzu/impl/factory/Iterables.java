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

package ponzu.impl.factory;

import java.util.Comparator;

import ponzu.api.bag.ImmutableBag;
import ponzu.api.bag.MutableBag;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.map.ImmutableMap;
import ponzu.api.map.MutableMap;
import ponzu.api.map.sorted.ImmutableSortedMap;
import ponzu.api.map.sorted.MutableSortedMap;
import ponzu.api.set.ImmutableSet;
import ponzu.api.set.MutableSet;
import ponzu.api.set.sorted.ImmutableSortedSet;
import ponzu.api.set.sorted.MutableSortedSet;

public final class Iterables
{
    private Iterables()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> MutableList<T> mList()
    {
        return Lists.mutable.of();
    }

    public static <T> MutableList<T> mList(T... elements)
    {
        return Lists.mutable.of(elements);
    }

    public static <T> MutableSet<T> mSet()
    {
        return Sets.mutable.of();
    }

    public static <T> MutableSet<T> mSet(T... elements)
    {
        return Sets.mutable.of(elements);
    }

    public static <T> MutableBag<T> mBag()
    {
        return Bags.mutable.of();
    }

    public static <T> MutableBag<T> mBag(T... elements)
    {
        return Bags.mutable.of(elements);
    }

    public static <K, V> MutableMap<K, V> mMap()
    {
        return Maps.mutable.of();
    }

    public static <K, V> MutableMap<K, V> mMap(K key, V value)
    {
        return Maps.mutable.of(key, value);
    }

    public static <K, V> MutableMap<K, V> mMap(K key1, V value1, K key2, V value2)
    {
        return Maps.mutable.of(key1, value1, key2, value2);
    }

    public static <K, V> MutableMap<K, V> mMap(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return Maps.mutable.of(key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> MutableMap<K, V> mMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return Maps.mutable.of(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public static <T> MutableSortedSet<T> mSortedSet()
    {
        return SortedSets.mutable.of();
    }

    public static <T> MutableSortedSet<T> mSortedSet(T... elements)
    {
        return SortedSets.mutable.of(elements);
    }

    public static <T> MutableSortedSet<T> mSortedSet(Comparator<? super T> comparator)
    {
        return SortedSets.mutable.of(comparator);
    }

    public static <T> MutableSortedSet<T> mSortedSet(Comparator<? super T> comparator, T... elements)
    {
        return SortedSets.mutable.of(comparator, elements);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap()
    {
        return SortedMaps.mutable.of();
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(K key, V value)
    {
        return SortedMaps.mutable.of(key, value);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(K key1, V value1, K key2, V value2)
    {
        return SortedMaps.mutable.of(key1, value1, key2, value2);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return SortedMaps.mutable.of(key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return SortedMaps.mutable.of(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(Comparator<? super K> comparator)
    {
        return SortedMaps.mutable.of(comparator);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(Comparator<? super K> comparator, K key, V value)
    {
        return SortedMaps.mutable.of(comparator, key, value);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(
            Comparator<? super K> comparator,
            K key1, V value1,
            K key2, V value2)
    {
        return SortedMaps.mutable.of(comparator, key1, value1, key2, value2);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(
            Comparator<? super K> comparator,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        return SortedMaps.mutable.of(comparator, key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> MutableSortedMap<K, V> mSortedMap(
            Comparator<? super K> comparator,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        return SortedMaps.mutable.of(comparator, key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public static <T> ImmutableList<T> iList()
    {
        return Lists.immutable.of();
    }

    public static <T> ImmutableList<T> iList(T one)
    {
        return Lists.immutable.of(one);
    }

    public static <T> ImmutableList<T> iList(T one, T two)
    {
        return Lists.immutable.of(one, two);
    }

    public static <T> ImmutableList<T> iList(T one, T two, T three)
    {
        return Lists.immutable.of(one, two, three);
    }

    public static <T> ImmutableList<T> iList(T one, T two, T three, T four)
    {
        return Lists.immutable.of(one, two, three, four);
    }

    public static <T> ImmutableList<T> iList(T one, T two, T three, T four, T five)
    {
        return Lists.immutable.of(one, two, three, four, five);
    }

    public static <T> ImmutableList<T> iList(T one, T two, T three, T four, T five, T six)
    {
        return Lists.immutable.of(one, two, three, four, five, six);
    }

    public static <T> ImmutableList<T> iList(T one, T two, T three, T four, T five, T six, T seven)
    {
        return Lists.immutable.of(one, two, three, four, five, six, seven);
    }

    public static <T> ImmutableList<T> iList(T one, T two, T three, T four, T five, T six, T seven, T eight)
    {
        return Lists.immutable.of(one, two, three, four, five, six, seven, eight);
    }

    public static <T> ImmutableList<T> iList(T one, T two, T three, T four, T five, T six, T seven, T eight, T nine)
    {
        return Lists.immutable.of(one, two, three, four, five, six, seven, eight, nine);
    }

    public static <T> ImmutableList<T> iList(T one, T two, T three, T four, T five, T six, T seven, T eight, T nine, T ten)
    {
        return Lists.immutable.of(one, two, three, four, five, six, seven, eight, nine, ten);
    }

    public static <T> ImmutableList<T> iList(T... elements)
    {
        return Lists.immutable.of(elements);
    }

    public static <T> ImmutableSet<T> iSet()
    {
        return Sets.immutable.of();
    }

    public static <T> ImmutableSet<T> iSet(T one)
    {
        return Sets.immutable.of(one);
    }

    public static <T> ImmutableSet<T> iSet(T one, T two)
    {
        return Sets.immutable.of(one, two);
    }

    public static <T> ImmutableSet<T> iSet(T one, T two, T three)
    {
        return Sets.immutable.of(one, two, three);
    }

    public static <T> ImmutableSet<T> iSet(T one, T two, T three, T four)
    {
        return Sets.immutable.of(one, two, three, four);
    }

    public static <T> ImmutableSet<T> iSet(T... elements)
    {
        return Sets.immutable.of(elements);
    }

    public static <T> ImmutableBag<T> iBag()
    {
        return Bags.immutable.of();
    }

    public static <T> ImmutableBag<T> iBag(T one)
    {
        return Bags.immutable.of(one);
    }

    public static <T> ImmutableBag<T> iBag(T... elements)
    {
        return Bags.immutable.of(elements);
    }

    public static <K, V> ImmutableMap<K, V> iMap()
    {
        return Maps.immutable.of();
    }

    public static <K, V> ImmutableMap<K, V> iMap(K key, V value)
    {
        return Maps.immutable.of(key, value);
    }

    public static <K, V> ImmutableMap<K, V> iMap(K key1, V value1, K key2, V value2)
    {
        return Maps.immutable.of(key1, value1, key2, value2);
    }

    public static <K, V> ImmutableMap<K, V> iMap(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return Maps.immutable.of(key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> ImmutableMap<K, V> iMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return Maps.immutable.of(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public static <T> ImmutableSortedSet<T> iSortedSet()
    {
        return SortedSets.immutable.of();
    }

    public static <T> ImmutableSortedSet<T> iSortedSet(T... elements)
    {
        return SortedSets.immutable.of(elements);
    }

    public static <T> ImmutableSortedSet<T> iSortedSet(Comparator<? super T> comparator)
    {
        return SortedSets.immutable.of(comparator);
    }

    public static <T> ImmutableSortedSet<T> iSortedSet(Comparator<? super T> comparator, T... elements)
    {
        return SortedSets.immutable.of(comparator, elements);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap()
    {
        return SortedMaps.immutable.of();
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(K key, V value)
    {
        return SortedMaps.immutable.of(key, value);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(K key1, V value1, K key2, V value2)
    {
        return SortedMaps.immutable.of(key1, value1, key2, value2);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return SortedMaps.immutable.of(key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return SortedMaps.immutable.of(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(Comparator<? super K> comparator)
    {
        return SortedMaps.immutable.of(comparator);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(Comparator<? super K> comparator, K key, V value)
    {
        return SortedMaps.immutable.of(comparator, key, value);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(
            Comparator<? super K> comparator,
            K key1, V value1,
            K key2, V value2)
    {
        return SortedMaps.immutable.of(comparator, key1, value1, key2, value2);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(
            Comparator<? super K> comparator,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        return SortedMaps.immutable.of(comparator, key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> ImmutableSortedMap<K, V> iSortedMap(
            Comparator<? super K> comparator,
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        return SortedMaps.immutable.of(comparator, key1, value1, key2, value2, key3, value3, key4, value4);
    }
}
