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

package ponzu.impl.utility;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import ponzu.api.RichIterable;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function0;
import ponzu.api.block.function.Function2;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.MutableList;
import ponzu.api.map.ImmutableMap;
import ponzu.api.map.MapIterable;
import ponzu.api.map.MutableMap;
import ponzu.api.map.UnsortedMapIterable;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.procedure.CollectProcedure;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.block.procedure.CountProcedure;
import ponzu.impl.block.procedure.FilterNotProcedure;
import ponzu.impl.block.procedure.MapEntryToProcedure2;
import ponzu.impl.block.procedure.MapPutProcedure;
import ponzu.impl.block.procedure.SelectProcedure;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.map.mutable.MapAdapter;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.tuple.AbstractImmutableEntry;
import ponzu.impl.tuple.Tuples;
import ponzu.impl.utility.internal.IterableIterate;

/**
 * Since Maps have two data-points per entry (i.e. key and value), most of the implementations in this class
 * iterates over the values only, unless otherwise specified.
 * To iterate over the keys, use keySet() with standard {@link Iterate} methods.
 *
 * @see Iterate
 * @since 1.0
 */
public final class MapIterate
{
    private MapIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * A null-safe check on a map to see if it isEmpty.  A null collection results in <code>true</code>.
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return map == null || map.isEmpty();
    }

    /**
     * A null-safe check on a map to see if it notEmpty.  A null collection results in <code>false</code>.
     */
    public static boolean notEmpty(Map<?, ?> map)
    {
        return map != null && !map.isEmpty();
    }

    /**
     * Get and return the value in the Map at the specified key, or if there is no value at the key, return the result
     * of evaluating the specified {@link Function0}, and put that value in the map at the specified key.
     * <p/>
     * This method handles the <code>null</code>-value-at-key case correctly.
     */
    public static <K, V> V getIfAbsentPut(Map<K, V> map, K key, Function0<? extends V> instanceBlock)
    {
        if (map instanceof MutableMap)
        {
            return ((MutableMap<K, V>) map).getIfAbsentPut(key, instanceBlock);
        }
        V result = map.get(key);
        if (MapIterate.isAbsent(result, map, key))
        {
            result = instanceBlock.value();
            map.put(key, result);
        }
        return result;
    }

    /**
     * Get and return the value in the Map at the specified key, or if there is no value at the key, return the result
     * of evaluating the specified {@link Function} with the <code>parameter</code>, and put that value in the map at
     * the specified key.
     */
    public static <K, V, P> V getIfAbsentPutWith(
            Map<K, V> map,
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        V result = map.get(key);
        if (MapIterate.isAbsent(result, map, key))
        {
            result = function.valueOf(parameter);
            map.put(key, result);
        }
        return result;
    }

    /**
     * Get and return the value in the Map that corresponds to the specified key, or if there is no value
     * at the key, return the result of evaluating the specified {@link Function0}.
     */
    public static <K, V> V getIfAbsent(Map<K, V> map, K key, Function0<? extends V> instanceBlock)
    {
        if (map instanceof UnsortedMapIterable)
        {
            return ((MapIterable<K, V>) map).getIfAbsent(key, instanceBlock);
        }
        V result = map.get(key);
        if (MapIterate.isAbsent(result, map, key))
        {
            result = instanceBlock.value();
        }
        return result;
    }

    /**
     * Get and return the value in the Map that corresponds to the specified key, or if there is no value
     * at the key, return the result of evaluating the specified {@link Function} with the specified parameter.
     */
    public static <K, V, P> V getIfAbsentWith(
            Map<K, V> map,
            K key,
            Function<? super P, ? extends V> function,
            P parameter)
    {
        if (map instanceof UnsortedMapIterable)
        {
            return ((MapIterable<K, V>) map).getIfAbsentWith(key, function, parameter);
        }
        V result = map.get(key);
        if (MapIterate.isAbsent(result, map, key))
        {
            result = function.valueOf(parameter);
        }
        return result;
    }

    /**
     * Get and return the value in the Map at the specified key, or if there is no value at the key, return the
     * <code>defaultValue</code>.
     */
    public static <K, V> V getIfAbsentDefault(Map<K, V> map, K key, V defaultValue)
    {
        V result = map.get(key);
        if (MapIterate.isAbsent(result, map, key))
        {
            result = defaultValue;
        }
        return result;
    }

    private static <K, V> boolean isAbsent(V result, Map<K, V> map, K key)
    {
        return result == null && !map.containsKey(key);
    }

    /**
     * If there is a value in the Map tat the specified key, return the result of applying the specified Function
     * on the value, otherwise return null.
     */
    public static <K, V, A> A ifPresentApply(
            Map<K, V> map,
            K key,
            Function<? super V, ? extends A> function)
    {
        if (map instanceof UnsortedMapIterable)
        {
            return ((MapIterable<K, V>) map).ifPresentApply(key, function);
        }
        V result = map.get(key);
        return MapIterate.isAbsent(result, map, key) ? null : function.valueOf(result);
    }

    /**
     * @see Iterate#filter(Iterable, Predicate)
     */
    public static <K, V> MutableList<V> filter(Map<K, V> map, Predicate<? super V> predicate)
    {
        return MapIterate.filter(map, predicate, FastList.<V>newList());
    }

    /**
     * @see Iterate#filter(Iterable, Predicate, Collection)
     */
    public static <K, V, R extends Collection<V>> R filter(
            Map<K, V> map,
            Predicate<? super V> predicate,
            R targetCollection)
    {
        Procedure<V> procedure = new SelectProcedure<V>(predicate, targetCollection);
        MapIterate.forEachValue(map, procedure);
        return targetCollection;
    }

    /**
     * @see Iterate#count(Iterable, Predicate)
     */
    public static <K, V> int count(Map<K, V> map, Predicate<? super V> predicate)
    {
        CountProcedure<V> procedure = new CountProcedure<V>(predicate);
        MapIterate.forEachValue(map, procedure);
        return procedure.getCount();
    }

    /**
     * For each <em>entry</em> of the source map, the Predicate2 is evaluated.
     * If the result of the evaluation is true, the map entry is moved to a result map.
     * The result map is returned containing all entries in the source map that evaluated to true.
     */
    public static <K, V> MutableMap<K, V> filterMapOnEntry(
            Map<K, V> map,
            Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.filterMapOnEntry(map, predicate, UnifiedMap.<K, V>newMap());
    }

    /**
     * For each <em>entry</em> of the source map, the Predicate2 is evaluated.
     * If the result of the evaluation is true, the map entry is moved to a result map.
     * The result map is returned containing all entries in the source map that evaluated to true.
     */
    public static <K, V, R extends Map<K, V>> R filterMapOnEntry(
            Map<K, V> map,
            final Predicate2<? super K, ? super V> predicate,
            R target)
    {
        final Procedure2<K, V> mapTransferProcedure = new MapPutProcedure<K, V>(target);
        Procedure2<K, V> procedure = new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                if (predicate.accept(key, value))
                {
                    mapTransferProcedure.value(key, value);
                }
            }
        };
        MapIterate.forEachKeyValue(map, procedure);

        return target;
    }

    /**
     * For each <em>key</em> of the source map, the Predicate is evaluated.
     * If the result of the evaluation is true, the map entry is moved to a result map.
     * The result map is returned containing all entries in the source map that evaluated to true.
     */
    public static <K, V> MutableMap<K, V> filterMapOnKey(Map<K, V> map, final Predicate<? super K> predicate)
    {
        MutableMap<K, V> resultMap = UnifiedMap.newMap();
        final Procedure2<K, V> mapTransferProcedure = new MapPutProcedure<K, V>(resultMap);
        Procedure2<K, V> procedure = new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                if (predicate.accept(key))
                {
                    mapTransferProcedure.value(key, value);
                }
            }
        };
        MapIterate.forEachKeyValue(map, procedure);
        return resultMap;
    }

    /**
     * For each <em>value</em> of the source map, the Predicate is evaluated.
     * If the result of the evaluation is true, the map entry is moved to a result map.
     * The result map is returned containing all entries in the source map that evaluated to true.
     */
    public static <K, V> MutableMap<K, V> filterMapOnValue(Map<K, V> map, final Predicate<? super V> predicate)
    {
        MutableMap<K, V> resultMap = UnifiedMap.newMap();
        final Procedure2<K, V> mapTransferProcedure = new MapPutProcedure<K, V>(resultMap);
        Procedure2<K, V> procedure = new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                if (predicate.accept(value))
                {
                    mapTransferProcedure.value(key, value);
                }
            }
        };
        MapIterate.forEachKeyValue(map, procedure);
        return resultMap;
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate)
     */
    public static <K, V> MutableList<V> filterNot(Map<K, V> map, Predicate<? super V> predicate)
    {
        return MapIterate.filterNot(map, predicate, FastList.<V>newList());
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate, Collection)
     */
    public static <K, V, R extends Collection<V>> R filterNot(
            Map<K, V> map,
            Predicate<? super V> predicate,
            R targetCollection)
    {
        Procedure<V> procedure = new FilterNotProcedure<V>(predicate, targetCollection);
        MapIterate.forEachValue(map, procedure);
        return targetCollection;
    }

    /**
     * For each <em>value</em> of the map, predicate is evaluated with the element as the parameter.
     * Each element which causes predicate to evaluate to false is included in the new collection.
     */
    public static <K, V> MutableMap<K, V> filterNotMapOnEntry(
            Map<K, V> map,
            Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.filterNotMapOnEntry(map, predicate, UnifiedMap.<K, V>newMap());
    }

    /**
     * For each <em>value</em> of the map, predicate is evaluated with the element as the parameter.
     * Each element which causes predicate to evaluate to false is added to the targetCollection.
     */
    public static <K, V, R extends Map<K, V>> R filterNotMapOnEntry(
            Map<K, V> map,
            final Predicate2<? super K, ? super V> predicate,
            final R target)
    {
        MapIterate.forEachKeyValue(map, new Procedure2<K, V>()
        {
            public void value(K argument1, V argument2)
            {
                if (!predicate.accept(argument1, argument2))
                {
                    target.put(argument1, argument2);
                }
            }
        });

        return target;
    }

    /**
     * Adds all the <em>keys</em> from map to a the specified targetCollection.
     */
    public static <K, V> Collection<K> addAllKeysTo(Map<K, V> map, Collection<K> targetCollection)
    {
        MapIterate.forEachKey(map, CollectionAddProcedure.<K>on(targetCollection));
        return targetCollection;
    }

    /**
     * Adds all the <em>values</em> from map to a the specified targetCollection.
     */
    public static <K, V> Collection<V> addAllValuesTo(Map<K, V> map, Collection<V> targetCollection)
    {
        MapIterate.forEachValue(map, CollectionAddProcedure.<V>on(targetCollection));
        return targetCollection;
    }

    /**
     * @see Iterate#transform(Iterable, Function)
     */
    public static <K, V, A> MutableList<A> transform(
            Map<K, V> map,
            Function<? super V, ? extends A> function)
    {
        return MapIterate.transform(map, function, FastList.<A>newList(map.size()));
    }

    /**
     * For each value of the map, the function is evaluated with the key and value as the parameter.
     * The results of these evaluations are collected into a new UnifiedMap.
     */
    public static <K, V, K2, V2> MutableMap<K2, V2> transform(
            Map<K, V> map,
            Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return MapIterate.transform(map, function, UnifiedMap.<K2, V2>newMap(map.size()));
    }

    /**
     * For each value of the map, the function is evaluated with the key and value as the parameter.
     * The results of these evaluations are collected into the target map.
     */
    public static <K1, V1, K2, V2, R extends Map<K2, V2>> R transform(
            Map<K1, V1> map,
            final Function2<? super K1, ? super V1, Pair<K2, V2>> function,
            final R target)
    {
        MapIterate.forEachKeyValue(map, new Procedure2<K1, V1>()
        {
            public void value(K1 key, V1 value)
            {
                Pair<K2, V2> pair = function.value(key, value);
                target.put(pair.getOne(), pair.getTwo());
            }
        });
        return target;
    }

    /**
     * For each key and value of the map, the function is evaluated with the key and value as the parameter.
     * The results of these evaluations are collected into the target map.
     */
    public static <K, V, V2> MutableMap<K, V2> transformValues(
            Map<K, V> map,
            Function2<? super K, ? super V, ? extends V2> function)
    {
        return MapIterate.transformValues(map, function, UnifiedMap.<K, V2>newMap(map.size()));
    }

    /**
     * For each key and value of the map, the function is evaluated with the key and value as the parameter.
     * The results of these evaluations are collected into the target map.
     */
    public static <K, V, V2, R extends Map<K, V2>> R transformValues(
            Map<K, V> map,
            final Function2<? super K, ? super V, ? extends V2> function,
            final R target)
    {
        MapIterate.forEachKeyValue(map, new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                target.put(key, function.value(key, value));
            }
        });

        return target;
    }

    /**
     * For each value of the map, the Predicate2 is evaluated with the key and value as the parameter,
     * and if true, then <code>function</code> is applied.
     * The results of these evaluations are collected into a new map.
     */
    public static <K1, V1, K2, V2> MutableMap<K2, V2> transformIf(
            Map<K1, V1> map,
            Function2<? super K1, ? super V1, Pair<K2, V2>> function,
            Predicate2<? super K1, ? super V1> predicate)
    {
        return MapIterate.transformIf(map, function, predicate, UnifiedMap.<K2, V2>newMap());
    }

    /**
     * For each value of the map, the Predicate2 is evaluated with the key and value as the parameter,
     * and if true, then <code>function</code> is applied.
     * The results of these evaluations are collected into the target map.
     */
    public static <K1, V1, K2, V2> MutableMap<K2, V2> transformIf(
            Map<K1, V1> map,
            final Function2<? super K1, ? super V1, Pair<K2, V2>> function,
            final Predicate2<? super K1, ? super V1> predicate,
            Map<K2, V2> target)
    {
        final MutableMap<K2, V2> result = MapAdapter.adapt(target);

        MapIterate.forEachKeyValue(map, new Procedure2<K1, V1>()
        {
            public void value(K1 key, V1 value)
            {
                if (predicate.accept(key, value))
                {
                    Pair<K2, V2> pair = function.value(key, value);
                    result.put(pair.getOne(), pair.getTwo());
                }
            }
        });

        return result;
    }

    /**
     * For each key-value entry of a map, applies a function to each, and adds the transformed entry to a new Map.
     */
    public static <K1, V1, K2, V2> MutableMap<K2, V2> transform(
            Map<K1, V1> map,
            Function<? super K1, ? extends K2> keyFunction,
            Function<? super V1, ? extends V2> valueFunction)
    {
        return MapIterate.transform(map, keyFunction, valueFunction, UnifiedMap.<K2, V2>newMap());
    }

    /**
     * For each key-value entry of a map, applies a function to each, and adds the transformed entry to the target Map.
     */
    public static <K1, V1, K2, V2> MutableMap<K2, V2> transform(
            Map<K1, V1> map,
            final Function<? super K1, ? extends K2> keyFunction,
            final Function<? super V1, ? extends V2> valueFunction,
            Map<K2, V2> target)
    {
        return MapIterate.transform(map, new Function2<K1, V1, Pair<K2, V2>>()
        {
            public Pair<K2, V2> value(K1 key, V1 value)
            {
                return Tuples.pair(keyFunction.valueOf(key), valueFunction.valueOf(value));
            }
        }, MapAdapter.adapt(target));
    }

    /**
     * @see Iterate#transform(Iterable, Function, Collection)
     */
    public static <K, V, A, R extends Collection<A>> R transform(
            Map<K, V> map,
            Function<? super V, ? extends A> function,
            R targetCollection)
    {
        Procedure<V> procedure = new CollectProcedure<V, A>(function, targetCollection);
        MapIterate.forEachValue(map, procedure);
        return targetCollection;
    }

    /**
     * For each value of the map, <code>procedure</code> is evaluated with the value as the parameter.
     */
    public static <K, V> void forEachValue(Map<K, V> map, Procedure<? super V> procedure)
    {
        if (map == null)
        {
            throw new IllegalArgumentException("Cannot perform a forEachValue on null");
        }

        if (MapIterate.notEmpty(map))
        {
            if (map instanceof UnsortedMapIterable)
            {
                ((MapIterable<K, V>) map).forEachValue(procedure);
            }
            else
            {
                IterableIterate.forEach(map.values(), procedure);
            }
        }
    }

    /**
     * For each key of the map, <code>procedure</code> is evaluated with the key as the parameter.
     */
    public static <K, V> void forEachKey(Map<K, V> map, Procedure<? super K> procedure)
    {
        if (map == null)
        {
            throw new IllegalArgumentException("Cannot perform a forEachKey on null");
        }

        if (MapIterate.notEmpty(map))
        {
            if (map instanceof UnsortedMapIterable)
            {
                ((MapIterable<K, V>) map).forEachKey(procedure);
            }
            else
            {
                IterableIterate.forEach(map.keySet(), procedure);
            }
        }
    }

    /**
     * For each entry of the map, <code>procedure</code> is evaluated with the element as the parameter.
     */
    public static <K, V> void forEachKeyValue(Map<K, V> map, Procedure2<? super K, ? super V> procedure)
    {
        if (map == null)
        {
            throw new IllegalArgumentException("Cannot perform a forEachKeyValue on null");
        }

        if (MapIterate.notEmpty(map))
        {
            if (map instanceof UnsortedMapIterable)
            {
                ((MapIterable<K, V>) map).forEachKeyValue(procedure);
            }
            else
            {
                IterableIterate.forEach(map.entrySet(), new MapEntryToProcedure2<K, V>(procedure));
            }
        }
    }

    public static <K, V> Pair<K, V> find(
            Map<K, V> map,
            final Predicate2<? super K, ? super V> predicate)
    {
        if (map == null)
        {
            throw new IllegalArgumentException("Cannot perform a find on null");
        }

        if (map instanceof ImmutableMap || map instanceof MutableMap)
        {
            RichIterable<Pair<K, V>> entries;
            if (map instanceof ImmutableMap)
            {
                entries = ((ImmutableMap<K, V>) map).keyValuesView();
            }
            else
            {
                entries = LazyIterate.adapt(map.entrySet()).transform(AbstractImmutableEntry.<K, V>getPairFunction());
            }
            return entries.find(new Predicate<Pair<K, V>>()
            {
                public boolean accept(Pair<K, V> each)
                {
                    return predicate.accept(each.getOne(), each.getTwo());
                }
            });
        }

        for (Map.Entry<K, V> entry : map.entrySet())
        {
            if (predicate.accept(entry.getKey(), entry.getValue()))
            {
                return Tuples.pairFrom(entry);
            }
        }
        return null;
    }

    /**
     * @see Iterate#find(Iterable, Predicate)
     */
    public static <K, V> V find(Map<K, V> map, Predicate<? super V> predicate)
    {
        return IterableIterate.find(map.values(), predicate);
    }

    /**
     * @see Iterate#findIfNone(Iterable, Predicate, Object)
     */
    public static <K, V> V findIfNone(Map<K, V> map, Predicate<? super V> predicate, V ifNone)
    {
        return Iterate.findIfNone(map.values(), predicate, ifNone);
    }

    /**
     * @see Iterate#foldLeft(Object, Iterable, Function2)
     */
    public static <K, V, IV> IV foldLeft(
            IV injectValue,
            Map<K, V> map,
            Function2<? super IV, ? super V, ? extends IV> function)
    {
        return Iterate.foldLeft(injectValue, map.values(), function);
    }

    /**
     * Same as {@link #foldLeft}, but only applies the value to the function
     * if the predicate returns true for the value.
     *
     * @see #foldLeft
     */
    public static <IV, K, V> IV foldLeftIf(
            IV initialValue,
            Map<K, V> map,
            final Predicate<? super V> predicate,
            final Function2<? super IV, ? super V, ? extends IV> function)
    {
        Function2<IV, ? super V, IV> ifFunction = new Function2<IV, V, IV>()
        {
            public IV value(IV accumulator, V item)
            {
                if (predicate.accept(item))
                {
                    return function.value(accumulator, item);
                }
                return accumulator;
            }
        };
        return Iterate.foldLeft(initialValue, map.values(), ifFunction);
    }

    /**
     * @see Iterate#anySatisfy(Iterable, Predicate)
     */
    public static <K, V> boolean anySatisfy(Map<K, V> map, Predicate<? super V> predicate)
    {
        return IterableIterate.anySatisfy(map.values(), predicate);
    }

    /**
     * @see Iterate#allSatisfy(Iterable, Predicate)
     */
    public static <K, V> boolean allSatisfy(Map<K, V> map, Predicate<? super V> predicate)
    {
        return IterableIterate.allSatisfy(map.values(), predicate);
    }

    /**
     * Iterate over the specified map applying the specified Function to each value
     * and return the results as a List.
     */
    public static <K, V> MutableList<Pair<K, V>> toListOfPairs(Map<K, V> map)
    {
        final MutableList<Pair<K, V>> pairs = FastList.newList(map.size());
        MapIterate.forEachKeyValue(map, new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                pairs.add(Tuples.pair(key, value));
            }
        });
        return pairs;
    }

    /**
     * Iterate over the specified map applying the specified Function to each value
     * and return the results as a sorted List using the specified Comparator.
     */
    public static <K, V> MutableList<V> toSortedList(
            Map<K, V> map,
            Comparator<? super V> comparator)
    {
        return Iterate.toSortedList(map.values(), comparator);
    }

    /**
     * Return a new map swapping key-value for value-key.
     * If the original map contains entries with the same value, the result mapping is undefined,
     * in that the last entry applied wins (the order of application is undefined).
     */
    public static <K, V> MutableMap<V, K> reverseMapping(Map<K, V> map)
    {
        final MutableMap<V, K> reverseMap = UnifiedMap.newMap(map.size());
        MapIterate.forEachKeyValue(map, new Procedure2<K, V>()
        {
            public void value(K sourceKey, V sourceValue)
            {
                reverseMap.put(sourceValue, sourceKey);
            }
        });
        return reverseMap;
    }

    /**
     * Return the number of occurrences of object in the specified map.
     */
    public static <K, V> int occurrencesOf(Map<K, V> map, V object)
    {
        return Iterate.count(map.values(), Predicates.equal(object));
    }

    /**
     * Return the number of occurrences where object is equal to the specified attribute in the specified map.
     */
    public static <K, V, A> int occurrencesOfAttribute(
            Map<K, V> map,
            Function<? super V, ? extends A> function,
            A object)
    {
        return Iterate.count(map.values(), Predicates.attributeEqual(function, object));
    }
}
