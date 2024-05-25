package com.kill.gaebokchi.domain.bogu.entity.dto.dogamBogu;

import com.kill.gaebokchi.domain.bogu.entity.Category;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiberatedBoguResponseDTO {
    private Long id;
    private List<String> categories;
    private String problem;
    private LocalDateTime createdAt;
    private LocalDateTime liberatedAt;

    public static LiberatedBoguResponseDTO from(EvolvedBogu entity){
        List<String> categories;
        categories = entity.getCategories().stream()
                .map(Category::getText)
                .toList();
        return LiberatedBoguResponseDTO.builder()
                .id(entity.getId())
                .categories(categories)
                .problem(entity.getProblem())
                .createdAt(entity.getCreatedAt())
                .liberatedAt(entity.getLiberatedAt())
                .build();
    }
}