package com.kill.gaebokchi.domain.bogu.service;

import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import com.kill.gaebokchi.domain.bogu.entity.dto.BoguResponseDTO;
import com.kill.gaebokchi.domain.bogu.repository.DefaultBoguRepository;
import com.kill.gaebokchi.domain.bogu.repository.EvolvedBoguRepository;
import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoguService {
    private final DefaultBoguService defaultBoguService;
    private final EvolvedBoguService evolvedBoguService;

    public BoguResponseDTO findAllBogus(Member member){
        BoguResponseDTO res = new BoguResponseDTO();
        Integer usage = defaultBoguService.countDefaultBogu(member);

        res.setTodayQuota(3-usage);
        res.setDefaultBogus(defaultBoguService.findDefaultBoguByHostAndNotEvolve(member));
        res.setEvolvedBogus(evolvedBoguService.findEvolvedBoguByHost(member));
        return res;
    }
}
