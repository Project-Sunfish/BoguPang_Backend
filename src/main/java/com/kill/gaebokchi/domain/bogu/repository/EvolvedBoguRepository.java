package com.kill.gaebokchi.domain.bogu.repository;

import com.kill.gaebokchi.domain.bogu.entity.Category;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import com.kill.gaebokchi.domain.user.entity.Member;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvolvedBoguRepository extends JpaRepository<EvolvedBogu, Long> {
    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.id = :id")
    Optional<EvolvedBogu> findByHostAndId(@Param("member")Member member, @Param("id") Long id);

    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.selectedCategory = :selectedCategory")
    List<EvolvedBogu> findByHostAndSelectedCategory(@Param("member") Member member, @Param("selectedCategory") Category selectedCategory);

    //지난 30일간 진화횟수
    @Query("SELECT eb FROM EvolvedBogu eb " +
            "WHERE eb.defaultForm.host = :member " +
            "AND eb.createdAt >= :createdAt")
    List<EvolvedBogu> findByHostAndCreatedAtAfter(@Param("member") Member member, @Param("createdAt") LocalDateTime createdAt);
    //지난 30일간 선택한 특정 카테고리의 횟수
    @Query("SELECT eb FROM EvolvedBogu eb " +
            "WHERE eb.defaultForm.host = :member " +
            "AND eb.selectedCategory = :selectedCategory " +
            "AND eb.createdAt >= :createdAt")
    List<EvolvedBogu> findByHostAndSelectedCategoryAndCreatedAtAfter(
            @Param("member") Member member,
            @Param("selectedCategory") Category selectedCategory,
            @Param("createdAt") LocalDateTime createdAt);

    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.isLiberated=false")
    List<EvolvedBogu> findByHostAndIsLiberatedFalse(@Param("member") Member member);

    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.isLiberated=true")
    List<EvolvedBogu> findByHostAndIsLiberatedTrue(@Param("member") Member member);

    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.selectedCategory = :category AND eb.variation = :variation")
    List<EvolvedBogu> findByHostAndType(@Param("member") Member member, @Param("category") Category category, @Param("variation") Integer variation);
}
