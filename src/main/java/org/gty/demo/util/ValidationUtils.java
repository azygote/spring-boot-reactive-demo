package org.gty.demo.util;

import javax.annotation.Nonnull;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;

public final class ValidationUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(@Nonnull final T bean) {
        final var violations = validator.validate(Objects.requireNonNull(bean, "bean must not be null"));

        if (!violations.isEmpty()) {
            final var errorMessageBuilder = new StringBuilder("[");
            violations.forEach(violation -> {
                final var field = violation.getPropertyPath().toString();
                final var message = violation.getMessage();

                errorMessageBuilder.append(field).append(" ").append(message).append("; ");
            });

            errorMessageBuilder.deleteCharAt(errorMessageBuilder.length() - 1);
            errorMessageBuilder.deleteCharAt(errorMessageBuilder.length() - 1);
            errorMessageBuilder.append("]");

            throw new IllegalArgumentException(errorMessageBuilder.toString());
        }
    }

    private ValidationUtils() {}
}
