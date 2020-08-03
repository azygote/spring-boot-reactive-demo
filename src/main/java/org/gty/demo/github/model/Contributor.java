package org.gty.demo.github.model;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

public class Contributor implements Serializable {

    private static final long serialVersionUID = -5671000243188757881L;

    private final String login;
    private final Integer contributions;

    public Contributor(final String login, final Integer contributions) {
        this.login = login;
        this.contributions = contributions;
    }

    public String getLogin() {
        return login;
    }

    public Integer getContributions() {
        return contributions;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Contributor)) return false;
        final var that = (Contributor) o;
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

    public static class Builder {
        private String login;
        private Integer contributions;

        public Builder() {
        }

        public Builder(final Contributor contributor) {
            this.login = contributor.getLogin();
            this.contributions = contributor.getContributions();
        }

        public Builder withLogin(final String login) {
            this.login = login;
            return this;
        }

        public Builder withContributions(final Integer contributions) {
            this.contributions = contributions;
            return this;
        }

        public Contributor build() {
            return new Contributor(login, contributions);
        }
    }
}
