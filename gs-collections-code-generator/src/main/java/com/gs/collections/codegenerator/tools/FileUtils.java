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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

public final class FileUtils
{
    private FileUtils()
    {
        throw new AssertionError("Suppress for noninstantiability");
    }

    public static void writeToFile(String data, File outputFile)
    {
        if (!outputFile.exists() || (outputFile.exists() && !readFile(outputFile.getAbsolutePath()).equals(data)))
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

    private static String readFile(String path)
    {
        FileInputStream stream = null;
        FileChannel fc;
        MappedByteBuffer bb = null;
        try
        {
            stream = new FileInputStream(new File(path));
            fc = stream.getChannel();
            bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if (stream != null)
                {
                    stream.close();
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        /* Instead of using default, pass in a decoder. */
        return Charset.defaultCharset().decode(bb).toString();
    }
}
