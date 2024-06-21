package com.kill.gaebokchi.domain.bogu.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class BoguResponseDTO implements Serializable {
    private Integer todayQuota;
    private List<DefaultBoguResponseDTO> defaultBogus;
    private List<EvolvedBoguResponseDTO> evolvedBogus;
}
