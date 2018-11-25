package org.gty.demo.model.po;

import org.apache.ibatis.type.JdbcType;
import org.gty.demo.util.PostgresOidTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

@Table(name = "t_student", schema = "public")
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

    @ColumnType(column = "student_balance", jdbcType = JdbcType.NUMERIC)
    private BigDecimal balance;

    @ColumnType(column = "student_other_information", jdbcType = JdbcType.VARCHAR)
    private String otherInformation;

    @ColumnType(column = "student_photo", typeHandler = PostgresOidTypeHandler.class)
    private byte[] photo;

    @ColumnType(column = "delete_flag", jdbcType = JdbcType.SMALLINT)
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
        return Objects.equals(getId(), student.getId()) &&
                Objects.equals(getName(), student.getName()) &&
                Objects.equals(getGender(), student.getGender()) &&
                Objects.equals(getAge(), student.getAge()) &&
                Objects.equals(getBalance(), student.getBalance()) &&
                Objects.equals(getOtherInformation(), student.getOtherInformation()) &&
                Arrays.equals(getPhoto(), student.getPhoto()) &&
                Objects.equals(getDeleteFlag(), student.getDeleteFlag()) &&
                Objects.equals(getCreatedDate(), student.getCreatedDate()) &&
                Objects.equals(getModifiedDate(), student.getModifiedDate());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getName(), getGender(), getAge(), getBalance(), getOtherInformation(), getDeleteFlag(), getCreatedDate(), getModifiedDate());
        result = 31 * result + Arrays.hashCode(getPhoto());
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("gender='" + gender + "'")
                .add("age=" + age)
                .add("balance=" + balance)
                .add("otherInformation='" + otherInformation + "'")
                .add("photo=" + Arrays.toString(photo))
                .add("deleteFlag=" + deleteFlag)
                .add("createdDate=" + createdDate)
                .add("modifiedDate=" + modifiedDate)
                .toString();
    }
}
