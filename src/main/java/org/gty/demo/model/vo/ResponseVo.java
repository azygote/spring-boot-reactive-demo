package org.gty.demo.model.vo;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class ResponseVo<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5608381641827943451L;

    private ResponseStatus status;
    private ResponseCode code;
    private T data;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseVo)) return false;
        final var that = (ResponseVo<?>) o;
        return status == that.status &&
            code == that.code &&
            Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, code, data);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("status", status)
            .add("code", code)
            .add("data", data)
            .toString();
    }

    @Nonnull
    public static <T> ResponseVo<T> success(@Nullable final T data) {
        return response(ResponseStatus.SUCCESS, null, data);
    }

    @Nonnull
    public static <T> ResponseVo<T> success() {
        return response(ResponseStatus.SUCCESS, null, null);
    }

    @Nonnull
    public static <T> ResponseVo<T> internalError(@Nullable final T data) {
        return response(ResponseStatus.FAILURE, ResponseCode.INTERNAL_ERROR, data);
    }

    @Nonnull
    public static <T> ResponseVo<T> jwtAuthorizationError(@Nullable final T data) {
        return response(ResponseStatus.FAILURE, ResponseCode.JWT_AUTHORIZATION_ERROR, data);
    }

    @Nonnull
    public static <T> ResponseVo<T> illegalParameters(@Nullable final T data) {
        return response(ResponseStatus.FAILURE, ResponseCode.ILLEGAL_PARAMETERS, data);
    }

    @Nonnull
    public static <T> ResponseVo<T> unauthorized(@Nullable final T data) {
        return response(ResponseStatus.FAILURE, ResponseCode.UNAUTHORIZED, data);
    }

    @Nonnull
    private static <T> ResponseVo<T> response(@Nonnull final ResponseStatus status,
                                              @Nullable final ResponseCode code,
                                              @Nullable final T data) {
        Objects.requireNonNull(status, "[status] must not be null");

        var response = new ResponseVo<T>();
        response.setStatus(status);
        response.setCode(code);
        response.setData(data);
        return response;
    }

    private enum ResponseStatus {
        SUCCESS,
        FAILURE
    }

    private enum ResponseCode {
        INTERNAL_ERROR,
        ILLEGAL_PARAMETERS,
        UNAUTHORIZED,
        JWT_AUTHORIZATION_ERROR
    }
}
