package com.kill.gaebokchi.domain.bogu.dto.response.dogam;

import com.kill.gaebokchi.domain.bogu.entity.Type;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectedBoguResponseDTO {
    Integer typeId;
    Boolean newFlag;
    Boolean liberatedFlag;
    String name;

    public static CollectedBoguResponseDTO from(Type type){
        return CollectedBoguResponseDTO.builder()
                .typeId(type.getId())
                .name(type.getName())
                .build();
    }
}
