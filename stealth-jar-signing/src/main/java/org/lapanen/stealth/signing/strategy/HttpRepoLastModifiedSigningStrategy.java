package org.lapanen.stealth.signing.strategy;

import java.net.URI;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.lapanen.stealth.maven.repo.UrlBuilder;
import org.lapanen.stealth.signing.SignedJarRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestOperations;

public class HttpRepoLastModifiedSigningStrategy extends AbstractLastModifiedSigningStrategy {

    private static final Logger log = LoggerFactory.getLogger(HttpRepoLastModifiedSigningStrategy.class);

    private final RestOperations rest;

    private final UrlBuilder urlBuilder;

    public HttpRepoLastModifiedSigningStrategy(final SignedJarRepo signedJarRepo, final RestOperations rest, final UrlBuilder urlBuilder) {
        super(signedJarRepo);
        this.rest = rest;
        this.urlBuilder = urlBuilder;
    }

    protected ReadableInstant getLastModifiedOriginal(Artifact artifact) {
        final URI jarUri = urlBuilder.buildUri(artifact);
        log.debug("Looking at artifact at URL {}", jarUri);
        // TODO: handle 404
        final HttpHeaders headers = rest.headForHeaders(jarUri);
        return new DateTime(headers.getLastModified());
    }

}
