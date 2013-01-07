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
import java.net.URL;
import java.util.List;

import com.gs.collections.codegenerator.model.Primitive;
import com.gs.collections.codegenerator.tools.FileUtils;
import com.gs.collections.codegenerator.tools.IntegerOrStringRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class GsCollectionsCodeGenerator
{
    public static final String GENERATED_TEST_SOURCES_LOCATION = "target/generated-test-sources/java/";
    public static final String GENERATED_SOURCES_LOCATION = "target/generated-sources/java/";
    private final String templateDirectory;
    private final File moduleBaseDir;
    private final List<URL> classPathURLs;
    private boolean isTest;

    public GsCollectionsCodeGenerator(String templateDirectory, File moduleBaseDir, List<URL> classPathURLs)
    {
        this.templateDirectory = templateDirectory;
        this.moduleBaseDir = moduleBaseDir;
        this.classPathURLs = classPathURLs;
    }

    public void generate()
    {
        List<URL> allTemplateFilesFromClassPath = FileUtils.getAllTemplateFilesFromClasspath(this.templateDirectory, this.classPathURLs);
        for (URL url : allTemplateFilesFromClassPath)
        {
            STGroupFile stGroupFile = new STGroupFile(url, "UTF-8", '<', '>');
            stGroupFile.registerRenderer(String.class, new IntegerOrStringRenderer());
            if (stGroupFile.isDefined("fileName"))
            {
                this.setTest(stGroupFile);
                for (Primitive primitive : Primitive.values())
                {
                    File targetPath = this.constructTargetPath(stGroupFile);
                    FileUtils.createDirectory(targetPath);

                    String sourceFileName = executeTemplate(stGroupFile, primitive, "fileName");
                    File outputFile = new File(targetPath, sourceFileName + ".java");

                    if (!sourceFileExists(outputFile))
                    {
                        String classContents = executeTemplate(stGroupFile, primitive, "class");
                        FileUtils.writeToFile(classContents, outputFile);
                    }
                }
            }
        }
    }

    private static String executeTemplate(STGroupFile stGroupFile, Primitive primitive, String templateName)
    {
        ST template = findTemplate(stGroupFile, templateName);
        template.add("primitive", primitive);
        return template.render();
    }

    private static ST findTemplate(STGroupFile stGroupFile, String templateName)
    {
        ST template = stGroupFile.getInstanceOf(templateName);
        if (template == null)
        {
            throw new RuntimeException("Could not find template " + templateName + " in " + stGroupFile.getFileName());
        }
        return template;
    }

    private void setTest(STGroupFile templateFile)
    {
        this.isTest = templateFile.getInstanceOf("isTest") == null ? false : Boolean.valueOf(templateFile.getInstanceOf("isTest").render());
    }

    private File constructTargetPath(STGroupFile templateFile)
    {
        ST targetPath = findTemplate(templateFile, "targetPath");
        return this.isTest ? new File(this.moduleBaseDir, GENERATED_TEST_SOURCES_LOCATION + targetPath.render())
                : new File(this.moduleBaseDir, GENERATED_SOURCES_LOCATION + targetPath.render());
    }

    private static boolean sourceFileExists(File outputFile)
    {
        File newPath = new File(outputFile.getAbsolutePath()
                .replace("target", "src")
                .replace("generated-sources", "main")
                .replace("generated-test-sources", "test"));
        return newPath.exists();
    }

    public boolean isTest()
    {
        return this.isTest;
    }
}
