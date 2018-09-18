package org.gty.demo.model.po;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "t_student")
public class Student implements Serializable {

    private static final long serialVersionUID = 2707444643224940512L;

    @Id
    @KeySql(useGeneratedKeys = true)
    @ColumnType(column = "student_id", jdbcType = JdbcType.BIGINT)
    private Long id;

    @ColumnType(column = "student_name", jdbcType = JdbcType.VARCHAR)
    private String name;

    @ColumnType(column = "student_gender", jdbcType = JdbcType.VARCHAR)
    private String gender;

    @ColumnType(column = "student_age", jdbcType = JdbcType.INTEGER)
    private Integer age;

    @ColumnType(column = "student_balance", jdbcType = JdbcType.DECIMAL)
    private BigDecimal balance;

    @ColumnType(column = "student_other_information", jdbcType = JdbcType.CLOB)
    private String otherInformation;

    @ColumnType(column = "student_photo", jdbcType = JdbcType.BLOB)
    private byte[] photo;

    @ColumnType(column = "delete_flag", jdbcType = JdbcType.TINYINT)
    private Integer deleteFlag;

    @ColumnType(column = "created_date", jdbcType = JdbcType.BIGINT)
    private Long createdDate;

    @ColumnType(column = "modified_date", jdbcType = JdbcType.BIGINT)
    private Long modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOtherInformation() {
        return otherInformation;
    }

    public void setOtherInformation(String otherInformation) {
        this.otherInformation = otherInformation;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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

        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        return new EqualsBuilder()
                .append(id, student.id)
                .append(name, student.name)
                .append(gender, student.gender)
                .append(age, student.age)
                .append(balance, student.balance)
                .append(otherInformation, student.otherInformation)
                .append(photo, student.photo)
                .append(deleteFlag, student.deleteFlag)
                .append(createdDate, student.createdDate)
                .append(modifiedDate, student.modifiedDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(gender)
                .append(age)
                .append(balance)
                .append(otherInformation)
                .append(photo)
                .append(deleteFlag)
                .append(createdDate)
                .append(modifiedDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("gender", gender)
                .append("age", age)
                .append("balance", balance)
                .append("otherInformation", otherInformation)
                .append("photo", photo)
                .append("deleteFlag", deleteFlag)
                .append("createdDate", createdDate)
                .append("modifiedDate", modifiedDate)
                .toString();
    }
}
