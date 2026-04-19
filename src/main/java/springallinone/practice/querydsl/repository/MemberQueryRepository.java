package springallinone.practice.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import springallinone.practice.jpa.constant.Role;
import springallinone.practice.jpa.entity.Member;
import springallinone.practice.jpa.entity.QMember;
import springallinone.practice.querydsl.dto.MemberSearchCondition;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Member> search(MemberSearchCondition condition) {

         QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(condition.email())) {
            builder.and(member.email.eq(condition.email()));
        }
        if (StringUtils.hasText(condition.name())) {
            builder.and(member.name.eq(condition.name()));
        }

        return jpaQueryFactory
                .selectFrom(member)
                .where(builder)
                .orderBy(member.createdAt.desc())
                .fetch();
    }

    public List<Member> findAllUsers() {
        QMember member = QMember.member;

        return jpaQueryFactory
                .selectFrom(member)
                .where(member.role.eq(Role.ROLE_USER))
                .orderBy(member.name.asc())
                .fetch();
    }
}
