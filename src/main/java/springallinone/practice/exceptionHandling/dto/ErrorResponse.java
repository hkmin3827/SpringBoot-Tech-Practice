package springallinone.practice.exceptionHandling.dto;

import springallinone.practice.exceptionHandling.constant.ErrorCode;

public record ErrorResponse(
        int status,
        String code,
        String message
) {
    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.getStatus().value(), errorCode.name(), errorCode.getMessage());
    }

    public ErrorResponse(ErrorCode errorCode, String detail) {
        this(errorCode.getStatus().value(), errorCode.name(), detail);
    }
}
