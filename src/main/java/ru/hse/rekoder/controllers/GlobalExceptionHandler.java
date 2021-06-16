package ru.hse.rekoder.controllers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.hse.rekoder.exceptions.*;
import ru.hse.rekoder.responses.ErrorsResponse;
import ru.hse.rekoder.responses.SingleErrorResponse;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class,
            TeamNotFoundException.class,
            ProblemNotFoundException.class,
            FolderNotFoundException.class,
            SubmissionNotFoundException.class})
    public ResponseEntity<ErrorsResponse> handleNotFoundException(ApiError ex) {
        SingleErrorResponse error = new SingleErrorResponse(ex.getErrorType(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorsResponse(List.of(error)));
    }

    @ExceptionHandler({UserConflictException.class,
            TeamConflictException.class,
            FolderConflictException.class})
    public ResponseEntity<ErrorsResponse> handleResourceConflictException(ApiError ex) {
        SingleErrorResponse error = new SingleErrorResponse(ex.getErrorType(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorsResponse(List.of(error)));
    }

    @ExceptionHandler({FolderException.class,
            ProblemException.class,
            SubmissionException.class,
            TeamException.class,
            UserException.class})
    public ResponseEntity<ErrorsResponse> handleResourceException(ApiError ex) {
        SingleErrorResponse error = new SingleErrorResponse(ex.getErrorType(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorsResponse(List.of(error)));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<SingleErrorResponse> errors = ex.getFieldErrors()
                .stream()
                .map(fieldError -> new SingleErrorResponse("not-valid-field", fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorsResponse(errors));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorsResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<SingleErrorResponse> errors = ex.getConstraintViolations()
                .stream()
                .map(error -> new SingleErrorResponse("constraint-violation", error.getMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorsResponse(errors));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));
        SingleErrorResponse error = new SingleErrorResponse("unsupported-method", builder.toString());
        return new ResponseEntity<>(new ErrorsResponse(List.of(error)), headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SingleErrorResponse error = new SingleErrorResponse("missing-parameter", ex.getParameterName() + " parameter is missing");
        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorsResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();
        String errorMessage = ex.getName() + " should be of type " +
                (Objects.isNull(requiredType) ? "any" : requiredType.getSimpleName());
        SingleErrorResponse error = new SingleErrorResponse("method-argument-type-mismatch", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorsResponse(List.of(error)));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        SingleErrorResponse error = new SingleErrorResponse("no-handler-found", errorMessage);
        return new ResponseEntity<>(new ErrorsResponse(List.of(error)), headers, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(type -> builder.append(type).append(", "));
        SingleErrorResponse error = new SingleErrorResponse("media-type-not-supported", builder.toString());
        return new ResponseEntity<>(new ErrorsResponse(List.of(error)), headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex) {
        SingleErrorResponse error = new SingleErrorResponse(
                "internal-server-error",
                "Oooops... Something went wrong. \n" +
                        "We are already solving the problem. \n" +
                        "Our apologies: https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorsResponse(List.of(error)));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorsResponse> handleAccessDeniedException(AccessDeniedException ex) {
        SingleErrorResponse error = new SingleErrorResponse(
            "access-denied",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorsResponse(List.of(error)));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        SingleErrorResponse error = new SingleErrorResponse("not-readable-body", "Failed to read the request body");
        return new ResponseEntity<>(new ErrorsResponse(List.of(error)), headers, HttpStatus.BAD_REQUEST);
    }
}
