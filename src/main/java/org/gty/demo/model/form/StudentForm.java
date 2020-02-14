package org.gty.demo.model.form;

import com.google.common.base.MoreObjects;
import org.gty.demo.constant.DeleteMark;
import org.gty.demo.model.entity.Student;
import org.gty.demo.util.NioUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.constraints.*;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Objects;

public class StudentForm implements Serializable {

    private static final long serialVersionUID = 7658581343746290006L;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    @Max(120)
    private Integer age;

    @NotBlank
    private String gender;

    @NotBlank
    @Pattern(regexp = "^\\s*(?=.*[0-9])\\d*(?:\\.\\d{1,2})?\\s*$",
        message = "大于等于零的两位小数")
    private String balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentForm)) return false;
        StudentForm that = (StudentForm) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(age, that.age) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, gender, balance);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("age", age)
            .add("gender", gender)
            .add("balance", balance)
            .toString();
    }

    @Nonnull
    public static Student build(@Nonnull StudentForm studentForm) {
        Objects.requireNonNull(studentForm, "studentForm must not be null");

        var student = new Student();
        student.setName(studentForm.getName().strip());
        student.setAge(studentForm.getAge());
        student.setGender(studentForm.getGender().strip());
        student.setBalance(new BigDecimal(studentForm.getBalance().strip()));
        student.setDeleteMark(DeleteMark.NOT_DELETED);
        student.setOtherInformation("Not Applicable");

        Resource resource = new ClassPathResource("images/logo.png");

        try (var in = resource.getInputStream()) {
            var bytes = NioUtils.toByteArray(in);
            var blob = new SerialBlob(bytes);

            student.setPhoto(blob);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return student;
    }
}
