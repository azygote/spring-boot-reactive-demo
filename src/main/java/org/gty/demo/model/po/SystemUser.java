package org.gty.demo.model.po;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_sys_user")
public class SystemUser implements Serializable {

    private static final long serialVersionUID = -884040706963724292L;

    @Id
    @ColumnType(column = "username", jdbcType = JdbcType.VARCHAR)
    private String username;

    @ColumnType(column = "password", jdbcType = JdbcType.VARCHAR)
    private String password;

    @ColumnType(column = "roles", jdbcType = JdbcType.CLOB)
    private String roles;

    @ColumnType(column = "delete_flag", jdbcType = JdbcType.TINYINT)
    private Integer deleteFlag;

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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
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

        return new EqualsBuilder()
                .append(username, that.username)
                .append(password, that.password)
                .append(roles, that.roles)
                .append(deleteFlag, that.deleteFlag)
                .append(createdDate, that.createdDate)
                .append(modifiedDate, that.modifiedDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(password)
                .append(roles)
                .append(deleteFlag)
                .append(createdDate)
                .append(modifiedDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("password", password)
                .append("roles", roles)
                .append("deleteFlag", deleteFlag)
                .append("createdDate", createdDate)
                .append("modifiedDate", modifiedDate)
                .toString();
    }
}
