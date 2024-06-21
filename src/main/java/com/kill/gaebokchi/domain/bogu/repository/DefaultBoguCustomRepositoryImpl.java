package com.kill.gaebokchi.domain.bogu.repository;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.entity.QMember;
import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import com.kill.gaebokchi.domain.bogu.entity.QDefaultBogu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DefaultBoguCustomRepositoryImpl implements DefaultBoguCustomRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<DefaultBogu> findByHost(Member host) {
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember member = QMember.member;

        return queryFactory.selectFrom(defaultBogu)
                .join(defaultBogu.host, member).fetchJoin()
                .where(member.eq(host))
                .fetch();
    }

    @Override
    public Optional<DefaultBogu> findByHostAndId(Member member, Long id) {
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        DefaultBogu result = queryFactory.selectFrom(defaultBogu)
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(qMember.eq(member)
                        .and(defaultBogu.id.eq(id)))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<DefaultBogu> findByHostAndEvolvedFormNotNull(Member member) {
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(defaultBogu)
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(qMember.eq(member)
                        .and(defaultBogu.evolvedForm.isNotNull()))
                .fetch();
    }

    @Override
    public List<DefaultBogu> findByHostAndEvolvedFormNull(Member member) {
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(defaultBogu)
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(qMember.eq(member)
                        .and(defaultBogu.evolvedForm.isNull()))
                .fetch();
    }
    @Override
    public Integer countByMemberAndCreatedAtToday(Member member, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.select(defaultBogu.count())
                .from(defaultBogu)
                .join(defaultBogu.host, qMember)
                .where(qMember.eq(member)
                        .and(defaultBogu.createdAt.between(startOfDay, endOfDay)))
                .fetchOne().intValue();
    }
}
