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

package com.gs.collections.codegenerator.maven;

import java.io.File;

import com.gs.collections.codegenerator.GsCollectionsCodeGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @goal generate
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GenerateMojo extends AbstractMojo
{
    /**
     * @parameter expression="${project.build.directory}/generated-sources"
     * @required
     */
    private File targetPath;

    /**
     * @parameter
     * @required
     */
    private String templateFileName;

    /**
     * @parameter
     * @required
     */
    private String destinationFileSuffix;

    /**
     * The Maven project to act upon.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * Whether the generated source should get added to the compile or test classpath.
     *
     * @parameter default-value="false"
     * @required
     */
    private boolean test;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (!this.targetPath.exists())
        {
            boolean mkdirs = this.targetPath.mkdirs();
            if (!mkdirs)
            {
                throw new MojoFailureException("Could not create directory " + this.targetPath);
            }
        }
        this.getLog().info("Writing generated code to " + this.targetPath);

        GsCollectionsCodeGenerator gsCollectionsCodeGenerator =
                new GsCollectionsCodeGenerator(this.targetPath, this.templateFileName, this.destinationFileSuffix);
        gsCollectionsCodeGenerator.generate();

        if (this.test)
        {
            this.project.addTestCompileSourceRoot(this.targetPath.getAbsolutePath());
        }
        else
        {
            this.project.addCompileSourceRoot(this.targetPath.getAbsolutePath());
        }
    }
}
