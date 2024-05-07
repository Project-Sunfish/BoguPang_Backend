package com.kill.gaebokchi.domain.user.repository;

import com.kill.gaebokchi.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByUsername(String username);
    Member findByUsername(String username);
}
