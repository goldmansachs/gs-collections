/*
 * Copyright 2014 Goldman Sachs.
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.CRC32;

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
    private int numFileWritten = 0;

    public GsCollectionsCodeGenerator(String templateDirectory, File moduleBaseDir, List<URL> classPathURLs, ErrorListener errorListener)
    {
        this.templateDirectory = templateDirectory;
        this.moduleBaseDir = moduleBaseDir;
        this.classPathURLs = classPathURLs;
        this.stErrorListener = new LoggingErrorListener(errorListener);
    }

    /**
     * Generates code and only write contents to disk which differ from the current file contents.
     *
     * @return The number of files written.
     */
    public int generateFiles()
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
                boolean skipBooleanKeys = this.templateFile.isDefined("skipBooleanKeys") && Boolean.valueOf(this.templateFile.getInstanceOf("skipBooleanKeys").render());

                if (hasTwoPrimitives)
                {
                    for (Primitive primitive1 : Primitive.values())
                    {
                        if (primitive1 == Primitive.BOOLEAN && (skipBoolean || skipBooleanKeys))
                        {
                            continue;
                        }
                        for (Primitive primitive2 : Primitive.values())
                        {
                            if (primitive2 == Primitive.BOOLEAN && skipBoolean)
                            {
                                continue;
                            }
                            String sourceFileName = this.executeTemplate(primitive1, primitive2, "fileName");
                            File outputFile = new File(targetPath, sourceFileName + ".java");

                            if (!GsCollectionsCodeGenerator.sourceFileExists(outputFile))
                            {
                                String classContents = this.executeTemplate(primitive1, primitive2, "class");
                                this.checkSumClassContentsAndWrite(classContents, targetPath, sourceFileName);
                            }
                        }
                    }
                }
                else
                {
                    for (Primitive primitive : Primitive.values())
                    {
                        if (primitive == Primitive.BOOLEAN && skipBoolean)
                        {
                            continue;
                        }
                        String sourceFileName = this.executeTemplate(primitive, "fileName");
                        File outputFile = new File(targetPath, sourceFileName + ".java");

                        if (!GsCollectionsCodeGenerator.sourceFileExists(outputFile))
                        {
                            String classContents = this.executeTemplate(primitive, "class");
                            this.checkSumClassContentsAndWrite(classContents, targetPath, sourceFileName);
                        }
                    }
                }
            }
        }

        return this.numFileWritten;
    }

    private void checkSumClassContentsAndWrite(String classContents, File targetPath, String sourceFileName)
    {
        long checksumValue = GsCollectionsCodeGenerator.calculateChecksum(classContents);

        File outputFile = new File(targetPath, sourceFileName + ".java");
        Path outputChecksumPath = Paths.get(targetPath.getAbsolutePath(), sourceFileName + ".java.crc");
        if (!outputChecksumPath.toFile().exists())
        {
            this.writeFileAndChecksum(outputFile, classContents, checksumValue, outputChecksumPath, false);
            return;
        }

        String existingChecksum = FileUtils.readFile(outputChecksumPath);
        if (existingChecksum.equals(String.valueOf(checksumValue)))
        {
            return;
        }

        this.writeFileAndChecksum(outputFile, classContents, checksumValue, outputChecksumPath, true);
    }

    private static long calculateChecksum(String string)
    {
        CRC32 checksum = new CRC32();
        checksum.update(string.getBytes(StandardCharsets.UTF_8));
        return checksum.getValue();
    }

    private void writeFileAndChecksum(File outputFile, String output, long checksumValue, Path outputChecksumPath, boolean outputFileMustExist)
    {
        this.numFileWritten++;
        FileUtils.writeToFile(output, outputFile, outputFileMustExist);
        FileUtils.writeToFile(String.valueOf(checksumValue), outputChecksumPath.toFile(), outputFileMustExist);
    }

    private String executeTemplate(Primitive primitive, String templateName)
    {
        ST template = this.findTemplate(templateName);
        template.add("primitive", primitive);
        return template.render();
    }

    private String executeTemplate(Primitive primitive1, Primitive primitive2, String templateName)
    {
        ST template = this.findTemplate(templateName);
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
