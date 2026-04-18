package springallinone.practice.event;

public record MemberRegisteredEvent(Long memberId, String email) {
}
