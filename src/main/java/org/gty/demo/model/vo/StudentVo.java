package org.gty.demo.model.vo;

import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.entity.Student;
import org.gty.demo.util.NioUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.StringJoiner;

public class StudentVo implements Serializable {

    private static final long serialVersionUID = 6561582571605431635L;

    private String name;
    private String gender;
    private Integer age;
    private String balance;
    private String otherInformation;
    private String createdDate;
    private String modifiedDate;
    private String photo;

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOtherInformation() {
        return otherInformation;
    }

    public void setOtherInformation(String otherInformation) {
        this.otherInformation = otherInformation;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentVo)) return false;
        StudentVo studentVo = (StudentVo) o;
        return Objects.equals(getName(), studentVo.getName()) &&
                Objects.equals(getGender(), studentVo.getGender()) &&
                Objects.equals(getAge(), studentVo.getAge()) &&
                Objects.equals(getBalance(), studentVo.getBalance()) &&
                Objects.equals(getOtherInformation(), studentVo.getOtherInformation()) &&
                Objects.equals(getCreatedDate(), studentVo.getCreatedDate()) &&
                Objects.equals(getModifiedDate(), studentVo.getModifiedDate()) &&
                Objects.equals(getPhoto(), studentVo.getPhoto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getGender(), getAge(), getBalance(), getOtherInformation(), getCreatedDate(), getModifiedDate(), getPhoto());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StudentVo.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("gender='" + gender + "'")
                .add("age=" + age)
                .add("balance='" + balance + "'")
                .add("otherInformation='" + otherInformation + "'")
                .add("createdDate='" + createdDate + "'")
                .add("modifiedDate='" + modifiedDate + "'")
                .add("photo='" + photo + "'")
                .toString();
    }

    @Nonnull
    public static StudentVo build(@Nonnull Student student) {
        Objects.requireNonNull(student, "student must not be null");

        var studentVo = new StudentVo();
        studentVo.setName(student.getName());
        studentVo.setGender(student.getGender());
        studentVo.setAge(student.getAge());
        studentVo.setOtherInformation(student.getOtherInformation());

        try (var in = student.getPhoto().getBinaryStream()) {
            var bytes = NioUtils.toByteArray(in);
            var base64String = new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
            var photo = "data:image/png;base64," + base64String;

            studentVo.setPhoto(photo);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (student.getBalance() != null) {
            studentVo.setBalance(student.getBalance().toString());
        }

        if (student.getCreatedDate() != null) {
            var instant = Instant.ofEpochMilli(student.getCreatedDate());
            var date = ZonedDateTime.ofInstant(instant, SystemConstants.defaultTimeZone);
            studentVo.setCreatedDate(date.format(SystemConstants.defaultDateTimeFormatter));
        }

        if (student.getModifiedDate() != null) {
            var instant = Instant.ofEpochMilli(student.getModifiedDate());
            var date = ZonedDateTime.ofInstant(instant, SystemConstants.defaultTimeZone);
            studentVo.setModifiedDate(date.format(SystemConstants.defaultDateTimeFormatter));
        }

        return studentVo;
    }
}
