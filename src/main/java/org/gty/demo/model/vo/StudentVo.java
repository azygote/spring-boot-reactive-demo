package org.gty.demo.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonDeserialize(builder = StudentVo.Builder.class)
public class StudentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 6561582571605431635L;

    private final String name;
    private final String gender;
    private final Integer age;
    private final String balance;
    private final String otherInformation;
    private final String createdDate;
    private final String modifiedDate;
    private final String photo;

    public StudentVo(final Builder builder) {
        name = builder.name;
        gender = builder.gender;
        age = builder.age;
        balance = builder.balance;
        otherInformation = builder.otherInformation;
        createdDate = builder.createdDate;
        modifiedDate = builder.modifiedDate;
        photo = builder.photo;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getBalance() {
        return balance;
    }

    public String getOtherInformation() {
        return otherInformation;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getPhoto() {
        return photo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentVo)) return false;
        final StudentVo studentVo = (StudentVo) o;
        return Objects.equals(name, studentVo.name) &&
            Objects.equals(gender, studentVo.gender) &&
            Objects.equals(age, studentVo.age) &&
            Objects.equals(balance, studentVo.balance) &&
            Objects.equals(otherInformation, studentVo.otherInformation) &&
            Objects.equals(createdDate, studentVo.createdDate) &&
            Objects.equals(modifiedDate, studentVo.modifiedDate) &&
            Objects.equals(photo, studentVo.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender, age, balance, otherInformation, createdDate, modifiedDate, photo);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("gender", gender)
            .add("age", age)
            .add("balance", balance)
            .add("otherInformation", otherInformation)
            .add("createdDate", createdDate)
            .add("modifiedDate", modifiedDate)
            .add("photo", photo)
            .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String name;
        private String gender;
        private Integer age;
        private String balance;
        private String otherInformation;
        private String createdDate;
        private String modifiedDate;
        private String photo;

        public Builder() {
        }

        public Builder(final StudentVo studentVo) {
            name = studentVo.name;
            gender = studentVo.gender;
            age = studentVo.age;
            balance = studentVo.balance;
            otherInformation = studentVo.otherInformation;
            createdDate = studentVo.createdDate;
            modifiedDate = studentVo.modifiedDate;
            photo = studentVo.photo;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withGender(final String gender) {
            this.gender = gender;
            return this;
        }

        public Builder withAge(final Integer age) {
            this.age = age;
            return this;
        }

        public Builder withBalance(final String balance) {
            this.balance = balance;
            return this;
        }

        public Builder withOtherInformation(final String otherInformation) {
            this.otherInformation = otherInformation;
            return this;
        }

        public Builder withCreatedDate(final String createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder withModifiedDate(final String modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public Builder withPhoto(final String photo) {
            this.photo = photo;
            return this;
        }

        public StudentVo build() {
            return new StudentVo(this);
        }
    }
}
