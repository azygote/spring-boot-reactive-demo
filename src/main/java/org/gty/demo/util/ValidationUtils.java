package org.gty.demo.util;

import javax.annotation.Nonnull;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;

public final class ValidationUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(@Nonnull T bean) {
        Objects.requireNonNull(bean, "bean must not be null");

        var violations = validator.validate(bean);

        if (!violations.isEmpty()) {
            var errorMessageBuilder = new StringBuilder("[");
            violations.forEach(violation -> {
                var field = violation.getPropertyPath().toString();
                var message = violation.getMessage();

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
