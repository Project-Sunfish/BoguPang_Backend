package com.kill.gaebokchi.domain.bogu.repository;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.bogu.entity.Category;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import com.kill.gaebokchi.domain.bogu.entity.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EvolvedBoguCustomRepository {
    Optional<EvolvedBogu> findByHostAndId(Member member, Long id);
    List<EvolvedBogu> findByHostAndType(Member member, Type type);
    List<EvolvedBogu> findByHost(Member member);
    List<EvolvedBogu> findByHostAndSelectedCategory(Member member, Category selectedCategory);

    //지난 30일간 진화횟수
    List<EvolvedBogu> findByHostAndCreatedAtAfter(Member member,LocalDateTime createdAt);
    //지난 30일간 선택한 특정 카테고리의 횟수
    List<EvolvedBogu> findByHostAndSelectedCategoryAndCreatedAtAfter(
            Member member,
            Category selectedCategory,
            LocalDateTime createdAt);
    List<EvolvedBogu> findByHostAndNonZeroCount(Member member);

    List<EvolvedBogu> findByHostAndIsLiberatedFalse(Member member);

}
