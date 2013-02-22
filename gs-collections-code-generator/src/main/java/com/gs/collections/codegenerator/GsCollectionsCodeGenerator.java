/*
 * Copyright 2013 Goldman Sachs.
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
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.misc.STMessage;

public class GsCollectionsCodeGenerator
{
    public static final String GENERATED_TEST_SOURCES_LOCATION = "target/generated-test-sources/java/";
    public static final String GENERATED_SOURCES_LOCATION = "target/generated-sources/java/";
    private final String templateDirectory;
    private final File moduleBaseDir;
    private final List<URL> classPathURLs;
    private boolean isTest;
    private STGroupFile templateFile;
    private final STErrorListener stErrorListener;
    private URL url;

    public GsCollectionsCodeGenerator(String templateDirectory, File moduleBaseDir, List<URL> classPathURLs, ErrorListener errorListener)
    {
        this.templateDirectory = templateDirectory;
        this.moduleBaseDir = moduleBaseDir;
        this.classPathURLs = classPathURLs;
        this.stErrorListener = new LoggingErrorListener(errorListener);
    }

    public void generate()
    {
        List<URL> allTemplateFilesFromClassPath = FileUtils.getAllTemplateFilesFromClasspath(this.templateDirectory, this.classPathURLs);
        for (URL url : allTemplateFilesFromClassPath)
        {
            this.url = url;
            this.templateFile = new STGroupFile(this.url, "UTF-8", '<', '>');
            this.templateFile.setListener(this.stErrorListener);
            this.templateFile.registerRenderer(String.class, new IntegerOrStringRenderer());
            if (this.templateFile.isDefined("fileName"))
            {
                this.setTest();
                File targetPath = this.constructTargetPath();
                FileUtils.createDirectory(targetPath);

                boolean hasTwoPrimitives = this.templateFile.isDefined("hasTwoPrimitives") && Boolean.valueOf(this.templateFile.getInstanceOf("hasTwoPrimitives").render());
                boolean skipBoolean = this.templateFile.isDefined("skipBoolean") && Boolean.valueOf(this.templateFile.getInstanceOf("skipBoolean").render());

                if (hasTwoPrimitives)
                {
                    for (Primitive primitive1 : Primitive.values())
                    {
                        if (primitive1 == Primitive.BOOLEAN && skipBoolean)
                        {
                            continue;
                        }
                        for (Primitive primitive2 : Primitive.values())
                        {
                            if (primitive2 == Primitive.BOOLEAN && skipBoolean)
                            {
                                continue;
                            }
                            String sourceFileName = executeTemplate(primitive1, primitive2, this.findTemplate("fileName"));
                            File outputFile = new File(targetPath, sourceFileName + ".java");

                            if (!sourceFileExists(outputFile))
                            {
                                String classContents = executeTemplate(primitive1, primitive2, this.findTemplate("class"));
                                FileUtils.writeToFile(classContents, outputFile);
                            }
                        }
                    }
                }
                else
                {
                    for (Primitive primitive : Primitive.values())
                    {
                        String sourceFileName = executeTemplate(primitive, this.findTemplate("fileName"));
                        File outputFile = new File(targetPath, sourceFileName + ".java");

                        if (!sourceFileExists(outputFile))
                        {
                            ST template = this.findTemplate("class");
                            if (this.templateFile.getFileName().contains("boolean")
                                    && primitive == Primitive.BOOLEAN
                                    && template.getAttributes().containsKey("sameTwoPrimitives"))
                            {
                                template.add("sameTwoPrimitives", true);
                            }
                            String classContents = executeTemplate(primitive, template);
                            FileUtils.writeToFile(classContents, outputFile);
                        }
                    }
                }
            }
        }
    }

    private static String executeTemplate(Primitive primitive, ST template)
    {
        template.add("primitive", primitive);
        return template.render();
    }

    private static String executeTemplate(Primitive primitive1, Primitive primitive2, ST template)
    {
        template.add("primitive1", primitive1);
        template.add("primitive2", primitive2);
        template.add("sameTwoPrimitives", primitive1 == primitive2);
        return template.render();
    }

    private ST findTemplate(String templateName)
    {
        ST template = this.templateFile.getInstanceOf(templateName);
        if (template == null)
        {
            throw new RuntimeException("Could not find template " + templateName + " in " + this.templateFile.getFileName());
        }
        return template;
    }

    private void setTest()
    {
        this.isTest = this.templateFile.getInstanceOf("isTest") == null ? false : Boolean.valueOf(this.templateFile.getInstanceOf("isTest").render());
    }

    private File constructTargetPath()
    {
        ST targetPath = this.findTemplate("targetPath");
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

    public interface ErrorListener
    {
        void error(String string);
    }

    private final class LoggingErrorListener implements STErrorListener
    {
        private final ErrorListener errorListener;

        private LoggingErrorListener(ErrorListener errorListener)
        {
            this.errorListener = errorListener;
        }

        private void logError(STMessage stMessage, String errorType)
        {
            String error = String.format("String template %s error while processing [%s]: %s", errorType, GsCollectionsCodeGenerator.this.url.getPath(), stMessage.toString());
            this.errorListener.error(error);
            throw new RuntimeException();
        }

        public void compileTimeError(STMessage stMessage)
        {
            this.logError(stMessage, "compile time");
        }

        public void runTimeError(STMessage stMessage)
        {
            this.logError(stMessage, "run time");
        }

        public void IOError(STMessage stMessage)
        {
            this.logError(stMessage, "IO");
        }

        public void internalError(STMessage stMessage)
        {
            this.logError(stMessage, "internal");
        }
    }
}
