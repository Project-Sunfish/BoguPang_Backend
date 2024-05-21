package com.kill.gaebokchi.domain.bogu.service;

import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import com.kill.gaebokchi.domain.bogu.entity.dto.DefaultBoguResponseDTO;
import com.kill.gaebokchi.domain.bogu.repository.DefaultBoguRepository;
import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.domain.user.repository.MemberRepository;
import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public Long saveDefaultBogu(Member member){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        if(defaultBoguRepository.countByMemberAndCreatedAtToday(member, startOfDay, endOfDay)>=3){
            throw new BadRequestException(EXCEED_DEFAULT_BOGU_CAPACITY);
        }
        DefaultBogu defaultBogu = new DefaultBogu();
        defaultBogu.setEvolvedForm(null);
        member.addBogus(defaultBogu);

        memberRepository.save(member);
        defaultBoguRepository.save(defaultBogu);
        return defaultBogu.getId();
    }

    public Integer countDefaultBogu(Member member){
        List<DefaultBogu> bogus = defaultBoguRepository.findByHost(member);
        return bogus.size();
    }

    public DefaultBogu findDefaultBoguByID(Long id) {
        return defaultBoguRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_DEFAULT_BOGU_ID));
    }

    public List<DefaultBoguResponseDTO> findDefaultBoguByHostAndNotEvolve(Member member){
        List<DefaultBogu> findLists = defaultBoguRepository.findByHostAndEvolvedFormNull(member);
        return findLists.stream()
                .map(DefaultBoguResponseDTO::from)
                .collect(Collectors.toList());
    }

}
