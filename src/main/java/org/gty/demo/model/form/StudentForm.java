package org.gty.demo.model.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import org.gty.demo.constant.DeleteMark;
import org.gty.demo.model.entity.Student;
import org.gty.demo.util.NioUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import javax.sql.rowset.serial.SerialBlob;
import jakarta.validation.constraints.*;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Objects;

@JsonDeserialize(builder = StudentForm.Builder.class)
public class StudentForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 7658581343746290006L;

    @NotBlank
    private final String name;

    @NotNull
    @Positive
    @Max(120)
    private final Integer age;

    @NotBlank
    private final String gender;

    @NotBlank
    @Pattern(regexp = "^\\s*(?=.*[0-9])\\d*(?:\\.\\d{1,2})?\\s*$",
        message = "大于等于零的两位小数")
    private final String balance;

    public StudentForm(final Builder builder) {
        name = builder.name;
        age = builder.age;
        gender = builder.gender;
        balance = builder.balance;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getBalance() {
        return balance;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String name;
        private Integer age;
        private String gender;
        private String balance;

        public Builder() {
        }

        public Builder(final StudentForm studentForm) {
            age = studentForm.age;
            balance = studentForm.balance;
            gender = studentForm.gender;
            name = studentForm.name;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withAge(final Integer age) {
            this.age = age;
            return this;
        }

        public Builder withGender(final String gender) {
            this.gender = gender;
            return this;
        }

        public Builder withBalance(final String balance) {
            this.balance = balance;
            return this;
        }

        public StudentForm build() {
            return new StudentForm(this);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentForm)) return false;
        final var that = (StudentForm) o;
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
    public static Student build(@Nonnull final StudentForm studentForm) {
        Objects.requireNonNull(studentForm, "studentForm must not be null");

        var student = new Student();
        student.setName(studentForm.getName().strip());
        student.setAge(studentForm.getAge());
        student.setGender(studentForm.getGender().strip());
        student.setBalance(new BigDecimal(studentForm.getBalance().strip()));
        student.setDeleteMark(DeleteMark.NOT_DELETED);
        student.setOtherInformation("Not Applicable");

        final Resource resource = new ClassPathResource("images/logo.png");

        try (final var in = resource.getInputStream()) {
            final var bytes = NioUtils.toByteArray(in);
            final var blob = new SerialBlob(bytes);

            student.setPhoto(blob);
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }

        return student;
    }
}
