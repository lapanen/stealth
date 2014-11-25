package org.lapanen.stealth.signing.strategy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.artifact.ArtifactDTO;
import org.lapanen.stealth.maven.repo.PathBuilder;
import org.lapanen.stealth.signing.ArtifactSigning;
import org.lapanen.stealth.signing.ArtifactSigningImpl;
import org.lapanen.stealth.signing.SignedJarRepo;
import org.mockito.Mockito;

import com.google.common.base.Optional;

public class SigningStrategyTest {

    private static final Artifact POM_ARTIFACT = new ArtifactDTO("a", "b", "0.1", "pom");
    private static final Artifact SNAPSHOT_ARTIFACT = new ArtifactDTO("a", "b", "0.1-SNAPSHOT");
    private static final Artifact GROUP_ID_ARTIFACT = new ArtifactDTO("org.lapanen", "stealth-core", "0.1-SNAPSHOT");

    private static final DateTime now = new DateTime();
    private static final DateTime aMinuteAgo = now.minusMinutes(1);
    private static final DateTime aMinuteFromNow = now.plusMinutes(1);

    @Test
    public void packaging_strategy() {
        final SigningStrategy packagingStrategy = new PackagingSigningStrategy("jar", "war");
        assertFalse(packagingStrategy.shouldSign(POM_ARTIFACT));
        assertTrue(packagingStrategy.shouldSign(SNAPSHOT_ARTIFACT));
    }

    @Test
    public void groupid_strategy() {
        final SigningStrategy groupIdStrategy = new GroupIdMatchingSigningStrategy("org\\..*");
        assertTrue(groupIdStrategy.shouldSign(GROUP_ID_ARTIFACT));
        assertFalse(groupIdStrategy.shouldSign(SNAPSHOT_ARTIFACT));
    }

    @Test
    public void and_strategy() {
        final SigningStrategy andStrategy = new AndSigningStrategy(new SnapshotSigningStrategy(), new PackagingSigningStrategy("jar"));
        assertTrue(andStrategy.shouldSign(SNAPSHOT_ARTIFACT));
        assertFalse(andStrategy.shouldSign(POM_ARTIFACT));
    }

    @Test
    public void lastmodified_file_strategy_on_earlier_signed() {
        try {
            final SignedJarRepo repo = mock(SignedJarRepo.class);
            when(repo.findLastSigningFor(POM_ARTIFACT)).thenReturn(Optional.<ArtifactSigning> of(new ArtifactSigningImpl(POM_ARTIFACT, null, aMinuteAgo)));
            final PathBuilder builder = mock(PathBuilder.class);
            final File original = File.createTempFile("signing", ".jar");
            original.deleteOnExit();
            when(builder.buildJarFilePath(POM_ARTIFACT)).thenReturn(original);
            final SigningStrategy lastModifiedFileStrategy = new FileSystemRepoLastModifiedSigningStrategy(repo, builder);
            assertTrue(lastModifiedFileStrategy.shouldSign(POM_ARTIFACT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void lastmodified_file_strategy_on_later_signed() {
        try {
            final SignedJarRepo repo = mock(SignedJarRepo.class);
            when(repo.findLastSigningFor(POM_ARTIFACT)).thenReturn(Optional.<ArtifactSigning> of(new ArtifactSigningImpl(POM_ARTIFACT, null, aMinuteFromNow)));
            final PathBuilder builder = mock(PathBuilder.class);
            final File original = File.createTempFile("signing", ".jar");
            original.deleteOnExit();
            when(builder.buildJarFilePath(POM_ARTIFACT)).thenReturn(original);
            final SigningStrategy lastModifiedFileStrategy = new FileSystemRepoLastModifiedSigningStrategy(repo, builder);
            assertFalse(lastModifiedFileStrategy.shouldSign(POM_ARTIFACT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
