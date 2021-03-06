package org.gty.demo.model.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "t_sys_user", schema = "public")
public class SystemUser extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = -884040706963724292L;

    @Id
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemUser)) return false;
        if (!super.equals(o)) return false;
        SystemUser that = (SystemUser) o;
        return Objects.equals(username, that.username) &&
            Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("username", username)
            .add("password", password)
            .toString();
    }
}
