package com.kill.gaebokchi.domain.bogu.dto.response;

import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultBoguResponseDTO implements Serializable {
    private Long id;
    public static DefaultBoguResponseDTO from(DefaultBogu entity){

        return DefaultBoguResponseDTO.builder()
                .id(entity.getId())
                .build();
    }
}
