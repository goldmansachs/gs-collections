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

import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;

/**
 * The base interface for all GS Collections.  All GS Collections are internally iterable, and this interface provides
 * the base set of internal iterators that every GS collection should implement.
 */
public interface InternalIterable<T>
        extends Iterable<T>
{
    /**
     * For each element of the receiver the procedure is executed with each element as the parameter.
     * <p/>
     * <pre>e.g.
     * people.forEach(new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *         LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     */
    void forEach(Procedure<? super T> procedure);

    /**
     * Iterates over a collection passing each element and the current relative int index to the specified instance of
     * ProcedureWithInt
     * <pre>e.g.
     * people.forEachWithIndex(new ProcedureWithInt<Person>()
     * {
     *     public void value(Person person, int index)
     *     {
     *         LOGGER.info("Index: " + index + " person: " + person.getName());
     *     }
     * });
     * </pre>
     */
    void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure);

    /**
     * For each element of the receiver the procedure is executed with each element as the first argument, and the
     * specified parameter as the second argument.
     * <p/>
     * <pre>e.g.
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
