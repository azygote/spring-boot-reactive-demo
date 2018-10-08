package org.gty.demo.model.form;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.gty.demo.constant.DeleteFlag;
import org.gty.demo.model.po.Student;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import javax.validation.constraints.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.time.Instant;
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

        return new EqualsBuilder()
                .append(name, that.name)
                .append(age, that.age)
                .append(gender, that.gender)
                .append(balance, that.balance)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(age)
                .append(gender)
                .append(balance)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("age", age)
                .append("gender", gender)
                .append("balance", balance)
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
        student.setDeleteFlag(DeleteFlag.NOT_DELETED.ordinal());
        student.setOtherInformation("Not Applicable");

        Resource resource = new ClassPathResource("images/logo.png");

        try (var input = Channels.newChannel(resource.getInputStream());
             var byteArrayOutputStream = new ByteArrayOutputStream();
             var bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream, 8);
             var output = Channels.newChannel(bufferedOutputStream)) {
            var buffer = ByteBuffer.allocate(16);
            while (input.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    output.write(buffer);
                }
                buffer.clear();
            }
            bufferedOutputStream.flush();
            var bytes = byteArrayOutputStream.toByteArray();

            student.setPhoto(bytes);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        var milliseconds = Instant.now().toEpochMilli();
        student.setCreatedDate(milliseconds);
        student.setModifiedDate(milliseconds);

        return student;
    }
}
