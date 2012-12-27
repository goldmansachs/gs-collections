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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gs.collections.codegenerator.GsCollectionsCodeGenerator;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Task;

public class GsCollectionsCodeGeneratorTask extends Task
{
    private String templateDirectory;

    @Override
    public void execute()
    {
        this.log("Scanning all template files from " + this.templateDirectory);

        GsCollectionsCodeGenerator gsCollectionsCodeGenerator =
                new GsCollectionsCodeGenerator(this.templateDirectory, this.getProject().getBaseDir(), this.getClassPathURLs());
        gsCollectionsCodeGenerator.generate();
    }

    public void setTemplateDirectory(String templateDirectory)
    {
        this.templateDirectory = templateDirectory;
    }

    private List<URL> getClassPathURLs()
    {
        List<URL> urls = new ArrayList<URL>();
        String[] classPathStrings = ((AntClassLoader) this.getClass().getClassLoader()).getClasspath().split(";");

        for (String classPathString : classPathStrings)
        {
            try
            {
                URL url = new File(classPathString).toURI().toURL();
                urls.add(url);
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException(e);
            }
        }
        return urls;
    }
}
