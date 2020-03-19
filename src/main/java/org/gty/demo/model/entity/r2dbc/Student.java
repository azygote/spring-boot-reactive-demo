package org.gty.demo.model.entity.r2dbc;

import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Table("t_student")
public class Student implements Serializable {

    private static final long serialVersionUID = 7263206029911443934L;

    @Id
    @Column("student_id")
    private Long id;

    @Column("student_name")
    private String name;

    @Column("student_gender")
    private String gender;

    @Column("student_age")
    private String age;

    @Column("student_balance")
    private BigDecimal balance;

    @Column("student_other_information")
    private String otherInfo;

    @Column("delete_mark")
    private Integer deleteFlag;

    @Column("created_date")
    private Long createdDate;

    @Column("modified_date")
    private Long modifiedDate;

    @Column("version")
    private Long version;

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
            Objects.equals(name, student.name) &&
            Objects.equals(gender, student.gender) &&
            Objects.equals(age, student.age) &&
            Objects.equals(balance, student.balance) &&
            Objects.equals(otherInfo, student.otherInfo) &&
            Objects.equals(deleteFlag, student.deleteFlag) &&
            Objects.equals(createdDate, student.createdDate) &&
            Objects.equals(modifiedDate, student.modifiedDate) &&
            Objects.equals(version, student.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, age, balance, otherInfo, deleteFlag, createdDate, modifiedDate, version);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("name", name)
            .add("gender", gender)
            .add("age", age)
            .add("balance", balance)
            .add("otherInfo", otherInfo)
            .add("deleteFlag", deleteFlag)
            .add("createdDate", createdDate)
            .add("modifiedDate", modifiedDate)
            .add("version", version)
            .toString();
    }
}
