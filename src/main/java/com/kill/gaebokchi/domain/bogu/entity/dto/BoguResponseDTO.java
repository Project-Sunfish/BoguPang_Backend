package com.kill.gaebokchi.domain.bogu.entity.dto;

import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoguResponseDTO {
    private Integer todayQuota;
    private List<DefaultBoguResponseDTO> defaultBogus;
    private List<EvolvedBoguResponseDTO> evolvedBogus;
}
