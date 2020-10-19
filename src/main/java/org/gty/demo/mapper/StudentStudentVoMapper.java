package org.gty.demo.mapper;

import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.entity.Student;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.util.NioUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Objects;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface StudentStudentVoMapper {

    @Nonnull
    default StudentVo studentToStudentVo(@Nonnull final Student student) {
        Objects.requireNonNull(student, "student must not be null");

        var studentVoBuilder = StudentVo.builder()
            .withName(student.getName())
            .withGender(student.getGender())
            .withAge(student.getAge())
            .withOtherInformation(student.getOtherInformation());

        try (final var in = student.getPhoto().getBinaryStream()) {
            final var bytes = NioUtils.toByteArray(in);
            final var base64String = new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
            final var photo = "data:image/png;base64," + base64String;

            studentVoBuilder = studentVoBuilder.withPhoto(photo);
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (student.getBalance() != null) {
            studentVoBuilder = studentVoBuilder.withBalance(student.getBalance().toString());
        }

        if (student.getCreatedDate() != null) {
            final var instant = Instant.ofEpochMilli(student.getCreatedDate());
            final var date = ZonedDateTime.ofInstant(instant, SystemConstants.defaultTimeZone);
            studentVoBuilder = studentVoBuilder.withCreatedDate(date.format(SystemConstants.defaultDateTimeFormatter));
        }

        if (student.getModifiedDate() != null) {
            final var instant = Instant.ofEpochMilli(student.getModifiedDate());
            final var date = ZonedDateTime.ofInstant(instant, SystemConstants.defaultTimeZone);
            studentVoBuilder = studentVoBuilder.withModifiedDate(date.format(SystemConstants.defaultDateTimeFormatter));
        }

        return studentVoBuilder.build();
    }
}
