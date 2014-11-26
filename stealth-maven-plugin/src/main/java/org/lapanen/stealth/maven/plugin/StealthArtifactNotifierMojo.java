package org.lapanen.stealth.maven.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.ResolutionNode;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.lapanen.stealth.StealthException;
import org.lapanen.stealth.maven.artifact.ArtifactDTO;
import org.lapanen.stealth.maven.artifact.DependencyImpl;
import org.lapanen.stealth.maven.artifact.transport.ArtifactTransport;
import org.lapanen.stealth.maven.artifact.transport.spi.ArtifactTransportService;

import com.google.common.base.Optional;

/**
 * @requiresDependencyResolution compile
 * @requiresDependencyCollection compile
 * @goal notify-artifact
 */
public class StealthArtifactNotifierMojo extends AbstractMojo {

    /**
     * @parameter default-value="${project.artifact}"
     * @required
     * @readonly
     */
    private Artifact artifact;

    /**
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     */
    private ArtifactFactory artifactFactory;

    /**
     * @component role="org.apache.maven.artifact.resolver.ArtifactResolver"
     */
    private ArtifactResolver resolver;

    /**
     * @parameter default-value="${localRepository}"
     */
    private ArtifactRepository localRepository;

    /**
     * @parameter default-value="${project.remoteArtifactRepositories}"
     */
    private List<ArtifactRepository> remoteRepositories;

    /**
     * @component role="org.apache.maven.project.MavenProjectBuilder"
     */
    private MavenProjectBuilder mavenProjectBuilder;

    /**
     * @component role="org.apache.maven.artifact.metadata.ArtifactMetadataSource"
     */
    private ArtifactMetadataSource artifactMetadataSource;

    /**
     * @parameter
     */
    private Map<String, String> transportConfiguration;

    /**
     * @parameter default-value="false"
     */
    private boolean failOnException;

    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException {

        getLog().debug(transportConfiguration.toString());
        try {
            final String groupId = artifact.getGroupId();
            final String artifactId = artifact.getArtifactId();
            final String version = artifact.getVersion();
            final String classifier = artifact.getClassifier();
            final Artifact pomArtifact = artifactFactory.createArtifact(groupId, artifactId, version, classifier, "pom");
            final MavenProject pomProject = mavenProjectBuilder.buildFromRepository(pomArtifact, remoteRepositories, localRepository);
            final String packaging = pomProject.getPackaging();
            final Set<Artifact> artifacts = pomProject.createArtifacts(this.artifactFactory, null, null);
            final ArtifactResolutionResult artifactResolutionResult = resolver
                    .resolveTransitively(artifacts, pomArtifact, localRepository, remoteRepositories, artifactMetadataSource, null);

            final Set<ResolutionNode> nodes = artifactResolutionResult.getArtifactResolutionNodes();

            final ArtifactDTO dto = new ArtifactDTO(groupId, artifactId, version, packaging);

            for (final ResolutionNode res : nodes) {
                final List<String> trail = res.getDependencyTrail();
                final DependencyImpl.Builder builder = DependencyImpl.Builder.ofArtifact(parse(trail.get(trail.size() - 1)));
                builder.setScope(res.getArtifact().getScope());
                builder.setType(res.getArtifact().getType());
                builder.setClassifier(res.getArtifact().getClassifier());
                dto.addDependency(builder.build());
            }
            for (final ArtifactDTO ancestor : buildAncestorHierarchy(pomProject)) {
                dto.addParent(ancestor);
            }
            Optional<ArtifactTransport> transportCandidate = ArtifactTransportService.getInstance().allocateTransport(transportConfiguration);
            if (transportCandidate.isPresent()) {
                final ArtifactTransport transport = transportCandidate.get();
                getLog().info("Trying to transport [" + dto + "] using " + transport);
                try {
                    transport.transport(dto);
                } catch (StealthException e) {
                    getLog().error("While trying to transport artifact: " + e.getMessage());
                }
            } else {
                getLog().error("Did not find any artifact transports");
            }
        } catch (final ProjectBuildingException | InvalidDependencyVersionException | ArtifactResolutionException | ArtifactNotFoundException e) {
            getLog().error("While handling " + this.artifact, e);
        }
    }

    public static ArtifactDTO parse(final String identifier) {
        final String[] parts = identifier.split(":");
        return new ArtifactDTO(parts[0], parts[1], parts[3], parts[2]);
    }

    private List<ArtifactDTO> buildAncestorHierarchy(final MavenProject project) {
        MavenProject child = project;
        final List<ArtifactDTO> ret = new ArrayList<ArtifactDTO>();
        while ((child = child.getParent()) != null) {
            ret.add(new ArtifactDTO(project.getGroupId(), project.getArtifactId(), project.getVersion(), project.getPackaging()));
        }
        return ret;
    }

}
