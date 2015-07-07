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

package com.gs.collections.impl.jmh.runner;

import org.junit.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;

public abstract class AbstractJMHTestRunner
{
    @Test
    public void runTests() throws RunnerException
    {
        int warmupCount = this.warmUpCount();
        int runCount = this.runCount();
        Options opts = new OptionsBuilder()
                .include(".*" + this.getClass().getName() + ".*")
                .warmupTime(TimeValue.seconds(2))
                .warmupIterations(warmupCount)
                .measurementTime(TimeValue.seconds(2))
                .measurementIterations(runCount)
                .verbosity(VerboseMode.EXTRA)
                .forks(2)
                .build();

        new Runner(opts).run();
    }

    protected int runCount()
    {
        return 10;
    }

    protected int warmUpCount()
    {
        return 20;
    }
}
