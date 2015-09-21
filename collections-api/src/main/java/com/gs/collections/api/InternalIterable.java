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

package com.gs.collections.api;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.ordered.OrderedIterable;

/**
 * The base interface for all GS Collections.  All GS Collections are internally iterable, and this interface provides
 * the base set of internal iterators that every GS collection should implement.
 */
public interface InternalIterable<T>
        extends Iterable<T>
{
    /**
     * The procedure is executed for each element in the iterable.
     * <p>
     * Example using a Java 8 lambda:
     * <pre>
     * people.forEach(Procedures.cast(person -> LOGGER.info(person.getName())));
     * </pre>
     * <p>
     * Example using an anonymous inner class:
     * <pre>
     * people.forEach(new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *         LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     * NOTE: This method started to conflict with {@link Iterable#forEach(java.util.function.Consumer)}
     * since Java 1.8. It is recommended to use {@link RichIterable#each(Procedure)} instead to avoid casting to Procedure.
     *
     * @see RichIterable#each(Procedure)
     * @see Iterable#forEach(java.util.function.Consumer)
     */
    @SuppressWarnings("UnnecessaryFullyQualifiedName")
    void forEach(Procedure<? super T> procedure);

    /**
     * Iterates over the iterable passing each element and the current relative int index to the specified instance of
     * ObjectIntProcedure.
     * <p>
     * Example using a Java 8 lambda:
     * <pre>
     * people.forEachWithIndex((Person person, int index) -> LOGGER.info("Index: " + index + " person: " + person.getName()));
     * </pre>
     * <p>
     * Example using an anonymous inner class:
     * <pre>
     * people.forEachWithIndex(new ObjectIntProcedure<Person>()
     * {
     *     public void value(Person person, int index)
     *     {
     *         LOGGER.info("Index: " + index + " person: " + person.getName());
     *     }
     * });
     * </pre>
     *
     * @deprecated in 6.0. Use {@link OrderedIterable#forEachWithIndex(ObjectIntProcedure)} instead.
     */
    @Deprecated
    void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure);

    /**
     * The procedure2 is evaluated for each element in the iterable with the specified parameter provided
     * as the second argument.
     * <p>
     * Example using a Java 8 lambda:
     * <pre>
     * people.forEachWith((Person person, Person other) ->
     *     {
     *         if (person.isRelatedTo(other))
     *         {
     *              LOGGER.info(person.getName());
     *         }
     *     }, fred);
     * </pre>
     * <p>
     * Example using an anonymous inner class:
     * <pre>
     * people.forEachWith(new Procedure2<Person, Person>()
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
    <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter);
}
