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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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

    public static List<URL> getAllTemplateFilesFromClasspath(String templateDirectory, List<URL> classPathURLs)
    {
        List<URL> files = new ArrayList<URL>();
        try
        {
            for (URL url : classPathURLs)
            {
                recurseURL(url, files, templateDirectory);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        return files;
    }

    private static void recurseURL(URL url, List<URL> files, String templateDirectory) throws URISyntaxException, IOException
    {
        if ("file".equals(url.getProtocol()))
        {
            recurse(new File(url.toURI()), new File(url.toURI()), files, templateDirectory);
        }
        else
        {
            if (url.getPath().endsWith(".jar"))
            {
                JarInputStream stream = new JarInputStream(url.openStream());
                processJar(stream, files, templateDirectory);
                stream.close();
            }
        }
    }

    private static void recurse(File rootDirectory, File file, List<URL> files, String templateDirectory) throws IOException
    {
        if (file.isDirectory())
        {
            File[] children = file.listFiles();
            if (children != null)
            {
                for (File child : children)
                {
                    recurse(rootDirectory, child, files, templateDirectory);
                }
            }
        }
        else
        {
            String filePath = file.getAbsolutePath();
            if (file.getName().endsWith(".jar"))
            {
                JarInputStream stream = new JarInputStream(new FileInputStream(file));
                processJar(stream, files, templateDirectory);
                stream.close();
            }
            else
            {
                String rootPath = rootDirectory.getAbsolutePath();
                if (filePath.contains(templateDirectory) && !rootPath.equals(filePath) && isTemplateFile(filePath))
                {
                    files.add(new URL("file:" + filePath));
                }
            }
        }
    }

    private static void processJar(
            JarInputStream stream,
            List<URL> files, String templateDirectory) throws IOException
    {
        JarEntry entry;
        while ((entry = stream.getNextJarEntry()) != null)
        {
            String entryName = entry.getName();
            if (isTemplateFile(entryName) && entryName.startsWith(templateDirectory))
            {
                files.add(FileUtils.class.getClassLoader().getResource(entryName));
            }
        }
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

    private static boolean isTemplateFile(String filePath)
    {
        return filePath.endsWith(".stg");
    }

    private static String readFile(String path)
    {
        FileInputStream stream = null;
        MappedByteBuffer bb = null;
        try
        {
            stream = new FileInputStream(new File(path));
            FileChannel fc = stream.getChannel();
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
