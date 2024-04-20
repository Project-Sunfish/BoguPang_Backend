package com.kill.gaebokchi.repository;

import com.kill.gaebokchi.entity.Member;
import com.kill.gaebokchi.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}

