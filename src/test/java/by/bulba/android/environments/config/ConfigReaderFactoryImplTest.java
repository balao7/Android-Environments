/*
 * Copyright (C) 2019. Ilya Pavlovskii
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package by.bulba.android.environments.config;

import by.bulba.android.environments.AndroidEnvironmentsExtension;
import org.gradle.api.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ConfigReaderFactoryImplTest {

    private static final String ROOT_PATH = "some/root/dir";
    private static final String CONFIG_PATH = "config/dir";
    private static final String CONFIG_FILE = "config.proper";
    private static final String PATTERN = ROOT_PATH + "/" +
            CONFIG_PATH + "/%s/" + CONFIG_FILE;

    @Mock
    private File rootFile;
    @Mock
    private Project project;
    private AndroidEnvironmentsExtension extension = new AndroidEnvironmentsExtension();
    private ConfigReaderFactoryImpl factory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        extension.configPath = CONFIG_PATH;
        extension.configFile = CONFIG_FILE;
        extension.useProductFlavors = true;
        extension.useBuildTypes = true;
        when(rootFile.getPath()).thenReturn(ROOT_PATH);
        when(project.getRootDir()).thenReturn(rootFile);
        factory = new ConfigReaderFactoryImpl(project, extension);
    }

    @Test
    public void checkConfigFilePatternBuilder() {
        String pattern = factory.readConfigFilePattern(project, extension);
        assertEquals(PATTERN, pattern);
    }

    @Test
    public void createPropertyConfigReaderWhenFileIsNotExists() {
        when(rootFile.exists()).thenReturn(false);
        ConfigReader reader = factory.createPropertyConfigReader(rootFile);
        assertThat(reader, instanceOf(PropertyConfigReader.class));
    }

    @Test
    public void createPropertyConfigReaderWhenFileExists() {
        ConfigReader reader = factory.createPropertyConfigReader(new File(""));
        assertThat(reader, instanceOf(PropertyConfigReader.class));
    }

    @Test
    public void createPropertyConfigReaderByDefault() {
        ConfigReader reader = factory.create("debug");
        assertThat(reader, instanceOf(PropertyConfigReader.class));
    }

}