package com.kill.gaebokchi.domain.bogu.repository;

import com.kill.gaebokchi.domain.bogu.entity.Category;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import com.kill.gaebokchi.domain.bogu.entity.Type;
import com.kill.gaebokchi.domain.account.entity.Member;
import org.springframework.cache.annotation.Cacheable;
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

    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.type = :type")
    List<EvolvedBogu> findByHostAndType(@Param("member")Member member, @Param("type") Type type);
    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member")
    List<EvolvedBogu> findByHost(@Param("member")Member member);
    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.selectedCategory = :selectedCategory")
    List<EvolvedBogu> findByHostAndSelectedCategory(@Param("member") Member member, @Param("selectedCategory") Category selectedCategory);

    //지난 30일간 진화횟수
    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.createdAt >= :createdAt")
    List<EvolvedBogu> findByHostAndCreatedAtAfter(@Param("member") Member member, @Param("createdAt") LocalDateTime createdAt);
    //지난 30일간 선택한 특정 카테고리의 횟수
    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.selectedCategory = :selectedCategory AND eb.createdAt >= :createdAt")
    List<EvolvedBogu> findByHostAndSelectedCategoryAndCreatedAtAfter(
            @Param("member") Member member,
            @Param("selectedCategory") Category selectedCategory,
            @Param("createdAt") LocalDateTime createdAt);
    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.count >= 1")
    @Cacheable(value="evolvedBogu")
    List<EvolvedBogu> findByHostAndNonZeroCount(@Param("member") Member member);

    @Query("SELECT eb FROM EvolvedBogu eb WHERE eb.defaultForm.host = :member AND eb.isLiberated = false")
    List<EvolvedBogu> findByHostAndIsLiberatedFalse(@Param("member") Member member);

}
