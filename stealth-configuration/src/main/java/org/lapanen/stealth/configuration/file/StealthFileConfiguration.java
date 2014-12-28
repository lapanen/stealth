package org.lapanen.stealth.configuration.file;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class StealthFileConfiguration {

    public static final String DEFAULT_HOME = "/home/stealth";

    public static final String MAVEN_PATH = "maven";

    public static final String MAVEN_POM_PATH = "pom";

    public static final String MAVEN_ASSEMBLY_PATH = "assembly";

    public void initDirectories() {
        try {
            for (final Path dir : new Path[] {home(), mavenRoot()}) {
                Files.createDirectories(dir, directoryAttributes());
            }
        } catch (IOException e) {
            throw new BeanCreationException("While trying to create directories", e);
        }
    }

    @Bean
    public Path home() {
        return Paths.get(stringToFileURI(homePath()));
    }

    @Bean
    public Path mavenRoot() {
        return Paths.get(stringToFileURI(mavenRootPath()));
    }

    /**
     *
     * @return {@code null}
     */
    protected FileAttribute<?>[] directoryAttributes() {
        return null;
    }

    /**
     * @return {@value #DEFAULT_HOME}
     */
    protected String homePath() {
        return DEFAULT_HOME;
    }

    protected String mavenRootPath() {
        return DEFAULT_HOME + "/" + MAVEN_PATH;
    }

    private static URI stringToFileURI(final String path) {
        try {
            return new URI("file:" + path);
        } catch (URISyntaxException e) {
            throw new BeanCreationException("While trying to create URI from '%s'", e);
        }
    }
}
