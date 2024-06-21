package com.kill.gaebokchi.domain.bogu.repository;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.entity.QMember;
import com.kill.gaebokchi.domain.bogu.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EvolvedBoguCustomRepositoryImpl implements EvolvedBoguCustomRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<EvolvedBogu> findByHostAndId(Member member, Long id) {
        QEvolvedBogu evolvedBogu = QEvolvedBogu.evolvedBogu;
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        EvolvedBogu result = queryFactory.selectFrom(evolvedBogu)
                .join(evolvedBogu.defaultForm, defaultBogu).fetchJoin()
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(defaultBogu.host.eq(member)
                        .and(evolvedBogu.id.eq(id)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<EvolvedBogu> findByHostAndType(Member member, Type type) {
        QEvolvedBogu evolvedBogu = QEvolvedBogu.evolvedBogu;
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(evolvedBogu)
                .join(evolvedBogu.defaultForm, defaultBogu).fetchJoin()
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(defaultBogu.host.eq(member)
                        .and(evolvedBogu.type.eq(type)))
                .fetch();
    }

    @Override
    public List<EvolvedBogu> findByHost(Member member) {
        QEvolvedBogu evolvedBogu = QEvolvedBogu.evolvedBogu;
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(evolvedBogu)
                .join(evolvedBogu.defaultForm, defaultBogu).fetchJoin()
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(defaultBogu.host.eq(member))
                .fetch();
    }

    @Override
    public List<EvolvedBogu> findByHostAndSelectedCategory(Member member, Category selectedCategory) {
        QEvolvedBogu evolvedBogu = QEvolvedBogu.evolvedBogu;
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(evolvedBogu)
                .join(evolvedBogu.defaultForm, defaultBogu).fetchJoin()
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(defaultBogu.host.eq(member)
                        .and(evolvedBogu.selectedCategory.eq(selectedCategory)))
                .fetch();
    }

    @Override
    public List<EvolvedBogu> findByHostAndCreatedAtAfter(Member member, LocalDateTime createdAt) {
        QEvolvedBogu evolvedBogu = QEvolvedBogu.evolvedBogu;
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(evolvedBogu)
                .join(evolvedBogu.defaultForm, defaultBogu).fetchJoin()
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(defaultBogu.host.eq(member)
                        .and(evolvedBogu.createdAt.goe(createdAt)))
                .fetch();
    }

    @Override
    public List<EvolvedBogu> findByHostAndSelectedCategoryAndCreatedAtAfter(Member member, Category selectedCategory, LocalDateTime createdAt) {
        QEvolvedBogu evolvedBogu = QEvolvedBogu.evolvedBogu;
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(evolvedBogu)
                .join(evolvedBogu.defaultForm, defaultBogu).fetchJoin()
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(defaultBogu.host.eq(member)
                        .and(evolvedBogu.selectedCategory.eq(selectedCategory))
                        .and(evolvedBogu.createdAt.goe(createdAt)))
                .fetch();
    }

    @Override
    public List<EvolvedBogu> findByHostAndNonZeroCount(Member member) {
        QEvolvedBogu evolvedBogu = QEvolvedBogu.evolvedBogu;
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;
        return queryFactory.selectFrom(evolvedBogu)
                .join(evolvedBogu.defaultForm, defaultBogu).fetchJoin()
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(defaultBogu.host.eq(member)
                        .and(evolvedBogu.count.goe(1)))
                .fetch();
    }

    @Override
    public List<EvolvedBogu> findByHostAndIsLiberatedFalse(Member member) {
        QEvolvedBogu evolvedBogu = QEvolvedBogu.evolvedBogu;
        QDefaultBogu defaultBogu = QDefaultBogu.defaultBogu;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(evolvedBogu)
                .join(evolvedBogu.defaultForm, defaultBogu).fetchJoin()
                .join(defaultBogu.host, qMember).fetchJoin()
                .where(qMember.eq(member)
                        .and(evolvedBogu.isLiberated.isFalse()))
                .fetch();
    }
}
