package org.gty.demo.model.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "t_sys_user_role", schema = "public")
public class SystemUserRole extends Base implements Serializable {

    private static final long serialVersionUID = 7471280241224013302L;

    @EmbeddedId
    private SystemUserRoleId id;

    public SystemUserRoleId getId() {
        return id;
    }

    public void setId(SystemUserRoleId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemUserRole)) return false;
        if (!super.equals(o)) return false;
        SystemUserRole that = (SystemUserRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }

    @Embeddable
    public static class SystemUserRoleId implements Serializable {

        private static final long serialVersionUID = 1886142281501479354L;

        private String username;

        private String role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SystemUserRoleId)) return false;
            SystemUserRoleId that = (SystemUserRoleId) o;
            return Objects.equals(username, that.username) &&
                Objects.equals(role, that.role);
        }

        @Override
        public int hashCode() {
            return Objects.hash(username, role);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("username", username)
                    .add("role", role)
                    .toString();
        }
    }
}
