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

package ponzu.impl.block.factory;

import java.io.IOException;
import java.io.PrintStream;

import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.impl.block.procedure.CaseProcedure;
import ponzu.impl.block.procedure.IfProcedure;

/**
 * Factory class for commonly used procedures.
 */
public final class Procedures
{
    private Procedures()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> Procedure<T> println(PrintStream stream)
    {
        return new PrintlnProcedure<T>(stream);
    }

    public static <T> Procedure<T> append(Appendable appendable)
    {
        return new AppendProcedure<T>(appendable);
    }

    /**
     * @deprecated since 1.2 - Inlineable
     */
    @Deprecated
    public static <T> Procedure<T> fromProcedureWithInt(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        return Procedures.fromObjectIntProcedure(objectIntProcedure);
    }

    public static <T> Procedure<T> fromObjectIntProcedure(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        return new ObjectIntProcedureAdapter<T>(objectIntProcedure);
    }

    public static <T> Procedure<T> ifTrue(Predicate<? super T> predicate, Procedure<? super T> block)
    {
        return new IfProcedure<T>(predicate, block);
    }

    public static <T> Procedure<T> ifElse(
            Predicate<? super T> predicate,
            Procedure<? super T> trueProcedure,
            Procedure<? super T> falseProcedure)
    {
        return new IfProcedure<T>(predicate, trueProcedure, falseProcedure);
    }

    public static <T> CaseProcedure<T> caseDefault(Procedure<? super T> defaultProcedure)
    {
        return new CaseProcedure<T>(defaultProcedure);
    }

    public static <T> CaseProcedure<T> caseDefault(
            Procedure<? super T> defaultProcedure,
            Predicate<? super T> predicate,
            Procedure<? super T> procedure)
    {
        return Procedures.<T>caseDefault(defaultProcedure).addCase(predicate, procedure);
    }

    public static <T> Procedure<T> synchronizedEach(Procedure<T> procedure)
    {
        return new Procedures.SynchronizedProcedure<T>(procedure);
    }

    private static final class PrintlnProcedure<T> implements Procedure<T>
    {
        private static final long serialVersionUID = 1L;

        private final PrintStream stream;

        private PrintlnProcedure(PrintStream stream)
        {
            this.stream = stream;
        }

        public void value(T each)
        {
            this.stream.println(each);
        }
    }

    private static final class AppendProcedure<T> implements Procedure<T>
    {
        private static final long serialVersionUID = 1L;

        private final Appendable appendable;

        private AppendProcedure(Appendable appendable)
        {
            this.appendable = appendable;
        }

        public void value(T each)
        {
            try
            {
                this.appendable.append(String.valueOf(each));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString()
        {
            return this.appendable.toString();
        }
    }

    private static final class ObjectIntProcedureAdapter<T> implements Procedure<T>
    {
        private static final long serialVersionUID = 1L;
        private int count;
        private final ObjectIntProcedure<? super T> objectIntProcedure;

        private ObjectIntProcedureAdapter(ObjectIntProcedure<? super T> objectIntProcedure)
        {
            this.objectIntProcedure = objectIntProcedure;
        }

        public void value(T each)
        {
            this.objectIntProcedure.value(each, this.count);
            this.count++;
        }
    }

    public static final class SynchronizedProcedure<T> implements Procedure<T>
    {
        private static final long serialVersionUID = 1L;
        private final Procedure<T> procedure;

        private SynchronizedProcedure(Procedure<T> procedure)
        {
            this.procedure = procedure;
        }

        public void value(T each)
        {
            if (each == null)
            {
                this.procedure.value(null);
            }
            else
            {
                synchronized (each)
                {
                    this.procedure.value(each);
                }
            }
        }
    }
}
