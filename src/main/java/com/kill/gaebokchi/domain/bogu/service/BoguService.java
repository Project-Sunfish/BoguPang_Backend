package com.kill.gaebokchi.domain.bogu.service;

import com.kill.gaebokchi.domain.bogu.dto.response.BoguResponseDTO;
import com.kill.gaebokchi.domain.account.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoguService {
    private final DefaultBoguService defaultBoguService;
    private final EvolvedBoguService evolvedBoguService;

    public BoguResponseDTO findAllBogus(Member member){
        long startTime = System.currentTimeMillis();

        BoguResponseDTO res = new BoguResponseDTO();
        Integer usage = defaultBoguService.countDefaultBogu(member);

        res.setTodayQuota(3-usage);
        res.setDefaultBogus(defaultBoguService.findDefaultBoguByHostAndNotEvolve(member));
        res.setEvolvedBogus(evolvedBoguService.findEvolvedBoguByHost(member));

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        log.info("***** duration time of get all bogus : "+durationTimeSec+"m/s *****");
        return res;
    }
}
