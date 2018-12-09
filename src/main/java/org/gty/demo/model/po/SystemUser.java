package org.gty.demo.model.po;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Table(name = "t_sys_user", schema = "public")
public class SystemUser implements Serializable {

    private static final long serialVersionUID = -884040706963724292L;

    @Id
    @ColumnType(column = "username", jdbcType = JdbcType.VARCHAR)
    private String username;

    @ColumnType(column = "password", jdbcType = JdbcType.VARCHAR)
    private String password;

    @ColumnType(column = "roles", jdbcType = JdbcType.VARCHAR)
    private String roles;

    @ColumnType(column = "delete_mark", jdbcType = JdbcType.SMALLINT)
    private Integer deleteMark;

    @ColumnType(column = "created_date", jdbcType = JdbcType.BIGINT)
    private Long createdDate;

    @ColumnType(column = "modified_date", jdbcType = JdbcType.BIGINT)
    private Long modifiedDate;

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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Integer getDeleteMark() {
        return deleteMark;
    }

    public void setDeleteMark(Integer deleteMark) {
        this.deleteMark = deleteMark;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemUser)) return false;
        SystemUser that = (SystemUser) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getRoles(), that.getRoles()) &&
                Objects.equals(getDeleteMark(), that.getDeleteMark()) &&
                Objects.equals(getCreatedDate(), that.getCreatedDate()) &&
                Objects.equals(getModifiedDate(), that.getModifiedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getRoles(), getDeleteMark(), getCreatedDate(), getModifiedDate());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SystemUser.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("roles='" + roles + "'")
                .add("deleteMark=" + deleteMark)
                .add("createdDate=" + createdDate)
                .add("modifiedDate=" + modifiedDate)
                .toString();
    }
}
