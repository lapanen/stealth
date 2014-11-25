package org.lapanen.stealth.maven.artifact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Optional;

public class ArtifactDTO implements Artifact {

    /**
     *
     */
    private static final long serialVersionUID = 3069478929687107508L;

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String packaging;
    private final List<Dependency> dependencies;
    private final List<Artifact> parents;

    public ArtifactDTO(final Artifact artifact) {
        this(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getPackaging());
    }

    public ArtifactDTO(final String groupId, final String artifactId, final String version) {
        this(groupId, artifactId, version, Artifact.DEFAULT_PACKAGING);
    }

    public ArtifactDTO(final String groupId, final String artifactId, final String version, final String packaging) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.packaging = packaging;
        dependencies = new ArrayList<Dependency>();
        parents = new ArrayList<Artifact>();
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getPackaging() {
        return packaging;
    }

    @Override
    public Optional<String> getClassifier() {
        return Optional.absent();
    }

    public void addParent(final Artifact parent) {
        parents.add(parent);
    }

    @Override
    public List<Artifact> getParents() {
        return Collections.unmodifiableList(parents);
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void addDependency(final Dependency dependency) {
        dependencies.add(dependency);
    }

    @Override
    public List<Dependency> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((packaging == null) ? 0 : packaging.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArtifactDTO other = (ArtifactDTO) obj;
        if (artifactId == null) {
            if (other.artifactId != null) {
                return false;
            }
        } else if (!artifactId.equals(other.artifactId)) {
            return false;
        }
        if (groupId == null) {
            if (other.groupId != null) {
                return false;
            }
        } else if (!groupId.equals(other.groupId)) {
            return false;
        }
        if (packaging == null) {
            if (other.packaging != null) {
                return false;
            }
        } else if (!packaging.equals(other.packaging)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version + ":" + packaging;
    }

}
