package com.sm.qms.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;

public class ApiResult<T> {

    @JsonProperty
    private final String success;
    @JsonProperty
    private final T payload;

    public ApiResult(String success, T payload) {
        this.success = success;
        this.payload = payload;
    }

    public static ApiResult<String> result(String success, String message) {
        return new ApiResult<>(success, message);
    }
}
