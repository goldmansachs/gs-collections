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

package com.gs.collections.codegenerator;

import java.io.File;
import java.util.ArrayList;

import com.gs.collections.codegenerator.model.Primitive;
import com.gs.collections.codegenerator.tools.FileUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class GsCollectionsCodeGenerator
{
    private final File templateDirectory;

    public GsCollectionsCodeGenerator(File templateDirectory)
    {
        this.templateDirectory = templateDirectory;
    }

    public void generate()
    {
        for (File file : FileUtils.listTemplateFilesRecursively(new ArrayList<File>(), this.templateDirectory))
        {
            if (new STGroupFile(file.getAbsolutePath()).isDefined("fileName"))
            {
                for (Primitive primitive : Primitive.values())
                {
                    String sourceFileName = getFileName(file, primitive);
                    File targetPath = getTargetPath(file);
                    FileUtils.createDirectory(targetPath);
                    File outputFile = new File(targetPath, sourceFileName + ".java");
                    if (!sourceFileExists(outputFile))
                    {
                        FileUtils.writeToFile(
                                generateSources(file, primitive),
                                outputFile);
                    }
                }
            }
        }
    }

    private static File getTargetPath(File templateFile)
    {
        ST targetPath = new STGroupFile(templateFile.getAbsolutePath()).getInstanceOf("targetPath");
        if (targetPath == null)
        {
            throw new RuntimeException("Could not parse targetPath in template file " + templateFile.getName());
        }
        return new File(targetPath.render());
    }

    private static boolean sourceFileExists(File outputFile)
    {
        File file = new File(outputFile.getAbsolutePath().replace("target", "src").replace("generated-sources", "main").replace("generated-test-sources", "test"));
        return file.exists();
    }

    private static String getFileName(File templateFile, Primitive primitive)
    {
        ST fileName = new STGroupFile(templateFile.getAbsolutePath()).getInstanceOf("fileName");
        if (fileName == null)
        {
            throw new RuntimeException("Could not parse fileName in template file " + templateFile.getName());
        }
        fileName.add("primitive", primitive);
        return fileName.render();
    }

    private static String generateSources(File templateFile, Primitive primitive)
    {
        ST clazz = new STGroupFile(templateFile.getAbsolutePath()).getInstanceOf("class");
        if (clazz == null)
        {
            throw new RuntimeException("Could not parse template " + templateFile.getName());
        }
        clazz.add("primitive", primitive);
        return clazz.render();
    }
}
