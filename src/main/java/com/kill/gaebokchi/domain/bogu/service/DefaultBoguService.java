package com.kill.gaebokchi.domain.bogu.service;

import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import com.kill.gaebokchi.domain.bogu.dto.response.DefaultBoguResponseDTO;
import com.kill.gaebokchi.domain.bogu.repository.DefaultBoguRepository;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.repository.MemberRepository;
import com.kill.gaebokchi.global.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.kill.gaebokchi.global.error.ExceptionCode.*;

@Service
@Slf4j
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class DefaultBoguService {
    private final DefaultBoguRepository defaultBoguRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public DefaultBoguResponseDTO saveDefaultBogu(Member member){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        if(defaultBoguRepository.countByMemberAndCreatedAtToday(member, startOfDay, endOfDay)>=3){
            throw new BadRequestException(EXCEED_DEFAULT_BOGU_CAPACITY);
        }
        DefaultBogu entity = DefaultBogu.builder()
                            .evolvedForm(null)
                            .build();
        member.addBogus(entity);

        memberRepository.save(member);
        defaultBoguRepository.save(entity);
        return DefaultBoguResponseDTO.from(entity);
    }

    public Integer countDefaultBogu(Member member){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return defaultBoguRepository.countByMemberAndCreatedAtToday(member, startOfDay, endOfDay);
    }

    public DefaultBogu findDefaultBoguByID(Member member, Long id) {
        return defaultBoguRepository.findByHostAndId(member, id)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_DEFAULT_BOGU_ID));
    }
    public List<DefaultBoguResponseDTO> findDefaultBoguByHostAndNotEvolve(Member member){
        List<DefaultBogu> findLists = defaultBoguRepository.findByHostAndEvolvedFormNull(member);
        if(findLists==null){
            return Collections.emptyList();
        }
        return findLists.stream()
                .map(DefaultBoguResponseDTO::from)
                .collect(Collectors.toList());
    }

}
