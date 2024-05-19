package com.kill.gaebokchi.domain.bogu.entity.dto;

import com.kill.gaebokchi.domain.bogu.entity.Category;
import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultBoguResponseDTO {
    private Long id;
    public static DefaultBoguResponseDTO from(DefaultBogu entity){

        return DefaultBoguResponseDTO.builder()
                .id(entity.getId())
                .build();
    }
}
