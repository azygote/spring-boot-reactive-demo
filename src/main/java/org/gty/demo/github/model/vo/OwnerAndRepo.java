package org.gty.demo.github.model.vo;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

public class OwnerAndRepo implements Serializable {

    private static final long serialVersionUID = -2913891333781890297L;

    private String owner;
    private String repo;

    public OwnerAndRepo() {
    }

    public OwnerAndRepo(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OwnerAndRepo)) return false;
        OwnerAndRepo that = (OwnerAndRepo) o;
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
}
