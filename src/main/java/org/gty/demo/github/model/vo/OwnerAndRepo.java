package org.gty.demo.github.model.vo;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

public class OwnerAndRepo implements Serializable {

    private static final long serialVersionUID = -2913891333781890297L;

    private final String owner;
    private final String repo;

    public OwnerAndRepo(final String owner, final String repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepo() {
        return repo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OwnerAndRepo)) return false;
        final var that = (OwnerAndRepo) o;
        return Objects.equals(owner, that.owner) &&
            Objects.equals(repo, that.repo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, repo);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("owner", owner)
            .add("repo", repo)
            .toString();
    }

    public static class Builder {
        private String owner;
        private String repo;

        public Builder() {
        }

        public Builder(final OwnerAndRepo ownerAndRepo) {
            this.owner = ownerAndRepo.getOwner();
            this.repo = ownerAndRepo.getRepo();
        }

        public Builder withOwner(final String owner) {
            this.owner = owner;
            return this;
        }

        public Builder withRepo(final String repo) {
            this.repo = repo;
            return this;
        }

        public OwnerAndRepo build() {
            return new OwnerAndRepo(owner, repo);
        }
    }
}
