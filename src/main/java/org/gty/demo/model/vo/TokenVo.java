package org.gty.demo.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonDeserialize(builder = TokenVo.Builder.class)
public class TokenVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -1665378479149672883L;

    private final String token;

    public TokenVo(final Builder builder) {
        token = builder.token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenVo)) return false;
        final TokenVo tokenVo = (TokenVo) o;
        return Objects.equals(token, tokenVo.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("token", token)
            .toString();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String token;

        public Builder() {
        }

        public Builder(final TokenVo tokenVo) {
            token = tokenVo.token;
        }

        public Builder withToken(final String token) {
            this.token = token;
            return this;
        }

        public TokenVo build() {
            return new TokenVo(this);
        }
    }
}
