package com.obss.softwarecrafter.model.dto.result;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ServiceResult<T> {
    private final T value;
    private final Exception exception;

    private ServiceResult(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    public static <T> ServiceResult<T> success(T value) {
        return new ServiceResult<>(value, null);
    }
    public static <T> ServiceResult<T> error(Exception exception) {
        return new ServiceResult<>(null, exception);
    }

    public boolean hasError(){
        return Objects.nonNull(exception);
    }
}
