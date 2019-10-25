package org.gty.demo.model.entity;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Objects;

@Entity
@Table(name = "t_student", schema = "public")
public class Student extends Base implements Serializable {

    private static final long serialVersionUID = 2707444643224940512L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "student_name")
    private String name;

    @Column(name = "student_gender")
    private String gender;

    @Column(name = "student_age")
    private Integer age;

    @Column(name = "student_balance", precision = 30, scale = 10)
    private BigDecimal balance;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "student_other_information")
    private String otherInformation;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "student_photo")
    private Blob photo;

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

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
            Objects.equals(name, student.name) &&
            Objects.equals(gender, student.gender) &&
            Objects.equals(age, student.age) &&
            Objects.equals(balance, student.balance) &&
            Objects.equals(otherInformation, student.otherInformation) &&
            Objects.equals(photo, student.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, gender, age, balance, otherInformation, photo);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("name", name)
            .add("gender", gender)
            .add("age", age)
            .add("balance", balance)
            .add("otherInformation", otherInformation)
            .add("photo", photo)
            .toString();
    }
}
