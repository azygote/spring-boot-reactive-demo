package org.gty.demo.model.po;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Table(name = "t_sys_user_role", schema = "public")
public class SystemUserRole implements Serializable {

    private static final long serialVersionUID = 7471280241224013302L;

    @Id
    @ColumnType(column = "username", jdbcType = JdbcType.VARCHAR)
    private String username;

    @Id
    @ColumnType(column = "role", jdbcType = JdbcType.VARCHAR)
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
        if (!(o instanceof SystemUserRole)) return false;
        SystemUserRole that = (SystemUserRole) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getRole(), that.getRole()) &&
                Objects.equals(getDeleteMark(), that.getDeleteMark()) &&
                Objects.equals(getCreatedDate(), that.getCreatedDate()) &&
                Objects.equals(getModifiedDate(), that.getModifiedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getRole(), getDeleteMark(), getCreatedDate(), getModifiedDate());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SystemUserRole.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("role='" + role + "'")
                .add("deleteMark=" + deleteMark)
                .add("createdDate=" + createdDate)
                .add("modifiedDate=" + modifiedDate)
                .toString();
    }
}
