package com.kill.gaebokchi.domain.bogu.dto.response;

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
