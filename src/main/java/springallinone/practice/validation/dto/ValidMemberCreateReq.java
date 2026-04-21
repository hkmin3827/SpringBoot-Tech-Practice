package springallinone.practice.validation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ValidMemberCreateReq(
        @Email(message = "이메일 형식이 아닙니다.") @NotBlank String email,
        @NotBlank @Size(min = 8, message = "password must be at least 8 characters") String password,
        @NotBlank @Size(min = 2, max = 30) String name
) {
}
