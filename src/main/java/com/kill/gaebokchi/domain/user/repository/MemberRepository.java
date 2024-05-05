package com.kill.gaebokchi.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kill.gaebokchi.domain.user.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);
}
