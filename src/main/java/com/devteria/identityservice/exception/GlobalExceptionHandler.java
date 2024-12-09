package com.devteria.identityservice.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devteria.identityservice.dto.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    // loi trung lap username
    // C1
    //    @ExceptionHandler(value = RuntimeException.class)
    //    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
    //       ApiResponse apiResponse = new ApiResponse();
    //       apiResponse.setCode(1001);
    //       apiResponse.setMessage(exception.getMessage());
    //
    //        return ResponseEntity.badRequest().body(apiResponse);
    //    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // Bat loi mk nhap chua du 8 ki tu
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = (exception.getFieldError().getDefaultMessage()); // defaultmessage la message cta tu tao

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintValidator =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintValidator.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());

        } catch (IllegalArgumentException e) {

        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException accessDeniedException) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
