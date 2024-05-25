package com.kill.gaebokchi.domain.bogu.entity.dto.dogamBogu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CollectedBoguResponseDTO {
    private Integer id;
    private String selectedCategory;
    private Integer variation;
    private Integer count;
    private List<LiberatedBoguResponseDTO> liberatedBogus;
}
