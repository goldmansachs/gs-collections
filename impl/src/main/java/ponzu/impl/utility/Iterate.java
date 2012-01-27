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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.SortedSet;

import ponzu.api.InternalIterable;
import ponzu.api.RichIterable;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Function3;
import ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.collection.MutableCollection;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.multimap.MutableMultimap;
import ponzu.api.partition.PartitionIterable;
import ponzu.api.tuple.Pair;
import ponzu.api.tuple.Twin;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.factory.Predicates;
import ponzu.impl.block.procedure.CollectionAddProcedure;
import ponzu.impl.block.procedure.MapCollectProcedure;
import ponzu.impl.block.procedure.MaxComparatorProcedure;
import ponzu.impl.block.procedure.MinComparatorProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.multimap.list.FastListMultimap;
import ponzu.impl.utility.internal.DefaultSpeciesNewStrategy;
import ponzu.impl.utility.internal.IterableIterate;
import ponzu.impl.utility.internal.RandomAccessListIterate;

/**
 * The Iterate utility class acts as a router to other utility classes to provide optimized iteration pattern
 * implementations based on the type of iterable.  The lowest common denominator used will normally be IterableIterate.
 * Iterate can be used when a JDK interface is the only type available to the developer, as it can
 * determine the best way to iterate based on instanceof checks.
 *
 * @since 1.0
 */
public final class Iterate
{
    private Iterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * The procedure is evaluated for each element of the iterable.
     * <p/>
     * <pre>e.g.
     * Iterate.forEach(people, new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *         LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     */
    public static <T> void forEach(Iterable<T> iterable, Procedure<? super T> procedure)
    {
        if (iterable instanceof InternalIterable)
        {
            ((InternalIterable<T>) iterable).forEach(procedure);
        }
        else if (iterable instanceof ArrayList)
        {
            ArrayListIterate.forEach((ArrayList<T>) iterable, procedure);
        }
        else if (iterable instanceof RandomAccess)
        {
            RandomAccessListIterate.forEach((List<T>) iterable, procedure);
        }
        else if (iterable != null)
        {
            IterableIterate.forEach(iterable, procedure);
        }
        else
        {
            throw new IllegalArgumentException("Cannot perform a forEach on null");
        }
    }

    /**
     * The procedure2 is evaluated for each element of the iterable with the specified parameter passed
     * as the second argument.
     * <p/>
     * <pre>e.g.
     * Iterate.forEachWith(people, new Procedure2<Person, Person>()
     * {
     *     public void value(Person person, Person other)
     *     {
     *         if (person.isRelatedTo(other))
     *         {
     *              LOGGER.info(person.getName());
     *         }
     *     }
     * }, fred);
     * </pre>
     */
    public static <T, P> void forEachWith(
            Iterable<T> iterable,
            Procedure2<? super T, ? super P> procedure,
            P parameter)
    {
        if (iterable instanceof InternalIterable)
        {
            ((InternalIterable<T>) iterable).forEachWith(procedure, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            ArrayListIterate.forEachWith((ArrayList<T>) iterable, procedure, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            RandomAccessListIterate.forEachWith((List<T>) iterable, procedure, parameter);
        }
        else if (iterable != null)
        {
            IterableIterate.forEachWith(iterable, procedure, parameter);
        }
        else
        {
            throw new IllegalArgumentException("Cannot perform a forEachWith on null");
        }
    }

    /**
     * Iterates over a collection passing each element and the current relative int index to the specified instance of
     * ObjectIntProcedure.
     */
    public static <T> void forEachWithIndex(Iterable<T> iterable, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (iterable instanceof InternalIterable)
        {
            ((InternalIterable<T>) iterable).forEachWithIndex(objectIntProcedure);
        }
        else if (iterable instanceof ArrayList)
        {
            ArrayListIterate.forEachWithIndex((ArrayList<T>) iterable, objectIntProcedure);
        }
        else if (iterable instanceof RandomAccess)
        {
            RandomAccessListIterate.forEachWithIndex((List<T>) iterable, objectIntProcedure);
        }
        else if (iterable != null)
        {
            IterableIterate.forEachWithIndex(iterable, objectIntProcedure);
        }
        else
        {
            throw new IllegalArgumentException("Cannot perform a forEachWithIndex on null");
        }
    }

    /**
     * Returns a new collection with only the elements that evaluated to true for the specified predicate.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>filter</b>(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getAddress().getCity().equals("Metuchen");
     *     }
     * });
     * </pre>
     */
    public static <T> Collection<T> filter(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).filter(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.filter((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.filter((List<T>) iterable, predicate);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, Collection<T>>filter(
                    iterable,
                    predicate,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.filter(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform a filter on null");
    }

    /**
     * Returns a new collection with only elements that evaluated to true for the specified predicate and parameter.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>filterWith</b>(integers, Predicates2.equal(), new Integer(5));
     * </pre>
     */
    public static <T, IV> Collection<T> filterWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).filterWith(predicate, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.filterWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.filterWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, IV, Collection<T>>filterWith(
                    iterable,
                    predicate,
                    parameter,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.filterWith(iterable, predicate, parameter, FastList.<T>newList());
        }
        throw new IllegalArgumentException("Cannot perform a filterWith on null");
    }

    /**
     * Filters a collection into two separate collections based on a predicate returned via a Twin.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>patitionWith</b>(lastNames, Predicates2.lessThan(), "Mason");
     * </pre>
     */
    public static <T, IV> Twin<MutableList<T>> partitionWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).partitionWith(predicate, injectedValue);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.partitionWith((ArrayList<T>) iterable, predicate, injectedValue);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.partitionWith((List<T>) iterable, predicate, injectedValue);
        }
        else if (iterable != null)
        {
            return IterableIterate.partitionWith(iterable, predicate, injectedValue);
        }
        throw new IllegalArgumentException("Cannot perform a partitionWith on null");
    }

    /**
     * Filters a collection into a PartitionIterable based on a predicate.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>partition</b>(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getAddress().getState().getName().equals("New York");
     *     }
     * });
     * </pre>
     */
    public static <T> PartitionIterable<T> partition(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable<?>)
        {
            return ((RichIterable<T>) iterable).partition(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.partition((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.partition((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.partition(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform a partition on null");
    }

    /**
     * Returns the total number of elements that evaluate to true for the specified predicate.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>count</b>(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getAddress().getState().getName().equals("New York");
     *     }
     * });
     * </pre>
     */
    public static <T> int count(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).count(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.count((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.count((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.count(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot get a count from null");
    }

    /**
     * Returns the total number of elements that evaluate to true for the specified predicate2 and parameter.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>countWith</b>(lastNames, Predicates2.equal(), "Smith");
     * </pre>
     */
    public static <T, IV> int countWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).countWith(predicate, injectedValue);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.countWith((ArrayList<T>) iterable, predicate, injectedValue);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.countWith((List<T>) iterable, predicate, injectedValue);
        }
        else if (iterable != null)
        {
            return IterableIterate.countWith(iterable, predicate, injectedValue);
        }
        throw new IllegalArgumentException("Cannot get a count from null");
    }

    /**
     * @see RichIterable#transformIf(Predicate, Function)
     */
    public static <T, V> Collection<V> transformIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).transformIf(predicate, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.transformIf((ArrayList<T>) iterable, predicate, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.tranformIf((List<T>) iterable, predicate, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, V, Collection<V>>tranformIf(
                    iterable,
                    predicate,
                    function,
                    DefaultSpeciesNewStrategy.INSTANCE.<V>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.transformIf(iterable, predicate, function);
        }
        throw new IllegalArgumentException("Cannot perform a transformIf on null");
    }

    /**
     * @see RichIterable#transformIf(Predicate, Function, Collection)
     */
    public static <T, V, R extends Collection<V>> R tranformIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).transformIf(predicate, function, target);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.tranformIf((ArrayList<T>) iterable, predicate, function, target);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.tranformIf((List<T>) iterable, predicate, function, target);
        }
        else if (iterable != null)
        {
            return IterableIterate.tranformIf(iterable, predicate, function, target);
        }
        throw new IllegalArgumentException("Cannot perform a transformIf on null");
    }

    /**
     * Same as the filter method with two parameters but uses the specified target collection
     * <p/>
     * <pre>e.g.
     * return Iterate.filter(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * }, FastList.newList());
     * </pre>
     * <p/>
     * <pre>e.g.
     * return Iterate.filter(collection, Predicates.attributeEqual("lastName", "Smith"), new ArrayList());
     * </pre>
     */
    public static <T, R extends Collection<T>> R filter(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).filter(predicate, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.filter((ArrayList<T>) iterable, predicate, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.filter((List<T>) iterable, predicate, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.filter(iterable, predicate, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a filter on null");
    }

    /**
     * Same as the filterWith method with two parameters but uses the specified target collection.
     */
    public static <T, P, R extends Collection<T>> R filterWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).filterWith(predicate, parameter, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.filterWith((ArrayList<T>) iterable, predicate, parameter, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.filterWith((List<T>) iterable, predicate, parameter, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.filterWith(iterable, predicate, parameter, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a filterWith on null");
    }

    /**
     * Returns the first count elements of the iterable or the iterable itself if count is greater than the length of
     * the iterable.
     *
     * @param iterable the collection to take from.
     * @param count    the number of items to take.
     * @return a new list with the items take from the given collection.
     */
    public static <T> Collection<T> take(Iterable<T> iterable, int count)
    {
        if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.take((ArrayList<T>) iterable, count);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.take((List<T>) iterable, count);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, Collection<T>>take(
                    iterable,
                    count,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable, count));
        }
        else if (iterable != null)
        {
            return IterableIterate.take(iterable, count);
        }
        throw new IllegalArgumentException("Cannot perform a take on null");
    }

    /**
     * Returns a collection without the first count elements of the iterable or the iterable itself if count is
     * non-positive.
     *
     * @param iterable the collection to drop from.
     * @param count    the number of items to drop.
     * @return a new list with the items dropped from the given collection.
     */
    public static <T> Collection<T> drop(Iterable<T> iterable, int count)
    {
        if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.drop((List<T>) iterable, count);
        }
        else if (iterable != null)
        {
            return IterableIterate.drop(iterable, count);
        }
        throw new IllegalArgumentException("Cannot perform a drop on null");
    }

    /**
     * Returns all elements of the iterable that evaluate to false for the specified predicate.
     * <p/>
     * <pre>e.g.
     * return Iterate.filterNot(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     * <p/>
     * <pre>e.g.
     * return Iterate.filterNot(collection, Predicates.attributeEqual("lastName", "Smith"));
     * </pre>
     */
    public static <T> Collection<T> filterNot(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).filterNot(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.filterNot((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.filterNot((List<T>) iterable, predicate);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, Collection<T>>filterNot(
                    iterable,
                    predicate,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.filterNot(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform a filterNot on null");
    }

    /**
     * SortThis is a mutating method.  The List passed in is also returned.
     */
    public static <T extends Comparable<? super T>, L extends List<T>> L sortThis(L list)
    {
        if (list instanceof MutableList<?>)
        {
            ((MutableList<T>) list).sortThis();
        }
        else if (list instanceof ArrayList)
        {
            ArrayListIterate.sortThis((ArrayList<T>) list);
        }
        else
        {
            if (list.size() > 1)
            {
                Collections.sort(list);
            }
        }
        return list;
    }

    /**
     * SortThis is a mutating method.  The List passed in is also returned.
     */
    public static <T, L extends List<T>> L sortThis(L list, Comparator<? super T> comparator)
    {
        if (list instanceof MutableList)
        {
            ((MutableList<T>) list).sortThis(comparator);
        }
        else if (list instanceof ArrayList)
        {
            ArrayListIterate.sortThis((ArrayList<T>) list, comparator);
        }
        else
        {
            if (list.size() > 1)
            {
                Collections.sort(list, comparator);
            }
        }
        return list;
    }

    /**
     * SortThis is a mutating method.  The List passed in is also returned.
     */
    public static <T, L extends List<T>> L sortThis(L list, final Predicate2<? super T, ? super T> predicate)
    {
        return Iterate.sortThis(
                list, new Comparator<T>()
        {
            public int compare(T o1, T o2)
            {
                if (predicate.accept(o1, o2))
                {
                    return -1;
                }
                if (predicate.accept(o2, o1))
                {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     * Sort the list by comparing an attribute defined by the function.
     * SortThisBy is a mutating method.  The List passed in is also returned.
     */
    public static <T, V extends Comparable<V>, L extends List<T>> L sortThisBy(L list, Function<? super T, ? extends V> function)
    {
        return Iterate.sortThis(list, Comparators.byFunction(function));
    }

    /**
     * Removes all elements from the iterable that evaluate to true for the specified predicate.
     */
    public static <T> Collection<T> removeIf(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof MutableCollection)
        {
            MutableCollection<T> mutableCollection = (MutableCollection<T>) iterable;
            mutableCollection.removeIf(predicate);
            return mutableCollection;
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.removeIf((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.removeIf((List<T>) iterable, predicate);
        }
        else if (iterable instanceof Collection)
        {
            IterableIterate.removeIf(iterable, predicate);
            return (Collection<T>) iterable;
        }
        else if (iterable != null)
        {
            IterableIterate.removeIf(iterable, predicate);
            // TODO: should this method return Iterable instead?  Would seem less useful if it did
            return null;
        }
        throw new IllegalArgumentException("Cannot perform a remove on null");
    }

    /**
     * Removes all elements of the iterable that evaluate to true for the specified predicate2 and parameter.
     */
    public static <T, P> Collection<T> removeIfWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            MutableCollection<T> mutableCollection = (MutableCollection<T>) iterable;
            mutableCollection.removeIfWith(predicate, parameter);
            return mutableCollection;
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.removeIfWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.removeIfWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof Collection)
        {
            IterableIterate.removeIfWith(iterable, predicate, parameter);
            return (Collection<T>) iterable;
        }
        else if (iterable != null)
        {
            IterableIterate.removeIfWith(iterable, predicate, parameter);
            // TODO: should this method return Iterarable instead?  Would seem less useful if it did
            return null;
        }
        throw new IllegalArgumentException("Cannot perform a remove on null");
    }

    /**
     * Returns all elements of the iterable that evaluate to false for the specified predicate2 and parameter.
     */
    public static <T, IV> Collection<T> filterNotWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).filterNotWith(predicate, injectedValue);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.filterNotWith((ArrayList<T>) iterable, predicate, injectedValue);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.filterNotWith((List<T>) iterable, predicate, injectedValue);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, IV, Collection<T>>filterNotWith(
                    iterable,
                    predicate,
                    injectedValue,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.filterNotWith(iterable, predicate, injectedValue, FastList.<T>newList());
        }
        throw new IllegalArgumentException("Cannot perform a filterNotWith on null");
    }

    /**
     * Same as the filterNot method with two parameters but uses the specified target collection for the results.
     * <p/>
     * <pre>e.g.
     * return Iterate.filterNot(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * }, FastList.newList());
     * </pre>
     */
    public static <T, R extends Collection<T>> R filterNot(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).filterNot(predicate, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.filterNot((ArrayList<T>) iterable, predicate, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.filterNot((List<T>) iterable, predicate, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.filterNot(iterable, predicate, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a filterNot on null");
    }

    /**
     * Same as the filterNotWith method with two parameters but uses the specified target collection.
     */
    public static <T, P, R extends Collection<T>> R filterNotWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).filterNotWith(predicate, parameter, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.filterNotWith(
                    (ArrayList<T>) iterable,
                    predicate,
                    parameter,
                    targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.filterNotWith((List<T>) iterable, predicate, parameter, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.filterNotWith(iterable, predicate, parameter, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a filterNotWith on null");
    }

    /**
     * Add all elements from the source Iterable to the target collection, return the target collection
     */
    public static <T, R extends Collection<T>> R addAllTo(Iterable<? extends T> iterable, R targetCollection)
    {
        Iterate.addAllIterable(iterable, targetCollection);
        return targetCollection;
    }

    public static <T> boolean addAllIterable(Iterable<? extends T> iterable, Collection<T> targetCollection)
    {
        if (iterable == null)
        {
            throw new NullPointerException();
        }
        if (iterable instanceof Collection<?>)
        {
            return targetCollection.addAll((Collection<T>) iterable);
        }
        int oldSize = targetCollection.size();
        Iterate.forEach(iterable, CollectionAddProcedure.on(targetCollection));
        return targetCollection.size() != oldSize;
    }

    /**
     * Returns a new collection with the results of applying the specified function for each element of the iterable.
     * <p/>
     * <pre>e.g.
     * return Iterate.transform(collection, new Function&lt;Person, String&gt;()
     * {
     *     public String value(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * });
     * </pre>
     */
    public static <T, V> Collection<V> transform(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).transform(function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.transform((ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.transform((List<T>) iterable, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, V, Collection<V>>transform(
                    iterable,
                    function,
                    DefaultSpeciesNewStrategy.INSTANCE.<V>speciesNew(
                            (Collection<T>) iterable,
                            ((Collection<T>) iterable).size()));
        }
        else if (iterable != null)
        {
            return IterableIterate.transform(iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform a transform on null");
    }

    /**
     * @see RichIterable#flatTransform(Function)
     */
    public static <T, V> Collection<V> flatTransform(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).flatTransform(function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.flatTransform((ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.flatTransform((List<T>) iterable, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, V, Collection<V>>flatTransform(
                    iterable,
                    function,
                    DefaultSpeciesNewStrategy.INSTANCE.<V>speciesNew(
                            (Collection<T>) iterable,
                            ((Collection<T>) iterable).size()));
        }
        else if (iterable != null)
        {
            return IterableIterate.flatTransform(iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform a flatTransform on null");
    }

    /**
     * Same as the transform method with two parameters, except that the results are gathered into the specified
     * targetCollection
     * <p/>
     * <pre>e.g.
     * return Iterate.transform(collection, new Function&lt;Person, String&gt;()
     * {
     *     public String value(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * }, FastList.newList());
     * </pre>
     */
    public static <T, A, R extends Collection<A>> R transform(
            Iterable<T> iterable,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).transform(function, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.transform((ArrayList<T>) iterable, function, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.transform((List<T>) iterable, function, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.transform(iterable, function, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a transform on null");
    }

    /**
     * @see RichIterable#flatTransform(Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R flatTransform(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<A>> function,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).flatTransform(function, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.flatTransform((ArrayList<T>) iterable, function, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.flatTransform((List<T>) iterable, function, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.flatTransform(iterable, function, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a flatTransform on null");
    }

    /**
     * Same as transform with a Function2 and specified parameter which is passed to the function.
     */
    public static <T, P, A> Collection<A> transformWith(
            Iterable<T> iterable,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).transformWith(function, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.transformWith((ArrayList<T>) iterable, function, parameter);
        }
        else if (iterable instanceof List<?>)
        {
            return ListIterate.transformWith((List<T>) iterable, function, parameter);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, P, A, Collection<A>>transformWith(
                    iterable,
                    function,
                    parameter,
                    DefaultSpeciesNewStrategy.INSTANCE.<A>speciesNew(
                            (Collection<T>) iterable,
                            ((Collection<T>) iterable).size()));
        }
        else if (iterable != null)
        {
            return IterableIterate.transformWith(iterable, function, parameter);
        }
        throw new IllegalArgumentException("Cannot perform a transformWith on null");
    }

    /**
     * Same as transformWith but with a targetCollection parameter to gather the results.
     */
    public static <T, P, A, R extends Collection<A>> R transformWith(
            Iterable<T> iterable,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).transformWith(function, parameter, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.transformWith((ArrayList<T>) iterable, function, parameter, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.transformWith((List<T>) iterable, function, parameter, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.transformWith(iterable, function, parameter, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a transformWith on null");
    }

    /**
     * Flattens a collection of collections into one "flat" collection.
     *
     * @param iterable A list of lists, e.g. { { 1,2,3 } , { 4,5 }, { 6 } }
     * @return A flattened list, e.g. { 1,2,3,4,5,6 }
     */
    public static <T> Collection<T> flatten(Iterable<? extends Iterable<T>> iterable)
    {
        return Iterate.flatTransform(iterable, Functions.<Iterable<T>>getPassThru());
    }

    /**
     * Same as {@link #flatten(Iterable)} except that the results are gathered into the specified targetCollection.
     */
    public static <T, R extends Collection<T>> R flatten(Iterable<? extends Iterable<T>> iterable, R targetCollection)
    {
        return Iterate.flatTransform(iterable, Functions.<Iterable<T>>getPassThru(), targetCollection);
    }

    /**
     * Returns the first element of a collection.  In the case of a List it is the element at the first index.  In the
     * case of any other Collection, it is the first element that would be returned during an iteration. If the
     * Collection is null, or empty, the result is <code>null</code>.
     * <p/>
     * WARNING!!! The order of Sets are not guaranteed (except for TreeSets and other Ordered Set implementations), so
     * if you use this method, the first element could be any element from the Set.
     */
    public static <T> T getFirst(Iterable<T> iterable)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).getFirst();
        }
        else if (iterable instanceof List)
        {
            return ListIterate.getFirst((List<T>) iterable);
        }
        else if (iterable instanceof SortedSet && !((SortedSet<T>) iterable).isEmpty())
        {
            return ((SortedSet<T>) iterable).first();
        }
        else if (iterable instanceof Collection)
        {
            return Iterate.isEmpty(iterable) ? null : iterable.iterator().next();
        }
        else if (iterable != null)
        {
            return IterableIterate.getFirst(iterable);
        }
        throw new IllegalArgumentException("Cannot get first from null");
    }

    /**
     * A null-safe check on a collection to see if it isEmpty.  A null collection results in a true.
     */
    public static boolean isEmpty(Iterable<?> iterable)
    {
        if (iterable == null)
        {
            return true;
        }
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<?>) iterable).isEmpty();
        }
        if (iterable instanceof Collection)
        {
            return ((Collection<?>) iterable).isEmpty();
        }
        return IterableIterate.isEmpty(iterable);
    }

    /**
     * A null-safe check on a collection to see if it is notEmpty.  A null collection results in a false.
     */
    public static boolean notEmpty(Iterable<?> iterable)
    {
        return !Iterate.isEmpty(iterable);
    }

    /**
     * Returns the last element of a collection.  In the case of a List it is the element at the last index.  In the
     * case of any other Collection, it is the last element that would be returned during an iteration. If the
     * Collection is null, or empty, the result is <code>null</code>.
     * <p/>
     * WARNING!!! The order of Sets are not guaranteed (except for TreeSets and other Ordered Set implementations), so
     * if you use this method, the last element could be any element from the Set.
     */
    public static <T> T getLast(Iterable<T> iterable)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).getLast();
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.getLast((List<T>) iterable);
        }
        else if (iterable instanceof SortedSet && !((SortedSet<T>) iterable).isEmpty())
        {
            return ((SortedSet<T>) iterable).last();
        }
        else if (iterable instanceof LinkedList && !((LinkedList<T>) iterable).isEmpty())
        {
            return ((LinkedList<T>) iterable).getLast();
        }
        else if (iterable != null)
        {
            return IterableIterate.getLast(iterable);
        }
        throw new IllegalArgumentException("Cannot get last from null");
    }

    /**
     * Returns the first element of the iterable that evaluates to true for the specified predicate, or null if
     * no element evaluates to true.
     * <p/>
     * <pre>e.g.
     * return Iterate.find(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getFirstName().equals("John") && person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     */
    public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).find(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.find((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.find((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.find(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform find on null");
    }

    /**
     * Returns the first element of the iterable that evaluates to true for the specified predicate2 and parameter,
     * or null if no element evaluates to true.
     * <p/>
     * <pre>e.g.
     * Iterate.findWith(collection, new Predicate2&lt;Person, String&gt;()
     * {
     *     public boolean value(Person person, String fullName)
     *     {
     *         return person.getFullName().equals(fullName);
     *     }
     * }, "John Smith");
     * </pre>
     */
    public static <T, P> T findWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).findWith(predicate, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.findWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.findWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.findWith(iterable, predicate, parameter);
        }
        throw new IllegalArgumentException("Cannot perform findWith on null");
    }

    /**
     * Returns the first element of the iterable that evaluates to true for the specified predicate, or returns the
     * result ifNone if no element evaluates to true.
     */
    public static <T> T findIfNone(Iterable<T> iterable, Predicate<? super T> predicate, T ifNone)
    {
        T result = Iterate.find(iterable, predicate);
        return result == null ? ifNone : result;
    }

    /**
     * Returns the first element of the iterable that evaluates to true for the specified predicate2 and parameter,
     * or returns the result ifNone if no element evaluates to true.
     */
    public static <T, P> T findWithIfNone(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            T ifNone)
    {
        T result = Iterate.findWith(iterable, predicate, parameter);
        return result == null ? ifNone : result;
    }

    /**
     * Searches for the first occurrence where the predicate evaluates to true.
     */
    public static <T> int findIndex(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof ArrayList<?>)
        {
            return ArrayListIterate.findIndex((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof List<?>)
        {
            return ListIterate.findIndex((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.findIndex(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform findIndex on null");
    }

    /**
     * Searches for the first occurrence where the predicate evaluates to true.
     */
    public static <T, P> int findIndexWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof ArrayList<?>)
        {
            return ArrayListIterate.findIndexWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof List<?>)
        {
            return ListIterate.findIndexWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.findIndexWith(iterable, predicate, parameter);
        }
        throw new IllegalArgumentException("Cannot perform findIndexWith on null");
    }

    /**
     * @see RichIterable#foldLeft(Object, Function2)
     */
    public static <T, IV> IV foldLeft(
            IV injectValue,
            Iterable<T> iterable,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).foldLeft(injectValue, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.foldLeft(injectValue, (ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.foldLeft(injectValue, (List<T>) iterable, function);
        }
        else if (iterable != null)
        {
            return IterableIterate.foldLeft(injectValue, iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform an foldLeft on null");
    }

    /**
     * @see RichIterable#foldLeft(int, IntObjectToIntFunction)
     */
    public static <T> int foldLeft(
            int injectValue,
            Iterable<T> iterable,
            IntObjectToIntFunction<? super T> function)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).foldLeft(injectValue, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.foldLeft(injectValue, (ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.foldLeft(injectValue, (List<T>) iterable, function);
        }
        else if (iterable != null)
        {
            return IterableIterate.foldLeft(injectValue, iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform an foldLeft on null");
    }

    /**
     * @see RichIterable#foldLeft(long, LongObjectToLongFunction)
     */
    public static <T> long foldLeft(
            long injectValue,
            Iterable<T> iterable,
            LongObjectToLongFunction<? super T> function)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).foldLeft(injectValue, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.foldLeft(injectValue, (ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.foldLeft(injectValue, (List<T>) iterable, function);
        }
        else if (iterable != null)
        {
            return IterableIterate.foldLeft(injectValue, iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform an foldLeft on null");
    }

    /**
     * @see RichIterable#foldLeft(double, DoubleObjectToDoubleFunction)
     */
    public static <T> double foldLeft(
            double injectValue,
            Iterable<T> iterable,
            DoubleObjectToDoubleFunction<? super T> function)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).foldLeft(injectValue, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.foldLeft(injectValue, (ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.foldLeft(injectValue, (List<T>) iterable, function);
        }
        else if (iterable != null)
        {
            return IterableIterate.foldLeft(injectValue, iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform an foldLeft on null");
    }

    public static <T, IV, P> IV foldLeftWith(
            IV injectValue,
            Iterable<T> iterable,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).foldLeftWith(injectValue, function, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.foldLeftWith(injectValue, (ArrayList<T>) iterable, function, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.foldLeftWith(injectValue, (List<T>) iterable, function, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.foldLeftWith(injectValue, iterable, function, parameter);
        }
        throw new IllegalArgumentException("Cannot perform an foldLeftWith on null");
    }

    /**
     * Returns true if the predicate evaluates to true for any element of the iterable.
     * Returns false if the iterable is empty or if no elements return true for the predicate.
     */
    public static <T> boolean anySatisfy(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).anySatisfy(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.anySatisfy((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.anySatisfy((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.anySatisfy(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform an anySatisfy on null");
    }

    /**
     * Returns true if the predicate2 and parameter evaluates to true for any element of the iterable.
     * Returns false if the iterable is empty or if no elements return true for the predicate2.
     */
    public static <T, P> boolean anySatisfyWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).anySatisfyWith(predicate, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.anySatisfyWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.anySatisfyWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.anySatisfyWith(iterable, predicate, parameter);
        }
        throw new IllegalArgumentException("Cannot perform an anySatisfyWith on null");
    }

    /**
     * Returns true if the predicate evaluates to true for every element of the iterable, or returns false.
     */
    public static <T> boolean allSatisfy(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).allSatisfy(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.allSatisfy((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.allSatisfy((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.allSatisfy(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform an allSatisfy on null");
    }

    /**
     * Returns true if the predicate evaluates to true for every element of the iterable, or returns false.
     */
    public static <T, P> boolean allSatisfyWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).allSatisfyWith(predicate, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.allSatisfyWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.allSatisfyWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.allSatisfyWith(iterable, predicate, parameter);
        }
        throw new IllegalArgumentException("Cannot perform an allSatisfyWith on null");
    }

    /**
     * Iterate over the specified collection applying the specified Function to each element to calculate
     * a key and return the results as a Map.
     */
    public static <T, K> MutableMap<K, T> toMap(
            Iterable<T> iterable,
            Function<? super T, ? extends K> keyFunction)
    {
        MutableMap<K, T> map = UnifiedMap.newMap();
        Iterate.forEach(iterable, new MapCollectProcedure<T, K, T>(map, keyFunction));
        return map;
    }

    /**
     * Iterate over the specified collection applying the specified Functions to each element to calculate
     * a key and value, and return the results as a Map.
     */
    public static <T, K, V> MutableMap<K, V> toMap(
            Iterable<T> iterable,
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return Iterate.addToMap(iterable, keyFunction, valueFunction, UnifiedMap.<K, V>newMap());
    }

    /**
     * Iterate over the specified collection applying a specific Function to each element to calculate a
     * key, and return the results as a Map.
     */
    public static <T, K, V, M extends Map<K, V>> M addToMap(
            Iterable<T> iterable,
            Function<? super T, ? extends K> keyFunction,
            M map)
    {
        Iterate.forEach(iterable, new MapCollectProcedure<T, K, V>(map, keyFunction));
        return map;
    }

    /**
     * Iterate over the specified collection applying the specified Functions to each element to calculate
     * a key and value, and return the results as a Map.
     */
    public static <T, K, V, M extends Map<K, V>> M addToMap(
            Iterable<T> iterable,
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction,
            M map)
    {
        Iterate.forEach(iterable, new MapCollectProcedure<T, K, V>(map, keyFunction, valueFunction));
        return map;
    }

    /**
     * Return the specified collection as a sorted List.
     */
    public static <T extends Comparable<? super T>> MutableList<T> toSortedList(Iterable<T> iterable)
    {
        return Iterate.toSortedList(iterable, Comparators.naturalOrder());
    }

    /**
     * Return the specified collection as a sorted List using the specified Comparator.
     */
    public static <T> MutableList<T> toSortedList(Iterable<T> iterable, Comparator<? super T> comparator)
    {
        return FastList.<T>newList(iterable).sortThis(comparator);
    }

    /**
     * Returns the size of an iterable.  In the case of Collections and RichIterables, the method size is called.  All
     * other iterables will force a complete iteration to happen, which can be unnecessarily costly.
     */
    public static int sizeOf(Iterable<?> iterable)
    {
        if (iterable instanceof Collection)
        {
            return ((Collection<?>) iterable).size();
        }
        else if (iterable instanceof RichIterable)
        {
            return ((RichIterable<?>) iterable).size();
        }
        return Iterate.count(iterable, Predicates.alwaysTrue());
    }

    /**
     * Returns true if the iterable contains the value.  In the case of Collections and RichIterables, the method contains
     * is called.  All other iterables will force a complete iteration to happen, which can be unnecessarily costly.
     */
    public static boolean contains(Iterable<?> iterable, Object value)
    {
        if (iterable instanceof Collection)
        {
            return ((Collection<?>) iterable).contains(value);
        }
        else if (iterable instanceof RichIterable)
        {
            return ((RichIterable<?>) iterable).contains(value);
        }
        return IterableIterate.findIndex(iterable, Predicates.equal(value)) > -1;
    }

    /**
     * Converts the specified iterable to an array.
     */
    public static <T> Object[] toArray(Iterable<T> iterable)
    {
        if (iterable == null)
        {
            throw new NullPointerException();
        }
        if (iterable instanceof Collection)
        {
            return ((Collection<T>) iterable).toArray();
        }
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).toArray();
        }
        MutableList<T> result = Lists.mutable.of();
        Iterate.addAllTo(iterable, result);
        return result.toArray();
    }

    /**
     * Copies the specified iterable into the specified array.
     */
    public static <T> T[] toArray(Iterable<? extends T> iterable, T[] target)
    {
        if (iterable instanceof Collection)
        {
            return ((Collection<T>) iterable).toArray(target);
        }
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).toArray(target);
        }
        MutableList<T> result = Lists.mutable.of();
        Iterate.addAllTo(iterable, result);
        return result.toArray(target);
    }

    /**
     * @see RichIterable#groupBy(Function)
     */
    public static <T, V> MutableMultimap<V, T> groupBy(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).groupBy(function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.groupBy((ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupBy((List<T>) iterable, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.groupBy(iterable, function, FastListMultimap.<V, T>newMultimap());
        }
        else if (iterable != null)
        {
            return IterableIterate.groupBy(iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform a groupBy on null");
    }

    /**
     * @see RichIterable#groupBy(Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function,
            R targetMultimap)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).groupBy(function, targetMultimap);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.groupBy((ArrayList<T>) iterable, function, targetMultimap);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupBy((List<T>) iterable, function, targetMultimap);
        }
        else if (iterable != null)
        {
            return IterableIterate.groupBy(iterable, function, targetMultimap);
        }
        throw new IllegalArgumentException("Cannot perform a groupBy on null");
    }

    /**
     * @see RichIterable#groupByEach(Function)
     */
    public static <T, V> MutableMultimap<V, T> groupByEach(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).groupByEach(function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.groupByEach((ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupByEach((List<T>) iterable, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.groupByEach(iterable, function, FastListMultimap.<V, T>newMultimap());
        }
        else if (iterable != null)
        {
            return IterableIterate.groupByEach(iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform a groupByEach on null");
    }

    /**
     * @see RichIterable#groupByEach(Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).groupByEach(function, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.groupByEach((ArrayList<T>) iterable, function, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupByEach((List<T>) iterable, function, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.groupByEach(iterable, function, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a groupBy on null");
    }

    /**
     * @see RichIterable#min(Comparator)
     */
    public static <T> T min(Iterable<T> iterable, Comparator<? super T> comparator)
    {
        MinComparatorProcedure<T> procedure = new MinComparatorProcedure<T>(comparator);
        Iterate.forEach(iterable, procedure);
        return procedure.getResult();
    }

    /**
     * @see RichIterable#max(Comparator)
     */
    public static <T> T max(Iterable<T> iterable, Comparator<? super T> comparator)
    {
        MaxComparatorProcedure<T> procedure = new MaxComparatorProcedure<T>(comparator);
        Iterate.forEach(iterable, procedure);
        return procedure.getResult();
    }

    /**
     * @see RichIterable#min()
     */
    public static <T> T min(Iterable<T> iterable)
    {
        return Iterate.min(iterable, Comparators.naturalOrder());
    }

    /**
     * @see RichIterable#max()
     */
    public static <T> T max(Iterable<T> iterable)
    {
        return Iterate.max(iterable, Comparators.naturalOrder());
    }

    public static <T> T getOnly(Iterable<T> iterable)
    {
        if (iterable != null)
        {
            return IterableIterate.getOnly(iterable);
        }
        throw new IllegalArgumentException("Cannot perform getOnly on null");
    }

    /**
     * @see RichIterable#zip(Iterable)
     */
    public static <X, Y> Collection<Pair<X, Y>> zip(Iterable<X> xs, Iterable<Y> ys)
    {
        if (xs instanceof MutableCollection)
        {
            return ((MutableCollection<X>) xs).zip(ys);
        }
        else if (xs instanceof ArrayList)
        {
            return ArrayListIterate.zip((ArrayList<X>) xs, ys);
        }
        else if (xs instanceof RandomAccess)
        {
            return RandomAccessListIterate.zip((List<X>) xs, ys);
        }
        else if (xs != null)
        {
            return IterableIterate.zip(xs, ys);
        }
        throw new IllegalArgumentException("Cannot perform a zip on null");
    }

    /**
     * @see RichIterable#zip(Iterable, Collection)
     */
    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(
            Iterable<X> xs,
            Iterable<Y> ys,
            R targetCollection)
    {
        if (xs instanceof RichIterable)
        {
            return ((RichIterable<X>) xs).zip(ys, targetCollection);
        }
        else if (xs instanceof ArrayList)
        {
            return ArrayListIterate.zip((ArrayList<X>) xs, ys, targetCollection);
        }
        else if (xs instanceof RandomAccess)
        {
            return RandomAccessListIterate.zip((List<X>) xs, ys, targetCollection);
        }
        else if (xs != null)
        {
            return IterableIterate.zip(xs, ys, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a zip on null");
    }

    /**
     * @see RichIterable#zipWithIndex()
     */
    public static <T> Collection<Pair<T, Integer>> zipWithIndex(Iterable<T> iterable)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).zipWithIndex();
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.zipWithIndex((ArrayList<T>) iterable);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.zipWithIndex((List<T>) iterable);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, Collection<Pair<T, Integer>>>zipWithIndex(
                    iterable,
                    DefaultSpeciesNewStrategy.INSTANCE.<Pair<T, Integer>>speciesNew(
                            (Collection<T>) iterable,
                            ((Collection<T>) iterable).size()));
        }
        else if (iterable != null)
        {
            return IterableIterate.zipWithIndex(iterable);
        }
        throw new IllegalArgumentException("Cannot perform a zipWithIndex on null");
    }

    /**
     * @see RichIterable#zipWithIndex(Collection)
     */
    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(
            Iterable<T> iterable,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).zipWithIndex(targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.zipWithIndex((ArrayList<T>) iterable, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.zipWithIndex((List<T>) iterable, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.zipWithIndex(iterable, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a zipWithIndex on null");
    }

    /**
     * @see RichIterable#chunk(int)
     */
    public static <T> RichIterable<RichIterable<T>> chunk(Iterable<T> iterable, int size)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).chunk(size);
        }
        else if (iterable != null)
        {
            return IterableIterate.chunk(iterable, size);
        }
        throw new IllegalArgumentException("Cannot perform a chunk on null");
    }
}
