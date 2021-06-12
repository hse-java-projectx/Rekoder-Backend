package ru.hse.rekoder.exceptions;

public interface ApiError {
    String getErrorType();
    String getMessage();
}
