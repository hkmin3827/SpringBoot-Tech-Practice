package springallinone.practice.jpa.dto;

import springallinone.practice.jpa.constant.Role;

public record MemberCreateReq(
        String email,
        String password,
        String name,
        Role role
) {
}
