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

    public GsCollectionsCodeGenerator(File targetPath, String templateFileName)
    {
        this.targetPath = targetPath;
        this.templateFileName = templateFileName;
    }

    public void generate()
    {
        for (Primitive primitive : Primitive.values())
        {
            String sourceFileName = this.getFileName(primitive);
            File outputFile = new File(this.targetPath, sourceFileName + ".java");
            if (!this.sourceFileExists(outputFile))
            {
                writeToFile(
                        this.generateSources(primitive),
                        outputFile);
            }
        }
    }

    private boolean sourceFileExists(File outputFile)
    {
        File file = new File(outputFile.getAbsolutePath().replace("target", "src").replace("generated-sources", "main").replace("generated-test-sources", "test"));
        return file.exists();
    }

    private String getFileName(Primitive primitive)
    {
        ST fileName = new STGroupFile(this.templateFileName).getInstanceOf("fileName");
        if (fileName == null)
        {
            throw new RuntimeException("Could not parse fileName in template file " + this.templateFileName);
        }
        fileName.add("primitive", primitive);
        return fileName.render();
    }

    private String generateSources(Primitive primitive)
    {
        ST clazz = new STGroupFile(this.templateFileName).getInstanceOf("class");
        if (clazz == null)
        {
            throw new RuntimeException("Could not parse template " + this.templateFileName);
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
                    throw new RuntimeException("Could not close filewriter: " + e);
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
                    throw new RuntimeException("Could not close bufferedwriter: " + e);
                }
            }
        }
    }
}
