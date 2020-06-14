package org.gty.demo.github.model;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

public class Contributor implements Serializable {

    private static final long serialVersionUID = -5671000243188757881L;

    private String login;
    private Integer contributions;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getContributions() {
        return contributions;
    }

    public void setContributions(Integer contributions) {
        this.contributions = contributions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contributor)) return false;
        Contributor that = (Contributor) o;
        return Objects.equals(login, that.login) &&
            Objects.equals(contributions, that.contributions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, contributions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("login", login)
            .add("contributions", contributions)
            .toString();
    }
}
