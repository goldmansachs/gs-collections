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

package com.gs.collections.api;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Pair;

/**
 * RichIterable is an interface which extends the InternalIterable interface with several internal iterator methods, from
 * the Smalltalk Collection protocol.  These include select, reject, detect, collect, injectInto, anySatisfy,
 * allSatisfy. The API also includes converter methods to convert a RichIterable to a List (toList), to a sorted
 * List (toSortedList), to a Set (toSet), and to a Map (toMap).
 *
 * @since 1.0
 */
public interface
        RichIterable<T>
        extends InternalIterable<T>
{
    /**
     * Returns the number of items in this iterable.
     *
     * @since 1.0
     */
    int size();

    /**
     * Returns true if this iterable has zero items.
     *
     * @since 1.0
     */
    boolean isEmpty();

    /**
     * The English equivalent of !this.isEmpty()
     *
     * @since 1.0
     */
    boolean notEmpty();

    /**
     * Returns the first element of an iterable.  In the case of a List it is the element at the first index.  In the
     * case of any other Collection, it is the first element that would be returned during an iteration.  If the
     * iterable is empty, null is returned.  If null is a valid element of the container, then a developer would need to
     * check to see if the iterable is empty to validate that a null result was not due to the container being empty.
     * <p/>
     * The order of Sets are not guaranteed (except for TreeSets and other Ordered Set implementations), so if you use
     * this method, the first element could be any element from the Set.
     *
     * @since 1.0
     */
    T getFirst();

    /**
     * Returns the last element of an iterable.  In the case of a List it is the element at the last index.  In the case
     * of any other Collection, it is the last element that would be returned during an iteration.  If the iterable is
     * empty, null is returned.  If null is a valid element of the container, then a developer would need to check to
     * see if the iterable is empty to validate that a null result was not due to the container being empty.
     * <p/>
     * The order of Sets are not guaranteed (except for TreeSets and other Ordered Set implementations), so if you use
     * this method, the last element could be any element from the Set.
     *
     * @since 1.0
     */
    T getLast();

    /**
     * Returns true if the iterable has an element which responds true to element.equals(object).
     *
     * @since 1.0
     */
    boolean contains(Object object);

    /**
     * Returns true if all elements in source are contained in this collection.
     *
     * @since 1.0
     */
    boolean containsAllIterable(Iterable<?> source);

    /**
     * @see Collection#containsAll(Collection)
     * @since 1.0
     */
    boolean containsAll(Collection<?> source);

    /**
     * Returns true if all elements in the specified var arg array are contained in this collection
     *
     * @since 1.0
     */
    boolean containsAllArguments(Object... elements);

    /**
     * Returns all elements of the source collection that return true when evaluating the predicate.  This method is also
     * commonly called filter.
     * <p/>
     * <pre>e.g.
     * return people.<b>select</b>(new Predicate&lt;Person&gt;()
     * {
     *     public boolean accept(Person person)
     *     {
     *         return person.getAddress().getCity().equals("Metuchen");
     *     }
     * });
     * </pre>
     *
     * @since 1.0
     */
    RichIterable<T> select(Predicate<? super T> predicate);

    /**
     * Same as the select method with one parameter but uses the specified target collection for the results.
     * <p/>
     * <pre>e.g.
     * return people.select(new Predicate&lt;Person&gt;()
     * {
     *     public boolean accept(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * }, Lists.mutable.of());
     * </pre>
     * <p/>
     * <pre>e.g.
     * return collection.select(Predicates.attributeEqual("lastName", "Smith"), new ArrayList());
     * </pre>
     *
     * @param predicate a {@link Predicate} to use as the select criteria
     * @param target    the Collection to append to for all elements in this {@code RichIterable} that meet select criteria {@code predicate}
     * @return {@code target}, which contains appended elements as a result of the select criteria
     * @see #select(Predicate)
     * @since 1.0
     */
    <R extends Collection<T>> R select(Predicate<? super T> predicate, R target);

    /**
     * Similar to {@link #select(Predicate, Collection)}, except with an evaluation parameter for the second generic argument in {@link Predicate2}.
     *
     * @param predicate        a {@link Predicate2} to use as the select criteria
     * @param parameter        a parameter to pass in for evaluation of the second argument {@code P} in {@code predicate}
     * @param targetCollection the Collection to append to for all elements in this {@code RichIterable} that meet select criteria {@code predicate}
     * @return {@code targetCollection}, which contains appended elements as a result of the select criteria
     * @see #select(Predicate)
     * @see #select(Predicate, Collection)
     * @since 1.0
     */
    <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection);

    /**
     * Returns all elements of the source collection that return false when evaluating of the predicate.  This method is also
     * sometimes called filterNot and is the equivalent of calling iterable.select(Predicates.not(predicate)).
     * <p/>
     * <pre>e.g.
     * return people.reject(new Predicate&lt;Person&gt;()
     * {
     *     public boolean accept(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     * <p/>
     * <pre>e.g.
     * return people.reject(Predicates.attributeEqual("lastName", "Smith"));
     * </pre>
     *
     * @param predicate a {@link Predicate} to use as the reject criteria
     * @return a RichIterable that contains elements that cause {@link Predicate#accept(Object)} method to evaluate to false
     * @since 1.0
     */
    RichIterable<T> reject(Predicate<? super T> predicate);

    /**
     * Same as the reject method with one parameter but uses the specified target collection for the results.
     * <p/>
     * <pre>e.g.
     * return people.reject(new Predicate&lt;Person&gt;()
     * {
     *     public boolean accept(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * }, Lists.mutable.of());
     * </pre>
     *
     * @param predicate a {@link Predicate} to use as the reject criteria
     * @param target    the Collection to append to for all elements in this {@code RichIterable} that cause {@code Predicate#accept(Object)} method to evaluate to false
     * @return {@code target}, which contains appended elements as a result of the reject criteria
     * @since 1.0
     */
    <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target);

    /**
     * Similar to {@link #reject(Predicate, Collection)}, except with an evaluation parameter for the second generic argument in {@link Predicate2}.
     * <p/>
     * E.g. return a {@link Collection} of Person elements where the person has a height <b>greater than</b> 100cm
     * <pre>
     * return people.reject(new Predicate2&lt;Person, Integer&gt;()
     * {
     *     public boolean accept(Person p, Integer i)
     *     {
     *         return p.getHeightInCm() < i.intValue();
     *     }
     * }, Integer.valueOf(100), FastList.<Person>newList());
     * </pre>
     *
     * @param predicate        a {@link Predicate2} to use as the reject criteria
     * @param parameter        a parameter to pass in for evaluation of the second argument {@code P} in {@code predicate}
     * @param targetCollection the Collection to append to for all elements in this {@code RichIterable} that cause {@code Predicate#accept(Object)} method to evaluate to false
     * @return {@code targetCollection}, which contains appended elements as a result of the reject criteria
     * @see #reject(Predicate)
     * @see #reject(Predicate, Collection)
     * @since 1.0
     */
    <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection);

    /**
     * Filters a collection into a PartitionedIterable based on the evaluation of the predicate.
     * <p/>
     * <pre>e.g.
     * return people.<b>partition</b>(new Predicate&lt;Person&gt;()
     * {
     *     public boolean accept(Person person)
     *     {
     *         return person.getAddress().getState().getName().equals("New York");
     *     }
     * });
     * </pre>
     *
     * @since 1.0.
     */
    PartitionIterable<T> partition(Predicate<? super T> predicate);

    /**
     * Returns all elements of the source collection that are instances of the Class {@code clazz}.
     *
     * @since 2.0
     */
    <S> RichIterable<S> selectInstancesOf(Class<S> clazz);

    /**
     * Returns a new collection with the results of applying the specified function on each element of the source
     * collection.  This method is also commonly called transform or map.
     * <p/>
     * <pre>e.g.
     * return people.collect(new Function&lt;Person, String&gt;()
     * {
     *     public String valueOf(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * });
     * </pre>
     *
     * @since 1.0
     */
    <V> RichIterable<V> collect(Function<? super T, ? extends V> function);

    /**
     * Same as {@link #collect(Function)}, except that the results are gathered into the specified {@code target}
     * collection.
     * <p/>
     * <pre>e.g.
     * return people.collect(new Function&lt;Person, String&gt;()
     * {
     *     public String valueOf(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * }, Lists.mutable.of());
     * </pre>
     *
     * @param function a {@link Function} to use as the collect transformation function
     * @param target   the Collection to append to for all elements in this {@code RichIterable} that meet select criteria {@code function}
     * @return {@code target}, which contains appended elements as a result of the collect transformation
     * @see #collect(Function)
     * @since 1.0
     */
    <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target);

    /**
     * Same as collectWith but with a targetCollection parameter to gather the results.
     * <p/>
     * <pre>e.g.
     * Function2<Integer, Integer, Integer> addParameterFunction =
     * new Function2<Integer, Integer, Integer>()
     * {
     *      public Integer value(final Integer each, final Integer parameter)
     *      {
     *          return each + parameter;
     *      }
     * };
     * FastList.newListWith(1, 2, 3).collectWith(addParameterFunction, Integer.valueOf(1), UnifiedSet.newSet());
     * </pre>
     *
     * @param function         a {@link Function2} to use as the collect transformation function
     * @param parameter        a parameter to pass in for evaluation of the second argument {@code P} in {@code function}
     * @param targetCollection the Collection to append to for all elements in this {@code RichIterable} that meet select criteria {@code function}
     * @return {@code targetCollection}, which contains appended elements as a result of the collect transformation
     * @since 1.0
     */
    <P, V, R extends Collection<V>> R collectWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter,
            R targetCollection);

    /**
     * Returns a new collection with the results of applying the specified function on each element of the source
     * collection, but only for those elements which return true upon evaluation of the predicate.  This is the
     * the optimized equivalent of calling iterable.select(predicate).collect(function).
     * <p/>
     * <pre>e.g.
     * Lists.mutable.of().with(1, 2, 3).collectIf(Predicates.notNull(), Functions.getToString())
     * </pre>
     *
     * @since 1.0
     */
    <V> RichIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    /**
     * Same as the collectIf method with two parameters but uses the specified target collection for the results.
     *
     * @param predicate a {@link Predicate} to use as the select criteria
     * @param function  a {@link Function} to use as the collect transformation function
     * @param target    the Collection to append to for all elements in this {@code RichIterable} that meet the collect criteria {@code predicate}
     * @return {@code targetCollection}, which contains appended elements as a result of the collect criteria and transformation
     * @see #collectIf(Predicate, Function)
     * @since 1.0
     */
    <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target);

    /**
     * {@code flatCollect} is a special case of {@link #collect(Function)}. With {@code collect}, when the {@link Function} returns
     * a collection, the result is a collection of collections. {@code flatCollect} outputs a single "flattened" collection
     * instead.  This method is commonly called flatMap.
     * <p/>
     * Consider the following example where we have a {@code Person} class, and each {@code Person} has a list of {@code Address} objects.  Take the following {@link Function}:
     * <pre>
     * Function&lt;Person, List&lt;Address&gt;&gt; addressFunction = new Function&lt;Person, List&lt;Address&gt;&gt;() {
     *     public List&lt;Address&gt; valueOf(Person person) {
     *         return person.getAddresses();
     *     }
     * };
     * MutableList&lt;Person&gt; people = ...;
     * </pre>
     * Using {@code collect} returns a collection of collections of addresses.
     * <pre>
     * MutableList&lt;List&lt;Address&gt;&gt; addresses = people.collect(addressFunction);
     * </pre>
     * Using {@code flatCollect} returns a single flattened list of addresses.
     * <pre>
     * MutableList&lt;Address&gt; addresses = people.flatCollect(addressFunction);
     * </pre>
     *
     * @param function The {@link Function} to apply
     * @return a new flattened collection produced by applying the given {@code function}
     * @since 1.0
     */
    <V> RichIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Same as flatCollect, only the results are collected into the target collection.
     *
     * @param function The {@link Function} to apply
     * @param target   The collection into which results should be added.
     * @return {@code target}, which will contain a flattened collection of results produced by applying the given {@code function}
     * @see #flatCollect(Function)
     */
    <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target);

    /**
     * Returns the first element of the iterable for which the predicate evaluates to true or null in the case where no
     * element returns true.  This method is commonly called find.
     * <p/>
     * <pre>e.g.
     * return people.detect(new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getFirstName().equals("John") && person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     *
     * @since 1.0
     */
    T detect(Predicate<? super T> predicate);

    /**
     * Returns the first element of the iterable for which the predicate evaluates to true.  If no element matches
     * the predicate, then returns the value of applying the specified function.
     *
     * @since 1.0
     */
    T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function);

    /**
     * Return the total number of elements that answer true to the specified predicate.
     * <p/>
     * <pre>e.g.
     * return people.<b>count</b>(new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getAddress().getState().getName().equals("New York");
     *     }
     * });
     * </pre>
     *
     * @since 1.0
     */
    int count(Predicate<? super T> predicate);

    /**
     * Returns true if the predicate evaluates to true for any element of the iterable. Returns
     * false if the iterable is empty, or if no element returned true when evaluating the predicate.
     *
     * @since 1.0
     */
    boolean anySatisfy(Predicate<? super T> predicate);

    /**
     * Returns true if the predicate evaluates to true for every element of the iterable or if the iterable is empty.
     * Otherwise, returns false.
     *
     * @since 1.0
     */
    boolean allSatisfy(Predicate<? super T> predicate);

    /**
     * Returns the final result of evaluating function using each element of the iterable and the previous evaluation
     * result as the parameters. The injected value is used for the first parameter of the first evaluation, and the current
     * item in the iterable is used as the second parameter.  This method is commonly called foldl or sometimes reduce.
     *
     * @since 1.0
     */
    <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function);

    /**
     * Returns the final int result of evaluating function using each element of the iterable and the previous evaluation
     * result as the parameters. The injected value is used for the first parameter of the first evaluation, and the current
     * item in the iterable is used as the second parameter.
     *
     * @since 1.0
     */
    int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function);

    /**
     * Returns the final long result of evaluating function using each element of the iterable and the previous evaluation
     * result as the parameters. The injected value is used for the first parameter of the first evaluation, and the current
     * item in the iterable is used as the second parameter.
     *
     * @since 1.0
     */
    long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function);

    /**
     * Returns the final float result of evaluating function using each element of the iterable and the previous evaluation
     * result as the parameters. The injected value is used for the first parameter of the first evaluation, and the current
     * item in the iterable is used as the second parameter.
     *
     * @since 2.0
     */
    float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function);

    /**
     * Returns the final double result of evaluating function using each element of the iterable and the previous evaluation
     * result as the parameters. The injected value is used for the first parameter of the first evaluation, and the current
     * item in the iterable is used as the second parameter.
     *
     * @since 1.0
     */
    double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function);

    /**
     * Converts the collection to a mutable MutableList implementation.
     *
     * @since 1.0
     */
    MutableList<T> toList();

    /**
     * Converts the collection to a MutableList implementation and sorts it using the natural order of the elements.
     *
     * @since 1.0
     */
    MutableList<T> toSortedList();

    /**
     * Converts the collection to a mutable MutableList implementation and sorts it using the specified comparator.
     *
     * @since 1.0
     */
    MutableList<T> toSortedList(Comparator<? super T> comparator);

    /**
     * Converts the collection to a MutableList implementation and sorts it based on the natural order of the
     * attribute returned by {@code function}.
     *
     * @since 1.0
     */
    <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function);

    /**
     * Converts the collection to a MutableSet implementation.
     *
     * @since 1.0
     */
    MutableSet<T> toSet();

    /**
     * Converts the collection to a MutableSortedSet implementation and sorts it using the natural order of the
     * elements.
     *
     * @since 1.0
     */
    MutableSortedSet<T> toSortedSet();

    /**
     * Converts the collection to a MutableSortedSet implementation and sorts it using the specified comparator.
     *
     * @since 1.0
     */
    MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator);

    /**
     * Converts the collection to a MutableSortedSet implementation and sorts it based on the natural order of the
     * attribute returned by {@code function}.
     *
     * @since 1.0
     */
    <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function);

    /**
     * Converts the collection to the default MutableBag implementation.
     *
     * @since 1.0
     */
    MutableBag<T> toBag();

    /**
     * Converts the collection to a MutableMap implementation using the specified key and value functions.
     *
     * @since 1.0
     */
    <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction);

    /**
     * Converts the collection to a MutableSortedMap implementation using the specified key and value functions
     * sorted by the key elements' natural ordering.
     *
     * @since 1.0
     */
    <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction);

    /**
     * Converts the collection to a MutableSortedMap implementation using the specified key and value functions
     * sorted by the given comparator.
     *
     * @since 1.0
     */
    <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction);

    /**
     * Returns a deferred iterable, most likely implemented by calling LazyIterate.defer(this).
     *
     * @since 1.0.
     */
    LazyIterable<T> asLazy();

    /**
     * @see Collection#toArray()
     * @since 1.0
     */
    Object[] toArray();

    /**
     * @see Collection#toArray(Object[])
     * @since 1.0
     */
    <T> T[] toArray(T[] a);

    /**
     * Returns the minimum element out of this container based on the comparator.
     *
     * @throws NoSuchElementException if the RichIterable is empty
     * @since 1.0
     */
    T min(Comparator<? super T> comparator);

    /**
     * Returns the maximum element out of this container based on the comparator.
     *
     * @throws NoSuchElementException if the RichIterable is empty
     * @since 1.0
     */
    T max(Comparator<? super T> comparator);

    /**
     * Returns the minimum element out of this container based on the natural order.
     *
     * @throws ClassCastException     if the elements are not {@link Comparable}
     * @throws NoSuchElementException if the RichIterable is empty
     * @since 1.0
     */
    T min();

    /**
     * Returns the maximum element out of this container based on the natural order.
     *
     * @throws ClassCastException     if the elements are not {@link Comparable}
     * @throws NoSuchElementException if the RichIterable is empty
     * @since 1.0
     */
    T max();

    /**
     * Returns the minimum elements out of this container based on the natural order of the attribute returned by Function.
     *
     * @throws NoSuchElementException if the RichIterable is empty
     * @since 1.0
     */
    <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function);

    /**
     * Returns the maximum elements out of this container based on the natural order of the attribute returned by Function.
     *
     * @throws NoSuchElementException if the RichIterable is empty
     * @since 1.0
     */
    <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function);

    /**
     * Returns the final int result of evaluating function for each element of the iterable and adding the results
     * together.
     *
     * @since 2.0
     */
    int sumOf(IntFunction<? super T> function);

    /**
     * Returns the final float result of evaluating function for each element of the iterable and adding the results
     * together.
     *
     * @since 2.0
     */
    float sumOf(FloatFunction<? super T> function);

    /**
     * Returns the final long result of evaluating function for each element of the iterable and adding the results
     * together.
     *
     * @since 2.0
     */
    long sumOf(LongFunction<? super T> function);

    /**
     * Returns the final double result of evaluating function for each element of the iterable and adding the results
     * together.
     *
     * @since 2.0
     */
    double sumOf(DoubleFunction<? super T> function);

    /**
     * Returns a string representation of this collection by delegating to {@link #makeString(String)} and defaulting
     * the separator parameter to the characters <tt>", "</tt> (comma and space).
     *
     * @return a string representation of this collection.
     * @since 1.0
     */
    String makeString();

    /**
     * Returns a string representation of this collection by delegating to {@link #makeString(String, String, String)}
     * and defaulting the start and end parameters to <tt>""</tt> (the empty String).
     *
     * @return a string representation of this collection.
     * @since 1.0
     */
    String makeString(String separator);

    /**
     * Returns a string representation of this collection.  The string representation consists of a list of the
     * collection's elements in the order they are returned by its iterator, enclosed in the start and end strings.
     * Adjacent elements are separated by the separator string.  Elements are converted to strings as by
     * <tt>String.valueOf(Object)</tt>.
     *
     * @return a string representation of this collection.
     * @since 1.0
     */
    String makeString(String start, String separator, String end);

    /**
     * Prints a string representation of this collection onto the given {@code Appendable}.  Prints the string returned
     * by {@link #makeString()}.
     *
     * @since 1.0
     */
    void appendString(Appendable appendable);

    /**
     * Prints a string representation of this collection onto the given {@code Appendable}.  Prints the string returned
     * by {@link #makeString(String)}.
     *
     * @since 1.0
     */
    void appendString(Appendable appendable, String separator);

    /**
     * Prints a string representation of this collection onto the given {@code Appendable}.  Prints the string returned
     * by {@link #makeString(String, String, String)}.
     *
     * @since 1.0
     */
    void appendString(Appendable appendable, String start, String separator, String end);

    /**
     * For each element of the iterable, the function is evaluated and the results of these evaluations are collected
     * into a new multimap, where the transformed value is the key and the original values are added to the same (or similar)
     * species of collection as the source iterable.
     * <p/>
     * <pre>e.g.
     * return people.groupBy(new Function&lt;Person, String&gt;()
     * {
     *     public String value(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * });
     * </pre>
     *
     * @since 1.0
     */
    <V> Multimap<V, T> groupBy(Function<? super T, ? extends V> function);

    /**
     * Same as {@link #groupBy(Function)}, except that the results are gathered into the specified {@code target}
     * multimap.
     * <p/>
     * <pre>e.g.
     * return people.groupBy(new Function&lt;Person, String&gt;()
     * {
     *     public String value(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * }, new FastListMultimap&lt;String, Person&gt;());
     * </pre>
     *
     * @since 1.0
     */
    <V, R extends MutableMultimap<V, T>> R groupBy(Function<? super T, ? extends V> function, R target);

    /**
     * Similar to {@link #groupBy(Function)}, except the result of evaluating function will return a collection of keys
     * for each value.
     *
     * @since 1.0
     */
    <V> Multimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Same as {@link #groupByEach(Function)}, except that the results are gathered into the specified {@code target}
     * multimap.
     *
     * @since 1.0
     */
    <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function,
            R target);

    /**
     * Returns a string representation of this RichIterable.  The string representation consists of a list of the
     * RichIterable's elements in the order they are returned by its iterator, enclosed in square brackets
     * (<tt>"[]"</tt>).  Adjacent elements are separated by the characters <tt>", "</tt> (comma and space).  Elements
     * are converted to strings as by {@link String#valueOf(Object)}.
     *
     * @return a string representation of this RichIterable
     * @since 1.0
     */
    @Override
    String toString();

    /**
     * Returns a {@code RichIterable} formed from this {@code RichIterable} and another {@code RichIterable} by
     * combining corresponding elements in pairs. If one of the two {@code RichIterable}s is longer than the other, its
     * remaining elements are ignored.
     *
     * @param that The {@code RichIterable} providing the second half of each result pair
     * @param <S>  the type of the second half of the returned pairs
     * @return A new {@code RichIterable} containing pairs consisting of corresponding elements of this {@code
     *         RichIterable} and that. The length of the returned {@code RichIterable} is the minimum of the lengths of
     *         this {@code RichIterable} and that.
     * @since 1.0
     */
    <S> RichIterable<Pair<T, S>> zip(Iterable<S> that);

    /**
     * Same as {@link #zip(Iterable)} but uses {@code target} for output.
     *
     * @since 1.0
     */
    <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target);

    /**
     * Zips this {@code RichIterable} with its indices.
     *
     * @return A new {@code RichIterable} containing pairs consisting of all elements of this {@code RichIterable}
     *         paired with their index. Indices start at 0.
     * @see #zip(Iterable)
     * @since 1.0
     */
    RichIterable<Pair<T, Integer>> zipWithIndex();

    /**
     * Same as {@link #zipWithIndex()} but uses {@code target} for output.
     *
     * @since 1.0
     */
    <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target);

    /**
     * Partitions elements in fixed size chunks.
     *
     * @param size the number of elements per chunk
     * @return A {@code RichIterable} containing {@code RichIterable}s of size {@code size}, except the last will be
     *         truncated if the elements don't divide evenly.
     * @since 1.0
     */
    RichIterable<RichIterable<T>> chunk(int size);
}
