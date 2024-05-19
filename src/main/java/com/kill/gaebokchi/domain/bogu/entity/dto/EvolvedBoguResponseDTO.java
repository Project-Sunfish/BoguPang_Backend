package com.kill.gaebokchi.domain.bogu.entity.dto;

import com.kill.gaebokchi.domain.bogu.entity.Category;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolvedBoguResponseDTO {
    private Long id;
    private Integer level;
    private List<String> categories;
    private String selected_category;
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
                .level(entity.getLevel())
                .categories(categories)
                .selected_category(entity.getSelectedCategory().getText())
                .variation(entity.getVariation())
                .name(entity.getName())
                .status(entity.getStatus())
                .count(entity.getCount())
                .problem(entity.getProblem())
                .build();

    }
}
