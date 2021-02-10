package org.gty.demo.model.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;

@JsonDeserialize(builder = TokenForm.Builder.class)
public class TokenForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 4524679836408168258L;

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    public TokenForm(final Builder builder) {
        username = builder.username;
        password = builder.password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String username;
        private String password;

        public Builder() {
        }

        public Builder(final TokenForm tokenForm) {
            username = tokenForm.username;
            password = tokenForm.password;
        }

        public Builder withUsername(final String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(final String password) {
            this.password = password;
            return this;
        }

        public TokenForm build() {
            return new TokenForm(this);
        }
    }
}
