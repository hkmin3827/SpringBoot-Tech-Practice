package springallinone.practice.jpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springallinone.practice.jpa.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
}
