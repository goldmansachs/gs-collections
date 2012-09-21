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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.gs.collections.codegenerator.model.Primitive;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class GsCollectionsCodeGenerator
{
    private final File targetPath;
    private final String templateFileName;
    private final String destinationFileSuffix;

    public GsCollectionsCodeGenerator(File targetPath, String templateFileName, String destinationFileSuffix)
    {
        this.targetPath = targetPath;
        this.templateFileName = templateFileName;
        this.destinationFileSuffix = destinationFileSuffix;
    }

    public void generate()
    {
        for (Primitive primitive : Primitive.values())
        {
            writeToFile(
                    generateSources(primitive, this.templateFileName),
                    new File(this.targetPath, primitive.getName() + this.destinationFileSuffix + ".java"));
        }
    }

    private static String generateSources(Primitive primitive, String templateFileName)
    {
        ST clazz = new STGroupFile(templateFileName).getInstanceOf("class");
        if (clazz == null)
        {
            throw new RuntimeException("Could not parse template " + templateFileName);
        }
        clazz.add("primitive", primitive);
        return clazz.render();
    }

    private static void writeToFile(String data, File outputFile)
    {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try
        {
            File directory = outputFile.getParentFile();
            if (!directory.exists())
            {
                boolean mkdirs = directory.mkdirs();
                if (!mkdirs)
                {
                    throw new RuntimeException("Failed to create directory: " + directory);
                }
            }
            fileWriter = new FileWriter(outputFile);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.flush();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not write generated sources to file: " + e);
        }
        finally
        {
            if (fileWriter != null)
            {
                try
                {
                    fileWriter.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("filewriter: " + e);
                }
            }
            if (bufferedWriter != null)
            {
                try
                {
                    bufferedWriter.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("bufwriter: " + e);
                }
            }
        }
    }
}
