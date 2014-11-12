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

package com.gs.collections.impl.factory;

import java.util.Comparator;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.multimap.bag.ImmutableBagMultimapImpl;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.multimap.list.ImmutableListMultimapImpl;
import com.gs.collections.impl.multimap.set.ImmutableSetMultimapImpl;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.multimap.set.sorted.ImmutableSortedSetMultimapImpl;
import com.gs.collections.impl.multimap.set.sorted.TreeSortedSetMultimap;

@SuppressWarnings("ConstantNamingConvention")
public final class Multimaps
{
    public static final ImmutableMultimaps immutable = new ImmutableMultimaps();
    public static final MutableMultimaps mutable = new MutableMultimaps();

    private Multimaps()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @SuppressWarnings("PublicField")
    public static final class ImmutableMultimaps
    {
        public final ImmutableListMultimapFactory list = new ImmutableListMultimapFactory();
        public final ImmutableSetMultimapFactory set = new ImmutableSetMultimapFactory();
        public final ImmutableSortedSetMultimapFactory sortedSet = new ImmutableSortedSetMultimapFactory();
        public final ImmutableBagMultimapFactory bag = new ImmutableBagMultimapFactory();

        private ImmutableMultimaps()
        {
        }

        public static final class ImmutableListMultimapFactory
        {
            public static final ImmutableListMultimap<Object, Object> EMPTY = new ImmutableListMultimapImpl<Object, Object>(Maps.immutable.<Object, ImmutableList<Object>>with());

            private ImmutableListMultimapFactory()
            {
            }

            public <K, V> ImmutableListMultimap<K, V> empty()
            {
                return (ImmutableListMultimap<K, V>) EMPTY;
            }

            public <K, V> ImmutableListMultimap<K, V> with()
            {
                return this.empty();
            }

            public <K, V> ImmutableListMultimap<K, V> with(K key, V value)
            {
                return new ImmutableListMultimapImpl<K, V>(Maps.immutable.with(key, Lists.immutable.with(value)));
            }

            public <K, V> ImmutableListMultimap<K, V> with(K key1, V value1, K key2, V value2)
            {
                FastListMultimap<K, V> fastListMultimap = FastListMultimap.newMultimap();
                fastListMultimap.put(key1, value1);
                fastListMultimap.put(key2, value2);
                return fastListMultimap.toImmutable();
            }

            public <K, V> ImmutableListMultimap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
            {
                FastListMultimap<K, V> fastListMultimap = FastListMultimap.newMultimap();
                fastListMultimap.put(key1, value1);
                fastListMultimap.put(key2, value2);
                fastListMultimap.put(key3, value3);
                return fastListMultimap.toImmutable();
            }
        }

        public static final class ImmutableSetMultimapFactory
        {
            public static final ImmutableSetMultimap<Object, Object> EMPTY = new ImmutableSetMultimapImpl<Object, Object>(Maps.immutable.<Object, ImmutableSet<Object>>with());

            private ImmutableSetMultimapFactory()
            {
            }

            public <K, V> ImmutableSetMultimap<K, V> empty()
            {
                return (ImmutableSetMultimap<K, V>) EMPTY;
            }

            public <K, V> ImmutableSetMultimap<K, V> with()
            {
                return this.empty();
            }

            public <K, V> ImmutableSetMultimap<K, V> with(K key, V value)
            {
                return new ImmutableSetMultimapImpl<K, V>(Maps.immutable.with(key, Sets.immutable.with(value)));
            }

            public <K, V> ImmutableSetMultimap<K, V> with(K key1, V value1, K key2, V value2)
            {
                UnifiedSetMultimap<K, V> unifiedSetMultimap = UnifiedSetMultimap.newMultimap();
                unifiedSetMultimap.put(key1, value1);
                unifiedSetMultimap.put(key2, value2);
                return unifiedSetMultimap.toImmutable();
            }

            public <K, V> ImmutableSetMultimap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
            {
                UnifiedSetMultimap<K, V> unifiedSetMultimap = UnifiedSetMultimap.newMultimap();
                unifiedSetMultimap.put(key1, value1);
                unifiedSetMultimap.put(key2, value2);
                unifiedSetMultimap.put(key3, value3);
                return unifiedSetMultimap.toImmutable();
            }
        }

        public static final class ImmutableSortedSetMultimapFactory
        {
            private ImmutableSortedSetMultimapFactory()
            {
            }

            public <K, V> ImmutableSortedSetMultimap<K, V> with(Comparator<V> comparator)
            {
                return new ImmutableSortedSetMultimapImpl<K, V>(Maps.immutable.<K, ImmutableSortedSet<V>>with(), comparator);
            }

            public <K, V> ImmutableSortedSetMultimap<K, V> with(Comparator<V> comparator, K key, V value)
            {
                return new ImmutableSortedSetMultimapImpl<K, V>(Maps.immutable.with(key, SortedSets.immutable.with(comparator, value)), comparator);
            }

            public <K, V> ImmutableSortedSetMultimap<K, V> with(Comparator<V> comparator, K key1, V value1, K key2, V value2)
            {
                TreeSortedSetMultimap<K, V> treeSortedSetMultimap = TreeSortedSetMultimap.newMultimap(comparator);
                treeSortedSetMultimap.put(key1, value1);
                treeSortedSetMultimap.put(key2, value2);
                return treeSortedSetMultimap.toImmutable();
            }

            public <K, V> ImmutableSortedSetMultimap<K, V> with(Comparator<V> comparator, K key1, V value1, K key2, V value2, K key3, V value3)
            {
                TreeSortedSetMultimap<K, V> treeSortedSetMultimap = TreeSortedSetMultimap.newMultimap(comparator);
                treeSortedSetMultimap.put(key1, value1);
                treeSortedSetMultimap.put(key2, value2);
                treeSortedSetMultimap.put(key3, value3);
                return treeSortedSetMultimap.toImmutable();
            }
        }

        public static final class ImmutableBagMultimapFactory
        {
            public static final ImmutableBagMultimap<Object, Object> EMPTY = new ImmutableBagMultimapImpl<Object, Object>(Maps.immutable.<Object, ImmutableBag<Object>>with());

            private ImmutableBagMultimapFactory()
            {
            }

            public <K, V> ImmutableBagMultimap<K, V> empty()
            {
                return (ImmutableBagMultimap<K, V>) EMPTY;
            }

            public <K, V> ImmutableBagMultimap<K, V> with()
            {
                return this.empty();
            }

            public <K, V> ImmutableBagMultimap<K, V> with(K key, V value)
            {
                return new ImmutableBagMultimapImpl<K, V>(Maps.immutable.with(key, Bags.immutable.with(value)));
            }

            public <K, V> ImmutableBagMultimap<K, V> with(K key1, V value1, K key2, V value2)
            {
                HashBagMultimap<K, V> hashBagMultimap = HashBagMultimap.newMultimap();
                hashBagMultimap.put(key1, value1);
                hashBagMultimap.put(key2, value2);
                return hashBagMultimap.toImmutable();
            }

            public <K, V> ImmutableBagMultimap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
            {
                HashBagMultimap<K, V> hashBagMultimap = HashBagMultimap.newMultimap();
                hashBagMultimap.put(key1, value1);
                hashBagMultimap.put(key2, value2);
                hashBagMultimap.put(key3, value3);
                return hashBagMultimap.toImmutable();
            }
        }
    }

    @SuppressWarnings("PublicField")
    public static final class MutableMultimaps
    {
        public final MutableListMultimapFactory list = new MutableListMultimapFactory();
        public final MutableSetMultimapFactory set = new MutableSetMultimapFactory();
        public final MutableSortedSetMultimapFactory sortedSet = new MutableSortedSetMultimapFactory();
        public final MutableBagMultimapFactory bag = new MutableBagMultimapFactory();

        private MutableMultimaps()
        {
        }

        public static final class MutableListMultimapFactory
        {
            private MutableListMultimapFactory()
            {
            }

            public <K, V> MutableListMultimap<K, V> empty()
            {
                return FastListMultimap.newMultimap();
            }

            public <K, V> MutableListMultimap<K, V> with()
            {
                return this.empty();
            }

            public <K, V> MutableListMultimap<K, V> with(K key, V value)
            {
                FastListMultimap<K, V> fastListMultimap = FastListMultimap.newMultimap();
                fastListMultimap.put(key, value);
                return fastListMultimap;
            }

            public <K, V> MutableListMultimap<K, V> with(K key1, V value1, K key2, V value2)
            {
                FastListMultimap<K, V> fastListMultimap = FastListMultimap.newMultimap();
                fastListMultimap.put(key1, value1);
                fastListMultimap.put(key2, value2);
                return fastListMultimap;
            }

            public <K, V> MutableListMultimap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
            {
                FastListMultimap<K, V> fastListMultimap = FastListMultimap.newMultimap();
                fastListMultimap.put(key1, value1);
                fastListMultimap.put(key2, value2);
                fastListMultimap.put(key3, value3);
                return fastListMultimap;
            }
        }

        public static final class MutableSetMultimapFactory
        {
            private MutableSetMultimapFactory()
            {
            }

            public <K, V> MutableSetMultimap<K, V> empty()
            {
                return UnifiedSetMultimap.newMultimap();
            }

            public <K, V> MutableSetMultimap<K, V> with()
            {
                return this.empty();
            }

            public <K, V> MutableSetMultimap<K, V> with(K key, V value)
            {
                UnifiedSetMultimap<K, V> unifiedSetMultimap = UnifiedSetMultimap.newMultimap();
                unifiedSetMultimap.put(key, value);
                return unifiedSetMultimap;
            }

            public <K, V> MutableSetMultimap<K, V> with(K key1, V value1, K key2, V value2)
            {
                UnifiedSetMultimap<K, V> unifiedSetMultimap = UnifiedSetMultimap.newMultimap();
                unifiedSetMultimap.put(key1, value1);
                unifiedSetMultimap.put(key2, value2);
                return unifiedSetMultimap;
            }

            public <K, V> MutableSetMultimap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
            {
                UnifiedSetMultimap<K, V> unifiedSetMultimap = UnifiedSetMultimap.newMultimap();
                unifiedSetMultimap.put(key1, value1);
                unifiedSetMultimap.put(key2, value2);
                unifiedSetMultimap.put(key3, value3);
                return unifiedSetMultimap;
            }
        }

        public static final class MutableSortedSetMultimapFactory
        {
            private MutableSortedSetMultimapFactory()
            {
            }

            public <K, V> MutableSortedSetMultimap<K, V> with(Comparator<V> comparator)
            {
                return TreeSortedSetMultimap.newMultimap(comparator);
            }

            public <K, V> MutableSortedSetMultimap<K, V> with(Comparator<V> comparator, K key, V value)
            {
                TreeSortedSetMultimap<K, V> treeSortedSetMultimap = TreeSortedSetMultimap.newMultimap(comparator);
                treeSortedSetMultimap.put(key, value);
                return treeSortedSetMultimap;
            }

            public <K, V> MutableSortedSetMultimap<K, V> with(Comparator<V> comparator, K key1, V value1, K key2, V value2)
            {
                TreeSortedSetMultimap<K, V> treeSortedSetMultimap = TreeSortedSetMultimap.newMultimap(comparator);
                treeSortedSetMultimap.put(key1, value1);
                treeSortedSetMultimap.put(key2, value2);
                return treeSortedSetMultimap;
            }

            public <K, V> MutableSortedSetMultimap<K, V> with(Comparator<V> comparator, K key1, V value1, K key2, V value2, K key3, V value3)
            {
                TreeSortedSetMultimap<K, V> treeSortedSetMultimap = TreeSortedSetMultimap.newMultimap(comparator);
                treeSortedSetMultimap.put(key1, value1);
                treeSortedSetMultimap.put(key2, value2);
                treeSortedSetMultimap.put(key3, value3);
                return treeSortedSetMultimap;
            }
        }

        public static final class MutableBagMultimapFactory
        {
            private MutableBagMultimapFactory()
            {
            }

            public <K, V> MutableBagMultimap<K, V> empty()
            {
                return HashBagMultimap.newMultimap();
            }

            public <K, V> MutableBagMultimap<K, V> with()
            {
                return this.empty();
            }

            public <K, V> MutableBagMultimap<K, V> with(K key, V value)
            {
                HashBagMultimap<K, V> hashBagMultimap = HashBagMultimap.newMultimap();
                hashBagMultimap.put(key, value);
                return hashBagMultimap;
            }

            public <K, V> MutableBagMultimap<K, V> with(K key1, V value1, K key2, V value2)
            {
                HashBagMultimap<K, V> hashBagMultimap = HashBagMultimap.newMultimap();
                hashBagMultimap.put(key1, value1);
                hashBagMultimap.put(key2, value2);
                return hashBagMultimap;
            }

            public <K, V> MutableBagMultimap<K, V> with(K key1, V value1, K key2, V value2, K key3, V value3)
            {
                HashBagMultimap<K, V> hashBagMultimap = HashBagMultimap.newMultimap();
                hashBagMultimap.put(key1, value1);
                hashBagMultimap.put(key2, value2);
                hashBagMultimap.put(key3, value3);
                return hashBagMultimap;
            }
        }
    }
}
