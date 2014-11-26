package org.lapanen.stealth.maven.artifact;

import com.google.common.base.Optional;

public class DependencyImpl implements Dependency {

    /**
     * 
     */
    private static final long serialVersionUID = 5883478048583959251L;

    private final Artifact dependency;
    private final String scope;

    private final Optional<String> type;

    private final Optional<String> classifier;

    public static class Builder {

        private final Artifact dependency;

        private String scope = Dependency.DEFAULT_SCOPE;

        private String type;

        private String classifier;

        public Builder(final Artifact dependency) {
            super();
            this.dependency = dependency;
        }

        public static Builder ofArtifact(final Artifact dependency) {
            return new Builder(dependency);
        }

        public Dependency build() {
            return new DependencyImpl(this);
        }

        public Builder setScope(final String scope) {
            this.scope = scope; return this;
        }

        public Builder setType(final String type) {
            this.type = type; return this;
        }

        public Builder setClassifier(final String classifier) {
            this.classifier = classifier; return this;
        }

    }

    private DependencyImpl(final Builder builder) {
        this.dependency = builder.dependency;
        this.scope = builder.scope;
        this.classifier = Optional.fromNullable(builder.classifier);
        this.type = Optional.fromNullable(builder.type);
    }

    @Override
    public Artifact getDependency() {
        return dependency;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dependency == null) ? 0 : dependency.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DependencyImpl other = (DependencyImpl) obj;
        if (dependency == null) {
            if (other.dependency != null)
                return false;
        } else if (!dependency.equals(other.dependency))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DependencyDTO [" + dependency + " (" + scope + ")]";
    }

    @Override
    public Optional<String> getClassifier() {
        return classifier;
    }

    @Override
    public Optional<String> getType() {
        return type;
    }

}
