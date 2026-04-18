package springallinone.practice.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springallinone.practice.event.MemberRegisteredEvent;
import springallinone.practice.jpa.dto.MemberCreateReq;
import springallinone.practice.jpa.entity.Member;
import springallinone.practice.jpa.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public Long register(MemberCreateReq req) {
        if (memberRepository.exitsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Member member = Member.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .name(req.name())
                .role(req.role())
                .build();
        Member saved = memberRepository.save(member);

        eventPublisher.publishEvent(new MemberRegisteredEvent(saved.getId(), saved.getEmail()));
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public void updateName(Long id, String name) {
        Member member = getMember(id);
        member.updateName(name);
    }
}
