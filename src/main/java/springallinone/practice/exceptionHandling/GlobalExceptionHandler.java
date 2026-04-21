package springallinone.practice.exceptionHandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springallinone.practice.exceptionHandling.constant.ErrorCode;
import springallinone.practice.exceptionHandling.dto.ErrorResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(ErrorCode.VALIDATION_FAILED.getStatus())
                .body(new ErrorResponse(ErrorCode.VALIDATION_FAILED, detail));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(ErrorCode.INVALID_ARGUMENT.getStatus())
                .body(new ErrorResponse(ErrorCode.INVALID_ARGUMENT, ErrorCode.INVALID_ARGUMENT.getMessage()));

    }
}
