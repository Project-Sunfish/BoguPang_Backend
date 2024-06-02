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
    String name;

    public static CollectedBoguResponseDTO from(Integer category, Integer variation){
        Type type = Type.getTypeFromCategoryAndVariation(category,variation);
        return CollectedBoguResponseDTO.builder()
                .typeId(type.getId())
                .newFlag(type.getNewFlag())
                .name(type.getName())
                .build();
    }
}
