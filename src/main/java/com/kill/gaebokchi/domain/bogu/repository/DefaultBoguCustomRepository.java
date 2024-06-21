package com.kill.gaebokchi.domain.bogu.repository;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DefaultBoguCustomRepository {
    List<DefaultBogu> findByHost(Member host);
    Optional<DefaultBogu> findByHostAndId(Member member, Long id);
    List<DefaultBogu> findByHostAndEvolvedFormNotNull(Member member);
    List<DefaultBogu> findByHostAndEvolvedFormNull(Member member);
    Integer countByMemberAndCreatedAtToday(Member member, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
