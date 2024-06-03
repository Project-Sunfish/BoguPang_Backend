package com.kill.gaebokchi.domain.bogu.entity.dto.dogamBogu;

import com.kill.gaebokchi.domain.bogu.entity.Category;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import com.kill.gaebokchi.domain.bogu.entity.Type;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
