/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.codegenerator.ant;

import java.io.File;

import com.gs.collections.codegenerator.GsCollectionsCodeGenerator;
import org.apache.tools.ant.Task;

public class GsCollectionsCodeGeneratorTask extends Task
{
    private File templateDirectory;

    @Override
    public void execute()
    {
        this.log("Scanning all template files from " + this.templateDirectory.getPath());

        GsCollectionsCodeGenerator gsCollectionsCodeGenerator =
                new GsCollectionsCodeGenerator(this.templateDirectory);
        gsCollectionsCodeGenerator.generate();
    }

    public void setTemplateDirectory(File templateDirectory)
    {
        this.templateDirectory = templateDirectory;
    }
}
