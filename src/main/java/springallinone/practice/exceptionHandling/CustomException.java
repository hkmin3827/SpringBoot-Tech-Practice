package springallinone.practice.exceptionHandling;

import lombok.Getter;
import springallinone.practice.exceptionHandling.constant.ErrorCode;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}