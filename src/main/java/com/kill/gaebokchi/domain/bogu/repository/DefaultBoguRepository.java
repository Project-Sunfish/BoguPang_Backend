package com.kill.gaebokchi.domain.bogu.repository;

import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import com.kill.gaebokchi.domain.user.entity.Member;
import lombok.Builder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DefaultBoguRepository extends JpaRepository<DefaultBogu, Long> {
    List<DefaultBogu> findByHost(Member host);
    Optional<DefaultBogu> findByHostAndId(Member member, Long id);

    List<DefaultBogu> findByHostAndEvolvedFormNotNull(Member member);
    List<DefaultBogu> findByHostAndEvolvedFormNull(Member member);
    @Query("SELECT COUNT(d) FROM DefaultBogu d WHERE d.host = :member AND d.createdAt >= :startOfDay AND d.createdAt < :endOfDay")
    Integer countByMemberAndCreatedAtToday(@Param("member") Member member,
                                        @Param("startOfDay") LocalDateTime startOfDay,
                                        @Param("endOfDay") LocalDateTime endOfDay);
}
