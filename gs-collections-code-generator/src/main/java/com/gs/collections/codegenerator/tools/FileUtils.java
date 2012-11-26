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

package com.gs.collections.codegenerator.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class FileUtils
{
    private FileUtils()
    {
        throw new AssertionError("Suppress for noninstantiability");
    }

    public static void writeToFile(String data, File outputFile)
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

    public static List<File> listTemplateFilesRecursively(List<File> files, File root)
    {
        if (!root.isDirectory() && root.getName().endsWith(".stg"))
        {
            files.add(root);
            return files;
        }

        File[] children = root.listFiles();
        if (children != null)
        {
            for (File file : children)
            {
                listTemplateFilesRecursively(files, file);
            }
        }
        return files;
    }

    public static void createDirectory(File path)
    {
        if (!path.exists())
        {
            boolean mkdirs = path.mkdirs();
            if (!mkdirs)
            {
                throw new RuntimeException("Could not create directory " + path);
            }
        }
    }
}
