package com.kill.gaebokchi.domain.bogu.dto.response;

import com.kill.gaebokchi.domain.bogu.entity.Category;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolvedBoguResponseDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private Boolean isLiberated;
    private Integer level;
    private List<String> categories;
    private String selectedCategory;
    private Integer variation;
    private String name;
    private Integer status;
    private Integer count;
    private String problem;

    public static EvolvedBoguResponseDTO from(EvolvedBogu entity){
        List<String> categories;
        categories = entity.getCategories().stream()
                    .map(Category::getText)
                    .toList();
        return EvolvedBoguResponseDTO.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .isLiberated(entity.getIsLiberated())
                .level(entity.getLevel())
                .categories(categories)
                .selectedCategory(entity.getSelectedCategory().getText())
                .variation(entity.getType().getVariation())
                .name(entity.getType().getName())
                .status(entity.getStatus())
                .count(entity.getCount())
                .problem(entity.getProblem())
                .build();

    }
}
