package springallinone.practice.exceptionHandling.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"회원이 존재하지 않습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "유효한 입력값이 아닙니다."),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다.");

    private final HttpStatus status;
    private final String message;
}
