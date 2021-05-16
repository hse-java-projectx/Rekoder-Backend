package ru.hse.rekoder.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

public class BaseResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected final String pathToResource;

    public BaseResponse(String pathToResource) {
        this.pathToResource = pathToResource;
    }

    public String getPathToResource() {
        return pathToResource;
    }
}
