package org.lapanen.stealth.signing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.cert.Certificate;
import java.util.List;

import org.joda.time.DateTime;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.repo.PathBuilder;
import org.lapanen.stealth.maven.repo.UrlBuilder;
import org.lapanen.stealth.signing.repository.ArtifactSigningRepository;
import org.lapanen.stealth.util.HttpDownloadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestOperations;

import com.google.common.base.Optional;

public class SignedJarRepoImpl implements SignedJarRepo {

    private static final Logger log = LoggerFactory.getLogger(SignedJarRepoImpl.class);

    @Autowired
    private ArtifactSigningRepository signingRepository;

    private final JarSigner signer;

    private final PathBuilder targetPathBuilder;

    private final UrlBuilder sourceUrlBuilder;

    private final RestOperations rest;

    public SignedJarRepoImpl(final JarSigner signer, final PathBuilder targetPathBuilder, final UrlBuilder sourceUrlBuilder,
            final RestOperations restTemplate) {
        this.signer = signer;
        this.targetPathBuilder = targetPathBuilder;
        this.sourceUrlBuilder = sourceUrlBuilder;
        this.rest = restTemplate;
    }

    @Override
    public boolean hasJar(final Artifact artifact) {
        return targetPathBuilder.buildJarFilePath(artifact).exists();
    }

    @Override
    public ArtifactSigning fetchAndSign(final Artifact artifact) throws SigningException {
        File tmp = null;
        FileOutputStream fos = null;
        try {
            final URI jarUri = sourceUrlBuilder.buildUri(artifact);
            tmp = File.createTempFile("signing-", ".jar");
            fos = new FileOutputStream(tmp);
            log.debug("Downloading {} from {} and writing response to {}", new Object[] { artifact, jarUri, tmp });
            HttpDownloadUtil.writeGetUrlTargetToStream(jarUri, fos, rest);
            final File target = targetPathBuilder.buildJarFilePath(artifact);
            final Certificate cert = signer.signJar(tmp.getAbsolutePath(), target.getAbsolutePath());
            return new ArtifactSigningImpl(artifact, cert, new DateTime());
        } catch (IOException e) {
            throw new SigningException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new SigningException(e);
                }
            }
            if (tmp != null) {
                tmp.delete();
            }
        }
    }

    @Override
    public Optional<ArtifactSigning> findLastSigningFor(final Artifact artifact) {
        final List<ArtifactSigning> signings = signingRepository
                .findByGroupIdAndArtifactIdAndVersion(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                        new Sort(Sort.Direction.DESC, "signingTime"));
        if (!signings.isEmpty()) {
            return Optional.of(signings.get(0));
        }
        return Optional.absent();
    }
}
